package org.waveprotocol.box.server.waveserver;

/**
 * Indicates a caller tried to submit or request deltas for a
 * participant that is not a particiant on the wavelet.
 *
 *
 *
 */
public class AccessControlException extends WaveServerException {

    public AccessControlException(String message) {
        super(message);
    }

    public AccessControlException(Throwable cause) {
        super(cause);
    }

    public AccessControlException(String message, Throwable cause) {
        super(message, cause);
    }
}
