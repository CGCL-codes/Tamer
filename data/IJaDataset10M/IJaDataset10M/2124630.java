package jeeobserver;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class CallTimeStatistics.
 *
 * @author Luca Mingardi
 * @version 3.1
 */
public abstract class CallTimeStatistics implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 31L;

    /** The first timestamp. */
    private final Date firstTimestamp;

    /** The total calls. */
    private final long totalCalls;

    /** The average time nanos. */
    private final double averageTimeNanos;

    /** The total time nanos. */
    private final long totalTimeNanos;

    /** The average cpu nanos. */
    private final double averageCpuNanos;

    /** The total cpu nanos. */
    private final long totalCpuNanos;

    /** The total exceptions. */
    private final long totalExceptions;

    /**
	 * Instantiates a new call time statistics.
	 *
	 * @param firstTimestamp the first timestamp
	 * @param totalCalls the total calls
	 * @param averageTimeNanos the average time nanos
	 * @param totalTimeNanos the total time nanos
	 * @param averageCpuNanos the average cpu nanos
	 * @param totalCpuNanos the total cpu nanos
	 * @param totalExceptions the total exceptions
	 */
    protected CallTimeStatistics(Date firstTimestamp, long totalCalls, double averageTimeNanos, long totalTimeNanos, double averageCpuNanos, long totalCpuNanos, long totalExceptions) {
        super();
        this.firstTimestamp = firstTimestamp;
        this.totalCalls = totalCalls;
        this.averageTimeNanos = averageTimeNanos;
        this.totalTimeNanos = totalTimeNanos;
        this.averageCpuNanos = averageCpuNanos;
        this.totalCpuNanos = totalCpuNanos;
        this.totalExceptions = totalExceptions;
    }

    /**
	 * Gets the first timestamp.
	 *
	 * @return the first timestamp
	 */
    public Date getFirstTimestamp() {
        return this.firstTimestamp;
    }

    /**
	 * Gets the total calls.
	 *
	 * @return the total calls
	 */
    public long getTotalCalls() {
        return this.totalCalls;
    }

    /**
	 * Gets the average time nanos.
	 *
	 * @return the average time nanos
	 */
    public double getAverageTimeNanos() {
        return this.averageTimeNanos;
    }

    /**
	 * Gets the total time nanos.
	 *
	 * @return the total time nanos
	 */
    public long getTotalTimeNanos() {
        return this.totalTimeNanos;
    }

    /**
	 * Gets the average cpu nanos.
	 *
	 * @return the average cpu nanos
	 */
    public double getAverageCpuNanos() {
        return this.averageCpuNanos;
    }

    /**
	 * Gets the total cpu nanos.
	 *
	 * @return the total cpu nanos
	 */
    public long getTotalCpuNanos() {
        return this.totalCpuNanos;
    }

    /**
	 * Gets the total exceptions.
	 *
	 * @return the total exceptions
	 */
    public long getTotalExceptions() {
        return this.totalExceptions;
    }
}
