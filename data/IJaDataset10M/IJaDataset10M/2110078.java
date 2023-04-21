package com.example.android.backuprestore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class BackupRestoreActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button buttonLogin = (Button) this.findViewById(R.id.login);
        buttonLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                displayMes("����˵�¼");
                Intent intent = new Intent();
                intent.setClass(BackupRestoreActivity.this, MainActivity.class);
                startActivity(intent);
                BackupRestoreActivity.this.finish();
            }
        });
    }

    public void displayMes(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_LONG).show();
    }
}
