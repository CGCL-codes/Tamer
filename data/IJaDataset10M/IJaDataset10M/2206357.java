package edu.rice.cs.cunit.instrumentors;

import java.io.IOException;

/**
 * An IOException that we can retry.
 * @author Mathias Ricken
 */
public abstract class RetryIOException extends IOException {

    /**
     * Creates a new retryable IOException
     * @param s message
     */
    public RetryIOException(String s) {
        super(s);
    }

    /**
     * Retry the failed operation one more time.
     * @throws java.io.IOException
     */
    public abstract void retry() throws IOException;
}
