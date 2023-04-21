package com.narunas;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewExperiments extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button btn = (Button) findViewById(R.id.Button01);
        btn.setOnClickListener(onSkinnedBtnClicked);
        btn = (Button) findViewById(R.id.Button02);
        btn.setOnClickListener(onProcBtnClick);
        btn = (Button) findViewById(R.id.Button03);
        btn.setOnClickListener(onShapesBtnClick);
        btn = (Button) findViewById(R.id.Button04);
        btn.setOnClickListener(onDialBtnClick);
    }

    private OnClickListener onShapesBtnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(ViewExperiments.this, ScalingTest.class);
            startActivity(intent);
        }
    };

    private OnClickListener onDialBtnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(ViewExperiments.this, Dialer.class);
            startActivity(intent);
        }
    };

    private OnClickListener onProcBtnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(ViewExperiments.this, ProcButtons.class);
            startActivity(intent);
        }
    };

    private OnClickListener onSkinnedBtnClicked = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(ViewExperiments.this, ButtonsSkinned.class);
            startActivity(intent);
        }
    };
}
