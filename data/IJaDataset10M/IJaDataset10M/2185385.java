package fr.gfi.gfinet.server;

import fr.gfi.gfinet.common.exception.ServiceException;

/**
 * Describes service errors.
 * 
 * @author Tiago Fernandez
 * @since 16 mars 07
 */
public class CostCenterServiceException extends ServiceException {

    /**
	 * Constructor for the exception object.
	 * 
	 * @param message description of the error
	 */
    public CostCenterServiceException(String message) {
        super(message);
    }

    /**
	 * Constructor for the exception object.
	 * 
	 * @param message description of the error
	 * @param cause the nested exception
	 */
    public CostCenterServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Constructor for the exception object.
	 * 
	 * @param cause the nested exception
	 */
    public CostCenterServiceException(Throwable cause) {
        super(cause);
    }
}
