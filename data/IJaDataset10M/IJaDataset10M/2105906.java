package com.excelsior.core;

/**
 * The Class DefaultRunnableComponent.
 */
public abstract class DefaultRunnableComponent implements RunnableComponent {

    /** The should run. */
    private volatile boolean shouldRun;

    /** The thread. */
    private Thread thread;

    public void start() {
        setShouldRun(true);
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        setShouldRun(false);
    }

    /**
	 * Checks if is should run.
	 * 
	 * @return true, if is should run
	 */
    public boolean isShouldRun() {
        return shouldRun;
    }

    /**
	 * Sets the should run.
	 * 
	 * @param shouldRun the new should run
	 */
    public void setShouldRun(boolean shouldRun) {
        this.shouldRun = shouldRun;
    }
}
