package com.thoughtworks.blipit.activities;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.thoughtworks.blipit.utils.BlipItUtils;

public class BlipNotificationClientHandler extends Handler implements ServiceConnection {

    private BlipItActivity blipItActivity;

    public BlipNotificationClientHandler(BlipItActivity blipItActivity) {
        this.blipItActivity = blipItActivity;
    }

    public void onServiceConnected(ComponentName componentName, IBinder service) {
        Messenger blipItNotificationService = new Messenger(service);
        try {
            Message message = Message.obtain(null, BlipItUtils.MSG_REGISTER_CLIENT);
            message.replyTo = blipItActivity.getBlipNotificationClientMessenger();
            blipItNotificationService.send(message);
            blipItActivity.setBlipItNotificationService(blipItNotificationService);
        } catch (RemoteException e) {
            blipItActivity.setBlipItNotificationService(null);
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        blipItActivity.setBlipItNotificationService(null);
    }

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what) {
            case BlipItUtils.MSG_BLIPS_UPDATED:
                blipItActivity.updateAds(msg.getData());
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
