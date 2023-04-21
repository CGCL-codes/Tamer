package com.bitgate.util.service.protocol;

import java.util.HashMap;
import java.util.Iterator;
import org.w3c.dom.Node;
import com.bitgate.util.wildcard.Wildcard;
import static com.bitgate.util.debug.Debug.*;

/**
 * This class serves as a context object for a currently running <code>Server</code>.  It contains the currently activated
 * configuration node for the server as well as its hostname, port, and other information pertaining to that server. 
 * 
 * @author kenji
 */
public class ServerContext {

    /**
	 * This is a container object for the currently running <code>Server</code>.
	 * 
	 * @author kenji
	 */
    public class ServiceContextContainer {

        private final Node configNode;

        private final String hostName;

        private final int port;

        /**
		 * Constructor.
		 * 
		 * @param configNode The <code>Node</code> containing the configuration XML node position.
		 * @param hostName The <code>String</code> containing the hostname for this context.
		 * @param port The <code>int</code> containing the port number attached to this server.
		 */
        public ServiceContextContainer(Node configNode, String hostName, int port) {
            this.configNode = configNode;
            this.hostName = hostName;
            this.port = port;
        }

        /**
		 * Returns the configuration node.
		 * 
		 * @return <code>Node</code> object.
		 */
        public Node getNode() {
            return this.configNode;
        }

        /**
		 * Returns the hostname for this server entry.
		 * 
		 * @return <code>String</code> containing the hostname.
		 */
        public String getHostname() {
            return this.hostName;
        }

        /**
		 * Returns the port number for this server entry.
		 * 
		 * @return <code>int</code> containing the port number.
		 */
        public int getPort() {
            return this.port;
        }
    }

    /** List of the currently running or known service context objects. */
    public final HashMap<String, ServiceContextContainer> services;

    private static final ServerContext _default = new ServerContext();

    private ServerContext() {
        services = new HashMap<String, ServiceContextContainer>();
    }

    /**
	 * Returns the default singleton instance of this class.
	 * 
	 * @return <code>static ServerContext</code> object.
	 */
    public static ServerContext getInstance() {
        return _default;
    }

    /**
	 * Adds a server context object to the system cache.
	 * 
	 * @param serviceId The ID of the running server object.
	 * @param configNode The <code>Node</code> containing the configuration node for this service.
	 * @param hostName The <code>String</code> containing the hostname it is bound to.
	 * @param port The <code>int</code> containing the port number the server is bound to.
	 */
    public void addServerContext(String serviceId, Node configNode, String hostName, int port) {
        services.put(serviceId, new ServiceContextContainer(configNode, hostName, port));
        if (isDebugEnabled()) {
            debug("Add context: ID='" + serviceId + "' Host='" + hostName + "' Port='" + port + "'");
        }
    }

    /**
	 * Returns a configuration node based on the service ID specified.
	 * 
	 * @param serviceId <code>String</code> containing the service ID.
	 * @return <code>Node</code> containing the configuration node.
	 */
    public Node getByServiceId(String serviceId) {
        if (services.get(serviceId) != null) {
            return services.get(serviceId).getNode();
        }
        return null;
    }

    /**
	 * Returns the service ID based on the given hostname and port numbers.
	 * 
	 * @param hostName The <code>String</code> containing the hostname.
	 * @param port The <code>int</code> containing the port number.
	 * @return The <code>String</code> containing the service ID, <code>null</code> if not located.
	 */
    public String getServiceId(String hostName, int port) {
        Iterator<String> it = services.keySet().iterator();
        while (it.hasNext()) {
            String serviceId = it.next();
            ServiceContextContainer scContainer = services.get(serviceId);
            if (Wildcard.toRegexp(scContainer.getHostname()).matches(hostName) && scContainer.getPort() == port) {
                if (isDebugEnabled()) {
                    debug("Returning service ID '" + serviceId + "', match '" + hostName + "' with port '" + port + "'");
                }
                return serviceId;
            }
        }
        return null;
    }
}
