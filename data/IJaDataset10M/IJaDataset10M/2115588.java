package services.notification.impl;

import javax.xml.namespace.QName;

/**
 * Ein einfaches Interface, das das Benennen und Auffinden von Resourcen anhand von Namen
 * vereinfacht.
 * @author rene
 */
public interface NotificationQNames {

    public static final String NS = "http://services.localhost.org/Notification/NotificationService";

    public static final QName RESOURCE_PROPERTIES = new QName(NS, "NotificationResourceProperties");

    public static final QName RESOURCE_REFERENCE = new QName(NS, "NotificationResourceReference");

    public static final QName RP_MAILINGLISTS = new QName(NS, "mailingLists");
}
