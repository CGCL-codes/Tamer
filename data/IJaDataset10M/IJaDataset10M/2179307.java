package com.manning.aip.canvasdemo;

import android.app.Activity;
import android.os.Bundle;

public class Canvas2DShapesAndTextBitmapActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShapesAndTextBitmapView view = new ShapesAndTextBitmapView(this);
        setContentView(view);
    }
}
