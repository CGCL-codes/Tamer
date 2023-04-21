package org.ikasan.tools.messaging.destination.resolution;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class DefaultJndiDestinationResolver implements DestinationResolver {

    /**
     * Logger instance
     */
    private static final Logger logger = Logger.getLogger(DefaultJndiDestinationResolver.class);

    /**
     * environment parameters for creating the <code>InitialContext</code>
     */
    private Hashtable<String, String> environment;

    /**
     * Constructor 
     * 
     * @param environment - environment parameters for creating the <code>InitialContext</code>
     */
    public DefaultJndiDestinationResolver(Map<String, String> environment) {
        this.environment = new Hashtable<String, String>(environment);
    }

    /**
     * Cached instance of the target <code>Destination</code>
     */
    private Map<String, Destination> destinations = new HashMap<String, Destination>();

    public Destination getDestination(String destinationJndiName) throws NamingException {
        if (destinations.get(destinationJndiName) == null) {
            Context context = new InitialContext(environment);
            destinations.put(destinationJndiName, (Destination) context.lookup(destinationJndiName));
        }
        return destinations.get(destinationJndiName);
    }

    /**
     * Accessor for environment
     * 
     * @return environment
     */
    public Map<?, ?> getEnvironment() {
        return environment;
    }
}
