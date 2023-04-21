package org.pannotas;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class PanNotasActivity extends Activity {

    RepositorySqlite rep;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        rep = new RepositorySqlite();
        rep.openAndroid(this, null);
        rep.writePage("test1", "what is going on");
        TextView tv = new TextView(this);
        tv.setText(rep.readPage("test1"));
        setContentView(tv);
        rep.close();
    }
}
