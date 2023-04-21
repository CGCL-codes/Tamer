package net.sf.asyncobjects.vats;

/**
 * This interface is supported by runners that might occupy the thread.
 * 
 * @author const
 */
public interface ThreadBasedRunner extends VatFactory {

    /**
	 * Stop this runner
	 */
    void stop();

    /**
	 * Start runner in the current thread.
	 */
    void startInCurrentThread();

    /**
	 * Start runner in new thread
	 */
    void startInNewThread();

    /**
	 * This method cases the runner to suspend its work.
	 */
    void suspend();

    /**
	 * This methods resumes the suspended runner in the current thread.
	 */
    void resumeInCurrentThread();
}
