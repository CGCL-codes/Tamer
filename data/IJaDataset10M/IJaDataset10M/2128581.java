package org.bupt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bupt.plugin.IBinaryOp;

public class PluginActivity extends Activity {

    private IBinaryOp sampleServiceIf;

    private final String lOG_TAG = "Service";

    private ButtonListener buttonListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        buttonListener = new ButtonListener(this);
        Intent intent = new Intent("PluginService");
        bindService(intent, sampleServiceConn, BIND_AUTO_CREATE);
        TextView textA = (TextView) findViewById(R.id.TextView01);
        TextView textB = (TextView) findViewById(R.id.TextView02);
        Button button1 = (Button) findViewById(R.id.Button1);
        button1.setOnClickListener(buttonListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(sampleServiceConn);
    }

    private class ButtonListener implements OnClickListener {

        private Context mContext;

        public ButtonListener(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public void onClick(View v) {
            try {
                String[] temp = sampleServiceIf.getUserInfo("hpc");
                Toast.makeText(mContext, "the result " + temp[0] + " " + temp[1] + " " + temp[2], Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private ServiceConnection sampleServiceConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(lOG_TAG, "Connected!");
            sampleServiceIf = IBinaryOp.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            sampleServiceIf = null;
        }
    };
}
