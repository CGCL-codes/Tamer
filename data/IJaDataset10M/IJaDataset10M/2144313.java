package org.opennms.netmgt.provision.detector.jmx.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import org.opennms.netmgt.provision.support.Client;
import org.opennms.netmgt.provision.support.jmx.connectors.ConnectionWrapper;

/**
 * @author Donald Desloge
 *
 */
public abstract class JMXClient implements Client<ConnectionWrapper, Integer> {

    private ConnectionWrapper m_connection;

    public void close() {
        if (m_connection != null) {
            m_connection.close();
        }
    }

    protected abstract ConnectionWrapper getMBeanServerConnection(Map<String, Object> parameterMap, InetAddress address);

    protected abstract Map<String, Object> generateMap(int port, int timeout);

    public void connect(InetAddress address, int port, int timeout) throws IOException, Exception {
        m_connection = getMBeanServerConnection(generateMap(port, timeout), address);
    }

    public Integer receiveBanner() throws IOException, Exception {
        if (m_connection != null) {
            return m_connection.getMBeanServer().getMBeanCount();
        } else {
            return -1;
        }
    }

    public Integer sendRequest(ConnectionWrapper request) throws IOException, Exception {
        return receiveResponse();
    }

    private Integer receiveResponse() throws IOException {
        return null;
    }
}
