package com.myapp.games.schnellen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /** ! method name referenced by strings.xml ! */
    public void buttonAction(View view) {
        switch(view.getId()) {
            case R.id.start_newButton:
                {
                    Intent i = new Intent(this, NewGameActivity.class);
                    startActivity(i);
                }
        }
    }
}
