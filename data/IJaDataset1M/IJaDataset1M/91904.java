package com.assoft.GraphicsExample;

import android.widget.LinearLayout;
import android.app.Activity;
import android.graphics.Canvas;

public class Desk extends LinearLayout {

    private Activity parent;

    public Desk(Activity aParent) {
        super(aParent);
        parent = aParent;
        setBackgroundColor(0xffffe67c);
    }

    private void init() {
        int fishkaWidth = getHeight() / 8;
        for (int i = 0; i < 7; i++) for (int j = 0; j < 7; j++) {
            addView(new Fishka(parent, i, j, fishkaWidth));
        }
    }

    private Boolean wasInit = false;

    @Override
    protected void onDraw(Canvas canvas) {
        if (!(wasInit)) {
            wasInit = true;
            init();
        }
        super.onDraw(canvas);
    }
}
