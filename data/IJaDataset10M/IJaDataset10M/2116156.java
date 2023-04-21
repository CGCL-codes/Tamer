package pl.rmalinowski.gwt2swf.client.ui.exceptions;

/**
 * @author Rafal Malinowski 
 *
 */
public class UnsupportedFlashPlayerVersionException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3894636978597293493L;

    /**
	 * 
	 */
    public UnsupportedFlashPlayerVersionException() {
    }

    /**
	 * @param message
	 */
    public UnsupportedFlashPlayerVersionException(String message) {
        super(message);
    }

    /**
	 * @param couse
	 */
    public UnsupportedFlashPlayerVersionException(Throwable couse) {
        super(couse);
    }

    /**
	 * @param message
	 * @param couse
	 */
    public UnsupportedFlashPlayerVersionException(String message, Throwable couse) {
        super(message, couse);
    }
}
