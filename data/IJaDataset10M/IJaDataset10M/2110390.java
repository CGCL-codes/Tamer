package com.acv.service.common.exception;

/**
 * The Class ObjectAlreadyExistsException.
 */
public class ObjectAlreadyExistsException extends ServiceException {

    private static final long serialVersionUID = 13L;

    /**
	 * Instantiates a new object already exists exception.
	 *
	 * @param message
	 *            the message
	 */
    public ObjectAlreadyExistsException(String message) {
        super(message);
    }
}
