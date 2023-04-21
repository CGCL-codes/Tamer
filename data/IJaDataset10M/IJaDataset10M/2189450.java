package net.sf.asyncobjects.util.timer;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.asyncobjects.Promise;

/**
 * An adapter that helps to impelemnt time tasks.
 * 
 * @author const
 */
public abstract class ExtensiveTimerTaskAdapter extends TimerTaskAdapterBase {

    /** Logger. */
    private static final Logger log = Logger.getLogger(ExtensiveTimerTaskAdapter.class.getName());

    /**
	 * Run the task. The method is indirectly dispatched from
	 * {@link java.util.TimerTask#run()}. Note that it is exectuted in the vat
	 * where this object has been created rather than in timer's thread.
	 * 
	 * @param scheduledExecutionTime
	 *            a time when the task was scheduled. it might be used to check
	 *            if the task is too late. The value provided by the
	 *            {@link java.util.TimerTask#scheduledExecutionTime()} is used.
	 * @return A promise that is resolved when timer taks finishes
	 * @throws Throwable
	 *             if there is a problem
	 */
    protected abstract Promise<Void> run(long scheduledExecutionTime) throws Throwable;

    /**
	 * @return an asynchronous facet
	 */
    public AExtensiveTimerTask export() {
        return new InternalTimerTask().export();
    }

    /**
	 * Internal class that acutally implemetns interface
	 */
    public final class InternalTimerTask extends InternalTimerTaskBase<AExtensiveTimerTask> implements AExtensiveTimerTask {

        /**
		 * A private constructor
		 */
        private InternalTimerTask() {
        }

        /**
		 * @see net.sf.asyncobjects.util.timer.AExtensiveTimerTask#run(long)
		 */
        public Promise<Void> run(long scheduledExecutionTime) {
            try {
                return ExtensiveTimerTaskAdapter.this.run(scheduledExecutionTime);
            } catch (Throwable ex) {
                if (log.isLoggable(Level.WARNING)) {
                    log.log(Level.WARNING, "Unexpected exception from timer " + "task is discarded.", ex);
                }
                return Promise.nullPromise();
            }
        }
    }
}
