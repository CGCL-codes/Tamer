package com.csii.exception;

/**
 * @author cuiyi
 *
 */
class ServiceException extends RuntimeException {

    /**
	 * @param e
	 */
    public ServiceException(DAOException e) {
    }

    /**
	 * 
	 */
    public ServiceException() {
        super();
    }

    /**
	 * @param arg0
	 */
    public ServiceException(String arg0) {
        super(arg0);
    }
}
