package com.misgod.pdbreader;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BrightnessDialog extends Dialog {

    public BrightnessDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.brightness);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        SeekBar seek = (SeekBar) findViewById(R.id.brightness_seek);
        int ib = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 100);
        seek.setProgress(ib);
        seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekbar, int i, boolean flag) {
                WindowManager.LayoutParams lp = getOwnerActivity().getWindow().getAttributes();
                lp.screenBrightness = Math.max(5, i) / 255f;
                getOwnerActivity().getWindow().setAttributes(lp);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekbar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekbar) {
            }
        });
    }
}
