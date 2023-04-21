package org.springframework.jms;

/**
 * Runtime exception mirroring the JMS InvalidDestinationException.
 *
 * @author Les Hazlewood
 * @since 1.1
 * @see javax.jms.InvalidDestinationException
 */
public class InvalidDestinationException extends JmsException {

    public InvalidDestinationException(javax.jms.InvalidDestinationException cause) {
        super(cause);
    }
}
