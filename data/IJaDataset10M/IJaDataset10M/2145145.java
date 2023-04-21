package com.continuent.tungsten.router.adaptor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.apache.log4j.Logger;
import com.continuent.tungsten.commons.patterns.notification.ResourceNotificationListener;
import com.continuent.tungsten.router.config.ResourceTypes;
import com.continuent.tungsten.router.manager.RouterException;

public class DriverNotificationListener implements ResourceNotificationListener, Runnable {

    private Logger logger = Logger.getLogger(DriverNotificationListener.class);

    private Map<String, BlockingQueue<Map<String, Object>>> notifications = new TreeMap<String, BlockingQueue<Map<String, Object>>>();

    Thread monitorThread = null;

    String connectorHost = "localhost";

    String connectorJMXPort = "3100";

    String fullURL = "service:jmx:rmi:///jndi/rmi://" + connectorHost + ":" + connectorJMXPort + "/jmxrmi";

    JMXConnector jmxConnector = null;

    MBeanServerConnection jmxConnection = null;

    String thisRouterUrlHead = "jdbc:t-router://localhost";

    private String notificationsType;

    public void init(String type) {
        notificationsType = type;
        createNotificationQueue(notificationsType);
        connectToC3p0JMX(fullURL);
    }

    private void connectToC3p0JMX(String jmxUrl) {
        try {
            JMXServiceURL address = new JMXServiceURL(jmxUrl);
            jmxConnector = JMXConnectorFactory.connect(address);
            logger.trace("JMX connection established to connector " + jmxUrl);
        } catch (MalformedURLException mue) {
            logger.error("Cannot connect to " + jmxUrl + ". Please check host name and port.", mue);
            jmxConnector = null;
        } catch (IOException ioe) {
            logger.warn("I/O error while establishing JMX connection to " + jmxUrl, ioe);
        }
        if (jmxConnector != null) {
            try {
                jmxConnection = jmxConnector.getMBeanServerConnection();
            } catch (IOException ioe) {
                logger.warn("Error while getting MBean server connection", ioe);
                jmxConnection = null;
            }
        }
    }

    public void start() {
        monitorThread = new Thread(this, this.getClass().getSimpleName());
        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    /**
     * {@inheritDoc}
     * @see com.continuent.tungsten.commons.patterns.notification.ResourceNotificationListener#notify(java.lang.String, java.util.Map)
     */
    @SuppressWarnings("unchecked")
    public void notify(String resourceType, Map<String, Object> notification) {
        logger.info("#### GOT DRIVER NOTIFICATION=" + notification + " ####");
        String resourceState = (String) notification.get("resourceState");
        if (resourceType == ResourceTypes.ROUTER) {
            logger.info("Router is " + resourceState);
        } else if (resourceType == ResourceTypes.DATASOURCE) {
            Properties resourceProperties = (Properties) notification.get("resourceProperties");
            String resouceUrl = resourceProperties.getProperty("url");
            logger.info("Datastore " + resouceUrl + " is " + resourceState);
        } else {
            logger.info("Nothing to do for resource " + resourceType + " notifications");
            return;
        }
        if (jmxConnector == null) connectToC3p0JMX(fullURL);
        if (jmxConnector == null) {
            logger.error("Cannot notify pool: couldn't connect to c3p0 via jmx.");
            return;
        }
        Hashtable<String, String> mbeanNameProps = new Hashtable<String, String>();
        mbeanNameProps.put("type", "PooledDataSource");
        try {
            ObjectName c3p0MBeans = new ObjectName("com.mchange.v2.c3p0:*");
            Set<ObjectName> mbeans = jmxConnection.queryNames(c3p0MBeans, null);
            for (ObjectName obj : mbeans) {
                if (obj.getKeyProperty("type").startsWith("PooledDataSource")) {
                    String url = (String) jmxConnection.getAttribute(obj, "jdbcUrl");
                    logger.debug("Found pooledDataSource with URL" + url);
                    if (url.startsWith(thisRouterUrlHead)) {
                        jmxConnection.invoke(obj, "softResetAllUsers", new Object[0], new String[0]);
                        logger.info("C3P0 successfully softReset-ed");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while resetting connection pool - closing connection");
            if (jmxConnector != null) {
                try {
                    jmxConnector.close();
                } catch (Exception ignored) {
                }
            }
            jmxConnection = null;
            jmxConnector = null;
        }
    }

    /**
     * TODO: processNotifications definition.
     * 
     * @throws RouterException
     */
    private void processNotifications() throws RouterException {
        Map<String, Object> notification = null;
        while ((notification = getNotification(notificationsType)) != null) {
            logger.debug("#### PROCESSING DRIVER NOTIFICATION=" + notification + " ####");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        logger.info("DRIVER MONITOR THREAD STARTED");
        try {
            processNotifications();
        } catch (RouterException r) {
            logger.fatal("DRIVER MONITOR: Exception while processing notifications:" + r);
        }
    }

    /**
     * Put a notification on the appropriate queue
     * 
     * @param type
     * @param notification
     * @throws RouterException
     */
    public void putNotification(String type, Map<String, Object> notification) throws RouterException {
        BlockingQueue<Map<String, Object>> queue = null;
        synchronized (notifications) {
            queue = notifications.get(type);
            if (queue == null) {
                throw new RouterException("Queue for type=" + type + " does not exist.");
            }
        }
        try {
            queue.put(notification);
        } catch (InterruptedException i) {
            throw new RouterException("Interrupted while trying to put notification of type=" + type);
        }
    }

    /**
     * Gets the next notification in the queue
     * 
     * @param type
     */
    public Map<String, Object> getNotification(String type) throws RouterException {
        Map<String, Object> notification = null;
        BlockingQueue<Map<String, Object>> queue = null;
        synchronized (notifications) {
            queue = notifications.get(type);
            if (queue == null) {
                throw new RouterException("Queue for type=" + type + " does not exist.");
            }
        }
        try {
            notification = queue.take();
        } catch (InterruptedException i) {
            throw new RouterException("Interrupted while waiting for notification of type=" + type);
        }
        return notification;
    }

    /**
     * Create a queue to contain notifications
     * 
     * @param type
     */
    public void createNotificationQueue(String type) {
        BlockingQueue<Map<String, Object>> queue = notifications.get(type);
        if (queue == null) {
            queue = new LinkedBlockingQueue<Map<String, Object>>();
            notifications.put(type, queue);
        }
    }
}
