package com.samples.progressdialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ProgressDialogActivity extends Activity {

    static final int IDD_PROGRESS = 0;

    ProgressThread mProgressThread;

    ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button callButton = (Button) findViewById(R.id.button);
        callButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                showDialog(IDD_PROGRESS);
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case IDD_PROGRESS:
                mProgressDialog = new ProgressDialog(ProgressDialogActivity.this);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setMessage("Loading...");
                mProgressThread = new ProgressThread(handler);
                mProgressThread.start();
                return mProgressDialog;
            default:
                return null;
        }
    }

    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            int total = msg.getData().getInt("total");
            mProgressDialog.setProgress(total);
            if (total >= 100) {
                dismissDialog(IDD_PROGRESS);
                mProgressThread.setState(ProgressThread.STATE_DONE);
                Toast.makeText(getApplicationContext(), "Task is finished", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
