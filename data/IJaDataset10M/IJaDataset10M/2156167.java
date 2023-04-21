package org.mnb;

import storage.Storage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class RepiolaActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Storage.getInstance(this.getApplicationContext()).load("code");
        setContentView(R.layout.main);
        assignListeners();
    }

    private void runScript() {
        TextView textEditor = (TextView) findViewById(R.id.text);
        Storage.getInstance(this.getApplicationContext()).save("code", textEditor.getText().toString());
        startActivity(new Intent(this, DisplayActivity.class));
    }

    private void exit() {
        this.finish();
    }

    private void assignListeners() {
        View runButton = findViewById(R.id.run_button);
        runButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                runScript();
            }
        });
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                exit();
            }
        });
    }
}
