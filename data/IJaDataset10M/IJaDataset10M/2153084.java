package de.iritgo.aktera.clients;

import de.iritgo.aktera.model.KeelRequest;
import de.iritgo.aktera.model.KeelResponse;
import de.iritgo.aktera.model.ModelException;
import org.apache.commons.logging.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * An abstract class that provides a Keel-client connector. In essence, this
 * class provides the mechanism to acquire KeelClient objects and also to
 * dispose of them whenever during execution of business-logic, the client
 * itself produces exceptions.
 *
 * In the near future, when multiple, clustered Keel-servers are supported, each
 * server will have a corresponding Keel-client. The management of these
 * multiple Keel-clients, failover and load-balancing will be implemented here
 * completely transparent to the actual Struts/Cocoon/etc. applications.
 *
 * @version $Revision: 1.5 $ $Date: 2004/03/06 18:31:53 $
 * @author Michael Nash
 * @author Schatterjee Created on May 3, 2003
 */
public abstract class AbstractClientConnector implements ClientConnector {

    protected static List myClients = new ArrayList();

    protected Log log = null;

    protected Map clientContext = null;

    /**
	 * @see de.iritgo.aktera.clients.ClientConnector#execute(de.iritgo.aktera.model.KeelRequest)
	 */
    public KeelResponse execute(KeelRequest kreq) throws ClientException, ModelException {
        KeelClient currentClient = getClient();
        KeelResponse kres = null;
        try {
            kres = currentClient.execute(kreq);
        } catch (IOException e) {
            log.error("Client returned error: ", e);
            removeClient(currentClient);
        }
        return kres;
    }

    /**
	 * This method examines the current list of available KeelClient objects,
	 * and returns the first one for use. If there are no clients, it calls
	 * createClients
	 */
    protected KeelClient getClient() throws ClientException {
        if (myClients.size() == 0) {
            synchronized (myClients) {
                if (myClients.size() == 0) {
                    createClients();
                    if (myClients.size() == 0) {
                        throw new ClientException("No clients configured");
                    }
                }
            }
        }
        return (KeelClient) myClients.get(0);
    }

    /**
	 * This method attempts to create one or more KeelClients. If the
	 * "jms-config" initial property has been specified in the web.xml file, it
	 * is used to configure these clients. If no such property was specified,
	 * default configuration (JMS via RMI to localhost on port 1099) is used
	 * instead, and only one client is configured. The format of the jms-config
	 * is config|config|config, where each "config" is of the form
	 * protocol://host:port/jndiname, e.g. like rmi://localhost:1099/JndiServer
	 * Each "config" indicates a separate client, and they are used in the order
	 * specified. If a connection goes bad to one client, the next is used and
	 * so forth until there are no more, at which point the first one is tried
	 * again.
	 */
    public void createClients() throws ClientException {
        if (clientContext == null) {
            String msg = "Client context wasn't set using the setContext() method";
            log.error(msg);
            throw new ClientException(msg);
        }
        String clientClass = (String) clientContext.get(KeelClient.CLIENT_CLASS);
        if ((clientClass == null) || ("".equals(clientClass))) {
            clientClass = "de.iritgo.aktera.clients.direct.KeelJmsClient";
            log.warn("Client class wasn't specified in context, assuming default: " + clientClass);
        }
        String clientConfig = (String) clientContext.get(KeelClient.CLIENT_CONFIG);
        if (clientConfig == null) {
            clientConfig = "rmi://localhost:1099/JndiServer";
            log.warn("Client config wasn't specified in context, assuming default: " + clientConfig);
        }
        StringTokenizer stk = new StringTokenizer(clientConfig, "|");
        while (stk.hasMoreTokens()) {
            String oneConfig = stk.nextToken();
            int clientId = myClients.size() + 1;
            KeelClient oneClient = null;
            try {
                oneClient = (KeelClient) Class.forName(clientClass).newInstance();
            } catch (Exception e) {
                throw new ClientException(e);
            }
            oneClient.setId(clientId);
            if (log.isInfoEnabled()) {
                log.info("Configuring Client " + clientId);
            }
            myClients.add(oneClient);
        }
    }

    /**
	 * @param currentClient
	 */
    protected void removeClient(KeelClient currentClient) {
        log.debug("Removing client " + currentClient.getId());
        myClients.remove(currentClient);
    }

    /**
	 * @see de.iritgo.aktera.clients.ClientConnector#setLogger(org.apache.commons.logging.Log)
	 */
    public void setLogger(Log log) {
        this.log = log;
    }

    /**
	 * @see de.iritgo.aktera.clients.ClientConnector#setContext(java.util.HashMap)
	 */
    public void setContext(Map clientContext) {
        this.clientContext = (HashMap) clientContext;
    }
}
