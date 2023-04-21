package com.continuent.tungsten.commons.patterns.notification.adaptor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import com.continuent.tungsten.commons.cluster.resource.notification.ClusterResourceNotification;
import com.continuent.tungsten.commons.patterns.notification.ResourceNotificationListener;

public class ResourceNotificationListenerStub implements ResourceNotificationListener, Runnable {

    private Logger logger = Logger.getLogger(ResourceNotificationListenerStub.class);

    private BlockingQueue<ClusterResourceNotification> notifications = new LinkedBlockingQueue<ClusterResourceNotification>();

    Thread monitorThread = null;

    private String type;

    public void init(String type) {
        this.type = type;
    }

    public void start() {
        monitorThread = new Thread(this, this.getClass().getSimpleName());
        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.commons.patterns.notification.ResourceNotificationListener#notify(ClusterResourceNotification)
     */
    public void notify(ClusterResourceNotification notification) {
        logger.debug("GOT NOTIFICATION TYPE=" + type + ":" + notification);
        try {
            notifications.put(notification);
        } catch (InterruptedException i) {
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        ClusterResourceNotification notification = null;
        try {
            while ((notification = notifications.take()) != null) {
                logger.debug("PROCESSING NOTIFICATION FOR TYPE=" + type + ":" + notification);
            }
        } catch (InterruptedException i) {
        }
    }

    public void setData(Object data) {
    }
}
