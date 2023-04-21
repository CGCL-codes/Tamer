package uk.co.thebadgerset.junit.extensions;

/**
 * SleepThrottle is a Throttle implementation that generates short pauses using the thread sleep methods. As the pauses
 * get shorter, this technique gets more innacurate. In practice, around 200 Hz is the cap rate for accuracy.
 *
 * <p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Accept throttling rate in operations per second.
 * <tr><td> Inject short pauses to fill out processing cycles to a specified rate.
 * </table>
 *
 * @todo Introduce an adaptive feedback to compensate for short term and accumulated errors.
 *
 * @todo Introduce active looping for very short pauses, using a dummy for loop to waste cpu time. Add a callibration
 *       method to initialize its callibration.
 *
 * @author Rupert Smith
 */
public class SleepThrottle implements Throttle {

    /** Holds the length of a single cycle in nano seconds. */
    private long cycleTimeNanos;

    /** Holds the time of the last call to the throttle method in nano seconds. */
    private long lastTimeNanos;

    /**
     * Flag used to detect the first call to the throttle method. Zero or negative start time cannot be relied on
     * to detect this as System.nanoTime can return zero or negative values.
     */
    boolean firstCall = true;

    /**
     * Specifies the throttling rate in operations per second. This must be called with with a value, the inverse
     * of which is a measurement in nano seconds, such that the number of nano seconds do not overflow a long integer.
     * The value must also be larger than zero.
     *
     * @param hertz The throttling rate in cycles per second.
     */
    public void setRate(float hertz) {
        if (hertz <= 0.0f) {
            throw new IllegalArgumentException("The throttle rate must be above zero.");
        }
        cycleTimeNanos = (long) (1000000000f / hertz);
        firstCall = false;
    }

    /**
     * This method can only be called at the rate set by the {@link #setRate} method, if it is called faster than this
     * it will inject short pauses to restrict the call rate to that rate.
     */
    public void throttle() {
        long currentTimeNanos = System.nanoTime();
        if (!firstCall) {
            long remainingTimeNanos = cycleTimeNanos - (currentTimeNanos - lastTimeNanos);
            if (remainingTimeNanos > 0) {
                long milliPause = remainingTimeNanos / 1000000;
                int nanoPause = (int) (remainingTimeNanos % 1000000);
                try {
                    Thread.sleep(milliPause, nanoPause);
                } catch (InterruptedException e) {
                }
            }
        } else {
            firstCall = false;
        }
        lastTimeNanos = System.nanoTime();
    }
}
