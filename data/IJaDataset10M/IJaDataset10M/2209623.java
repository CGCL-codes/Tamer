package info.kmm.retriever.scheduler;

import java.util.Date;

/**
 * The scheduler class is responsible for scheduling objects whose
 * classes implement the java.lang.Runnable interface.
 * 
 * The scheduler only one runnable per instance, and allows users
 * to define the number of times they want the runnable to be executed,
 * as well as the interval between each execution.
 */
public class Scheduler implements Runnable {

    private Runnable runnable;

    private int numberOfRepetitions = -1;

    private long intervalInMilliseconds = 0;

    private Date startTime;

    /**
	 * Creates an instance to schedule a java.lang.Runnable passing in
	 * as parameter.
	 * 
	 * @param runnable The java.lang.Runnable to be scheduled.
	 */
    public Scheduler(final Runnable runnable) {
        this.runnable = runnable;
    }

    /**
	 * Starts the scheduler.
	 */
    public void run() {
        try {
            if (this.startTime != null) {
                final long timeToWait = this.startTime.getTime() - System.currentTimeMillis();
                if (timeToWait > 0) Thread.sleep(timeToWait);
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        int i = 0;
        while (this.isInfiniteNumberOfExecutions() || this.canExecute(i)) {
            this.runnable.run();
            if (!this.isInfiniteNumberOfExecutions()) i++;
            if (this.isInfiniteNumberOfExecutions() || this.canExecute(i)) {
                try {
                    Thread.sleep(this.intervalInMilliseconds);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
	 * Defines the number of times that the runnable will be executed.
	 * 
	 * If you don't explicitly set the number of repetitions, the scheduler will
	 * execute the runnable infinite times.
	 * 
	 * @param numberOfRepetitions The number of times that the runnable will
	 *        be executed.
	 */
    public void setNumberOfRepetitions(final int numberOfRepetitions) {
        this.numberOfRepetitions = numberOfRepetitions;
    }

    /**
	 * Defines the interval in milliseconds between the executions.
	 * 
	 * @param intervalInMilliseconds The interval in milliseconds between the
	 *        executions.
	 */
    public void setIntervalBetweenRepetitions(final long intervalInMilliseconds) {
        this.intervalInMilliseconds = intervalInMilliseconds;
    }

    /**
	 * Defines when the runnable will be first executed.
	 * 
	 * @param startTime Defines when the runnable will be first executed.
	 */
    public void startTime(final Date startTime) {
        this.startTime = startTime;
    }

    private boolean canExecute(int i) {
        return i < numberOfRepetitions;
    }

    private boolean isInfiniteNumberOfExecutions() {
        return numberOfRepetitions == -1;
    }
}
