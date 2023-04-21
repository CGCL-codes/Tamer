package com.android.unit_tests.os;

import android.app.Service;
import android.content.Intent;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

public class MessengerService extends Service {

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Message reply = Message.obtain();
            reply.copyFrom(msg);
            try {
                msg.replyTo.send(reply);
            } catch (RemoteException e) {
            }
        }
    };

    private final Messenger mMessenger = new Messenger(mHandler);

    public MessengerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
