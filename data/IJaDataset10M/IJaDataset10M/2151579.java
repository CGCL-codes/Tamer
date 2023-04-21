package com.iparelan.util;

import java.util.concurrent.TimeUnit;
import com.iparelan.util.annotations.Copyright;

/**
 * Various constants that need a good home.
 *
 * @author Greg Mattes
 * @version October 2008
 */
@Copyright("Copyright &copy; 2008, Iparelan Solutions, LLC. All rights reserved.")
public final class UtilityConstants {

    /** The number of bytes in a megabyte: 2<sup>20</sup>. */
    public static final int BYTES_PER_MEGABYTE = (1024 * 1024);

    /** Divide by this to get a percentage. */
    public static final float PERCENTAGE_SCALE = 100.0F;

    /**
     * Several convenient, pre-fabricated intervals of time.
     *
     * <p>Example usage:
     *
     * <p>{@code TimeInterval.TEN_SECONDS.toMillis();}
     *
     * @to.do
     *
     * <ul>
     *
     * <li>Rename this to {@code Duration}? As in JavaFX...
     *
     * </ul>
     */
    public enum TimeInterval {

        /** One second.     */
        ONE_SECOND(1L, TimeUnit.SECONDS), /** Two seconds.    */
        TWO_SECONDS(2L, TimeUnit.SECONDS), /** Three seconds.  */
        THREE_SECONDS(3L, TimeUnit.SECONDS), /** Five seconds.   */
        FIVE_SECONDS(5L, TimeUnit.SECONDS), /** Ten seconds.    */
        TEN_SECONDS(10L, TimeUnit.SECONDS), /** Thirty seconds. */
        THIRTY_SECONDS(30L, TimeUnit.SECONDS), /** One minute.     */
        ONE_MINUTE(1L, TimeUnit.MINUTES);

        /** The amount of milliseconds in this interval. */
        private long milliseconds;

        /**
         * Creates a {@code TimeInterval}.
         *
         * @param sourceDuration
         *
         *        The length of this intervale in {@code sourceUnit}s.
         *
         * @param sourceUnit
         *
         *        The unit of time in which {@code sourceDuration} is
         *        specified. May not be {@code null}.
         */
        TimeInterval(final long sourceDuration, final TimeUnit sourceUnit) {
            milliseconds = sourceUnit.toMillis(sourceDuration);
        }

        /**
         * Produces the amount of milliseconds in this interval.
         *
         * @return The amount of milliseconds in this interval.
         */
        public long toMillis() {
            return milliseconds;
        }
    }

    /** Not instantiable. */
    private UtilityConstants() {
    }
}
