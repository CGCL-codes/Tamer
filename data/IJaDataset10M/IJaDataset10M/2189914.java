package org.sempere.commons.jmx;

import org.sempere.commons.base.*;
import org.slf4j.*;
import javax.management.*;
import javax.management.remote.*;
import java.io.*;
import java.net.*;
import java.util.*;
import static javax.management.remote.JMXConnector.*;

/**
 * Helper class that provides several methods to work with JMX.
 *
 * @author bsempere
 */
public final class JMXHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JMXHelper.class);

    private JMXHelper() {
    }

    public static void registerMBean(String objectName, Class<?> clazz, MBeanServerConnection connection) {
        registerMBean(createObjectName(objectName), clazz, connection);
    }

    public static void registerMBean(String objectName, String clazzName, MBeanServerConnection connection) {
        registerMBean(createObjectName(objectName), clazzName, connection);
    }

    public static void registerMBean(ObjectName objectName, Class<?> clazz, MBeanServerConnection connection) {
        registerMBean(objectName, clazz.getName(), connection);
    }

    public static void registerMBean(ObjectName objectName, String clazzName, MBeanServerConnection connection) {
        try {
            LOGGER.debug("About to register MBean with name [" + objectName + "]");
            connection.createMBean(clazzName, objectName);
            LOGGER.debug("MBean with name [" + objectName + "] registered successfully");
        } catch (Exception e) {
            throw new JMXException("Cannot register MBean [" + objectName + "].", e);
        }
    }

    public static void unregisterMBean(String objectName, MBeanServerConnection connection) {
        unregisterMBean(createObjectName(objectName), connection);
    }

    public static void unregisterMBean(ObjectName objectName, MBeanServerConnection connection) {
        try {
            LOGGER.debug("About to unregister MBean with name [" + objectName + "]");
            connection.unregisterMBean(objectName);
            LOGGER.debug("MBean with name [" + objectName + "] unregistered successfully");
        } catch (Exception e) {
            throw new JMXException("Cannot unregister MBean [" + objectName + "].", e);
        }
    }

    public static ObjectName queryObjectName(String objectNamePattern, MBeanServerConnection connection) {
        try {
            return queryObjectName(new ObjectName(objectNamePattern), connection);
        } catch (MalformedObjectNameException e) {
            throw new JMXException("Unable to find MBeans that match pattern [" + objectNamePattern + "].", e);
        } catch (NullPointerException e) {
            throw new JMXException("Unable to find MBeans that match pattern [" + objectNamePattern + "].", e);
        }
    }

    public static ObjectName queryObjectName(ObjectName objectNamePattern, MBeanServerConnection connection) {
        LOGGER.debug("About to find the first MBean that match pattern [" + objectNamePattern + "].");
        Collection<ObjectName> objectNames = queryObjectNames(objectNamePattern, connection);
        ObjectName objectName = null;
        if (!objectNames.isEmpty()) {
            objectName = objectNames.iterator().next();
            LOGGER.debug("MBean with name [" + objectName.getCanonicalName() + "] was found.");
        }
        return objectName;
    }

    public static Collection<ObjectName> queryObjectNames(String objectNamePattern, MBeanServerConnection connection) {
        try {
            return queryObjectNames(new ObjectName(objectNamePattern), connection);
        } catch (MalformedObjectNameException e) {
            throw new JMXException("Unable to find MBeans that match pattern [" + objectNamePattern + "].", e);
        } catch (NullPointerException e) {
            throw new JMXException("Unable to find MBeans that match pattern [" + objectNamePattern + "].", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Collection<ObjectName> queryObjectNames(ObjectName objectNamePattern, MBeanServerConnection connection) {
        try {
            LOGGER.debug("About to find MBeans that match pattern [" + objectNamePattern.getCanonicalName() + "].");
            return connection.queryNames(objectNamePattern, null);
        } catch (IOException e) {
            throw new JMXException("Unable to find MBeans that match pattern [" + objectNamePattern + "].", e);
        }
    }

    public static <T> T getMBeanProxy(String objectName, Class<T> clazz, boolean isNotificationBroadcaster, MBeanServerConnection connection) {
        return getMBeanProxy(createObjectName(objectName), clazz, isNotificationBroadcaster, connection);
    }

    public static <T> T getMBeanProxy(String objectName, String clazzName, boolean isNotificationBroadcaster, MBeanServerConnection connection) {
        Class<T> clazz = ReflectionHelper.loadClass(clazzName);
        return getMBeanProxy(objectName, clazz, isNotificationBroadcaster, connection);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getMBeanProxy(ObjectName objectName, Class<T> clazz, boolean isNotificationBroadcaster, MBeanServerConnection connection) {
        return (T) MBeanServerInvocationHandler.newProxyInstance(connection, objectName, clazz, isNotificationBroadcaster);
    }

    public static <T> T getMBeanProxy(ObjectName objectName, String clazzName, boolean isNotificationBroadcaster, MBeanServerConnection connection) {
        Class<T> clazz = ReflectionHelper.loadClass(clazzName);
        return getMBeanProxy(objectName, clazz, isNotificationBroadcaster, connection);
    }

    public static ObjectName createObjectName(String objectName) {
        try {
            ObjectName toReturn = new ObjectName(objectName);
            LOGGER.debug("ObjectName [" + toReturn + "] created successfully.");
            return toReturn;
        } catch (NullPointerException e) {
            throw new JMXException("Cannot create ObjectName when name is null.", e);
        } catch (MalformedObjectNameException e) {
            throw new JMXException("Cannot create ObjectName with name [" + objectName + "] because it is malformed.", e);
        }
    }

    public static JMXServiceURL createServiceURL(String url) {
        try {
            JMXServiceURL serviceURL = new JMXServiceURL(url);
            LOGGER.debug("JMXServiceURL [" + serviceURL + "] created successfully.");
            return serviceURL;
        } catch (MalformedURLException e) {
            throw new JMXException("Cannot create JMXServiceURL from URL [" + url + "].", e);
        }
    }

    public static JMXServiceURL createServiceURL(String protocol, String host, int port, String urlPath) {
        try {
            JMXServiceURL serviceURL = new JMXServiceURL(protocol, host, port, urlPath);
            LOGGER.debug("JMXServiceURL [" + serviceURL + "] created successfully.");
            return serviceURL;
        } catch (MalformedURLException e) {
            throw new JMXException("Cannot create JMXServiceURL from protocol [" + protocol + "], host [" + host + "], port [" + port + "], urlPath [" + urlPath + "].", e);
        }
    }

    public static JMXConnector createConnector(JMXServiceURL serviceURL, String user, String password, boolean connect) {
        return createConnector(serviceURL, createConnectorEnvironment(user, password), connect);
    }

    public static JMXConnector createConnector(JMXServiceURL serviceURL, Map<String, ?> environment, boolean connect) {
        try {
            JMXConnector connector = null;
            if (connect) {
                connector = JMXConnectorFactory.connect(serviceURL, environment);
                LOGGER.debug("JMXConnector [" + connector + "] with id [" + getConnectionId(connector) + "] created successfully. Connect status is [" + connect + "].");
            } else {
                connector = JMXConnectorFactory.newJMXConnector(serviceURL, environment);
                LOGGER.debug("JMXConnector [" + connector + "] created successfully. Connect status is [" + connect + "].");
            }
            return connector;
        } catch (IOException e) {
            throw new JMXException("Cannot create JMXConnector from JMXServiceURL [" + serviceURL.toString() + "].", e);
        }
    }

    public static Map<String, Object> createConnectorEnvironment(String user, String password) {
        Map<String, Object> environment = new HashMap<String, Object>();
        environment.put(CREDENTIALS, new String[] { user, password });
        LOGGER.debug("Credentials for user [" + user + "] set.");
        return environment;
    }

    public static String getConnectionId(JMXConnector connector) {
        try {
            return connector.getConnectionId();
        } catch (IOException e) {
            throw new JMXException("Cannot get connectionId from JMXConnector.", e);
        }
    }

    public static MBeanServerConnection getMBeanServerConnection(JMXConnector connector) {
        try {
            return connector.getMBeanServerConnection();
        } catch (IOException e) {
            throw new JMXException("Cannot get MBeanServerConnection from JMXConnector.", e);
        }
    }
}
