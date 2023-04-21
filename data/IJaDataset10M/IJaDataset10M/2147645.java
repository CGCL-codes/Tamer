package com.amazon.merchants.remote;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class Emitter extends NotificationBroadcasterSupport implements EmitterMBean {

    private static int sequenceNumber = 0;

    public void touch(String type) {
        Notification notification = new Notification(type, this, Emitter.sequenceNumber++);
        sendNotification(notification);
    }
}
