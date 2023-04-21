package net.sf.jagile.core.exception.business;

/**
 * @author leonardop Jan 10, 2005
 */
public class DuplicatedPropertyException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor of DuplicatedPropertyException
     */
    public DuplicatedPropertyException() {
        super();
    }

    /**
	 * Constructor of DuplicatedPropertyException
	 * @param message
	 * @param cause
	 */
    public DuplicatedPropertyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Constructor of DuplicatedPropertyException
	 * @param message
	 */
    public DuplicatedPropertyException(String message) {
        super(message);
    }

    /**
	 * Constructor of DuplicatedPropertyException
	 * @param cause
	 */
    public DuplicatedPropertyException(Throwable cause) {
        super(cause);
    }
}
