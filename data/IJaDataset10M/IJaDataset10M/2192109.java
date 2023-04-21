package com.vu.crepe.notifier;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import android.os.RemoteException;
import android.os.crepe.ICrepePolicyManagerService;
import android.os.ServiceManager;

public class CRePENotifier extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        int id = 0;
        try {
            crepeTest2(1);
        } catch (RemoteException re) {
        }
        Intent data = getIntent();
        String msg = data.getStringExtra("message");
        TextView tv = (TextView) findViewById(R.id.txtMsg);
        tv.setText(msg);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(0xBEEF);
    }
}
