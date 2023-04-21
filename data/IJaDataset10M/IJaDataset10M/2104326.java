package org.apache.harmony.jndi.provider.dns;

import javax.naming.NamingException;

/**
 * The exception that can be thrown from the DNS Resolver classes.
 */
public class DomainProtocolException extends NamingException {

    private static final long serialVersionUID = -6631370496197297208L;

    public DomainProtocolException(String message) {
        super(message);
    }

    public DomainProtocolException(Exception cause) {
        super();
        setRootCause(cause);
    }

    public DomainProtocolException(String mes, Exception cause) {
        super(mes);
        setRootCause(cause);
    }
}
