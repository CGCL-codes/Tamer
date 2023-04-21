package layout;

/**
 * This exception gets thrown when there was a problem during layout
 * (i.e. malformed XML, invalid ui component, etc.)
 * 
 * @author Andrew Klofas
 *
 */
public class LayoutException extends RuntimeException {

    /**
	 * Serialized version. !!This class is not meant to be serialized!!
	 */
    private static final long serialVersionUID = 1737472092747298173L;

    /**
	 * Initializes exception with no debug message
	 *
	 */
    public LayoutException() {
        super();
    }

    /**
	 * Initializes exception with a debug message
	 * @param msg the debug message
	 */
    public LayoutException(String msg) {
        super(msg);
    }

    public LayoutException(String msg, Throwable t) {
        super(msg, t);
    }
}
