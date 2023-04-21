package net.sf.mxlosgi.core;

/**
 * @author noah
 * 
 */
public interface Future {

    /**
	 * Get connection associated with the future.
	 * 
	 * @return connection
	 */
    public XmppConnection getConnection();

    /**
	 * Waits if necessary for the task to complete. Warn: Block operate
	 * may lead to artificially low throughput.
	 * 
	 */
    public void complete();

    /**
	 * Waits if necessary for the task to complete or the timeout period
	 * has expired. Warn: Block operate may lead to artificially low
	 * throughput.
	 * 
	 * @param timeout
	 *                  timeout
	 * @return is completed
	 */
    public boolean complete(int timeout);

    /**
	 * Return true if the task completed.
	 * 
	 * @return is completed
	 */
    public boolean isCompleted();
}
