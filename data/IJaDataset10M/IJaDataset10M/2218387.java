package ca.cbc.sportwire.util;

import org.apache.log4j.Category;

/**
 * <p><b>PeriodicData.java</b>: Base class for all periodically
 * refreshed or expired data times. These objects have a timer thread
 * which will invoke a <code>refresh()</code> method to rebuild or
 * re-populate the object state.</p>
 *
 * <p>This might be easier with the java.util.TimerTask class, except
 * that we needed a means to free unscheduled events and there's no
 * mention in the TimerTask pages if the cancel actually deletes the
 * object; we'll leave this as an exercise for later.
 *
 * </p>
 *
 * Created: Wed Jan  2 13:37:13 2002
 * <pre>
 * $Log: not supported by cvs2svn $
 * Revision 1.7  2002/02/11 07:09:25  garym
 * TTL will now test for Stoppable instead of Periodic
 *
 * Revision 1.6  2002/01/24 19:49:25  garym
 * added path search and debug statements
 *
 * Revision 1.5  2002/01/18 03:09:28  garym
 * Extended unit tests for TTLCacheMap; implemented email unit test reports
 *
 * Revision 1.4  2002/01/16 23:09:25  garym
 * Implemented the JDBC pool support and sql property beans
 *
 * Revision 1.3  2002/01/15 08:28:50  garym
 * implemented MRU cache for the JDOMFile objects
 *
 * Revision 1.2  2002/01/14 21:51:46  garym
 * bug fixes in topics and config files migration to Extended Properties
 *
 * Revision 1.1  2002/01/04 18:37:20  garym
 * PeriodicData moved into sportwire.util package
 *
 * Revision 1.1  2002/01/03 03:36:25  garym
 * Abstracted periodic-refresh data cache objects
 *
 *
 * </pre>
 * @author <a href="mailto:garym@teledyn.com">Gary Lawrence Murphy</a>
 * @version $Id: PeriodicData.java,v 1.8 2002-02-13 19:18:08 garym Exp $
 */
public abstract class PeriodicData extends Thread implements Stoppable {

    /**
	 * Set up a reporting category in Log4J
	 */
    static Category cat = Category.getInstance(PeriodicData.class.getName());

    /**
	 * <code>refresh</code>: abstract method invoked by the timer.
	 *
	 */
    protected abstract void refresh();

    /**
	 * <code>PeriodicData</code> base constructor does nothing.
	 *
	 */
    public PeriodicData() {
    }

    /**
	 * <code>PeriodicData</code> constructor requires the string
	 * representation of the refresh rate; the refresh rate is
	 * typically provided by calling this constructor from a subclass
	 * with the refresh taken from a properties object such as
	 * <code>super( properties.getProperty("refresh.sec",
	 * "30")</code>.  <b>Note: the constructor does <u>not</u>
	 * start the thread method.</b> This allows subclasses to do more
	 * setup before the first refresh is called.
	 *
	 * @param secProp a <code>String</code> value
	 */
    public PeriodicData(String secProp) {
        this.interval = 300;
        try {
            this.interval = Integer.parseInt(secProp);
        } catch (Exception e) {
        }
    }

    /**
	 * <code>PeriodicData</code> constructor accepting the interval as
	 * an integer seconds value
	 *
	 * @param seconds an <code>int</code> value
	 */
    public PeriodicData(int seconds) {
        this.interval = seconds;
    }

    private int interval = 300;

    /**
	 * <code>setInterval</code>: set the refresh interval for the
	 * periodic data item.
	 *
	 * @param sec an <code>int</code> value
	 */
    public void setInterval(int sec) {
        this.interval = sec;
    }

    private boolean running = true;

    /**
	 * Detect if the object is still live.  Used internally to stop
	 * the refresh thread.
	 * @return boolean value of running.
	 */
    public boolean isRunning() {
        return running;
    }

    /**
	 * <code>setRunning</code>:  can be set to
	 * false to terminate the thread.
	 *
	 * @param v  Value to assign to running.
	 */
    public void setRunning(boolean v) {
        cat.debug("Thread terminating");
        synchronized (this) {
            this.running = v;
            if (!v) this.notifyAll();
        }
    }

    /**
	 * <code>bump</code>: can be used to nudge any waiting access
	 * methods. just locks the object and calls notifyAll()
	 *
	 */
    public void bump() {
        synchronized (this) {
            this.notifyAll();
        }
    }

    /**
	 * <code>run</code> implements the refresh timer; if the running
	 * flag is true, the refresh is run, then the thread pauses for
	 * interval seconds.
	 */
    public void run() {
        cat.debug("Starting refresh thread");
        while (isRunning()) {
            refresh();
            try {
                this.sleep(this.interval * 1000);
            } catch (InterruptedException e) {
                cat.debug("Interrupted?");
            }
        }
        cat.debug("Terminating refresh thread");
    }
}
