package org.xaware.server.statistics;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.StatisticalSummary;

/**
 * Class for storing historical statistics in a fixed size rolling buffer for calculating "sliding window" averages and
 * other statistics.
 * 
 * @author Tim Uttormark
 */
public class FixedSizeRollingBuffer implements IStatsBuffer {

    /** The object which stores the data values and calculates stats from them */
    protected DescriptiveStatistics statsBuffer = null;

    /**
     * Constructor.
     */
    public FixedSizeRollingBuffer(final int size) {
        statsBuffer = DescriptiveStatistics.newInstance();
        statsBuffer.setWindowSize(size);
    }

    public void addValue(final double newValue, final long timestamp) {
        statsBuffer.addValue(newValue);
    }

    public synchronized StatisticalSummary getStatisticalSummary() {
        return new DerivedStats(statsBuffer);
    }
}
