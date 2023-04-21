package org.geonetwork.utils.visitor;

/**
 * 
 * @author heikki doeleman
 *
 */
public class ObjectGraphVisitorException extends Exception {

    private static final long serialVersionUID = -2297033641381751461L;

    public ObjectGraphVisitorException() {
        super();
    }

    public ObjectGraphVisitorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectGraphVisitorException(String message) {
        super(message);
    }

    public ObjectGraphVisitorException(Throwable cause) {
        super(cause);
    }
}
