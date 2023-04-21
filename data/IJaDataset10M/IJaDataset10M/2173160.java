package org.opennms.web.notification.filter;

import org.opennms.web.filter.EqualsFilter;
import org.opennms.web.filter.SQLType;

public class NotificationIdFilter extends EqualsFilter<Integer> {

    public static final String TYPE = "notificationIdFilter";

    public NotificationIdFilter(int notificationId) {
        super(TYPE, SQLType.INT, "NOTIFICATIONS.NOTIFYID", "notifyId", new Integer(notificationId));
    }
}
