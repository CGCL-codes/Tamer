package org.ignoramus.application;

/**
 * 
 */
public class IgnoramusControllerImpl implements IgnoramusController {

    /** The singleton instance */
    private static IgnoramusControllerImpl instance;

    /**
	 * Singleton getter.
	 * 
	 * @return the singleton instance.
	 */
    public static IgnoramusControllerImpl getInstance() {
        if (null == instance) {
            instance = new IgnoramusControllerImpl();
        }
        return instance;
    }

    /**
	 * Sole private constructor.
	 */
    private IgnoramusControllerImpl() {
    }

    /**
	 * Releases the resource.
	 */
    public static void dispose() {
        instance = null;
    }

    /**
	 * Called when the application is started.
	 */
    public void applicationStarted() {
    }

    /**
	 * Called when a test is completed.
	 */
    public void testCompleted() {
    }
}
