package genj.util;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * An Action
 */
public abstract class ActionDelegate implements Cloneable {

    /** async modes */
    public static final int ASYNC_NOT_APPLICABLE = 0, ASYNC_SAME_INSTANCE = 1, ASYNC_NEW_INSTANCE = 2;

    /** attributes */
    public ImgIcon img, roll;

    public String txt;

    public String stxt;

    public String tip;

    /** whether we're async or not */
    private int async = ASYNC_NOT_APPLICABLE;

    /** the thread executing asynchronously */
    private Thread thread;

    private Object threadLock = new Object();

    /**
   * trigger execution
   */
    public final ActionDelegate trigger() {
        if (async == ASYNC_NEW_INSTANCE) {
            try {
                ActionDelegate ad = (ActionDelegate) clone();
                ad.setAsync(ASYNC_SAME_INSTANCE);
                return ad.trigger();
            } catch (Throwable t) {
                t.printStackTrace();
                handleThrowable("trigger", new RuntimeException("Couldn't clone instance of " + getClass().getName() + " for ASYNC_NEW_INSTANCE"));
            }
            return this;
        }
        boolean preExecuteOk;
        try {
            preExecuteOk = preExecute();
        } catch (Throwable t) {
            handleThrowable("preExecute", t);
            preExecuteOk = false;
        }
        if (preExecuteOk) try {
            if (async != ASYNC_NOT_APPLICABLE) {
                synchronized (threadLock) {
                    getThread().start();
                }
            } else execute();
        } catch (Throwable t) {
            handleThrowable("execute(sync)", t);
        }
        if ((async == ASYNC_NOT_APPLICABLE) || (!preExecuteOk)) try {
            postExecute();
        } catch (Throwable t) {
            handleThrowable("postExecute", t);
        }
        return this;
    }

    /**
   * Setter 
   */
    protected void setAsync(int set) {
        async = set;
    }

    /** 
   * Stops asynchronous execution
   */
    public void cancel(boolean wait) {
        Thread t = getThread();
        if (t != null && t.isAlive()) {
            t.interrupt();
            if (wait) try {
                t.join();
            } catch (InterruptedException e) {
            }
        }
    }

    /**
   * The thread running this asynchronously
   * @return thread or null
   */
    public Thread getThread() {
        if (async != ASYNC_SAME_INSTANCE) return null;
        synchronized (threadLock) {
            if (thread == null) thread = new Thread(new CallAsyncExecute());
            return thread;
        }
    }

    /**
   * Implementor's functionality (always sync to EDT)
   */
    protected boolean preExecute() {
        return true;
    }

    /**
   * Implementor's functionality 
   * (called asynchronously to EDT if !ASYNC_NOT_APPLICABLE)
   */
    protected abstract void execute();

    /**
   * Implementor's functionality (always sync to EDT)
   */
    protected void postExecute() {
    }

    /** 
   * Handle an uncaught throwable (always sync to EDT)
   */
    protected void handleThrowable(String phase, Throwable t) {
        Debug.log(Debug.ERROR, this, "Action failed in " + phase, t);
    }

    /**
   * Image 
   */
    public ActionDelegate setImage(ImgIcon i) {
        img = i;
        return this;
    }

    /**
   * Rollover
   */
    public ActionDelegate setRollover(ImgIcon r) {
        roll = r;
        return this;
    }

    /**
   * Text
   */
    public ActionDelegate setText(String t) {
        txt = t;
        return this;
    }

    /**
   * Text
   */
    public ActionDelegate setShortText(String t) {
        stxt = t;
        return this;
    }

    /**
   * Tip
   */
    public ActionDelegate setTip(String t) {
        tip = t;
        return this;
    }

    /**
   * A default Frame close Action
   */
    public static class ActionDisposeFrame extends ActionDelegate {

        /** a frame */
        private Frame frame;

        /** constructor */
        public ActionDisposeFrame(Frame f) {
            frame = f;
        }

        /** run */
        public void execute() {
            frame.dispose();
        }
    }

    /**
   * Returns this delegate wrapped in a proxy now triggered
   * by that contract (without selector)
   */
    public Object as(Class contract) {
        return as(contract, null);
    }

    /**
   * Returns this delegate wrapped in a proxy now triggered
   * by that contract (with selector)
   */
    public Object as(Class contract, String selector) {
        if (contract == WindowListener.class) {
            return new AsWindowListener(selector);
        }
        if (contract == ActionListener.class) {
            return new AsActionListener();
        }
        if (contract == Runnable.class) {
            return new AsRunnable();
        }
        if (contract == ListSelectionListener.class) {
            return new AsListSelectionListener();
        }
        throw new RuntimeException("Unsupported contract '" + contract + "' for ActionDelegate");
    }

    /**
   * A converter - WindowListener
   */
    private class AsWindowListener extends WindowAdapter {

        /** selector */
        private String selector;

        /** constructor */
        protected AsWindowListener(String s) {
            selector = s;
        }

        /** the routed close */
        public void windowClosed(WindowEvent e) {
            if ("windowClosed".equals(selector)) trigger();
        }

        /** the routed closing */
        public void windowClosing(WindowEvent e) {
            if ("windowClosing".equals(selector)) trigger();
        }
    }

    /**
   * A converter - ListSelectionListener
   */
    private class AsListSelectionListener implements ListSelectionListener {

        /** the routed selection */
        public void valueChanged(ListSelectionEvent e) {
            trigger();
        }
    }

    /**
   * A converter - Runnable.run
   */
    private class AsRunnable implements Runnable {

        /** the routed call */
        public void run() {
            trigger();
        }
    }

    /**
   * A converter - ActionListener
   */
    private class AsActionListener implements ActionListener {

        /** the routed call */
        public void actionPerformed(ActionEvent e) {
            trigger();
        }
    }

    /**
   * Async Execution
   */
    private class CallAsyncExecute implements Runnable {

        public void run() {
            try {
                execute();
            } catch (Throwable t) {
                SwingUtilities.invokeLater(new CallSyncHandleThrowable(t));
            }
            synchronized (threadLock) {
                thread = null;
            }
            SwingUtilities.invokeLater(new CallSyncPostExecute());
        }
    }

    /**
   * Sync (EDT) Post Execute
   */
    private class CallSyncPostExecute implements Runnable {

        public void run() {
            try {
                postExecute();
            } catch (Throwable t) {
                handleThrowable("postExecute", t);
            }
        }
    }

    /**
   * Sync (EDT) handle throwable
   */
    private class CallSyncHandleThrowable implements Runnable {

        private Throwable t;

        protected CallSyncHandleThrowable(Throwable set) {
            t = set;
        }

        public void run() {
            try {
                handleThrowable("execute(async)", t);
            } catch (Throwable t) {
            }
        }
    }
}
