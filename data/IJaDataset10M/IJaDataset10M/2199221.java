package org.argouml.ui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.apache.log4j.Logger;
import org.argouml.swingext.GlassPane;
import org.argouml.taskmgmt.ProgressMonitor;
import org.argouml.util.ArgoFrame;

/**
 * This is the 3rd version of SwingWorker (also known as
 * SwingWorker 3), an abstract class that you subclass to
 * perform GUI-related work in a dedicated thread.  For
 * instructions on and examples of using this class, see:
 * 
 * http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
 *
 * Note that the API changed slightly in the 3rd version:
 * You must now invoke start() on the SwingWorker after
 * creating it.
 */
public abstract class SwingWorker {

    private static final Logger LOG = Logger.getLogger(SwingWorker.class);

    private Object value;

    private GlassPane glassPane;

    private Timer timer;

    private ProgressMonitor pmw;

    /** 
     * Class to maintain reference to current worker thread
     * under separate synchronization control.
     */
    private static class ThreadVar {

        private Thread thread;

        ThreadVar(Thread t) {
            thread = t;
        }

        synchronized Thread get() {
            return thread;
        }

        synchronized void clear() {
            thread = null;
        }
    }

    private ThreadVar threadVar;

    /** 
     * Get the value produced by the worker thread, or null if it 
     * hasn't been constructed yet.
     * 
     * @return 	the value produced by the worker thread
     */
    protected synchronized Object getValue() {
        return value;
    }

    /** 
     * Set the value produced by worker thread 
     */
    private synchronized void setValue(Object x) {
        value = x;
    }

    /** 
     * Compute the value to be returned by the <code>get</code> method. 
     * 
     * @param progressMonitor	the ProgressMonitorWindow class - this 
     * 	            class shall be registered as a progress listener.
     * @return 		the value to be returned
     */
    public abstract Object construct(ProgressMonitor progressMonitor);

    /** 
     * Instantiate and initialize an instance of ProgressMonitorWindow 
     * 
     * @return      an instance of ProgressMonitorWindow
     */
    public abstract ProgressMonitor initProgressMonitorWindow();

    /**
     * This method calls the construct(),  
     * 
     * @return		the value to be returned by the <code>get</code> method.
     */
    public Object doConstruct() {
        activateGlassPane();
        pmw = initProgressMonitorWindow();
        ArgoFrame.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Object retVal = null;
        timer = new Timer(25, new TimerListener());
        timer.start();
        try {
            retVal = construct(pmw);
        } catch (Exception exc) {
            LOG.error("Error while loading project: " + exc);
        } finally {
            pmw.close();
        }
        return retVal;
    }

    /**
     * The actionPerformed method in this class
     * is called each time the Timer "goes off".
     */
    class TimerListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            if (pmw.isCanceled()) {
                threadVar.thread.interrupt();
                interrupt();
                timer.stop();
            }
        }
    }

    /**
     * Activate the capabilities of glasspane
     */
    protected void activateGlassPane() {
        GlassPane aPane = GlassPane.mount(ArgoFrame.getInstance(), true);
        setGlassPane(aPane);
        if (getGlassPane() != null) {
            getGlassPane().setVisible(true);
        }
    }

    /**
     * Deactivate the glasspane
     */
    private void deactivateGlassPane() {
        if (getGlassPane() != null) {
            getGlassPane().setVisible(false);
        }
    }

    /**
     * Called on the event dispatching thread (not on the worker thread)
     * after the <code>construct</code> method has returned.
     */
    public void finished() {
        deactivateGlassPane();
        ArgoFrame.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Getter method for the glassPange
     *
     * @return GlassPane	the blocking glassPane
     */
    protected GlassPane getGlassPane() {
        return glassPane;
    }

    /**
     * Setter method
     *
     * @param newGlassPane GlassPane
     */
    protected void setGlassPane(GlassPane newGlassPane) {
        glassPane = newGlassPane;
    }

    /**
     * A new method that interrupts the worker thread.  Call this method
     * to force the worker to stop what it's doing.
     */
    public void interrupt() {
        Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
    }

    /**
     * Return the value created by the <code>construct</code> method.  
     * Returns null if either the constructing thread or the current
     * thread was interrupted before a value was produced.
     * 
     * @return the value created by the <code>construct</code> method
     */
    public Object get() {
        while (true) {
            Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }

    /**
     * Construct a worker thread that will call the <code>construct</code> method
     * and then exit.
     */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {

            public void run() {
                finished();
            }
        };
        Runnable doConstruct = new Runnable() {

            public void run() {
                try {
                    setValue(doConstruct());
                } finally {
                    threadVar.clear();
                }
                SwingUtilities.invokeLater(doFinished);
            }
        };
        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }

    public SwingWorker(String threadName) {
        this();
        threadVar.get().setName(threadName);
    }

    /**
     * Start the worker thread.
     */
    public void start() {
        Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }
}
