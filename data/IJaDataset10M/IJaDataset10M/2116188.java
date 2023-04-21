package org.jpos.q2.jetty;

import java.io.FileInputStream;
import org.jpos.q2.QBeanSupport;
import org.mortbay.jetty.Server;
import org.mortbay.xml.XmlConfiguration;

/**
 * @author Alejandro Revilla
 * @version $Revision$ $Date$
 * @jmx:mbean description="Jetty QBean" extends="org.jpos.q2.QBeanSupportMBean"
 */
public class Jetty6 extends QBeanSupport implements Jetty6MBean {

    String config;

    Server server;

    public void initService() throws Exception {
        server = new Server();
        FileInputStream fis = new FileInputStream(config);
        XmlConfiguration xml = new XmlConfiguration(fis);
        xml.configure(server);
    }

    public void startService() throws Exception {
        server.start();
    }

    public void stopService() throws Exception {
        server.stop();
    }

    /**
     * @jmx:managed-attribute description="Configuration File"
     */
    public void setConfig(String config) {
        this.config = config;
    }

    /**
     * @jmx:managed-attribute description="Configuration File"
     */
    public String getConfig() {
        return config;
    }
}
