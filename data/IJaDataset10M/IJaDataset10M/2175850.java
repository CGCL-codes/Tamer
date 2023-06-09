package lejos.util;

/**
 * Simple collection of time delay routines that are non interruptable.
 * @author andy
 */
public class Delay {

    /**
     * Wait for the specified number of milliseconds.
     * Delays the current thread for the specified period of time. Can not
     * be interrupted (but it does preserve the interrupted state).
     * @param period time to wait in ms
     */
    public static void msDelay(long period) {
        if (period <= 0) return;
        long end = System.currentTimeMillis() + period;
        boolean interrupted = false;
        do {
            try {
                Thread.sleep(period);
            } catch (InterruptedException ie) {
                interrupted = true;
            }
            period = end - System.currentTimeMillis();
        } while (period > 0);
        if (interrupted) Thread.currentThread().interrupt();
    }

    /**
     * Wait for the specified number of microseconds.
     * Delays the current thread for the specified period of time. Can not
     * be interrupted.
     * @param period time to wait in us
     */
    @SuppressWarnings("empty-statement")
    public static void usDelay(long period) {
        long end = System.nanoTime() + period * 1000;
        msDelay(period / 1000);
        while (System.nanoTime() < end) ;
    }

    /**
     * Wait for the specified number of nanoseconds.
     * Delays the current thread for the specified period of time. Can not
     * be interrupted.
     * @param period time to wait in ns
     */
    @SuppressWarnings("empty-statement")
    public static void nsDelay(long period) {
        long end = System.nanoTime() + period;
        msDelay(period / 1000000);
        while (System.nanoTime() < end) ;
    }
}
