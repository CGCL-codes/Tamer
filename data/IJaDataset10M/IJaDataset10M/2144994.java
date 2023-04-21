package org.springframework.jmx.access;

import javax.management.JMRuntimeException;

/**
 * Thrown when trying to invoke an operation on a proxy that is not exposed
 * by the proxied resource's management interface.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.2
 * @see MBeanClientInterceptor
 */
public class InvalidInvocationException extends JMRuntimeException {

    /**
	 * Create a new <code>InvalidInvocationException</code> with the supplied
	 * error message.
	 * @param msg the error message
	 */
    public InvalidInvocationException(String msg) {
        super(msg);
    }
}
