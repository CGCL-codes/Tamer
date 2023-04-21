package org.opennms.netmgt.provision.support.jmx.connectors;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;

public class JBossConnectionWrapper implements ConnectionWrapper {

    private MBeanServer mbeanServer;

    public JBossConnectionWrapper(MBeanServer mbeanServer) {
        this.mbeanServer = mbeanServer;
    }

    public void close() {
        mbeanServer = null;
    }

    public MBeanServerConnection getMBeanServer() {
        return mbeanServer;
    }
}
