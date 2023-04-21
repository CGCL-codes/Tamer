package org.jmx4odp.notificationWorkers;

/**
 * Objects must implement this interface to be regsitered with NotificationWorkers.
 * When the NotificationWorker receives a notification that passes the filters,
 * it will pass it to its registered NotificationProcessor. When the NotificationWorker
 * is set to active, it will cann preStart on the NotificationProcessor. When the object
 * is set to inactive, it will call postStop. If the NotificationWorker's NotifcationProcessor
 * is set to null, it will set active to false.
 *
 * @author  Lucas McGregor
 * @see org.jmx4odp.notificationWorkers.NotificationWorker
 */
public interface NotificationProcessor {

    public void preStart() throws Exception;

    public void postStop() throws Exception;

    public void processNotification(javax.management.Notification notif, java.lang.Object obj);
}
