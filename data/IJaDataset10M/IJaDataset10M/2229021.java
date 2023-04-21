package test.javax.management.remote.support;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

/**
 * @version $Revision: 1.3 $
 */
public class Marshalling implements MarshallingMBean, NotificationEmitter {

    private NotificationBroadcasterSupport nbs = new NotificationBroadcasterSupport();

    private long sequenceNo = 0;

    public Marshalling() {
    }

    public Marshalling(Unknown u) {
    }

    public Unknown unknownReturnValue() {
        return new Unknown();
    }

    public void unknownArgument(Unknown u) {
    }

    public Unknown getUnknownAttribute() {
        return new Unknown();
    }

    public void setUnknownAttribute(Unknown u) {
        Notification notification = new Notification(u.getClass().getName(), this, sequenceNo);
        sequenceNo++;
        nbs.sendNotification(notification);
    }

    public void removeNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws ListenerNotFoundException {
        nbs.removeNotificationListener(listener, filter, handback);
    }

    public MBeanNotificationInfo[] getNotificationInfo() {
        return nbs.getNotificationInfo();
    }

    public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws IllegalArgumentException {
        nbs.addNotificationListener(listener, filter, handback);
    }

    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
        nbs.removeNotificationListener(listener);
    }
}
