package org.greatlogic.gae;

import com.google.appengine.repackaged.com.google.common.collect.*;
import java.util.*;
import java.util.concurrent.*;

public final class GLTimers {

    private static final String _defaultTimerID;

    private static final GLTimer _unknownTimer;

    private static final Map<String, GLTimer> _timerMap;

    static {
        _defaultTimerID = "";
        _unknownTimer = new GLTimer("");
        _timerMap = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);
    }

    /**
 * Returns the total and average time for the number of iterations used in the <code>start</code>
 * request.
 * @param timerID The timer ID.
 * @return The total and average time in a format that can be used in a log line.
 */
    public static String getResultMicros(final String timerID) {
        return getResult(timerID, TimeUnit.MICROSECONDS);
    }

    /**
 * Returns the total and average time for the number of iterations used in the <code>start</code>
 * request.
 * @param timerID The timer ID.
 * @return The total and average time in a format that can be used in a log line.
 */
    public static String getResultMillis(final String timerID) {
        return getResult(timerID, TimeUnit.MILLISECONDS);
    }

    /**
 * Returns the total and average time for the number of iterations used in the <code>start</code>
 * request.
 * @param timerID The timer ID.
 * @return The total and average time in a format that can be used in a log line.
 */
    public static String getResultNanos(final String timerID) {
        return getResult(timerID, TimeUnit.NANOSECONDS);
    }

    private static String getResult(final String timerID, final TimeUnit timeUnit) {
        return getTimer(timerID).toString(timeUnit);
    }

    /**
 * Returns the timer for the requested timer identifier.
 * @param timerID The timer identifier.
 * @return The timer identified by the requested timer identifier.  If there is no timer for the
 * requested identifier then an empty timer will be returned.
 */
    public static GLTimer getTimer(final String timerID) {
        String localTimerID = timerID == null ? _defaultTimerID : timerID;
        GLTimer result = _timerMap.get(localTimerID);
        return result == null ? _unknownTimer : result;
    }

    public static long getTotalMicros(final String timerID) {
        return getTimer(timerID).getTotalMicros();
    }

    public static long getTotalMillis(final String timerID) {
        return getTimer(timerID).getTotalMillis();
    }

    public static long getTotalNanos(final String timerID) {
        return getTimer(timerID).getTotalNanos();
    }

    /**
 * Starts a new or existing timer.  If the timer ID already exists (as a result of a previous
 * <code>start()</code> request) then it will be restarted, and time will be added to the
 * previously logged time for that timer.  If the timer is already running then attempting to start
 * it again will have no effect (it will continue to run).
 * @param timerID The identifier for the timer.  This can be null.
 * @param resetBeforeUsing Indicates whether the timer should be reset before using it.  If the
 * timer is not reset then additional time will be added to the previously accumulated time.  If
 * the timer did not already exist then this indicator will have no effect.
 */
    public static void start(final String timerID, final boolean resetBeforeUsing) {
        String localTimerID = timerID == null ? _defaultTimerID : timerID;
        GLTimer timer = _timerMap.get(localTimerID);
        if (timer == null || resetBeforeUsing) {
            timer = new GLTimer(localTimerID);
            _timerMap.put(localTimerID, timer);
        }
        timer.start();
    }

    /**
 * Stops a timer.  If the timer does not exist or if the timer is already stopped then this request
 * will have no effect.
 * @param timerID The identifier for the timer.  This can be null.
 * @param iterations The number of iterations that have been processed since the timer was started.
 */
    public static void stop(final String timerID, final long iterations) {
        String localTimerID = timerID == null ? _defaultTimerID : timerID;
        GLTimer timer = _timerMap.get(localTimerID);
        if (timer != null) {
            timer.stop(iterations);
        }
    }

    private GLTimers() {
    }
}
