package net.assimilator.watch;

import net.jini.config.Configuration;

/**
 * A Watch for capturing elapsed time
 */
public class StopWatch extends ThresholdWatch {

    public static final String VIEW = "net.assimilator.watch.ResponseTimeCalculableView";

    /** Holds value of property startTime. */
    private long startTime;

    /**
     * Creates new Stop Watch
     * 
     * @param id the identifier for this watch
     */
    public StopWatch(String id) {
        super(id);
        setView(VIEW);
    }

    /**
     * Creates new StopWatch, creates and exports a
     * WatchDataSourceImpl if the WatchDataSource is null using the
     * Configuration object provided
     *
     * @param id The identifier for this watch
     * @param config Configuration object used for constructing a WatchDataSource
     */
    public StopWatch(String id, Configuration config) {
        super(id, null, config);
        setView(VIEW);
    }

    /**
     * Creates new StopWatch, creates and exports a
     * WatchDataSourceImpl if the WatchDataSource is null using the
     * Configuration object provided
     *
     * @param id The identifier for this watch
     * @param archivable The Archivable
     * @param config Configuration object used for constructing a WatchDataSource
     */
    public StopWatch(String id, Archivable archivable, Configuration config) {
        super(id, archivable, config);
        setView(VIEW);
    }

    /**
     * Creates new Stop Watch
     * 
     * @param watchDataSource the watch data source associated with this watch
     * @param id the identifier for this watch
     */
    public StopWatch(WatchDataSource watchDataSource, String id) {
        super(watchDataSource, id);
        setView(VIEW);
    }

    /**
     * Start the timing
     */
    public void startTiming() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Stop the timing
     */
    public void stopTiming() {
        long now = System.currentTimeMillis();
        setElapsedTime(now - startTime, now);
    }

    /**
     * Sets the elapsed time of the measured interval
     * 
     * @param elapsed milliseconds of elapsed time
     */
    public void setElapsedTime(long elapsed) {
        setElapsedTime(elapsed, System.currentTimeMillis());
    }

    /**
     * Sets the elapsed time of the measured interval
     * 
     * @param elapsed milliseconds of elapsed time
     * @param now the current time in milliseconcds since the epoch.
     */
    public void setElapsedTime(long elapsed, long now) {
        addWatchRecord(new Calculable(id, (double) elapsed, now));
    }

    /**
     * Getter for property startTime
     * 
     * @return Value of property startTime.
     */
    public long getStartTime() {
        return (startTime);
    }

    /**
     * Setter for property startTime
     * 
     * @param startTime New value of property startTime.
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
