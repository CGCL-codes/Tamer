package uk.ac.ebi.intact.util;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * ETA (Estimated Time of Arrival) Calculator.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 2.1
 */
@Deprecated
public class ETACalculator {

    private static final Log log = LogFactory.getLog(ETACalculator.class);

    private final long startTime;

    private final long totalCount;

    /**
     * @param startTime  start time in milliseconds between the current time and midnight, January 1, 1970 UTC.
     * @param totalCount total could of units to be processed.
     */
    public ETACalculator(long startTime, long totalCount) {
        this.startTime = startTime;
        this.totalCount = totalCount;
    }

    /**
     * Create a calculator for which the start time is now.
     *
     * @param totalCount total could of units to be processed.
     */
    public ETACalculator(long totalCount) {
        this(System.currentTimeMillis(), totalCount);
    }

    /**
     * Calculate ETA and returns the remaining time in seconds.
     *
     * @param currentCount current count of unit processed.
     * @return count of seconds.
     */
    public long calculateETA(long currentCount) {
        if (currentCount > totalCount) {
            log.warn("ETA Calculator: user gave a number of unit processed (" + currentCount + ") greater than the maximum (" + totalCount + ")");
            return 0;
        }
        final long currentTime = System.currentTimeMillis();
        final long timeElapsed = currentTime - startTime;
        if (currentCount == 0) {
            currentCount = 1;
        }
        final long eta = (totalCount * timeElapsed) / currentCount;
        return eta / 1000;
    }

    /**
     * Print a user friendly time for estimated time of completion of the task.
     *
     * @param currentCount current count of unit processed.
     * @return a nicely formatted remaining time.
     */
    public String printETA(long currentCount) {
        long eta = calculateETA(currentCount);
        return new ElapsedTime((int) eta).toString();
    }
}
