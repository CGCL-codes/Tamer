package fr.gfi.gfinet.server;

import fr.gfi.gfinet.common.exception.ServiceException;

/**
 * @author Jean DAT	
 * @since  September 27, 2007
 */
public class CvServiceException extends ServiceException {

    /**
	 * Constructor for the exception object.
	 * 
	 * @param message description of the error
	 */
    public CvServiceException(String message) {
        super(message);
    }

    /**
	 * Constructor for the exception object.
	 * 
	 * @param message description of the error
	 * @param cause the nested exception
	 */
    public CvServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Constructor for the exception object.
	 * 
	 * @param cause the nested exception
	 */
    public CvServiceException(Throwable cause) {
        super(cause);
    }
}
