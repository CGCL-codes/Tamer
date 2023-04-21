package edu.harvard.mcz.imagecapture.exceptions;

/** UnreadableFileException can be thrown when a file can't be read
 * for any reason (non-existent, not allowed by security context, etc).  
 * 
 * @author Paul J. Morris
 *
 */
public class UnreadableFileException extends Exception {

    private static final long serialVersionUID = -8398415097676132152L;

    /** Exception to throw for any sort of problem reading 
	 * a file.
	 */
    public UnreadableFileException() {
    }

    /**
	 * @param message
	 */
    public UnreadableFileException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public UnreadableFileException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public UnreadableFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
