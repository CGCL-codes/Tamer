package com.example.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class AlbumEditor_View extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albumeditor);
        Button addtagBtn = (Button) findViewById(R.id.albumeditor_addtagbutton);
        Button addlinkBtn = (Button) findViewById(R.id.albumeditor_addlinkbutton);
        Button dummytagBtn = (Button) findViewById(R.id.albumeditor_dummytagbutton);
        Button dummylinkBtn = (Button) findViewById(R.id.albumeditor_dummylinkbutton);
        Button backToParentBtn = (Button) findViewById(R.id.albumeditor_backtoparentbutton);
        Button doneAndSaveBtn = (Button) findViewById(R.id.albumeditor_donebutton);
        Button openToolBarBtn = (Button) findViewById(R.id.albumeditor_opentoolbarbutton);
    }
}
