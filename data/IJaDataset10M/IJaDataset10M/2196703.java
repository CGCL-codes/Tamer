package org.piuframework.service;

import org.piuframework.PiuFrameworkException;

/**
 * TODO
 * 
 * @author Dirk Mascher
 */
public class ServiceException extends PiuFrameworkException {

    private static final long serialVersionUID = 7379138599592690422L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
