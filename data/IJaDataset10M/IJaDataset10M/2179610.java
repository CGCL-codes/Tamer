package com.dmurph.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dmurph.mvc.monitor.EventMonitor;
import com.dmurph.mvc.monitor.LoggingMonitor;
import com.dmurph.mvc.monitor.WarningMonitor;
import com.dmurph.mvc.tracking.ICustomTracker;
import com.dmurph.mvc.tracking.ITrackable;
import com.dmurph.tracking.JGoogleAnalyticsTracker;

/**
 * This stores all the listener information, dispatches events
 * to the corresponding listeners. To dispatch events use {@link MVCEvent#dispatch()}
 * .</br>
 * </br>
 * Also, look at {@link #splitOff()}. To set up Google analytics, call
 * {@link #setTracker(JGoogleAnalyticsTracker)},
 * or implement {@link ICustomTracker} in your events to be tracked, and then any event
 * that implements {@link ITrackable} will be tracked. If
 * {@link ITrackable#getTrackingCategory()} or {@link ITrackable#getTrackingAction()}
 * returns <code>null</code>, then it will be ignored.
 * 
 * @author Daniel Murphy
 */
public class MVC extends Thread {

    private static final Logger log = LoggerFactory.getLogger(MVC.class);

    private static final ThreadGroup mvcThreadGroup = new ThreadGroup("MVC Thread Group");

    private static final ArrayList<MVC> mvcThreads = new ArrayList<MVC>();

    private static final HashMap<String, LinkedList<IEventListener>> listeners = new HashMap<String, LinkedList<IEventListener>>();

    private static final Queue<MVCEvent> eventQueue = new LinkedList<MVCEvent>();

    private static final Object trackerLock = new Object();

    private static volatile JGoogleAnalyticsTracker tracker = null;

    private static final Object monitorLock = new Object();

    private static volatile IGlobalEventMonitor monitor = new LoggingMonitor();

    private static final Object mainThreadLock = new Object();

    private static volatile MVC mainThread;

    private static volatile String currKey = null;

    private volatile boolean running = false;

    private final int threadCount;

    private MVC(int argNum) {
        super(mvcThreadGroup, "MVC Thread #" + argNum);
        threadCount = argNum;
        mvcThreads.add(this);
    }

    public static void setTracker(JGoogleAnalyticsTracker argTracker) {
        synchronized (trackerLock) {
            tracker = argTracker;
        }
    }

    public static JGoogleAnalyticsTracker getTracker() {
        return tracker;
    }

    /**
	 * Adds a listener for the given event key. If the listener is already listening
	 * to that key, then nothing is done. On the rare occurrence that the key is being
	 * dispatched at the same time by the mvc thread, this call will wait till all the
	 * events
	 * of that key are dispatched before adding and returning. If that happens and the
	 * thead making this call is also the mvc thread,
	 * (a listener for a key adds another listener for the same key), then a runtime
	 * exception is thrown.
	 * 
	 * @param argKey
	 * @param argListener
	 */
    public static void addEventListener(String argKey, IEventListener argListener) {
        if (argKey == null) {
            throw new RuntimeException("Key cannot be null");
        }
        synchronized (listeners) {
            synchronized (mainThreadLock) {
                if (argKey.equals(currKey) && Thread.currentThread() == mainThread) {
                    throw new RuntimeException("Cannot add a listener to the same key that's being dispatched");
                }
            }
            LinkedList<IEventListener> fifo;
            if (listeners.containsKey(argKey)) {
                if (listeners.get(argKey).contains(argListener)) {
                    log.debug("We already have that listener here", argListener);
                    return;
                }
                fifo = listeners.get(argKey);
            } else {
                fifo = new LinkedList<IEventListener>();
                listeners.put(argKey, fifo);
            }
            fifo.add(argListener);
        }
    }

    /**
	 * Checks to see if the listener is listening to the given key.
	 * 
	 * @param argKey
	 * @param argListener
	 * @return
	 */
    public static boolean isEventListener(String argKey, IEventListener argListener) {
        if (argKey == null) {
            throw new RuntimeException("Key cannot be null");
        }
        synchronized (listeners) {
            if (!listeners.containsKey(argKey)) {
                return false;
            }
            LinkedList<IEventListener> stack = listeners.get(argKey);
            return stack.contains(argListener);
        }
    }

    /**
	 * Gets a copy of the listeners for the given event key.
	 * 
	 * @param argKey
	 * @return
	 */
    public static LinkedList<IEventListener> getListeners(String argKey) {
        if (argKey == null) {
            throw new RuntimeException("Key cannot be null");
        }
        synchronized (listeners) {
            if (listeners.containsKey(argKey)) {
                return new LinkedList<IEventListener>(listeners.get(argKey));
            } else {
                return new LinkedList<IEventListener>();
            }
        }
    }

    /**
	 * removes a listener from the given key.
	 * 
	 * @param argKey
	 * @param argListener
	 * @return true if the listener was removed, and false if it wasn't there to
	 *         begin with
	 */
    public static boolean removeEventListener(String argKey, IEventListener argListener) {
        if (argKey == null) {
            throw new RuntimeException("Key cannot be null");
        }
        synchronized (listeners) {
            synchronized (mainThreadLock) {
                if (argKey.equals(currKey) && Thread.currentThread() == mainThread) {
                    throw new RuntimeException("Cannot remove a listener to the same key that's being dispatched.  Return false instead.");
                }
            }
            if (listeners.containsKey(argKey)) {
                LinkedList<IEventListener> stack = listeners.get(argKey);
                return stack.remove(argListener);
            } else {
                return false;
            }
        }
    }

    /**
	 * Adds an event to the dispatch queue for the MVC thread.
	 * Used by {@link MVCEvent#dispatch()}.
	 * 
	 * @param argEvent
	 */
    protected static void dispatchEvent(MVCEvent argEvent) {
        boolean hasListeners;
        synchronized (listeners) {
            hasListeners = listeners.containsKey(argEvent.key);
        }
        if (hasListeners) {
            synchronized (eventQueue) {
                eventQueue.add(argEvent);
                eventQueue.notify();
            }
            if (!isDispatchThreadRunning()) {
                startDispatchThread();
            }
        } else {
            synchronized (monitorLock) {
                if (monitor != null) {
                    try {
                        monitor.noListeners(argEvent);
                    } catch (Exception e) {
                        log.error("Exception caught from monitor", e);
                    }
                }
            }
        }
    }

    /**
	 * Split off the current MVC thread, all queued events and future
	 * event dispatches are handled by a new MVC thread, while this one
	 * runs to completion. If the thread calling this is not the current
	 * core MVC thread, then an exception is thrown
	 * 
	 * @throws IllegalThreadException
	 *             if the thread calling this is not an MVC thread
	 * @throws IncorrectThreadException
	 *             if the MVC thread calling this is not the main thread, e.g.
	 *             it has already split off.
	 */
    public static void splitOff() throws IllegalThreadException, IncorrectThreadException {
        if (Thread.currentThread() instanceof MVC) {
            MVC thread = (MVC) Thread.currentThread();
            synchronized (mainThreadLock) {
                if (thread == mainThread) {
                    log.debug("Splitting off...");
                    MVC old = mainThread;
                    old.running = false;
                    mainThread = new MVC(old.threadCount + 1);
                    log.debug("Starting next MVC thread");
                    mainThread.start();
                } else {
                    log.error("Can't split off when this isn't the main thread");
                    throw new IncorrectThreadException();
                }
            }
        } else {
            log.error("Can't split off, we're not in the MVC thread.");
            throw new IllegalThreadException();
        }
    }

    /**
	 * Wait for all remaining events to dispatch
	 * 
	 * @param timeoutMillis
	 *            The maximum number of milliseconds to wait.
	 */
    public static void completeRemainingEvents(long timeoutMillis) {
        boolean fifoEmpty = false;
        long absTimeout = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < absTimeout) {
            synchronized (eventQueue) {
                fifoEmpty = (eventQueue.size() == 0);
            }
            if (fifoEmpty) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
	 * Stops the dispatch thread, dispatching any remaining events
	 * before cleanly returning. Thread automatically gets started
	 * when new events are dispatched
	 */
    public static void stopDispatchThread(long argTimeoutMillis) {
        synchronized (mainThreadLock) {
            mainThread.running = false;
            synchronized (eventQueue) {
                eventQueue.notify();
            }
            if ((mainThread != null) && (argTimeoutMillis > 0)) {
                try {
                    mainThread.join(argTimeoutMillis);
                } catch (InterruptedException e) {
                }
                mainThread = null;
            }
        }
    }

    public static boolean isDispatchThreadRunning() {
        synchronized (mainThreadLock) {
            return mainThread != null && (mainThread.running || mainThread.getState() == State.RUNNABLE);
        }
    }

    /**
	 * Manually starts the dispatch thread.
	 */
    public static void startDispatchThread() {
        synchronized (mainThreadLock) {
            if (mainThread == null) {
                mainThread = new MVC(0);
            }
            if (!mainThread.running) {
                if (mainThread.getState() == State.NEW) {
                    mainThread.start();
                }
            }
        }
    }

    /**
	 * Sets the global event monitor, which is called before and after each event is
	 * dispatched.
	 * 
	 * @param argMonitor
	 * @see IGlobalEventMonitor
	 */
    public static void setGlobalEventMonitor(IGlobalEventMonitor argMonitor) {
        synchronized (monitorLock) {
            monitor = argMonitor;
        }
    }

    /**
	 * Gets the global event monitor. Default is {@link WarningMonitor}.
	 * 
	 * @return
	 * @see IGlobalEventMonitor
	 */
    public static IGlobalEventMonitor getGlobalEventMonitor() {
        synchronized (monitorLock) {
            return monitor;
        }
    }

    private static volatile EventMonitor guiMonitor = null;

    /**
	 * Convenience method to construct and show an {@link EventMonitor}. To
	 * have more control on how the {@link EventMonitor} is configured,
	 * you can just create it yourself and use
	 * {@link #setGlobalEventMonitor(IGlobalEventMonitor)} to have it be the global event
	 * monitor.
	 * 
	 * @return the {@link EventMonitor}.
	 */
    public static EventMonitor showEventMonitor() {
        if (guiMonitor == null) {
            synchronized (monitorLock) {
                guiMonitor = new EventMonitor(monitor);
                setGlobalEventMonitor(guiMonitor);
            }
        }
        guiMonitor.setVisible(true);
        return guiMonitor;
    }

    /**
	 * Hides the event monitor, if you had used {@link #showEventMonitor()}.
	 */
    public static void hideEventMonitor() {
        if (guiMonitor != null) {
            guiMonitor.setVisible(false);
        }
    }

    @Override
    public void run() {
        running = true;
        log.info("MVC thread #" + threadCount + " starting up");
        while (running) {
            try {
                MVCEvent event = null;
                synchronized (eventQueue) {
                    if (eventQueue.isEmpty()) {
                        eventQueue.wait();
                    }
                    if (!eventQueue.isEmpty()) {
                        event = eventQueue.poll();
                    }
                }
                if (event != null) {
                    internalDispatchEvent(event);
                }
            } catch (Exception e) {
                log.error("Caught exception in dispatch thread", e);
            }
        }
        mvcThreads.remove(this);
    }

    private void internalDispatchEvent(MVCEvent argEvent) {
        if (monitor != null) {
            synchronized (monitorLock) {
                try {
                    monitor.beforeDispatch(argEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Exception caught from monitor", e);
                }
            }
        }
        if (argEvent instanceof ITrackable) {
            ITrackable event = (ITrackable) argEvent;
            if (event.getTrackingCategory() != null && event.getTrackingAction() != null) {
                if (event instanceof ICustomTracker) {
                    ((ICustomTracker) event).getCustomTracker().trackEvent(event.getTrackingCategory(), event.getTrackingAction(), event.getTrackingLabel(), event.getTrackingValue());
                } else if (tracker != null) {
                    synchronized (trackerLock) {
                        tracker.trackEvent(event.getTrackingCategory(), event.getTrackingAction(), event.getTrackingLabel(), event.getTrackingValue());
                    }
                } else {
                    log.warn("Event could not be tracked, as the tracker is null", event);
                }
            }
        }
        currKey = argEvent.key;
        LinkedList<IEventListener> stack;
        synchronized (listeners) {
            stack = listeners.get(argEvent.key);
            Iterator<IEventListener> it = stack.iterator();
            while (it.hasNext() && argEvent.isPropagating()) {
                try {
                    if (!it.next().eventReceived(argEvent)) {
                        it.remove();
                    }
                } catch (Exception e) {
                    synchronized (monitorLock) {
                        if (monitor != null) {
                            try {
                                monitor.exceptionThrown(argEvent, e);
                            } catch (Exception e2) {
                                log.error("Exception caught from event dispatch", e);
                                log.error("Exception caught from monitor", e2);
                            }
                        } else {
                            log.error("Exception caught from event dispatch", e);
                        }
                    }
                }
            }
            currKey = null;
        }
        synchronized (monitorLock) {
            if (monitor != null) {
                try {
                    monitor.afterDispatch(argEvent);
                } catch (Exception e) {
                    log.error("Exception caught from monitor", e);
                }
            }
        }
    }
}
