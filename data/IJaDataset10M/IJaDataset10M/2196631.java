package com.android.testplugin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TestPlugin extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
