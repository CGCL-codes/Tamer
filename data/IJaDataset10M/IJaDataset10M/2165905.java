package org.mobicents.media.server.ctrl.mgcp.signal;

/**
 *
 * @author kulikov
 */
public class UnknownSignalException extends Exception {

    /**
     * Creates a new instance of <code>UnknownSignalException</code> without detail message.
     */
    public UnknownSignalException() {
    }

    /**
     * Constructs an instance of <code>UnknownSignalException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public UnknownSignalException(String msg) {
        super(msg);
    }
}
