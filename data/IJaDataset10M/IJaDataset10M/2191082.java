package edu.rice.cs.cunit.record.syncPoints.sync;

import edu.rice.cs.cunit.record.graph.LockInfo;
import edu.rice.cs.cunit.record.graph.ThreadInfo;
import edu.rice.cs.cunit.record.syncPoints.ISyncPoint;
import edu.rice.cs.cunit.instrumentors.DoNotInstrument;

/**
 * General class for synchronized object-related synchronization points.
 * @author Mathias Ricken
 */
@DoNotInstrument
public abstract class ASynchronizedObjectSyncPoint implements ISyncPoint {

    /**
     * Object whose lock was acquired.
     */
    protected final Object _object;

    /**
     * Thread which acquired the lock.
     */
    protected final Thread _thread;

    /**
     * Constructor for this synchronization point.
     * @param object object whose lock was acquired.
     * @param thread thread that acquired the lock.
     */
    public ASynchronizedObjectSyncPoint(Object object, Thread thread) {
        _object = object;
        _thread = thread;
    }

    /**
     * Class for translated versions on the monitor side.
     */
    public abstract static class Translated implements ISyncPoint.Translated {

        /**
         * Info about the object.
         */
        LockInfo _lockInfo;

        /**
         * Info about the thread.
         */
        ThreadInfo _threadInfo;

        /**
         * Constructor for translated version on the monitor side.
         * @param lockInfo info about the object
         * @param threadInfo info about the thread
         */
        public Translated(LockInfo lockInfo, ThreadInfo threadInfo) {
            _lockInfo = lockInfo;
            _threadInfo = threadInfo;
        }

        /**
         * Returns the info about the object.
         * @return lock info
         */
        public LockInfo getLockInfo() {
            return _lockInfo;
        }

        /**
         * Returns the info about the thread.
         * @return thread info
         */
        public ThreadInfo getThreadInfo() {
            return _threadInfo;
        }

        /**
         * Returns a string representation of the object.
         * @return a string representation of the object.
         */
        public String toString() {
            return this.getClass().getEnclosingClass().getSimpleName() + ": LockInfo={" + _lockInfo + "} ThreadInfo={" + _threadInfo + "}";
        }
    }
}
