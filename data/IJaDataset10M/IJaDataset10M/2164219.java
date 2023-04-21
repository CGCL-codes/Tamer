package org.ps.jgroupdownloadercore.exception;

/**
 * Thrown to indicate that the application has attempted to read/interpret ate html photo page and
 * don't found a expected element.
 * 
 * @author Rennan Stefan Boni - <a href="mailto:rennanboni@gmail.com">rennanboni@gmail.com</a>
 */
public class RequestSearchGroupPhotosException extends Exception {

    private static final long serialVersionUID = 7864842194737932475L;

    /**
	 * Constructor class.
	 * 
	 * @see Exception#Exception()
	 */
    public RequestSearchGroupPhotosException() {
        super();
    }

    /**
	 * Constructor class.
	 * 
	 * @see Exception#Exception(String, Throwable)
	 */
    public RequestSearchGroupPhotosException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Constructor class.
	 * 
	 * @see Exception#Exception(String)
	 */
    public RequestSearchGroupPhotosException(String message) {
        super(message);
    }

    /**
	 * Constructor class.
	 * 
	 * @see Exception#Exception(Throwable)
	 */
    public RequestSearchGroupPhotosException(Throwable cause) {
        super(cause);
    }
}
