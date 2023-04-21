package org.mc4j.ems.impl.jmx.connection.support.providers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import org.mc4j.ems.impl.jmx.connection.support.providers.proxy.JMXRemotingMBeanServerProxy;
import org.mc4j.ems.impl.jmx.connection.support.providers.proxy.StatsProxy;

public class PramatiConnectionProvider extends AbstractConnectionProvider {

    private JMXConnector jmxConnector;

    private MBeanServerConnection serverConnection;

    private MBeanServer mbeanServer;

    private static final String PROTOCOL_PROVIDER_PACKAGE = "jmx.remote.protocol.provider.pkgs";

    private static final String PRAMATI_PROTOCOL_PROVIDER_PACKAGE = "com.pramati.jmx.connector";

    protected void doConnect() throws Exception {
        ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        JMXServiceURL url = new JMXServiceURL(this.connectionSettings.getServerUrl());
        HashMap<String, Object> env = new HashMap<String, Object>();
        env.put(Context.SECURITY_PRINCIPAL, connectionSettings.getPrincipal());
        env.put(Context.SECURITY_CREDENTIALS, connectionSettings.getCredentials());
        if (connectionSettings.getAdvancedProperties() != null) {
            for (Map.Entry<Object, Object> entry : connectionSettings.getAdvancedProperties().entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                env.put(key, value);
            }
        }
        env.put(PROTOCOL_PROVIDER_PACKAGE, PRAMATI_PROTOCOL_PROVIDER_PACKAGE);
        String[] credentials = new String[] { this.connectionSettings.getPrincipal(), this.connectionSettings.getCredentials() };
        env.put(JMXConnector.CREDENTIALS, credentials);
        this.jmxConnector = JMXConnectorFactory.connect(url, env);
        serverConnection = this.jmxConnector.getMBeanServerConnection();
        StatsProxy proxy = new JMXRemotingMBeanServerProxy(serverConnection);
        setStatsProxy(proxy);
        this.mbeanServer = proxy.buildServerProxy();
        super.connect();
    }

    public void doDisconnect() {
        try {
            this.jmxConnector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MBeanServer getMBeanServer() {
        return this.mbeanServer;
    }
}
