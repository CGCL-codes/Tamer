package org.datanucleus.management.mx4j;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.NoSuchObjectException;
import java.util.Hashtable;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import mx4j.tools.naming.NamingService;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.management.ManagementServer;
import org.datanucleus.util.NucleusLogger;

/**
 * Management Server starts and stops a Management Server. Starts a MBeanServer using MX4J
 * Plugin Extension Point: org.datanucleus.management_server
 */
public class Mx4jManagementServer implements ManagementServer {

    MBeanServer server;

    JMXConnectorServer jmxServer;

    NamingService naming;

    /**
     * Start the Management Server. If this operation is invoked
     * while the server is started, this operation is ignored.
     * This operation can also connect to a remote MBeanServer,
     * instead of creating a new MBeanServer instance. This depends
     * of the configuration.
     */
    public void start() {
        if (NucleusLogger.MANAGEMENT.isDebugEnabled()) {
            NucleusLogger.MANAGEMENT.debug("Starting ManagementServer");
        }
        int port = 1199;
        try {
            naming = new NamingService(port);
            naming.start();
            server = MBeanServerFactory.createMBeanServer();
            String hostName = InetAddress.getLocalHost().getHostName();
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
            env.put(Context.PROVIDER_URL, "rmi://" + hostName + ":" + port);
            JMXServiceURL address = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + hostName + ":" + port + "/datanucleus");
            JMXConnectorServer jmxServer = JMXConnectorServerFactory.newJMXConnectorServer(address, env, server);
            jmxServer.start();
            if (NucleusLogger.MANAGEMENT.isDebugEnabled()) {
                NucleusLogger.MANAGEMENT.debug("MBeanServer listening at " + jmxServer.getAddress().toString());
            }
        } catch (Exception e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    /**
     * Stop the Management Server. If this operation is invoked
     * while the server is stop, this operation is ignored 
     * This operation can also disconnect from a remote MBeanServer,
     * instead of destroying a MBeanServer instance. This depends
     * of the configuration.
     */
    public void stop() {
        if (NucleusLogger.MANAGEMENT.isDebugEnabled()) {
            NucleusLogger.MANAGEMENT.debug("Stopping ManagementServer");
        }
        if (jmxServer != null) {
            try {
                jmxServer.stop();
            } catch (IOException e) {
                NucleusLogger.MANAGEMENT.error(e);
            }
        }
        if (naming != null) {
            try {
                naming.stop();
            } catch (NoSuchObjectException e) {
                NucleusLogger.MANAGEMENT.error(e);
            }
        }
        jmxServer = null;
        naming = null;
        server = null;
    }

    /**
     * Register a MBean into the MBeanServer
     * @param mbean the MBean instance
     * @param name the mbean name
     */
    public void registerMBean(Object mbean, String name) {
        try {
            ObjectName objName = new ObjectName(name);
            server.registerMBean(mbean, objName);
        } catch (Exception e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    /**
     * Unregister a MBean from the MBeanServer
     * @param name the mbean name
     */
    public void unregisterMBean(String name) {
        try {
            ObjectName objName = new ObjectName(name);
            server.unregisterMBean(objName);
        } catch (Exception e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    /**
     * Acessor for underlying Management Server
     */
    public Object getMBeanServer() {
        return server;
    }
}
