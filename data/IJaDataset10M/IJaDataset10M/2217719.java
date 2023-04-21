package org.rjam.base;

import org.rjam.xml.Token;

/**
 * Calling the java.lang.Thread.stop method causes the thread to stop
 * without regard to the state of processing.  Because of that, it is 
 * considered bad practice to call the stop method of a Java Thread.  
 * This class gives us an easy way to manage a Thread and aviod that
 * problem.
 * 
 * @author Tony Bringardner
 *
 */
public abstract class BaseThread extends BaseComponent implements Runnable {

    private static final long serialVersionUID = 1L;

    private static final String PROP_DEAMON = "Daemon";

    private static final String PROP_PRIORITY = "Priority";

    /**
	 * The default priority for all threads,
	 * initial value = Thread.MIN_PRIORITY.
	 */
    private static int defaultPriority = Thread.NORM_PRIORITY;

    private Thread thread;

    /**
	 * The JVM will continue to run until all 'non daemon' threads 
	 * have stopped or System.exit is called.  This flag is used to 
	 * set the daemon flag in the java.lang.Thread created in the start method.
	 * @see java.lang.Thread.setDaemon()   
	 */
    private boolean daemon = true;

    /**
	 * When the thread is started, we use this oriority.
	 * By default it's set to the Thread.MIN_PRIORITY so we
	 * reduce the chance that we will impact application response time.
	 */
    private int priority = getDefaultPriority();

    /**
	 * This flag is used to indicate to external viewers that the 
	 * thread is still running.
	 */
    protected boolean running = false;

    ;

    /**
	 * This flag is used to request that the thread stop gracefully.
	 */
    protected boolean stopping = false;

    public static int getDefaultPriority() {
        return defaultPriority;
    }

    public static void setDefaultPriority(int defaultPriority) {
        BaseThread.defaultPriority = defaultPriority;
    }

    public void configure(Token configToken) {
        super.configure(configToken);
        setDaemon(Boolean.valueOf(getProperty(PROP_DEAMON, "true")).booleanValue());
        String tmp = getProperty(PROP_PRIORITY);
        if (tmp != null && tmp.length() > 0) {
            try {
                setPriority(Integer.parseInt(tmp));
            } catch (Exception e) {
            }
        }
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public void start() {
        String name = getThreadName();
        logDebug("Enter Start " + name);
        if (thread == null) {
            logDebug("Enter Initialize Thread " + name);
            thread = new Thread(this);
            thread.setName(name);
            thread.setDaemon(isDaemon());
            thread.setPriority(getPriority());
            thread.start();
            logDebug("Exit Initialize Thread " + name + " Priority=" + thread.getPriority());
        }
        logDebug("Exit Start " + name);
    }

    public abstract String getThreadName();

    /**
	 * @return true if the thread is currently running
	 */
    public boolean isRunning() {
        return running;
    }

    /**
	 * @return true if the thread is stopping ( stop() has been called)
	 */
    public boolean isStopping() {
        return stopping;
    }

    /**
	 * @return true if the thread has started and stopped.
	 */
    public boolean isStopped() {
        return !running && stopping;
    }

    /**
	 * ask the thread to stop running
	 * (set the stopping flag to true).
	 */
    public void stop() {
        if (thread != null) {
            stopping = true;
            thread.interrupt();
        }
        logDebug("Stop requested.");
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
