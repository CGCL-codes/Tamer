package net.sourceforge.oradoc.utils;

/**
 * @author Igor
 *
 */
public class DynamicFactoryException extends Exception {

    /**
   * Comment for <code>serialVersionUID</code>
   */
    private static final long serialVersionUID = 5886059548959222865L;

    /**
	 *
	 */
    public DynamicFactoryException() {
        super();
    }

    /**
	 * @param message
	 */
    public DynamicFactoryException(String message) {
        super(message);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public DynamicFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param cause
	 */
    public DynamicFactoryException(Throwable cause) {
        super(cause);
    }
}
