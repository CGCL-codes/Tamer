package com.turnpage;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class TurnPageActivity extends Activity {

    /** Called when the activity is first created. */
    public static PageManager pageManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Black_NoTitleBar_Fullscreen);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pageManager = new PageManager(this);
        BookView bv = new BookView(this);
        bv.setPosition(200, 30);
        bv.setSize(150, 200);
        bv.setPage(pageManager.getPageCount(), 0);
        bv.setDoublePage(true);
        setContentView(bv);
    }
}
