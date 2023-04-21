package net.sf.balm.common.bind;

/**
 * @author dz
 */
public class DataBindException extends RuntimeException {

    /**
     * 
     *
     */
    public DataBindException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public DataBindException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public DataBindException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public DataBindException(Throwable cause) {
        super(cause);
    }
}
