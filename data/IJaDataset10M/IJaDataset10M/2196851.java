package com.andriod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class ActivityThree extends Activity {

    private Button buttonHome;

    private LinearLayout lenearLayout;

    private String className;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main3);
        lenearLayout = new LinearLayout(getApplicationContext());
        buttonHome = new Button(getApplicationContext());
        buttonHome.setText("Home");
        buttonHome.setOnClickListener(listenerButtonHome);
        lenearLayout.addView(buttonHome);
        setContentView(lenearLayout);
        className = getClass().getSimpleName();
        Util.messageBox(className + " " + Util.MESSAGE_CREATE, getApplicationContext());
    }

    public OnClickListener listenerButtonHome = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = Util.createIntent(Intent.ACTION_VIEW, ActivityMain.class.getName(), getPackageName());
            startActivity(intent);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Util.messageBox(className + " " + Util.MESSAGE_START, getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        Util.messageBox(className + " " + Util.MESSAGE_RESUME, getApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        Util.messageBox(className + " " + Util.MESSAGE_PAUSE, getApplicationContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        Util.messageBox(className + " " + Util.MESSAGE_STOP, getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Util.messageBox(className + " " + Util.MESSAGE_DESTROY, getApplicationContext());
    }
}
