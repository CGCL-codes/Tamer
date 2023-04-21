package com.leemba.monitor.server.exceptions;

/**
 *
 * @author mrjohnson
 */
public class NrpeProtocolException extends Exception {

    public NrpeProtocolException(Throwable cause) {
        super(cause);
    }

    public NrpeProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public NrpeProtocolException(String message) {
        super(message);
    }
}
