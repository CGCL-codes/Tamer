package org.opennms.protocols.jmx.connectors;

import java.net.InetAddress;
import java.util.*;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.apache.log4j.Category;
import org.opennms.core.utils.ParameterMap;
import org.opennms.core.utils.ThreadCategory;

public class Jsr160ConnectionFactory {

    static Category log = ThreadCategory.getInstance(Jsr160ConnectionFactory.class);

    public static Jsr160ConnectionWrapper getMBeanServerConnection(Map propertiesMap, InetAddress address) {
        Jsr160ConnectionWrapper connectionWrapper = null;
        JMXServiceURL url = null;
        String factory = ParameterMap.getKeyedString(propertiesMap, "factory", "STANDARD");
        int port = ParameterMap.getKeyedInteger(propertiesMap, "port", 1099);
        String protocol = ParameterMap.getKeyedString(propertiesMap, "protocol", "rmi");
        String urlPath = ParameterMap.getKeyedString(propertiesMap, "urlPath", "/jmxrmi");
        log.debug("JMX: " + factory + " - service:" + protocol + "//" + address.getHostAddress() + ":" + port + urlPath);
        if (factory == null || factory.equals("STANDARD")) {
            try {
                url = new JMXServiceURL("service:jmx:" + protocol + ":///jndi/" + protocol + "://" + address.getHostAddress() + ":" + port + urlPath);
                JMXConnector connector = JMXConnectorFactory.connect(url);
                MBeanServerConnection connection = connector.getMBeanServerConnection();
                connectionWrapper = new Jsr160ConnectionWrapper(connector, connection);
            } catch (Exception e) {
                log.warn("Unable to get MBeanServerConnection: " + url);
            }
        } else if (factory.equals("PASSWORD-CLEAR")) {
            try {
                String username = ParameterMap.getKeyedString(propertiesMap, "username", null);
                String password = ParameterMap.getKeyedString(propertiesMap, "password", null);
                HashMap env = new HashMap();
                String[] credentials = new String[] { username, password };
                env.put("jmx.remote.credentials", credentials);
                url = new JMXServiceURL("service:jmx:" + protocol + ":///jndi/" + protocol + "://" + address.getHostAddress() + ":" + port + urlPath);
                JMXConnector connector = JMXConnectorFactory.newJMXConnector(url, null);
                try {
                    connector.connect(env);
                } catch (SecurityException x) {
                    log.error("Security exception: bad credentials");
                    throw x;
                }
                MBeanServerConnection connection = connector.getMBeanServerConnection();
                connectionWrapper = new Jsr160ConnectionWrapper(connector, connection);
            } catch (Exception e) {
                log.error("Unable to get MBeanServerConnection: " + url, e);
            }
        }
        return connectionWrapper;
    }
}
