package com.samples.alertdialogbutton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AlertDialogButtonActivity extends Activity {

    private final int IDD_EXIT = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button callButton = (Button) findViewById(R.id.button);
        callButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(IDD_EXIT);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case IDD_EXIT:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialogButtonActivity.this.finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.setCancelable(false);
                return builder.create();
            default:
                return null;
        }
    }
}
