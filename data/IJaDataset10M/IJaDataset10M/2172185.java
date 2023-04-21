package org.mc4j.console.connection;

import com.sun.jdmk.ServiceName;
import com.sun.jdmk.comm.RemoteMBeanServer;
import com.sun.jdmk.comm.RmiConnectorAddress;
import com.sun.jdmk.comm.RmiConnectorClient;
import java.net.URL;
import java.util.Hashtable;
import javax.management.MBeanServer;
import javax.naming.InitialContext;
import org.mc4j.console.connection.proxy.GenericMBeanServerProxy;
import org.openide.ErrorManager;
import org.openide.TopManager;

/**
 * Represents a Connection to a Mx4j JMX Management service. This connection
 * works against the Mx4j JRMP RMI connector.
 *
 * Copyright 2002 Sapient
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), January 2002
 * @version $Revision: 66 $($Author: ghinkle $ / $Date: 2002-09-13 17:49:13 -0400 (Fri, 13 Sep 2002) $)
 */
public class JDMKConnectionNode extends ConnectionNode {

    private RmiConnectorClient client;

    private MBeanServer mbeanServer;

    public void connect() throws Exception {
        System.setProperty("jmx.serial.form", "1.1");
        String url = connectionSettings.getServerUrl();
        String hostname = url.substring(0, url.indexOf(':'));
        int port = Integer.parseInt(url.substring(url.indexOf(':') + 1, url.length()));
        RmiConnectorAddress defaultAddress = new RmiConnectorAddress();
        RmiConnectorAddress connectorAddress = new RmiConnectorAddress(hostname, port, connectionSettings.getJndiName());
        this.client = new RmiConnectorClient();
        client.connect(connectorAddress);
        this.mbeanServer = GenericMBeanServerProxy.buildServerProxy(client);
        super.connect();
    }

    public void disconnect() throws Exception {
        super.disconnect();
        this.client.disconnect();
        super.connected = false;
    }

    public MBeanServer getMBeanServer() {
        return this.mbeanServer;
    }
}
