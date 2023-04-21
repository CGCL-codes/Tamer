package com.wifirecommenderap;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScanningService extends Service {

    String thisTag = "Wifi-ScanningService";

    final int DEFAULT_DELAY = 3000;

    final int DEFAULT_SFREQ = 6000;

    WifiManager wifi;

    BroadcastReceiver receiver;

    boolean wifiReceiverFinished = true;

    int delay = DEFAULT_DELAY;

    int frequency = DEFAULT_SFREQ;

    public int getDelay() {
        return delay;
    }

    public int getFrequency() {
        return frequency;
    }

    public void prefsUpdateHandler() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Timer timer;

    private NotificationManager nm;

    LocationManager locMgr;

    private TimerTask task = new TimerTask() {

        @Override
        public void run() {
            Log.i(thisTag, "Scanned Wifi at " + System.currentTimeMillis());
            WifiScan();
            wifi.startScan();
            Log.i("WifiStart", "Started scanning...");
        }
    };

    public void WifiScan() {
        final String TAG = "WiFiDemo";
        Log.i(TAG, "WifiScan...");
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (receiver == null) receiver = new WifiScanReceive(this);
        registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void updateTimer() {
        this.timer = new Timer();
        this.timer.schedule(this.task, getDelay(), getFrequency());
    }

    @Override
    public void onCreate() {
        updateTimer();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.v(this.thisTag, "ScanningService.onStart():  Service Started");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
        unregisterReceiver(receiver);
        Log.v(this.thisTag, "ScanningService.onDestroy():  Service Stopped");
    }
}
