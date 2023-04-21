package org.dolmen.core.lang.thread;

/**
 * Handler events of a {@link Worker}
 * 
 * @since 0.0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public interface WorkerHandler {

    /**
	 * fired when and Exception occurs during work
	 * 
	 * @param aException
	 *          the Exception
	 */
    public void onException(Throwable aException);

    /**
	 * fired when the worker's job is completed
	 * 
	 * @param aResult
	 *          the result of the work (may be null)
	 */
    public void onWorkCompleted(Object aResult);
}
