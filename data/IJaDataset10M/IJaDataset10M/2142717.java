package net.sf.asyncobjects.util.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import net.sf.asyncobjects.Promise;
import net.sf.asyncobjects.When;
import net.sf.asyncobjects.util.Condition;
import net.sf.asyncobjects.util.callbacks.ACallable;
import net.sf.asyncobjects.util.callbacks.AListener;
import net.sf.asyncobjects.util.callbacks.CallableAdapter;
import net.sf.asyncobjects.util.callbacks.ListenerAdapter;

/**
 * A wrapper for extensive timer tasks. Note that native timer interfaces cannot
 * be used because timer think that the method run is synchronous.
 * 
 * @author const
 * 
 */
public class InternalExtensiveTimerTaskWrapper {

    /**
	 * If true the task has been canceled.
	 */
    boolean cancelled;

    /**
	 * A wrapped timer taks
	 */
    final AExtensiveTimerTask task;

    /**
	 * A current step
	 */
    StepTask currentStep;

    /** a timer to use */
    final Timer timer;

    /**
	 * A period for timer tasks
	 */
    long period;

    /**
	 * A constructor
	 * 
	 * @param timer
	 *            a timer to use
	 * @param task
	 *            a task to execute
	 */
    public InternalExtensiveTimerTaskWrapper(final Timer timer, final AExtensiveTimerTask task) {
        this.task = task;
        this.timer = timer;
    }

    /**
	 * @return an created cancel callable action
	 */
    public ACallable<Boolean> createCancelAction() {
        return new CallableAdapter<Boolean>() {

            protected Promise<Boolean> call() throws Throwable {
                if (cancelled) {
                    return Condition.falsePromise();
                }
                cancelled = true;
                if (currentStep == null) {
                    return Condition.truePromise();
                } else {
                    return Condition.valueOf(currentStep.cancel());
                }
            }
        }.export();
    }

    /**
	 * Do step at specified time
	 * 
	 * @param time
	 *            a time to do the step
	 */
    void doStepAt(Date time) {
        if (cancelled) {
            currentStep = null;
            return;
        }
        currentStep = new StepTask(new ListenerAdapter<Long>() {

            protected void onEvent(Long event) throws Throwable {
                if (cancelled) {
                    return;
                }
                new When<Void, Void>(Promise.finished(task.run(event), true)) {

                    protected Promise<Void> resolved(Void value) throws Throwable {
                        doStepAt(new Date(System.currentTimeMillis() + period));
                        return null;
                    }
                };
            }
        }.export());
        timer.schedule(currentStep, time);
    }

    /**
	 * A timer task for delay step. Note that task is recreated on each step.
	 */
    class StepTask extends TimerTask {

        /** a listener for event */
        private final AListener<Long> listener;

        /**
		 * A constructor
		 * 
		 * @param listener
		 *            a listener
		 */
        public StepTask(final AListener<Long> listener) {
            super();
            this.listener = listener;
        }

        /**
		 * @see java.util.TimerTask#run()
		 */
        public void run() {
            listener.onEvent(scheduledExecutionTime());
        }
    }
}
