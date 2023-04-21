package org.afk.job;

/**
 * Simple Job to wrap around a Runnable or Thread
 * @author axl
 */
public class ThreadJob extends Job {

    private Thread thread;

    private Runnable source;

    /**
	 * 
	 */
    public ThreadJob(Runnable runner, String name) {
        source = runner;
        this.thread = new Thread(runner, "Job_" + name);
    }

    /**
	 * 
	 */
    public ThreadJob(Thread thread) {
        if (thread.isAlive()) throw new IllegalStateException("Thread was already started");
        this.thread = thread;
    }

    public ExecutionResult execute() {
        ExecutionResult executionResult = new ExecutionResult();
        try {
            thread.start();
            synchronized (thread) {
                thread.join();
            }
            executionResult.setResult(ExecutionResult.SUCCESS);
            if (source != null) executionResult.setResultData(source); else executionResult.setResultData(thread);
        } catch (InterruptedException e) {
            executionResult.setError(e);
            executionResult.setResult(ExecutionResult.ERROR);
        }
        return executionResult;
    }
}
