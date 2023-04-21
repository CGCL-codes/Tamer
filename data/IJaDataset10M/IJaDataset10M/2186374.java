package edu.rice.cs.cunit.record.syncPoints.thread;

import edu.rice.cs.cunit.record.graph.ThreadInfo;
import edu.rice.cs.cunit.record.syncPoints.ISyncPointVisitor;
import edu.rice.cs.cunit.instrumentors.DoNotInstrument;

/**
 * Synchronization point corresponding to the end of a Thread.sleep call.
 *
 * @author Mathias Ricken
 */
@DoNotInstrument
public class ThreadLeaveSleepSyncPoint extends AThreadSyncPoint {

    /**
     * Duration of sleep.
     */
    private long _duration;

    /**
     * Constructor for this synchronization point.
     * @param thread current thread
     * @param duration duration of sleep
     */
    public ThreadLeaveSleepSyncPoint(Thread thread, long duration) {
        super(thread);
        _duration = duration;
    }

    /**
     * Class for translated versions on the monitor side.
     */
    public static class Translated extends AThreadSyncPoint.Translated {

        /**
         * Duration of sleep.
         */
        private long _duration;

        /**
         * Constructor for translated version on the monitor side.
         * @param thread current thread
         * @param duration duration of sleep
         */
        public Translated(ThreadInfo thread, long duration) {
            super(thread);
            _duration = duration;
        }

        /**
         * Executes a visitor.
         *
         * @param visitor visitor to execute
         * @param param   visitor-specific parameter
         *
         * @return visitor-specific return value
         */
        public <R, P> R execute(ISyncPointVisitor<R, P> visitor, P param) {
            return visitor.threadSleepEndCase(this, param);
        }

        /**
         * Returns the duration of the sleep
         * @return duration
         */
        public long getDuration() {
            return _duration;
        }

        /**
         * Returns a string representation of the object.
         * @return a string representation of the object.
         */
        public String toString() {
            return super.toString() + " duration=" + _duration;
        }
    }
}
