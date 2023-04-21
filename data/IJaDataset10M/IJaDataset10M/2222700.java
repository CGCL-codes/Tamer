package huf.misc.threadmonitor;

import java.io.PrintStream;

/**
 * A thread monitor for Java VM.
 *
 * <p>
 * <code>ThreadMonitor</code> can be used to monitor JVM threads. It can display
 * data either in a separate frame, print it out to terminal or return a string
 * to user application.
 * </p>
 *
 * <p>
 * <b>NOTE:</b> thread monitor has any use only when launched from within some other application.
 * That's why this class has no <code>main()</code> method :-)
 * </p>
 *
 * <p>
 * ThreadMonitor running in graphical mode:<br>
 * <img src="doc-files/huf_misc_threadmonitor_ThreadMonitor_graphic.png" width=615 height=300
 *     alt="Thread monitor running in graphical mode">
 * </p>
 *
 * <p>
 * ThreadMonitor sending output to <code>PrintStream</code> (<code>System.out</code> in this
 * case):<br>
 * <img src="doc-files/huf_misc_threadmonitor_ThreadMonitor_text.png" width=471 height=131
 *     alt="Thread monitor running in text mode">
 * </p>
 */
public class ThreadMonitor {

    /**
	 * Default graphical monitor updated every single second.
	 */
    public ThreadMonitor() {
        this(1000);
    }

    /**
	 * Graphical monitor updated every <code>refreshFrequency</code> miliseconds.
	 *
	 * <p>
	 * If <code>refreshFrequency</code> is less than 100 (1/10th second) then refresh frequency is set to
	 * 100 to avoid thread monitor eating too much CPU time.
	 * </p>
	 *
	 * @param refreshFrequency		time in miliseconds between thread monitor refreshes
	 */
    public ThreadMonitor(int refreshFrequency) {
        monitorOutput = new FrameMonitorOutput(findRootGroup());
        thread = new ThreadMonitorThread(monitorOutput, refreshFrequency);
    }

    /**
	 * Monitor that sends thread list to <code>stream</code> every single second.
	 *
	 * @param stream				<code>PrintStream</code> to which output will be sent
	 */
    public ThreadMonitor(PrintStream stream) {
        this(stream, 1000);
    }

    /**
	 * Monitor that sends thread list to <code>stream</code> every <code>refreshFrequency</code> miliseconds.
	 *
	 * <p>
	 * If <code>refreshFrequency</code> is less than 100 (1/10th second) then refresh frequency is set to
	 * 100 to avoid thread monitor eating too much CPU time.
	 * </p>
	 *
	 * @param refreshFrequency		time in miliseconds between thread monitor refreshes
	 * @param stream				<code>PrintStream</code> to which output will be sent
	 */
    public ThreadMonitor(PrintStream stream, int refreshFrequency) {
        monitorOutput = new StreamMonitorOutput(stream, findRootGroup());
        thread = new ThreadMonitorThread(monitorOutput, refreshFrequency);
    }

    private MonitorOutput monitorOutput = null;

    private ThreadMonitorThread thread = null;

    /** Refreshes thread monitor display right now */
    public void refresh() {
        monitorOutput.refresh();
    }

    /**
	 * Sets new thread list refresh frequency to <code>refreshFrequency</code> miliseconds.
	 *
	 * @param miliseconds			time in miliseconds between thread monitor refreshes
	 */
    public void setRefreshFrequency(int miliseconds) {
        thread.setRefreshFrequency(miliseconds);
    }

    /**
	 * Starts monitor.
	 */
    public void start() {
        thread.start();
    }

    /**
	 * Stops monitor, closes monitor windows (if any) and frees resources.
	 */
    public void stop() {
        thread.running = false;
        thread = null;
    }

    /**
	 * Returns top-level thread group.
	 *  
	 * @return top-level thread group
	 */
    private ThreadGroup findRootGroup() {
        ThreadGroup current = Thread.currentThread().getThreadGroup();
        ThreadGroup upper = current;
        do {
            current = upper;
            upper = current.getParent();
        } while (upper != null);
        return current;
    }
}
