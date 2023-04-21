package mx4j.tools.heartbeat;

import java.util.Properties;
import mx4j.connector.RemoteMBeanServer;
import mx4j.connector.rmi.RMIConnector;
import mx4j.connector.rmi.jrmp.JRMPConnector;
import mx4j.log.Log;
import mx4j.log.Logger;

/**
 *
 * @author <a href="mailto:mgore@users.sourceforge.net">Michael Gorelik</a>
 * @version $Revision: 730 $
 */
public class HeartBeatConnectorFactory {

    private static HeartBeatConnectorFactory m_singleton = null;

    public static HeartBeatConnectorFactory getFactory() {
        if (m_singleton == null) {
            m_singleton = new HeartBeatConnectorFactory();
        }
        return m_singleton;
    }

    private HeartBeatConnectorFactory() {
    }

    public RemoteMBeanServer getConnector(Object connType, Object address) throws ConnectorException {
        if (connType.equals(HeartBeatMBean.RMI_TYPE)) {
            RMIConnector conn;
            Properties env = System.getProperties();
            try {
                Logger logger = getLogger();
                if (logger.isEnabledFor(Logger.TRACE)) logger.trace(getClass().getName() + ".getConnector: connType=" + connType.toString() + " addr=" + address.toString());
                conn = new JRMPConnector();
                conn.connect((String) address, env);
            } catch (Exception ex) {
                throw new ConnectorException(ex.getClass().getName());
            }
            return conn.getRemoteMBeanServer();
        }
        throw new ConnectorException("Unknown connector type");
    }

    private Logger getLogger() {
        return Log.getLogger(getClass().getName());
    }
}
