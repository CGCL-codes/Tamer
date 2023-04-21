package org.jini.activation;

/**
 *
 * @author  Alfio Zappala
 * @version 1.0
 */
public class ServiceUpdateFailedException extends ServiceActionException {

    public ServiceUpdateFailedException() {
    }

    /**
     * Constructs an <code>ServiceUpdateFailedException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ServiceUpdateFailedException(String msg) {
        super(msg);
    }
}
