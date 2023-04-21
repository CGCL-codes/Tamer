package org.jboss.web.tomcat.service.session.notification;

/**
 * @author jean.deruelle@gmail.com
 * 
 */
public class ClusteredSipSessionNotificationCapability {

    /**
	 * Does the container support invoking <code>SipSessionListener</code>
	 * callbacks under the given conditions?
	 * 
	 * @param status
	 *            the status of the session
	 * @param cause
	 *            the cause of the session notification
	 * @param local
	 *            <code>true</code> if the event driving the notification
	 *            originated on this node; <code>false</code> otherwise
	 * 
	 * @return <code>true</code> if the notification is supported,
	 *         <code>false</code> if not
	 */
    public boolean isSipSessionListenerInvocationSupported(ClusteredSessionManagementStatus status, ClusteredSessionNotificationCause cause, boolean local) {
        return local && status.isLocallyUsed() && !ClusteredSessionNotificationCause.STATE_TRANSFER.equals(cause);
    }

    /**
	 * Under the given conditions, does the container support invoking
	 * <code>SipSessionAttributeListener</code> callbacks?
	 * 
	 * @param status
	 *            the status of the session
	 * @param cause
	 *            the cause of the session notification
	 * @param local
	 *            <code>true</code> if the event driving the notification
	 *            originated on this node; <code>false</code> otherwise
	 * 
	 * @return <code>true</code> if the notification is supported,
	 *         <code>false</code> if not
	 */
    public boolean isSipSessionAttributeListenerInvocationSupported(ClusteredSessionManagementStatus status, ClusteredSessionNotificationCause cause, boolean local) {
        return local && status.isLocallyUsed() && !ClusteredSessionNotificationCause.STATE_TRANSFER.equals(cause);
    }

    /**
	 * Under the given conditions, does the container support invoking
	 * <code>SipSessionBindingListener</code> callbacks?
	 * 
	 * @param status
	 *            the status of the session
	 * @param cause
	 *            the cause of the session notification
	 * @param local
	 *            <code>true</code> if the event driving the notification
	 *            originated on this node; <code>false</code> otherwise
	 * 
	 * @return <code>true</code> if the notification is supported,
	 *         <code>false</code> if not
	 */
    public boolean isSipSessionBindingListenerInvocationSupported(ClusteredSessionManagementStatus status, ClusteredSessionNotificationCause cause, boolean local) {
        return local && status.isLocallyUsed() && !ClusteredSessionNotificationCause.STATE_TRANSFER.equals(cause);
    }

    /**
	 * Is the container able to distinguish whether a session that has been
	 * {@link ClusteredSessionManagementStatus#isLocallyUsed() locally used} is
	 * also {@link ClusteredSessionManagementStatus#getLocallyActive() locally
	 * active}?
	 * 
	 * @return <code>true</code> if the container is able to make this
	 *         distinction; <code>false</code> if not
	 */
    public boolean isLocallyActiveAware() {
        return false;
    }

    /**
	 * Is the container able to distinguish whether a session is
	 * {@link ClusteredSessionManagementStatus#getLocallyOwned() locally owned}?
	 * 
	 * @return <code>true</code> if the container is able to make this
	 *         distinction; <code>false</code> if not
	 */
    public boolean isLocallyOwnedAware() {
        return false;
    }

    /**
	 * Returns whether the local container is aware of events on remote nodes
	 * that could give rise to notifications.
	 * 
	 * @param cause
	 *            the cause
	 * @return <code>true</code> if the local container is aware of the remote
	 *         event, <code>false</code> if not.
	 */
    public boolean isRemoteCauseAware(ClusteredSessionNotificationCause cause) {
        return ClusteredSessionNotificationCause.CREATE.equals(cause) || ClusteredSessionNotificationCause.MODIFY.equals(cause) || ClusteredSessionNotificationCause.INVALIDATE.equals(cause);
    }
}
