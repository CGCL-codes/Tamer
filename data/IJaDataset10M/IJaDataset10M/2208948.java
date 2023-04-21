package com.angel.common.providers.exceptions;

import com.angel.architecture.exceptions.BusinessException;

/**
 * @author William
 *
 */
public class ObjectNotSupportIdentifierNameException extends BusinessException {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    public ObjectNotSupportIdentifierNameException() {
        super();
    }

    public ObjectNotSupportIdentifierNameException(String message) {
        super(message);
    }

    public ObjectNotSupportIdentifierNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotSupportIdentifierNameException(Throwable cause) {
        super(cause);
    }
}
