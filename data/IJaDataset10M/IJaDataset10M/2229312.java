package wsl.fw.notification;

import java.rmi.RemoteException;
import java.io.Serializable;
import wsl.fw.remote.RmiServant;

/**
 * Notifier interface, handles the registration of listeners and propogation
 * of generic remote notifications.
 */
public interface Notifier extends RmiServant {

    /** Receive all notifications, irrespective of type or subtype. use only
     * for listening, not for sending */
    public static final String NT_ALL = "wsl.fw.notification.type._all_";

    /** Receive all notifications irrespective of subtype, use only for
     * listening, not sending */
    public static final String NST_ALL = "wsl.fw.notification.subtype._all_";

    /** Used to send or recieve notifications that do not have a subtype */
    public static final String NST_NONE = "wsl.fw.notification.subtype._none_";

    /**
     * Send a notification to the Notifier which will be propogated to all
     * listeners that have registered interest in the notificationType and
     * subtype. NT_ALL and NST_ALL may not be used.
     * @param notificationType, a string specifying the type of notification,
     *   used to determine which listeners to notify. Not null.
     * @param notificationSubtype, a string specifying the subtype of the
     *   notification, used to determine which listeners to notify. Not null.
     *   If there is no subtype use NST_NONE.
     * @param notificationData, notification information specific to this
     *   notificationType. Listeners are expected to know how to interpret this
     *   data and perform appropriate processing. May be null. May be a
     *   DataObject if the notification server implementation sets a
     *   DataManager. Must be Serializable.
     * @throws RemoteException if there is an RMI error.
     */
    public void sendNotification(String notificationType, String notificationSubtype, Serializable notificationData) throws RemoteException;

    /**
     * Add a new NotificationListener for the specified notification type.
     * It is ok (and advisable as unresponsive listeners are culled) to call
     * this function more than once for the same listener. It is ok for the same
     * listener to listen to multiple notification types.
     * @param listener, the NotificationListener that will be called whenever
     *   a notification event of the specified type occurs. Not null.
     * @param notificationType, a String specifying the type of notification to
     *   listen for. Not null. If NT_ALL then subtype is ignored and all
     *   notifications are listened to.
     * @param notificationSubtype, a string specifying the subtype of the
     *   notification, used to determine which listeners to notify. Not null.
     *   If there is no subtype use NST_NONE. If NST_ALL then all notification
     *   subtypes are listened to.
     * @throws RemoteException if there is an RMI error.
     */
    public void addNotificationListener(NotificationListener listener, String notificationType, String notificationSubtype) throws RemoteException;

    /**
     * Remove the NotificationListener so it no longer receives notifications
     * of the specified type. Should be called before exit by any program that
     * has added notification listeners to remove those listeners.
     * @param listener, the NotificationListener to remove. Not null.
     * @param notificationType, a String specifying the type of notification
     *   that will no longer be listened for. If NT_ALL subtype is ignored and
     *   this listener is removed  for all notification types.
     * @param notificationSubtype, a string specifying the subtype of the
     *   notification, that will not longer be listened to. Not null.
     *   If there is no subtype use NST_NONE. If NST_ALL then all notification
     *   subtypes are removed.
     * @throws RemoteException if there is an RMI error.
     */
    public void removeNotificationListener(NotificationListener listener, String notificationType, String notificationSubtype) throws RemoteException;
}
