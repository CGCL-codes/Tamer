package com.fom2008.applifecycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.fom2008.R;

public class FOMApplicationLifecycle extends Activity {

    private static volatile int gid = 0;

    private int uid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applifecycle);
        TextView t = (TextView) this.findViewById(R.id.text1);
        synchronized (FOMApplicationLifecycle.class) {
            uid = ++gid;
        }
        t.setText(uid + "");
        Log.i("FOM", "Activity with uid: " + uid + " calls onCreate");
        Log.i("FOM", gid + " are currently running");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("FOM", "Activity with uid: " + uid + " calls onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("FOM", "Activity with uid: " + uid + " calls onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("FOM", "Activity with uid: " + uid + " calls onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (gid < 10) startActivity(new Intent(this, FOMApplicationLifecycle.class));
        Log.i("FOM", "Activity with uid: " + uid + " calls onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("FOM", "Activity with uid: " + uid + " calls onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("FOM", "Activity with uid: " + uid + " calls onDestroy");
    }
}
