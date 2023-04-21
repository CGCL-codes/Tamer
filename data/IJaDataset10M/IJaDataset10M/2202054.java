package com.deroid.yang;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

class ViewFactory {

    public static final int BUTTON = 1;

    public static final int LIST = 2;

    public static final int CUBE = 3;

    public static final int BOOK = 4;

    public static final int TEMP = 5;

    public static View createView(int type, Context c) {
        switch(type) {
            case BUTTON:
                return new Button(c);
            case LIST:
                return new ListView(c);
            case CUBE:
                return new CubesurfaceView(c);
            case BOOK:
                return new Book(c);
            case TEMP:
                return new LinearLayout(c);
        }
        return null;
    }

    public static int getType(View v) {
        if (v instanceof Button) return BUTTON; else if (v instanceof ListView) return LIST; else if (v instanceof CubesurfaceView) return CUBE; else if (v instanceof Book) return BOOK; else if (v instanceof LinearLayout) return TEMP;
        return -1;
    }
}
