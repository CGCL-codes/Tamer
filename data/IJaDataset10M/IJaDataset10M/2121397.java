package prajna.data;

import java.util.Date;
import java.util.SortedSet;

/**
 * This interface is a utility for handling sequences of time events. Time
 * spans are added to an internal list, and the Sequential interface allows an
 * object to represent spans of time - for instance, when something is active,
 * or when a series of specific events occurred.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public interface Sequential {

    /**
     * Adds a time segment - a start/stop time - to the sequence's list of
     * start/stop times.
     * 
     * @param start the new start time
     * @param stop the new stop time
     */
    public void addTimeSegment(Date start, Date stop);

    /**
     * Adds a time segment - a start/stop time - to the sequence's list of
     * start/stop times. The stop time is computed to be <tt>duration</tt>
     * milliseconds after the start time
     * 
     * @param start the new start time
     * @param duration the duration of the time segment
     */
    public void addTimeSegment(Date start, long duration);

    /**
     * Adds a TimeSpan to the sequence's list of TimeSpans.
     * 
     * @param span the new time span
     */
    public void addTimeSpan(TimeSpan span);

    /**
     * Gets the start time of the time segment that includes <tt>time</tt>
     * 
     * @param time the time to use to find the time segment
     * @return the start time of the time segment that includes <tt>time</tt>
     */
    public Date getActiveStartTime(Date time);

    /**
     * Gets the stop time of the time segment that includes <tt>time</tt>
     * 
     * @param time the time to use to find the time segment
     * @return the stop time of the time segment that includes <tt>time</tt>
     */
    public Date getActiveStopTime(Date time);

    /**
     * Returns the earliest time that this sequence is active. If there are no
     * time segments in this edge, returns null.
     * 
     * @return the earliest time this edge is active.
     */
    public Date getEarliestTime();

    /**
     * Returns the latest time that this sequence is active. If there are no
     * time segments in this edge, returns null.
     * 
     * @return the latest time this edge is active.
     */
    public Date getLatestTime();

    /**
     * Returns the sorted set of TimeSpan objects representing the sequence.
     * The TimeSpans should be in time order (earliest to latest).
     * 
     * @return an ordered sequence of TimeSpans
     */
    public SortedSet<TimeSpan> getTimeSpans();

    /**
     * Returns whether the sequence of time spans is active at the test date.
     * If the testDate is between a start/stop time pair, the sequence is
     * active; otherwise it isn't.
     * 
     * @param testDate the date to test to see if the sequence is active.
     * @return whether the sequence is active
     */
    public boolean isActive(Date testDate);

    /**
     * Returns whether the sequence is active at the specified time. The
     * date/time tested is equal to <tt>millis</tt> milliseconds after Jan 1,
     * 1970
     * 
     * @param millis the time, in milliseconds after Jan 1, 1970, to test to
     *            see if the sequence is active.
     * @return whether the sequence is active
     */
    public boolean isActive(long millis);
}
