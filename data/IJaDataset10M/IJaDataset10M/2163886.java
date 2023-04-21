package org.gudy.azureus2.plugins.update;

/**
 * @author parg
 *
 */
public interface UpdateCheckerListener {

    /**
		 * Called when the checking process has completed successfully
		 * @param checker
		 */
    public void completed(UpdateChecker checker);

    /**
		 * Called when the checking process failed
		 * @param checker
		 */
    public void failed(UpdateChecker checker);

    /**
		 * Called when the checking process has been cancelled
		 * @param checker
		 */
    public void cancelled(UpdateChecker checker);
}
