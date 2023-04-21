package net.jgf.core.service;

import net.jgf.core.JgfRuntimeException;

/**
 * <p>This exception represents an error condition in a Service.</p>
 *
 * @author jjmontes
 */
public class ServiceException extends JgfRuntimeException {

    /**
	 * Id for serialization
	 */
    private static final long serialVersionUID = 4325218818170184700L;

    /**
	 * Builds a new ServiceException with the message specified.
	 */
    public ServiceException(String message) {
        super(message);
    }

    /**
	 * Builds a new ServiceException with the given message and the given nested exception.
	 */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
