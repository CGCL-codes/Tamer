package org.jpropeller.properties.impl;

/**
 * An exception thrown when a PathProp cannot look up its value
 * @author shingoki
 */
public class PathPropException extends RuntimeException {

    private static final long serialVersionUID = -3038172194366314897L;

    public PathPropException() {
    }

    public PathPropException(String message) {
        super(message);
    }

    public PathPropException(Throwable cause) {
        super(cause);
    }

    public PathPropException(String message, Throwable cause) {
        super(message, cause);
    }
}
