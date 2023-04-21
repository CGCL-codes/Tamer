package org.dbe.servent.bpel;

/**
 * Used in deployed bpels to stop call execution
 *
 * @author bob
 */
public class BpelAdapterException extends Exception {

    /**
     * @param message
     */
    public BpelAdapterException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public BpelAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
