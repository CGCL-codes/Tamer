package org.identifylife.descriptlet.store;

/**
 * @author dbarnier
 * 
 */
@SuppressWarnings("serial")
public class StoreException extends RuntimeException {

    public StoreException(String msg) {
        super(msg);
    }

    public StoreException(Throwable cause) {
        super(cause);
    }

    public StoreException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
