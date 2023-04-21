package com.sun.sgs.impl.util;

/**
 * A utility method that provides support for determining when and whether to
 * retry I/O operations.
 */
public class ShouldRetryIo {

    /** The maximum number of milliseconds to retry failing I/O operations. */
    private final long maxRetry;

    /** The number of milliseconds to wait between retries. */
    private final long retryWait;

    /**
     * The time of the first I/O failure, or {@code -1} if no failures have
     * been seen.
     */
    private long failureStarted = -1;

    /**
     * Creates an instance of this class.
     *
     * @param	maxRetry the maximum number of milliseconds to retry failing
     *		I/O operations
     * @param	retryWait the number of milliseconds to wait between retries
     * @throws	IllegalArgumentException if either argument is negative
     */
    public ShouldRetryIo(long maxRetry, long retryWait) {
        if (maxRetry < 0 || retryWait < 0) {
            throw new IllegalArgumentException("The maxRetry and retryWait must not be negative");
        }
        this.maxRetry = maxRetry;
        this.retryWait = retryWait;
    }

    /**
     * Checks if an I/O operation that had an I/O failure should be retried,
     * delaying returning if the retry should be delayed.
     *
     * @return	whether the I/O operation should be retried
     */
    public boolean shouldRetry() {
        long now = System.currentTimeMillis();
        if (failureStarted == -1) {
            failureStarted = now;
        } else if (now + retryWait - failureStarted > maxRetry) {
            return false;
        }
        try {
            while (true) {
                long remaining = (now + retryWait) - System.currentTimeMillis();
                if (remaining <= 0) {
                    break;
                }
                Thread.sleep(remaining);
            }
        } catch (InterruptedException e) {
        }
        return true;
    }

    /**
     * Notes that an I/O operation has succeeded.  Use this method when an I/O
     * operation succeeds but retries are still needed, so that the timer for
     * failing I/O operations is reset.
     */
    public void ioSucceeded() {
        failureStarted = -1;
    }
}
