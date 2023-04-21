package org.jmx4odp.notificationWorkers;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.MBeanNotificationInfo;
import javax.management.ListenerNotFoundException;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;

/**
 * This Object simplifies the implementation of NotificationEmitters. You can 
 * either extend or aggregate this object into an existing class.
 * 
 * The JMX
 *
 * @author Lucas McGregor
 */
public class ThreadedNotificationEmitter implements NotificationEmitter, NotificationListener {

    private final ArrayList<NotificationHandle> listeners = new ArrayList();

    private MBeanNotificationInfo[] mbeanNotificationInfo = null;

    public ThreadedNotificationEmitter() {
    }

    /**
     * Remove the combo of listener, filter, and handback.
     * @param listner
     * @param filter
     * @param handback
     * @throws javax.management.ListenerNotFoundException
     */
    public void removeNotificationListener(NotificationListener listner, NotificationFilter filter, Object handback) throws ListenerNotFoundException {
        if (listeners.size() == 0) {
            return;
        }
        synchronized (listeners) {
            NotificationHandle nh = new NotificationHandle();
            nh.filter = filter;
            nh.listener = listner;
            nh.handback = handback;
            listeners.remove(nh);
        }
    }

    /**
     * This will add the listener/filter/handback combo if they have
     * not been addeded before. If you add the same listener multiple times, but
     * with different filters or handbacks, it will get called once for each time it
     * was sucessfully added.
     *
     * So if you have filters that are not multially exclusive, you can get the same
     * event multiple times.
     * 
     * @param listner
     * @param filter
     * @param handback
     * @throws java.lang.IllegalArgumentException
     */
    public void addNotificationListener(NotificationListener listner, NotificationFilter filter, Object handback) throws IllegalArgumentException {
        NotificationHandle nh = new NotificationHandle();
        nh.filter = filter;
        nh.listener = listner;
        nh.handback = handback;
        if (listeners.contains(nh)) {
            return;
        }
        synchronized (listeners) {
            listeners.add(nh);
        }
    }

    /**
     * This will find ALL the listners that match and remove them.
     * It will not care about handbacks or filters.
     * @param listener
     * @throws javax.management.ListenerNotFoundException
     */
    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
        if (listener == null) {
            throw new ListenerNotFoundException("Listener in arg is null");
        }
        if (listeners.size() == 0) {
            throw new ListenerNotFoundException("This object has no Listners");
        }
        ArrayList<NotificationHandle> itemsToBeRemoved = new ArrayList();
        synchronized (listeners) {
            for (NotificationHandle nh : listeners) {
                if (nh.listener.equals(listener)) {
                    itemsToBeRemoved.add(nh);
                }
            }
        }
        listeners.removeAll(itemsToBeRemoved);
    }

    public MBeanNotificationInfo[] getNotificationInfo() {
        return mbeanNotificationInfo;
    }

    /**
     * Set the MBeanNotificationInfo[] that will be returned by getNotificationInfo()
     * @param mbeanNotificationInfo the mbeanNotificationInfo to set
     */
    public void setNotificationInfo(MBeanNotificationInfo[] mbeanNotificationInfo) {
        this.mbeanNotificationInfo = mbeanNotificationInfo;
    }

    /**
     * This will cycle through all listeners.
     * If the filters pass, send the notification
     * It will spawn a ne thread to handle each notification.
     * @param event
     * @param handback this object is ignored and should be left null. It
     * will use the one set during addListener
     */
    public void handleNotification(Notification event, Object handback) {
        synchronized (listeners) {
            for (NotificationHandle nh : listeners) {
                if (nh.filter == null) {
                    processNotification(event, nh.listener, nh.handback);
                } else if (nh.filter.isNotificationEnabled(event)) {
                    processNotification(event, nh.listener, nh.handback);
                }
            }
        }
    }

    /**
     * Create an instance of the workerThread to handle
     * the work of notification.
     **/
    private void processNotification(Notification notification, NotificationListener listener, Object handback) {
        try {
            WorkerThread wt = new WorkerThread(notification, listener, handback);
            wt.start();
        } catch (Exception e) {
        }
    }

    class NotificationHandle {

        public NotificationListener listener;

        public NotificationFilter filter;

        public Object handback;

        public boolean equals(Object aThat) {
            if (this == aThat) {
                return true;
            }
            if (!(aThat instanceof NotificationHandle)) {
                return false;
            }
            NotificationHandle that = (NotificationHandle) aThat;
            return ((areEqual(this.listener, that.listener)) && (areEqual(this.filter, that.filter)) && (areEqual(this.handback, that.handback)));
        }

        private boolean areEqual(Object one, Object two) {
            if ((one != null) && (two == null)) {
                return false;
            }
            if ((one == null) && (two != null)) {
                return false;
            }
            return (one.equals(two));
        }

        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + ((this.listener == null) ? 0 : this.listener.hashCode());
            hash = 31 * hash + ((this.filter == null) ? 0 : this.filter.hashCode());
            hash = 31 * hash + ((this.handback == null) ? 0 : this.handback.hashCode());
            return hash;
        }
    }

    /**
     * Inner class that extends Thread. It handles the actual notification
     * of a listener on the seperate thread so that simultanous notifications
     * may occur. All WorkerThreads are started as daemons so that they will
     * not keep running if the rest of the JVM's threads have stopped.
     **/
    class WorkerThread extends Thread {

        private Notification notification = null;

        private NotificationListener listener = null;

        private Object handbackObject = null;

        public WorkerThread(Notification not, NotificationListener list, Object handback) {
            notification = not;
            listener = list;
            handbackObject = handback;
            setDaemon(true);
        }

        public void run() {
            try {
                listener.handleNotification(notification, handbackObject);
            } catch (Exception e) {
            }
        }
    }
}
