package net.sf.daro.util.concurrent;

import java.util.EventListener;

/**
 * Defines the interface for an object that listens to changes of an
 * ExecutorService.
 * 
 * @author daniel
 * 
 * @see ExecutorServiceExt
 */
public interface ExecutorServiceListener extends EventListener {

    /**
	 * Notifies the listener that a task is added to the service.
	 * 
	 * @param event
	 *            the event object
	 */
    void taskAdded(ExecutorServiceEvent event);

    /**
	 * Notifies the listener that a task is removed from the service.
	 * 
	 * @param event
	 *            the event object
	 */
    void taskRemoved(ExecutorServiceEvent event);

    /**
	 * Notifies the listener that a task is executed by the service.
	 * 
	 * @param event
	 *            the event object
	 */
    void taskExecuted(ExecutorServiceEvent event);

    /**
	 * Notifies the listener that a task is rejected by the service.
	 * 
	 * @param event
	 *            the event object
	 */
    void taskRejected(ExecutorServiceEvent event);
}
