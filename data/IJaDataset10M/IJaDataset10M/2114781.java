package fr.gfi.foundation.core.service;

/**
 * Generic exception thrown by services.
 * 
 * @author Tiago Fernandez
 * @since 0.1
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = -929500032980877221L;

    /**
	 * Constructor.
	 */
    public ServiceException() {
        super();
    }

    /**
	 * Constructor.
	 * 
	 * @param message describing the exception
	 */
    public ServiceException(String message) {
        super(message);
    }

    /**
	 * Constructor.
	 * 
	 * @param message describing the exception
	 * @param ex the wrapped exception
	 */
    public ServiceException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
	 * Constructor.
	 * 
	 * @param ex the wrapped exception
	 */
    public ServiceException(Throwable ex) {
        super(ex);
    }
}
