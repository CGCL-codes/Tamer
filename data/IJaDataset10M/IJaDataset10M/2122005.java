package com.fanfq;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

public class NETWork extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            if (cwjManager.getActiveNetworkInfo().isAvailable()) {
                Toast.makeText(NETWork.this, "net work succuess", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(NETWork.this, "net work fail", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(NETWork.this, "net work fail", Toast.LENGTH_LONG).show();
        } finally {
            this.finish();
        }
    }
}
