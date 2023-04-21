package org.nightlabs.jfire.base.jdo.notification;

import org.nightlabs.jfire.jdo.notification.IJDOLifecycleListenerFilter;

/**
 * There are many interfaces extending this basic interface. You <b>must not</b> directly
 * implement this interface, because the sub-interface decides about which thread will be
 * used for execution. In most cases, you'd probably want to extend
 * {@link JDOLifecycleAdapterJob}.
 *
 * @author Marco Schulze - marco at nightlabs dot de
 */
public interface JDOLifecycleListener {

    void setActiveJDOLifecycleEvent(JDOLifecycleEvent jdoLifecycleEvent);

    JDOLifecycleEvent getActiveJDOLifecycleEvent();

    /**
	 * @return Returns the filter defining in which events this listener is interested.
	 */
    IJDOLifecycleListenerFilter getJDOLifecycleListenerFilter();

    /**
	 * This method is triggered on the client side, whenever a JDO object has been
	 * newly created / modified / deleted and the filter matched on the server-side.
	 *
	 * @param event The event containing detailed information about what happened.
	 */
    void notify(JDOLifecycleEvent event);
}
