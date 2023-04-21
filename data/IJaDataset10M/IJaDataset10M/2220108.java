package org.purdue.acm.sigapp.mortarboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class Main extends Activity {

    private final int SETTINGS = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ImageView main = (ImageView) findViewById(R.id.icon);
        main.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Blah", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void showSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void showHelp() {
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.id.help, 0, "Help");
        menu.add(0, R.id.settings, 0, "Settings");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                showSettings();
                return true;
            case R.id.help:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
