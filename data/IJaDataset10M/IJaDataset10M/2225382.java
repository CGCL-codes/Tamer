package org.springframework.jms;

/**
 * Runtime exception mirroring the JMS InvalidSelectorException.
 *
 * @author Les Hazlewood
 * @since 1.1
 * @see javax.jms.InvalidSelectorException
 */
public class InvalidSelectorException extends JmsException {

    public InvalidSelectorException(javax.jms.InvalidSelectorException cause) {
        super(cause);
    }
}
