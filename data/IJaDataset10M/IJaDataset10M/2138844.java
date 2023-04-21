package com.rit.secs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Configuration extends ListActivity {

    private static final String TAG = "ConfigurationMenu";

    public static boolean amIActive;

    private static ArrayAdapter<String> mArrayAdapter;

    private ArrayList<String> list_config;

    private static CountDownTimer counter;

    private long currentTime;

    private static long duration = 30000L;

    private Context ConfigContext;

    private ConnectThread mConnectThread;

    private int mState;

    public static final int STATE_NONE = 1;

    public static final int STATE_CONNECTING = 2;

    public static final int STATE_CONNECTED = 3;

    private static BluetoothDevice device = null;

    public static boolean autoConnect;

    private static Thread BTThread;

    private static final UUID MY_UUID = UUID.fromString("176c8f30-2fdc-486e-a284-b8ae61ecd540");

    private BluetoothAdapter mBluetoothAdapter = null;

    private String deviceAddress;

    private static final int REQUEST_CONNECT_DEVICE = 1;

    private static final int REQUEST_ENABLE_BT = 2;

    public void BT_Thread() {
        BTThread = new Thread(new Runnable() {

            public void run() {
                System.out.println("Inside BT_THREAD(), state = " + mState);
                while (autoConnect && (mState != STATE_CONNECTED)) {
                    if (mState == STATE_NONE) {
                        System.out.println("BT_Thread is attempting to connect");
                        connect(device);
                    }
                }
                if (counter != null) {
                    counter.cancel();
                }
            }
        });
        BTThread.start();
    }

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            System.out.println("Inside handler");
            if (mArrayAdapter.getItem(2).equals("Auto-connect (true)")) {
                mArrayAdapter.remove("Auto-connect (true)");
                mArrayAdapter.insert("Auto-connect (" + autoConnect + ")", 2);
            }
        }
    };

    private synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
        if (state == STATE_CONNECTED) {
            System.out.println("STATE = STATE_CONNECTED");
            autoConnect = false;
            mHandler.obtainMessage(1).sendToTarget();
            if (BTThread != null) {
                if (BTThread.isAlive()) {
                    System.out.println("BTThread resume");
                    if (BTThread.isInterrupted()) BTThread.resume();
                }
            }
        } else if (state == STATE_CONNECTING) {
            if (BTThread != null) {
                if (BTThread.isAlive()) {
                    System.out.println("BTThread interrupt");
                    if (BTThread.isInterrupted()) BTThread.interrupt();
                }
            }
        } else {
            if (BTThread != null) {
                if (BTThread.isAlive()) {
                    System.out.println("BTThread resume");
                    if (BTThread.isInterrupted()) BTThread.resume();
                }
            }
        }
    }

    private void createTimer(long duration) {
        counter = new CountDownTimer(duration, 1000) {

            public void onTick(long millisUntilFinished) {
                currentTime = millisUntilFinished / 1000;
                int remainingTime = (int) (millisUntilFinished / 1000);
                System.out.println("BT Timer remaining time = " + remainingTime);
            }

            public void onFinish() {
                System.out.println("BT Timer done");
                autoConnect = false;
                mArrayAdapter.remove("Auto-connect (true)");
                mArrayAdapter.insert("Auto-connect (" + autoConnect + ")", 2);
                Toast.makeText(ConfigContext, "Bluetooth Scan Timeout", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        amIActive = true;
        if (savedInstanceState != null) {
            System.out.println("onCreate savedInstace");
            duration = savedInstanceState.getLong("duration");
            currentTime = savedInstanceState.getLong("currentTime");
            autoConnect = savedInstanceState.getBoolean("autoConnect");
            deviceAddress = savedInstanceState.getString("MAC");
            SECS.threadWaitTime = savedInstanceState.getLong("threadWaitTime");
            list_config = savedInstanceState.getStringArrayList("list_config");
        }
        ConfigContext = this;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        setState(STATE_NONE);
        if (deviceAddress != null) {
            device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
        }
        System.out.println("onCreate().... autoConnect = " + autoConnect);
        list_config = new ArrayList<String>();
        int temp = (int) SECS.threadWaitTime / 1000;
        list_config.add("Adjust Camera Refresh Rate (" + temp + ")");
        temp = (int) duration / 1000;
        System.out.println("temp duration = " + temp);
        list_config.add("Adjust Bluetooth Scan Time (" + temp + ")");
        boolean enable = autoConnect;
        list_config.add("Auto-connect (" + enable + ")");
        if (device == null) {
            enable = false;
        } else {
            enable = true;
        }
        list_config.add("Pre-configure Bluetooth (" + enable + ")");
        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.configuration, list_config);
        setListAdapter(mArrayAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("MAC", deviceAddress);
        savedInstanceState.putBoolean("autoConnect", autoConnect);
        savedInstanceState.putLong("duration", duration);
        savedInstanceState.putLong("currentTime", currentTime);
        savedInstanceState.putLong("threadWaitTime", SECS.threadWaitTime);
        savedInstanceState.putStringArrayList("list_config", list_config);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        duration = savedInstanceState.getLong("duration");
        currentTime = savedInstanceState.getLong("currentTime");
        autoConnect = savedInstanceState.getBoolean("autoConnect");
        deviceAddress = savedInstanceState.getString("MAC");
        SECS.threadWaitTime = savedInstanceState.getLong("threadWaitTime");
        list_config = savedInstanceState.getStringArrayList("list_config");
    }

    @Override
    public void onStop() {
        super.onStop();
        amIActive = false;
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Object o = this.getListAdapter().getItem(position);
        String keyword = o.toString();
        if (keyword.startsWith("Pre-configure Bluetooth")) {
            if (mBluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth is not available on this device.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (!mBluetoothAdapter.isEnabled()) {
                    System.out.println("Enabling BT...");
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                } else {
                    Intent enableIntent = new Intent(this, DeviceListActivity.class);
                    startActivityForResult(enableIntent, REQUEST_CONNECT_DEVICE);
                }
            }
        } else if (keyword.startsWith("Auto-connect")) {
            final String key = keyword;
            int temp = mArrayAdapter.getPosition(key);
            System.out.println("auto-connect temp = " + temp);
            if (device == null) {
                Toast.makeText(this, "No pre-configured Bluetooth device.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (BTThread != null) {
                System.out.println(";;;;;; BTThread is not null");
                while (BTThread.isAlive()) {
                    System.out.println("BTThread is still alive......");
                    autoConnect = false;
                }
            }
            autoConnect = true;
            mArrayAdapter.remove(key);
            mArrayAdapter.insert("Auto-connect (" + autoConnect + ")", temp);
            BT_Thread();
            if (counter == null) {
                createTimer(duration);
            } else {
                counter.cancel();
                createTimer(duration);
            }
        } else if (keyword.startsWith("Adjust Camera Refresh Rate")) {
            final String key = keyword;
            final CharSequence[] items = getResources().getStringArray(R.array.refresh_rate_array);
            int currentTime = (int) SECS.threadWaitTime / 1000;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select new refresh rate (" + currentTime + ")");
            builder.setItems(items, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int item) {
                    String select = (items[item]).toString();
                    SECS.threadWaitTime = Long.valueOf(select.substring(0, select.indexOf(" "))) * 1000;
                    System.out.println("New Thread wait time = " + SECS.threadWaitTime);
                    int temp = mArrayAdapter.getPosition(key);
                    mArrayAdapter.remove(key);
                    mArrayAdapter.insert("Adjust Camera Refresh Rate (" + ((int) (SECS.threadWaitTime / 1000)) + ")", temp);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            System.out.println("Thread wait time = " + SECS.threadWaitTime);
        } else if (keyword.startsWith("Adjust Bluetooth Scan Time")) {
            final String key = keyword;
            final CharSequence[] items = getResources().getStringArray(R.array.bt_scan_array);
            int currentTime = (int) duration / 1000;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select new scan time (" + currentTime + ")");
            builder.setItems(items, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int item) {
                    String select = (items[item]).toString();
                    duration = Long.valueOf(select.substring(0, select.indexOf(" "))) * 1000;
                    System.out.println("New duration = " + duration);
                    int temp = mArrayAdapter.getPosition(key);
                    mArrayAdapter.remove(key);
                    mArrayAdapter.insert("Adjust Bluetooth Scan Time (" + ((int) (duration / 1000)) + ")", temp);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);
        switch(requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    deviceAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
                    mArrayAdapter.remove("Pre-configure Bluetooth (false)");
                    mArrayAdapter.insert("Pre-configure Bluetooth (true)", 3);
                    System.out.println("We paired with the device!");
                    Toast.makeText(this, "Pairing successful!", Toast.LENGTH_SHORT).show();
                    connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    if (device == null) {
                        Intent enableIntent = new Intent(this, DeviceListActivity.class);
                        startActivityForResult(enableIntent, REQUEST_CONNECT_DEVICE);
                    } else {
                        connect(device);
                    }
                } else {
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, "Unable to start Bluetooth", Toast.LENGTH_SHORT).show();
                }
        }
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        Log.d(TAG, "connect to: " + device);
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;

        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            System.out.println("Attempting to make a connection...");
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                System.out.println("Connection attempt failed");
            }
            mmSocket = tmp;
        }

        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try {
                System.out.println("XXXXXXXX mmSocket.connect!");
                mmSocket.connect();
            } catch (IOException connectException) {
                System.out.println("ConnectThread: Unable to make a connection AAAAAA");
                connectException.printStackTrace();
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    System.out.println("Unable to close the socket connection!!!!");
                }
                setState(STATE_NONE);
                return;
            }
            manageConnectedSocket(mmSocket);
        }

        public void manageConnectedSocket(BluetoothSocket socket) {
            final BluetoothSocket mmSocket = socket;
            System.out.println("ManageConnectedSocket!!!!");
            setState(STATE_CONNECTED);
            Thread connectedThread = new Thread() {

                public void run() {
                    OutputStream tmpOut = null;
                    try {
                        tmpOut = mmSocket.getOutputStream();
                    } catch (IOException e) {
                        Log.e(TAG, "temp sockets not created", e);
                    }
                    final OutputStream mmOutStream = tmpOut;
                    String message = ServerComm.grantViaBluetooth();
                    try {
                        mmOutStream.write(message.getBytes());
                        MediaPlayer mp = MediaPlayer.create(ConfigContext, R.raw.connected);
                        mp.start();
                    } catch (IOException e) {
                        System.out.println("WE HAVE BEEN DISCONNECTED!");
                    } finally {
                        setState(STATE_NONE);
                    }
                }
            };
            connectedThread.start();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
