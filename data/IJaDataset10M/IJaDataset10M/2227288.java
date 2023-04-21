package com.example.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartPage_View extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startpage);
        Button createBtn = (Button) findViewById(R.id.startpage_newbutton);
        Button viewBtn = (Button) findViewById(R.id.startpage_viewbutton);
        createBtn.setOnClickListener(createBtnListener);
        viewBtn.setOnClickListener(viewBtnListener);
    }

    private OnClickListener createBtnListener = new OnClickListener() {

        public void onClick(View v) {
            Intent albumPreloadIntent = new Intent(StartPage_View.this, PreloadImage_View.class);
            StartPage_View.this.startActivity(albumPreloadIntent);
        }
    };

    private OnClickListener viewBtnListener = new OnClickListener() {

        public void onClick(View v) {
            Intent albumShelfIntent = new Intent(StartPage_View.this, AlbumShelf_View.class);
            StartPage_View.this.startActivity(albumShelfIntent);
        }
    };
}
