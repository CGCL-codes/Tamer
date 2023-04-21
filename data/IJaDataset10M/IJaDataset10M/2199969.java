package plasmid;

import java.util.Vector;
import java.util.Enumeration;

/**
 * This abstract class provides a set of common helper fields and methods for
 * Runnable classes which manage a persistent object (and hence must
 * be transaction aware). This could also be used for
 * classes which don't run as a separate thread.
 **/
public class SessionRunnableGroup {

    protected Vector srGroup = new Vector(5, 10);

    protected ThreadGroup threadGroup;

    protected int priority = Thread.MIN_PRIORITY;

    protected boolean terminated = false;

    public SessionRunnableGroup(String name) {
        threadGroup = new ThreadGroup(name);
    }

    public SessionRunnableGroup(String name, int priority) {
        this.priority = priority;
        threadGroup = new ThreadGroup(name);
    }

    public SessionRunnableGroup(ThreadGroup threadGroup, int priority) {
        this.threadGroup = threadGroup;
        setPriority(priority);
    }

    public void setPriority(int p) {
        priority = p;
    }

    public void add(SessionRunnable sr) {
        srGroup.addElement(sr);
    }

    public int getNumThreads() {
        return srGroup.size();
    }

    public boolean start() {
        for (Enumeration e = srGroup.elements(); e.hasMoreElements(); ) {
            SessionRunnable sr = (SessionRunnable) e.nextElement();
            Thread t = new Thread(threadGroup, sr);
            t.setPriority(priority);
            t.setDaemon(true);
            t.start();
        }
        return true;
    }

    /** Shutdown all the SessionRunnables in this group, waiting at most waitMs
     * for each.
     * <p>#### NEEDSWORK: Waiting should be based on elapsed time,
     * not waitMs * Number of threads.
     * <p>### Return value is bogus/useless.
     * Should return true on complete shutdown.
     **/
    public boolean shutdown(long waitMs) {
        for (Enumeration e = srGroup.elements(); e.hasMoreElements(); ) {
            SessionRunnable sr = (SessionRunnable) e.nextElement();
            sr.shutdown();
        }
        for (Enumeration e = srGroup.elements(); e.hasMoreElements(); ) {
            SessionRunnable sr = (SessionRunnable) e.nextElement();
            sr.join(waitMs);
        }
        srGroup.removeAllElements();
        return true;
    }

    public String toString() {
        return (terminated ? " TERMINATED" : "") + "{SRgroup:\n " + srGroup + "\n}";
    }
}
