package com.jeronimo.eko.p2p.transport;

/**
 * Whenever the communication is interrupted because the network timeout has been reached.
 * @author J�r�me Bonnet
 *
 */
@SuppressWarnings("serial")
public class TimeoutException extends TransportException {

    public TimeoutException() {
        super();
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(Throwable cause) {
        super(cause);
    }
}
