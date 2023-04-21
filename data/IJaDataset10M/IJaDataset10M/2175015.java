package org.opt4j.config;

/**
 * The {@link TaskListener}.
 * 
 * @author lukasiewycz
 * 
 */
public interface TaskListener extends TaskStateListener {

    /**
	 * Invoked if a {@link Task} is added.
	 * 
	 * @param task
	 *            the added task
	 */
    public void added(Task task);
}
