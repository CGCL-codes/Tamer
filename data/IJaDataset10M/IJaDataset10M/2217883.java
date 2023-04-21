package org.bpmsuite.service;

/**
 * @author Dirk Weiser
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = -938695166813652800L;

    public ServiceException() {
        super();
    }

    public ServiceException(String msg) {
        super(msg);
    }
}
