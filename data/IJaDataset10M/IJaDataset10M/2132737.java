package net.sf.parcinj;

/**
 * Immutable class representing the result of a {@link Symbol#matches(Token)}
 * invocation. If matching isn't successful a human readable failure reason is
 * provided.
 * 
 */
public final class MatchingResult {

    /**
   * Successful matching result.
   */
    public static final MatchingResult OK = new MatchingResult(null);

    /**
   * Creates a matching failure for the specified reason. If reason isn't
   * specified <code>unknown</code> is used.
   */
    public static MatchingResult failure(String reason) {
        if (reason == null || reason.length() == 0) {
            reason = "unknown";
        }
        return new MatchingResult(reason);
    }

    private final boolean _successful;

    private final String _failureReason;

    private MatchingResult(String failureReason) {
        _successful = failureReason == null;
        _failureReason = _successful ? "" : failureReason;
    }

    /**
   * Returns <code>true</code> if matching was successful.
   */
    public final boolean isSuccessful() {
        return _successful;
    }

    /**
   * Returns the reason of failure.
   */
    public final String getFailureReason() {
        return _failureReason;
    }
}
