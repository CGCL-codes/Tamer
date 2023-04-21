package org.starmx.jmx.mbeanserver;

import javax.management.MBeanServerConnection;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

public interface MBeanServerService {

    public MBeanServerConnection getMBeanServer(String id);

    public void addMBeanRegistrationListener(String mbeanServerId, NotificationListener listener, NotificationFilter filter, Object handback) throws MBeanServerConnectionException;

    public void addMBeanRegistrationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws MBeanServerConnectionException;

    public void removeMBeanRegistrationListener(String mbeanServerId, NotificationListener listener) throws MBeanServerConnectionException;

    public void removeMBeanRegistrationListener(NotificationListener listener) throws MBeanServerConnectionException;
}
