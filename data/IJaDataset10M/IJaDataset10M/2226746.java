package org.rg.common.datatypes.ttw;

/**
 * This is the tilted time window interface.
 * @author redman
 */
public interface XTimeWindow {

    /**
     * Approximate the count over the given period of time from start time to
     * end time.
     * @param startTime this is the point in time where the query should start.
     * @param endTime the is the last point in time for the query.
     * @return the approximate count within the window.
     */
    public long approximate(long startTime, long endTime);

    /**
     * Approximate the count over the given period of time from start time to
     * end time. This deprecated method returns an Object, and includes a query
     * time, the time the query is run.
     * @param queryTime the time of the query.
     * @param startTime this is the point in time where the query should start.
     * @param endTime the is the last point in time for the query.
     * @return the approximate count within the window.
     * @deprecated
     */
    public Object approximate(long queryTime, long startTime, long endTime);

    /**
     * Get the most historic time covered by this window.
     * @return the most ancient time covered by this window.
     */
    public long getStartTime();

    /**
     * Get the time of the first update event.
     * @return the time of the first update.
     */
    public long getFirstUpdateTime();

    /**
     * Get the most current time covered by this window. This time is triggered
     * by updates, so it only reflects the starting point of the most recent
     * slot in the window.
     * @return the most ancient time covered by this window.
     */
    public long getCurrentTime();

    /**
     * Get an exact count over the given period of time from start time to end
     * time.
     * @param startTime this is the point in time where the query should start.
     * @param endTime the is the last point in time for the query.
     * @return the approximate count within the window.
     * @throws InexactQueryException
     */
    public long query(long startTime, long endTime) throws InexactQueryException;

    /**
     * Get an exact count over the given period of time from start time to end
     * time. Deprecated in favor of the method that takes no queryTime and
     * returns a long.
     * @param queryTime this will go away, it is the time of the query.
     * @param startTime this is the point in time where the query should start.
     * @param endTime the is the last point in time for the query.
     * @return the approximate count within the window.
     * @throws InexactQueryException
     * @deprecated
     */
    public Object query(long queryTime, long startTime, long endTime) throws InexactQueryException;

    /**
     * This method will hopefully go away.
     * @return a summary.
     */
    public long summary();

    /**
     * Have new data, update the count.
     * @param data
     * @param timestamp
     */
    public void update(long data, long timestamp);

    /**
     * Have new data, update the count. this method takes an object as a
     * parameter, it has been deprecated.
     * @param data
     * @param timestamp
     * @deprecated
     */
    public void update(Object data, long timestamp);
}
