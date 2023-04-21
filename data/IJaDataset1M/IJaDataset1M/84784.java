package org.jsemantic.services.logging.exception;

import org.jsemantic.jservice.core.service.exception.ServiceException;

public class LoggingServiceException extends ServiceException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public LoggingServiceException() {
        super();
    }

    public LoggingServiceException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public LoggingServiceException(String arg0) {
        super(arg0);
    }

    public LoggingServiceException(Throwable arg0) {
        super(arg0);
    }
}
