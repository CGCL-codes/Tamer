package it.pronetics.madstore.repository.test.util;

/**
 * Thread extension for recording test failures and verifying them.
 */
public class TesterThread extends Thread {

    private final Runnable test;

    private Exception exceptionFailure;

    private AssertionError assertionFailure;

    /**
     * Package access: use {@link TesterThreadFactory#newThread(Runnable )} instead.
     */
    TesterThread(Runnable test) {
        this.test = test;
    }

    public final void run() {
        try {
            this.test.run();
        } catch (Exception ex) {
            this.exceptionFailure = ex;
        } catch (AssertionError err) {
            this.assertionFailure = err;
        }
    }

    public void verifyAndThrow() throws AssertionError, Exception {
        if (this.exceptionFailure != null) {
            throw exceptionFailure;
        }
        if (this.assertionFailure != null) {
            throw assertionFailure;
        }
    }
}
