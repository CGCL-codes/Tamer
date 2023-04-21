package com.ibm.realtime.flexotask.scheduling.streaming;

/**
 * A connection runner that executes immediately after which it informs the
 * target task that it executed. This type of connection runner is used
 * only where the source and the target tasks are executed by different compute
 * nodes, and where basic coordination is needed to ensure that the target task
 * only executes once the source task has produced the required number of 
 * outputs. The notifying connection runner is always run as a postrequisite to
 * the source task when the source task is a splitter.
 */
class NotifyingConnectionRunner extends CrossChainConnectionRunner {

    /** The task runner that is located just before or after this 
	 * connection driver and which is executed by another thread than this
	 * connection runner. */
    private TaskRunner targetTask;

    /**
	 * Constructor.
	 * @param toRun the Runnable to run
	 * @param gcFrequency the frequency at which this Runner should be collected 
	 * iff it turns out to be a task runner (connection runners never collect, 
	 * of course)
	 * @param gcOrder the initial value of the gcCounter.  Non-zero values are 
	 * used to spread collections so they don't all happen in the same period
	 * @param taskInputRate the number of elements consumed per task execution
	 * by the task that the connection runner uses as output. 
	 * @param taskOutputRate the number of elements produced per execution by 
	 * the task that the connection runner uses as input.
	 * @param moveValues moves the input value, if true, otherwise simply copies
	 * the input value on the input port to the output port.
	 * @param sourceTask the task runner providing input to this connection
	 * @param targetTask the task runner that receives the output of this 
	 * connection runner and which is located in another chain. 
	 */
    NotifyingConnectionRunner(Runnable toRun, int gcFrequency, int gcOrder, int taskInputRate, int taskOutputRate, boolean moveValues, TaskRunner sourceTask, TaskRunner targetTask) {
        super(toRun, gcFrequency, gcOrder, taskInputRate, taskOutputRate, moveValues, sourceTask);
        this.targetTask = targetTask;
    }

    /**
	 * Returns the task runner that is the task of this connection runner and 
	 * which is located in another chain, and thus executed by another thread 
	 * than this connection runner.
	 * @return the task runner that is the task of this connection runner and 
	 * which is located in another chain, and thus executed by another thread 
	 * than this connection runner.
	 */
    TaskRunner getTargetTaskRunner() {
        return targetTask;
    }

    Object getTaskLock() {
        return targetTask.getLock();
    }

    public String toString() {
        return "[NotifyingConnectionRunner] " + toRun + " taskInputRate: " + taskInputRate + " : taskOutputRate: " + taskOutputRate;
    }

    /**
	 * Run the Runnable of this Runner 
	 */
    void run() {
        synchronized (targetTask.getLock()) {
            super.doRun();
            notifyRunner();
        }
    }
}
