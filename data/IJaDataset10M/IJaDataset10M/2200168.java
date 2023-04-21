package edu.rice.cs.cunit.record.syncPoints.thread;

import edu.rice.cs.cunit.record.graph.ThreadInfo;
import edu.rice.cs.cunit.record.syncPoints.ISyncPointVisitor;
import edu.rice.cs.cunit.instrumentors.DoNotInstrument;

/**
 * Synchronization point corresponding to a Thread.interrupt call.
 *
 * @author Mathias Ricken
 */
@DoNotInstrument
public class ThreadInterruptSyncPoint extends AThreadSyncPoint {

    /**
     * Constructor for this synchronization point.
     * @param thread thread whose interrupt method was called.
     */
    public ThreadInterruptSyncPoint(Thread thread) {
        super(thread);
    }

    /**
     * Class for translated versions on the monitor side.
     */
    public static class Translated extends AThreadSyncPoint.Translated {

        /**
         * Constructor for translated version on the monitor side.
         * @param threadInfo info about the thread
         */
        public Translated(ThreadInfo threadInfo) {
            super(threadInfo);
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
            return visitor.threadInterruptCase(this, param);
        }
    }
}
