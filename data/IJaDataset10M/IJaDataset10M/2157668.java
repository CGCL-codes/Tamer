package com.overflow.madokaalarm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class AlarmService extends Service {

    private AlertManager alertManager = null;

    private Context context;

    private boolean isRunning = true;

    Calendar cl = Calendar.getInstance();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i("AlarmService", "----------AlarmService start");
        Thread td = new Thread(new loop());
        td.start();
    }

    public class loop implements Runnable {

        @Override
        public void run() {
            Log.i("AlarmService Thread", "Running");
            FileInputStream inputStream;
            try {
                inputStream = openFileInput("alarmtime.xml");
                alertManager = new AlertManager();
                alertManager.initAlarm(inputStream, context);
                while (isRunning) {
                    cl.setTimeInMillis(System.currentTimeMillis());
                    if (alertManager.IsTick(cl)) {
                        Log.i("AlarmService", "OK,It's the Time! ");
                        Intent arIntent = new Intent();
                        arIntent.setClass(AlarmService.this, ShowActivity.class);
                        arIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(arIntent);
                    }
                    Thread.sleep(60 * 1000);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.i("AlarmService", "----------AlarmService stop");
        isRunning = false;
        super.onDestroy();
    }
}
