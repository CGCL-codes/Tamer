package org.opennms.web.notification.bobject;

/**
 * An interface used to encapsulate targets for notifications
 * 
 * @author <A HREF="mailto:jason@opennms.org">Jason Johns </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 * 
 * @version 1.1.1.1
 */
public abstract class NotificationTarget {

    /**
     * The types of targets possible
     */
    public static final int TARGET_TYPE_USER = 1;

    public static final int TARGET_TYPE_NOTIF = 2;

    public static final int TARGET_TYPE_GROUP = 3;

    /**
     * Returns the type of a specific target
     */
    public abstract int getType();
}
