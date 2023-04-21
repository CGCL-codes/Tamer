package de.iritgo.aktera.threading;

public interface ThreadManager {

    /**
	 * Add a delayed async execution to the thread system
	 *
	 * @param delay The delay in ms
	 * @param runnable The runnable
	 */
    public void addDelayedAsyncExecution(long delay, Runnable runnable);
}
