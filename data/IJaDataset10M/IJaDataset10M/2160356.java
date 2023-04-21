package net.sf.asyncobjects.util.timer;

import net.sf.asyncobjects.AsyncUnicastServer;
import net.sf.asyncobjects.ExplicitSharing;
import net.sf.asyncobjects.Promise;
import net.sf.asyncobjects.util.callbacks.ACallable;

/**
 * An base class for adapters that helps to impelemnt time tasks.
 * 
 * @author const
 */
public abstract class TimerTaskAdapterBase implements ExplicitSharing {

    /** a promise for cancel action */
    final Promise<ACallable<Boolean>> cancelAction = new Promise<ACallable<Boolean>>();

    /**
	 * Cancel task. Note that promise might never resolves ans the task is not
	 * accpeted by the timer. So do not rely on in in the application logic.
	 * 
	 * @return true if some further execution of the task were prevented
	 */
    @SuppressWarnings("unchecked")
    public Promise<Boolean> cancel() {
        return ((ACallable<Boolean>) cancelAction.willBe(ACallable.class)).call();
    }

    /**
	 * Internal class that acutally implemetns interface
	 * 
	 * @param <T>
	 *            a timer task type
	 */
    public abstract class InternalTimerTaskBase<T extends ATimerTaskBase> extends AsyncUnicastServer<T> implements ATimerTaskBase {

        /**
		 * A private constructor
		 */
        protected InternalTimerTaskBase() {
        }

        /**
		 * @see net.sf.asyncobjects.util.timer.ATimerTaskBase#accepted(net.sf.asyncobjects.util.callbacks.ACallable)
		 */
        public void accepted(ACallable<Boolean> cancelAction) {
            TimerTaskAdapterBase.this.cancelAction.resolver().resolve(cancelAction);
        }
    }
}
