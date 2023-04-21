package org.nexopenframework.management.agent.jmx;

import java.util.ArrayList;
import java.util.List;
import javax.management.MBeanServerConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nexopenframework.management.agent.AgentException;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Main class for dealing with location of {@link MBeanServerConnection} related to application 
 *    server processes in a given node.</p>
 * 
 * @see javax.management.MBeanServerConnection
 * @see org.nexopenframework.management.agent.jmx.MBeanServerConnectionAdapter
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0.0.m2
 */
public class MBeanServerConnectionLocator {

    /**logging facility*/
    private static final Log logger = LogFactory.getLog(MBeanServerConnectionLocator.class);

    /**List of adapters to retrieve {@link MBeanServerConnection}*/
    private List<MBeanServerConnectionAdapter> adapters = new ArrayList<MBeanServerConnectionAdapter>();

    /**
	 * @param adapters
	 */
    public void setMBeanServerConnectionAdapters(final List<MBeanServerConnectionAdapter> adapters) {
        this.adapters = adapters;
    }

    public void addMBeanServerConnectionAdapters(final MBeanServerConnectionAdapter adapter) {
        if (this.adapters != null && !this.adapters.contains(adapter)) {
            this.adapters.add(adapter);
        }
    }

    /**
	 * <p></p>
	 * 
	 * @see org.nexopenframework.management.agent.jmx.MBeanServerConnectionAdapter#locateMBeanServerConnection()
	 * @return
	 */
    public List<MBeanServerConnection> locateMBeanServerConnections() {
        final List<MBeanServerConnection> connections = new ArrayList<MBeanServerConnection>();
        for (final MBeanServerConnectionAdapter adapter : adapters) {
            try {
                final MBeanServerConnection connection = adapter.locateMBeanServerConnection();
                if (connection != null) {
                    connections.add(connection);
                }
            } catch (final AgentException e) {
                if (logger.isInfoEnabled()) {
                    logger.info("Problem in location of MBeanServerConnection", e);
                }
            }
        }
        return connections;
    }
}
