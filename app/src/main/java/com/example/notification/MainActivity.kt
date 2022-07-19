package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var notificationManager: NotificationManager? = null
    private var channelId = "channel_id"

    private lateinit var countDown: CountDownTimer
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //register channel kedalam sistem
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelId, "count down", "description")

        binding.btnStart.setOnClickListener {
            countDown.start()
        }
        countDown = object : CountDownTimer(5000, 1000) {
            override fun onTick(p0: Long) {
                //masukin text dari string
                binding.timer.text = getString(R.string.time_reamining, p0 / 1000)
            }

            override fun onFinish() {
                displayNotification()
            }

        }
    }

    private fun displayNotification() {
        //untuk berpindah ke activity setelah pencet tombol notif
        val intent = Intent(this, Destination::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, channelId)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("My Kitchen")
            .setContentText("See your favorite recipe")
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged."))

        with(NotificationManagerCompat.from(this)) {
            notify(404, builder.build())
        }
    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String) {
        //validasi notif akan dibuat juga version SDK 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}