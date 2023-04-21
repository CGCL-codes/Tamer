package com.timothy.android;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.timothy.android.BaseActivity.EneterIncomingHandler;
import com.timothy.android.handler.MyCrashHandler;
import com.timothy.android.util.ActivityStackControlUtil;
import com.timothy.android.util.SysUtil;
import com.timothy.service.BackLightService;
import com.timothy.service.CommunicationService;
import com.timothy.service.MessageReceiveService;
import com.timothy.service.MessageSenderUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.SharedPreferences;

public class LoginActivity extends Activity {

    /** Called when the activity is first created. */
    private static final String LOG_TAG = "LoginActivity";

    private EditText hostAddr = null;

    private EditText hostPort = null;

    private Button loginBtn = null;

    private LinearLayout layout1;

    SharedPreferences sharedPreferences;

    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "onStart()...");
        int flag = getIntent().getIntExtra("flag", 0);
        Log.i(LOG_TAG, "flag=" + flag);
        if (flag == SysUtil.EXIT_APPLICATION) {
            finish();
        }
        super.onResume();
    }

    @Override
    protected void onResume() {
        super.onStart();
        Log.i(LOG_TAG, "onResume()...");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(LOG_TAG, "onNewIntent()...");
        int flag = getIntent().getIntExtra("flag", 0);
        Log.i(LOG_TAG, "flag=" + flag);
        if (flag == SysUtil.EXIT_APPLICATION) {
            finish();
        }
        super.onNewIntent(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackControlUtil.add(this);
        setContentView(R.layout.login);
        Log.i(LOG_TAG, "onResume()...");
        this.setTitle("(Address:" + getWifiIP() + ")");
        hostAddr = (EditText) this.findViewById(R.id.hostAddr);
        hostPort = (EditText) this.findViewById(R.id.hostPort);
        sharedPreferences = this.getSharedPreferences("powersmart", MODE_PRIVATE);
        String preHostAddr = sharedPreferences.getString("pcip", null);
        int preHostPort = sharedPreferences.getInt("pcport", 0);
        Log.i(LOG_TAG, "load history from sharedPreferences:host=" + preHostAddr);
        Log.i(LOG_TAG, "load history from sharedPreferences:port=" + preHostPort);
        if (preHostAddr != null) hostAddr.setText(preHostAddr);
        if (preHostPort != 0) hostPort.setText(String.valueOf(preHostPort));
        loginBtn = (Button) this.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                MessageSenderUtil.PC_IP = hostAddr.getText().toString();
                MessageSenderUtil.PC_PORT = Integer.valueOf(hostPort.getText().toString());
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                if (!wifiManager.isWifiEnabled()) {
                    ShowDialog("Wifi is not available!");
                    return;
                }
                MessageSenderUtil.PHONE_IP = getWifiIP();
                if (MessageSenderUtil.PHONE_IP == null || MessageSenderUtil.PHONE_IP.equals("") || MessageSenderUtil.PHONE_IP.equals("0.0.0.0")) {
                    ShowDialog("Your mobile is not in a available NetWork!");
                    return;
                } else {
                    doBindService();
                }
            }
        });
    }

    protected void processMessage(int what, String content) {
        Log.i(LOG_TAG, "what=" + what + " content=" + content);
        if (MessageReceiveService.MSG_BASE_STATION == what) {
            Intent intent = new Intent();
            intent.putExtra("baseStationStr", content);
            intent.putExtra("hostIp", hostAddr.getText().toString());
            intent.putExtra("hostPort", Integer.valueOf(hostPort.getText().toString()));
            intent.setClass(getBaseContext(), BaseStations.class);
            startActivity(intent);
        }
    }

    /**
	 * 
	 * @param ipAddress
	 * @return
	 */
    public boolean pingIP(String ipAddress) {
        Log.i(LOG_TAG, "Ping Poller Starts...");
        boolean pingResult = true;
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            System.out.println("Sending Ping Request to " + ipAddress);
            Log.i(LOG_TAG, "IP:" + ipAddress);
            boolean status = inet.isReachable(5000);
            if (status) {
                Log.i(LOG_TAG, "Status : Host is reachable");
            } else {
                Log.i(LOG_TAG, "Status : Host is not reachable");
                pingResult = false;
            }
        } catch (UnknownHostException e) {
            System.err.println("Host does not exists");
            Log.i(LOG_TAG, "Host does not exists");
            pingResult = false;
        } catch (IOException e) {
            System.err.println("Error in reaching the Host");
            Log.i(LOG_TAG, "Error in reaching the Host");
            pingResult = false;
        }
        return pingResult;
    }

    private String getWifiIP() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        Log.i("BaseStations", "WIFI IP:" + ip);
        return ip;
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    void doBindService() {
        Log.i(LOG_TAG, "start bind service CommunicationService...");
        bindService(new Intent(LoginActivity.this, CommunicationService.class), commConnection, Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (eneterIsBound) {
            unbindService(commConnection);
            eneterIsBound = false;
        }
    }

    private Messenger eneterRMessenger = new Messenger(new EneterIncomingHandler());

    private Messenger eneterSMessenger = null;

    private boolean eneterIsBound;

    private ServiceConnection commConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i(LOG_TAG, "commConnection.onServiceConnected()...");
            eneterSMessenger = new Messenger(service);
            try {
                Message msg = Message.obtain(null, CommunicationService.MSG_REGISTER_CLIENT);
                msg.replyTo = eneterRMessenger;
                eneterSMessenger.send(msg);
            } catch (RemoteException e) {
            }
            eneterIsBound = true;
            String message = MessageSenderUtil.addHeader(MessageSenderUtil.Get_Base_Stations);
            if (!sendMessage(message)) {
                ShowDialog("Can not login Server!");
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("pcip", hostAddr.getText().toString());
                editor.putInt("pcport", Integer.valueOf(hostPort.getText().toString()));
                editor.commit();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.i(LOG_TAG, "commConnection.onServiceDisconnected()...");
            Message msg = Message.obtain(null, CommunicationService.MSG_UNREGISTER_CLIENT);
            msg.replyTo = eneterRMessenger;
            try {
                eneterSMessenger.send(msg);
            } catch (RemoteException e) {
            }
            eneterIsBound = false;
        }
    };

    public boolean sendMessage(String command) {
        Log.i(LOG_TAG, "command:" + command);
        Message msg = Message.obtain(null, CommunicationService.MSG_SEND_SERVER, command);
        try {
            eneterSMessenger.send(msg);
            Log.i(LOG_TAG, "CommunicationService.MSG_SEND_SERVERE=" + CommunicationService.MSG_SEND_SERVER);
        } catch (RemoteException e) {
            Log.i(LOG_TAG, "RemoteException:" + e.getMessage());
        }
        return true;
    }

    class EneterIncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Log.i(LOG_TAG, "EneterIncomingHandler.handleMessage() Start...");
            processMessage(msg.what, (String) msg.obj);
            Log.i(LOG_TAG, "msg.what=" + msg.what);
            Log.i(LOG_TAG, "msg.obj=" + msg.obj);
            Log.i(LOG_TAG, "EneterIncomingHandler.handleMessage() End.");
        }
    }

    public void ShowDialog(String msg) {
        new AlertDialog.Builder(this).setTitle("提示").setMessage(msg).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                ActivityStackControlUtil.finishProgram();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
