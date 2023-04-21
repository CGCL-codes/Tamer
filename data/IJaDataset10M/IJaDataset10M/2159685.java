package org.datanucleus.exceptions;

import org.datanucleus.util.Localiser;

/**
 * An TransactionNotReadableException is thrown if an operation needs either of
 * an active transaction or non-transactional read and neither is true.
 */
public class TransactionNotReadableException extends TransactionNotActiveException {

    private static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.Localisation", org.datanucleus.ClassConstants.NUCLEUS_CONTEXT_LOADER);

    /**
     * Constructor.
     */
    public TransactionNotReadableException() {
        super(LOCALISER.msg("015041"), null);
    }

    /**
     * Constructor.
     * @param message the localized error message
     * @param failedObject the failed object
     */
    public TransactionNotReadableException(String message, Object failedObject) {
        super(message, failedObject);
    }
}
