package org.jazzteam.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ViewsFromCode extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.views);
        final Button firstButton = (Button) findViewById(R.id.first_button);
        final Button secondButton = (Button) findViewById(R.id.second_button);
        OnClickListener oclBtnOk = new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                secondButton.setEnabled(false);
            }
        };
        OnClickListener oclBtnOk2 = new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                firstButton.setEnabled(false);
            }
        };
        firstButton.setOnClickListener(oclBtnOk);
        secondButton.setOnClickListener(oclBtnOk2);
    }
}
