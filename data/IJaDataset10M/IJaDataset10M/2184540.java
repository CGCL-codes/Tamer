package org.xaware.server.engine.channel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.NamingException;
import org.springframework.jms.support.destination.DestinationResolutionException;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

/**
 * This is an extension of Spring's JndiTemplate which uses the Designer's
 * dynamic ClassLoader for all operations.
 */
public class JndiDestinationResolver extends JndiLocatorSupport implements DestinationResolver {

    private boolean cache = true;

    private boolean fallbackToDynamicDestination = false;

    private DestinationResolver dynamicDestinationResolver = new DynamicDestinationResolver();

    private final Map destinationCache = Collections.synchronizedMap(new HashMap());

    /**
	 * Set the ability of JmsTemplate to create dynamic destinations
	 * if the destination name is not found in JNDI. Default is "false".
	 */
    public void setFallbackToDynamicDestination(boolean fallbackToDynamicDestination) {
        this.fallbackToDynamicDestination = fallbackToDynamicDestination;
    }

    /**
	 * Set the DestinationResolver to use when falling back to dynamic destinations.
	 * Default is a DynamicDestinationResolver.
	 * @see #setFallbackToDynamicDestination
	 * @see DynamicDestinationResolver
	 */
    public void setDynamicDestinationResolver(DestinationResolver dynamicDestinationResolver) {
        this.dynamicDestinationResolver = dynamicDestinationResolver;
    }

    @SuppressWarnings("unchecked")
    public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException {
        Destination dest = (Destination) this.destinationCache.get(destinationName);
        if (dest == null) {
            try {
                dest = (Destination) lookup(destinationName, Destination.class);
            } catch (NamingException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Destination [" + destinationName + "] not found in JNDI", ex);
                }
                if (this.fallbackToDynamicDestination) {
                    dest = this.dynamicDestinationResolver.resolveDestinationName(session, destinationName, pubSubDomain);
                } else {
                    throw new DestinationResolutionException("Destination [" + destinationName + "] not found in JNDI", ex);
                }
            }
            if (this.cache) {
                this.destinationCache.put(destinationName, dest);
            }
        }
        return dest;
    }
}
