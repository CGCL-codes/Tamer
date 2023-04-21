package protocol.com.kenmccrary.jtella.util;

/**
 *  A threadgroup to log unexpected exceptions
 *
 */
public class LoggingThreadGroup extends ThreadGroup {

    public LoggingThreadGroup() {
        super("JTella-ThreadGroup");
    }

    /**
	 *  Logs an uncaught exception
	 *
	 */
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable instanceof ThreadDeath) {
            return;
        }
        System.out.println("*uncaught exception*");
        Log.getLog().logUnexpectedException(throwable);
    }
}
