package com.pfe.okassa;

import org.opencv.core.Mat;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.pfe.okassa.Service_;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Main extends Activity {

    LayoutInflater controlInflater = null;

    Message message;

    MyReceiver myReceiver;

    public static Mat img;

    public ProgressDialog mProgressDialog;

    public static boolean click;

    View_ view;

    String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        click = false;
        view = new View_(this);
        setContentView(view);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.control, null);
        LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        this.addContentView(viewControl, layoutParamsControl);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Service_.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
        ((Button) findViewById(R.id.mbuttonAnalyse)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                click = true;
                SystemClock.sleep(1000);
                Toast.makeText(getBaseContext(), "Picture saved... ", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Main.this, Service_.class);
                Toast.makeText(getBaseContext(), "Starting service... ", Toast.LENGTH_LONG).show();
                startService(i);
            }
        });
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.hasExtra("DETECTFEATURES")) {
                String orgData = arg1.getStringExtra("DETECTFEATURES");
                Toast.makeText(Main.this, orgData, Toast.LENGTH_LONG).show();
                Log.i("MyActivity", "BROADCAST TYPE DETECTFEATURES");
            }
            if (arg1.hasExtra("SHOWPROGRESSBAR")) {
                ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar1);
                progressbar.setVisibility(View.VISIBLE);
                Log.i("MyActivity", "BROADCAST TYPE SHOWPROGRESSBAR");
            }
            if (arg1.hasExtra("HIDEPROGRESSBAR")) {
                ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar1);
                progressbar.setVisibility(View.INVISIBLE);
                Log.i("MyActivity", "BROADCAST TYPE HIDEPROGRESSBAR");
            }
        }
    }
}
