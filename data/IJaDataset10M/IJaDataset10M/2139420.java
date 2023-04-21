package com.timothy.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.timothy.android.util.XMLUtil;
import com.timothy.config.Configuration;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessageReceiveService extends Service {

    private static final String LOG_TAG = "MessageReceiveService";

    public static final int MSG_REGISTER_CLIENT = 1;

    public static final int MSG_UNREGISTER_CLIENT = 2;

    public static final int MSG_QUESTION = 3;

    public static final int MSG_BASE_STATION = 4;

    public static final int MSG_SHORT_MESSAGE = 5;

    public static final int MSG_SLIDE_RESULT = 6;

    public static final int MSG_CLEAR = 7;

    public static final int MSG_CONNECT_BASE = 8;

    final Messenger rMessenger = new Messenger(new IncomingHandler());

    private static final int PORT = 9999;

    private static final String COMMAND_START = "<Message";

    private static final String COMMAND_END = "</Message>";

    List<Messenger> sMessengers = new ArrayList<Messenger>();

    @Override
    public IBinder onBind(Intent intent) {
        return rMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "MessageReceiveService.onCreate()...");
        Thread receiveThread = new ReceiveThread();
        receiveThread.start();
        Log.i(LOG_TAG, "receiveThread.start()...");
    }

    private void processCommand(String command) {
        Log.i(LOG_TAG, "----------------------processCommand start----------------------------");
        Object obj = null;
        int objType = 0;
        String type = XMLUtil.getPropertyValue(command, "Type");
        String content = XMLUtil.getChildren(command).get(0);
        Log.i(LOG_TAG, "command:" + command);
        Log.i(LOG_TAG, "type:" + type);
        Log.i(LOG_TAG, "content:" + content);
        if ("CommonQuestion".equals(type)) {
            obj = XMLUtil.getChildren(content).get(0);
            objType = MSG_QUESTION;
        } else if ("SlideResult".equals(type)) {
            obj = XMLUtil.getChildren(content).get(0);
            objType = MSG_SLIDE_RESULT;
        } else if ("Background".equals(type)) {
            String value = XMLUtil.getChildren(content).get(0);
            Configuration.background = XMLUtil.getElementValue(value);
        } else if ("BackLight".equals(type)) {
            String value = XMLUtil.getChildren(content).get(0);
            Configuration.backLight = Integer.valueOf(XMLUtil.getElementValue(value));
        } else if ("BubbleBackColor".equals(type)) {
            String value = XMLUtil.getChildren(content).get(0);
            Configuration.bubbleBackColor = XMLUtil.getElementValue(value);
        } else if ("ButtonBackColor".equals(type)) {
            String value = XMLUtil.getChildren(content).get(0);
            Configuration.buttonBackColor = XMLUtil.getElementValue(value);
        } else if ("BubbleFontColor".equals(type)) {
            String value = XMLUtil.getChildren(content).get(0);
            Configuration.bubbleFontColor = XMLUtil.getElementValue(value);
        } else if ("ButtonFontColor".equals(type)) {
            String value = XMLUtil.getChildren(content).get(0);
            Configuration.buttonFontColor = XMLUtil.getElementValue(value);
        } else if ("BubbleFontSize".equals(type)) {
            String value = XMLUtil.getChildren(content).get(0);
            Configuration.bubbleFontSize = Integer.valueOf(XMLUtil.getElementValue(value));
        } else if ("ButtonFontSize".equals(type)) {
            String value = XMLUtil.getChildren(content).get(0);
            Configuration.buttonFontSize = Integer.valueOf(XMLUtil.getElementValue(value));
        } else if ("BaseStations".equals(type)) {
            obj = content;
            objType = MSG_BASE_STATION;
        } else if ("ConnectBase".equalsIgnoreCase(type)) {
            obj = XMLUtil.getElementValue(XMLUtil.getChildren(content).get(0));
            objType = MSG_CONNECT_BASE;
        } else if ("ShortMessage".equals(type)) {
            obj = XMLUtil.getElementValue(XMLUtil.getChildren(content).get(0));
            objType = MSG_SHORT_MESSAGE;
        } else if ("ScreenClear".equals(type)) {
            obj = XMLUtil.getElementValue(XMLUtil.getChildren(content).get(0));
            objType = MSG_CLEAR;
        } else if ("Deleted".equals(type)) {
            obj = XMLUtil.getElementValue(XMLUtil.getChildren(content).get(0));
            objType = MSG_CLEAR;
        } else if ("true".equalsIgnoreCase(type)) {
            obj = XMLUtil.getElementValue(XMLUtil.getChildren(content).get(0));
            objType = MSG_SHORT_MESSAGE;
        } else {
            Log.i(LOG_TAG, "Message can not be parsed");
        }
        if (obj != null) {
            for (int i = sMessengers.size() - 1; i >= 0; i--) {
                try {
                    Log.i(LOG_TAG, "sMessengers.send " + i);
                    sMessengers.get(i).send(Message.obtain(null, objType, obj));
                } catch (RemoteException e) {
                    sMessengers.remove(i);
                }
            }
        }
        Log.i(LOG_TAG, "------------------------processCommand end--------------------------");
    }

    public class ReceiveThread extends Thread {

        public void run() {
            try {
                ServerSocket server = new ServerSocket(PORT);
                while (true) {
                    try {
                        Socket client = server.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        StringBuffer commond = new StringBuffer();
                        while (true) {
                            String msg = in.readLine();
                            if (msg != null) {
                                msg = msg.trim();
                                if (msg.startsWith(COMMAND_START)) {
                                    commond.setLength(0);
                                    commond.append(msg);
                                } else if (COMMAND_END.equalsIgnoreCase(msg)) {
                                    commond.append(msg);
                                    break;
                                } else {
                                    commond.append(msg);
                                }
                            } else {
                                break;
                            }
                        }
                        in.close();
                        client.close();
                        processCommand(commond.toString());
                    } catch (IOException e) {
                        Log.i("MessageReceiveService error: ", e.getMessage());
                    }
                }
            } catch (IOException e) {
                Log.i("MessageReceiveService error can't open port to listen: ", e.getMessage());
            }
        }
    }

    class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Log.i(LOG_TAG, "IncomingHandler.handleMessage(): " + msg.what);
            switch(msg.what) {
                case MSG_REGISTER_CLIENT:
                    sMessengers.add(msg.replyTo);
                    Log.i(LOG_TAG, "MSG_REGISTER_CLIENT->msg.replyTo:" + msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    sMessengers.remove(msg.replyTo);
                    Log.i(LOG_TAG, "MSG_UNREGISTER_CLIENT->msg.replyTo:" + msg.replyTo);
                    break;
            }
        }
    }
}
