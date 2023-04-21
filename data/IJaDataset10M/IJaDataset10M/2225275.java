package net.sf.simplecq.core.service;

import java.util.List;
import javax.jms.ConnectionFactory;
import net.sf.simplecq.core.ResourceCreationException;
import net.sf.simplecq.core.model.MessagingEndpoint;

/**
 * Service for {@link MessagingEndpoint}.
 * 
 * @author Sherif Behna
 */
public interface MessagingEndpointService {

    /**
	 * Returns a list of all active {@link MessagingEndpoint}.
	 */
    public abstract List<MessagingEndpoint> findActive();

    /**
	 * Finds a {@link MessagingEndpoint} with the specified name.
	 */
    public abstract MessagingEndpoint findByName(String name);

    /**
	 * Saves a {@link MessagingEndpoint}.
	 */
    public abstract MessagingEndpoint save(MessagingEndpoint me);

    /**
	 * Tests if the messaging endpoint is valid.
	 * 
	 * @param me
	 *            The {@link MessagingEndpoint} to be tested.
	 * @return True if the messaging endpoint is valid, false otherwise.
	 */
    public abstract boolean isValid(MessagingEndpoint me);

    /**
	 * Returns an actual {@link ConnectionFactory} that can be used to connect
	 * to the specified {@link MessagingEndpoint}.
	 * 
	 * Implementors are required to return the same {@link ConnectionFactory}
	 * object for identical {@link MessagingEndpoint endpoints}.
	 * 
	 * @param me
	 *            Information to create the {@link ConnectionFactory}.
	 * @return A {@link ConnectionFactory} object.
	 */
    public abstract ConnectionFactory getConnectionFactory(MessagingEndpoint me) throws ResourceCreationException;
}
