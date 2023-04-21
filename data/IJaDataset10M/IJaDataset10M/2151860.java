package de.fhg.igd.semoa.comm;

import java.io.IOException;

/**
 * Thrown by the Portal Daemon (Pod) components is a packet
 * could not be delivered.
 *
 * @author Volker Roth
 * @version "$Id: PodException.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class PodException extends IOException {

    /**
     * The error code.
     */
    private int err_;

    public PodException() {
    }

    public PodException(String message) {
        super(message);
    }

    public PodException(String message, int err) {
        super(message);
        err_ = err;
    }

    public int getErrorCode() {
        return err_;
    }
}
