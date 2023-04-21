package com.ibm.realtime.flexotask.scheduling.streaming;

/**
 * An abstract class used for connection runners that cross chain boundaries, 
 * i.e., where the source and the target task are in different chain (i.e., the
 * source and target tasks are executed by different computational nodes). For 
 * a connection runner crossing chain boundaries to execute, it is required that
 * neither the source nor the target tasks are executed by another thread at
 * the time that the connection runner is execution. If this were to happen, the
 * Flexotask runtime system would detect this and through a system exception.
 * <p>
 * To prevent this, both the source and target task of a cross chain connection
 * runner must each have a lock that must both be obtained before the cross chain
 * connection runner can execute. Since connection runners are either executed
 * as prerequisites (to a BlockingConnectionRunner) or postrequisites (to a 
 * NotifyingConnectionRunner), one task lock is per default locked. The lock
 * of the source/target task must however first be obtained (and might cause
 * for a blocking).
 * <p>
 * Each of these task locks are also required to be obtained by any other 
 * connection runners that are adjacent to the two tasks that are connected with 
 * the cross chain connection runner. 
 */
abstract class CrossChainConnectionRunner extends ConnectionRunner {

    /** A counter indicating how many times the target task runner notified the 
	 * connection runner since the last time it executed. This counter is used
	 * to prevent the connection runner from missing out on any notifications
	 * by the target task runner. */
    protected int notifyCount = 0;

    /** The number of elements produced per execution of the source task, and
	 * thus the number of elements produced per invocation of the notifyRunner
	 * method. */
    protected int taskOutputRate;

    /**
	 * Constructor.
	 * @param toRun the Runnable to run
	 * @param gcFrequency the frequency at which this Runner should be collected 
	 * iff it turns out to be a task runner (connection runners never collect, 
	 * of course)
	 * @param gcOrder the initial value of the gcCounter.  Non-zero values are 
	 * used to spread collections so they don't all happen in the same period
	 * @param taskInputRate the number of elements consumed per execution by the 
	 * target task of this connection runner.
	 * @param taskOutputRate the number of elements produced per execution by 
	 * the task that the connection runner uses as input.
	 * @param moveValues moves the input value, if true, otherwise simply copies
	 * the input value on the input port to the output port.
	 * @param the task runner that is located just before or after this 
	 * connection driver and which is executed by another thread than this
	 * connection runner.
	 */
    CrossChainConnectionRunner(Runnable toRun, int gcFrequency, int gcOrder, int taskInputRate, int taskOutputRate, boolean moveValues, TaskRunner sourceTask) {
        super(toRun, gcFrequency, gcOrder, taskInputRate, moveValues, sourceTask);
        this.taskOutputRate = taskOutputRate;
    }

    /**
	 * Blocks and waits for a notification, or just falls through if there was
	 * one or more notifications pending.
	 */
    final void waitForNotification() {
        synchronized (getTaskLock()) {
            while (notifyCount < taskInputRate) {
                print("Blocking: " + this + " lock: " + getTaskLock() + " transferRate: " + taskInputRate + " notifyCount: " + notifyCount);
                try {
                    getTaskLock().wait();
                } catch (InterruptedException e) {
                }
            }
            notifyCount -= taskInputRate;
            print("notifyCount-: " + notifyCount + " " + this);
        }
    }

    /**
	 * Notifies this runner if notification should not be skipped.
	 */
    final void notifyRunner() {
        synchronized (getTaskLock()) {
            notifyCount += taskOutputRate;
            print("notifyCount+: " + notifyCount + " " + this + " lock: " + getTaskLock());
            getTaskLock().notify();
        }
    }

    /**
	 * Get the lock on the task that this connection runner inputs from or 
	 * outputs to.
	 * @return the lock on the task that this connection runner inputs from or 
	 * outputs to.
	 */
    abstract Object getTaskLock();
}
