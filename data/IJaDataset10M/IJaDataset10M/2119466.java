package utilities.utilities4testing.generator;

/**
 * Pause a thread execution or wait until a condition was true.
 */
public abstract class Execution {

    /**
	 * Thread.sleep ignoring InterruptedException.
	 * @param timeInMilliseconds time in milliseconds
	 */
    public static void pause(long timeInMilliseconds) {
        try {
            Thread.sleep(timeInMilliseconds);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Wait until the condition specified was true or a timeout occur.
     * @param timeoutInMilliseconds total timeout
     * @param intervalInMilliseconds interval between tentatives
     * @return true if the condition was satisfied or false on error or timeout.
     */
    public boolean waitForCondition(long timeoutInMilliseconds, long intervalInMilliseconds) {
        long tentatives = timeoutInMilliseconds / intervalInMilliseconds;
        while (tentatives > 0 && !until()) {
            try {
                Thread.sleep(intervalInMilliseconds);
            } catch (InterruptedException e) {
            }
            tentatives--;
        }
        return tentatives > 0;
    }

    /**
     * Override this method. Method waiting will wait until this method returns true or
     * a timeout occur.
     * @return a condition to wait for
     */
    protected abstract boolean until();
}
