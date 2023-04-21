package org.waveprotocol.wave.federation;

import org.waveprotocol.wave.federation.FederationErrorProto.FederationError;

/**
 * Wraps a federation error.
 *
 * @author soren@google.com (Soren Lassen)
 */
public class FederationException extends Exception {

    private final FederationError error;

    public FederationException(FederationError error) {
        super(error.getErrorCode() + " " + error.getErrorMessage());
        this.error = error;
    }

    public FederationError getError() {
        return error;
    }
}
