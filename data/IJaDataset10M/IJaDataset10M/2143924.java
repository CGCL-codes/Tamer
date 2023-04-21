package edu.rice.cs.cunit.record.syncPoints.sync;

import edu.rice.cs.cunit.record.graph.LockInfo;
import edu.rice.cs.cunit.record.graph.ThreadInfo;
import edu.rice.cs.cunit.record.syncPoints.ISyncPointVisitor;
import edu.rice.cs.cunit.instrumentors.DoNotInstrument;

/**
 * Synchronization point corresponding to the beginning of a synchronized block.
 *
 * @author Mathias Ricken
 */
@DoNotInstrument
public class SynchronizedEnterBlockSyncPoint extends ASynchronizedObjectSyncPoint {

    /**
     * Constructor for this synchronization point.
     * @param object object whose lock was acquired.
     * @param thread thread that acquired the lock
     */
    public SynchronizedEnterBlockSyncPoint(Object object, Thread thread) {
        super(object, thread);
    }

    /**
     * Class for translated versions on the monitor side.
     */
    public static class Translated extends ASynchronizedObjectSyncPoint.Translated {

        /**
         * Constructor for translated version on the monitor side.
         * @param object info about the object
         * @param thread info about the thread
         */
        public Translated(LockInfo object, ThreadInfo thread) {
            super(object, thread);
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
            return visitor.synchronizedBlockBeginCase(this, param);
        }
    }
}
