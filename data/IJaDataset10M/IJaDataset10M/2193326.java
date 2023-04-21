package com.newisys.eventsim;

import java.io.PrintStream;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.newisys.random.PRNG;
import com.newisys.random.PRNGFactory;

/**
 * Represents a thread of execution in a simulation.
 * <p>
 * Note: This class does not extend java.lang.Thread to avoid exposing Thread
 * methods that should not be used with simulation threads.
 * 
 * @author Trevor Robinson
 */
public final class SimulationThread {

    static final Logger logger = SimulationManager.logger;

    private static int activeThreads = 0;

    private static int totalThreads = 0;

    private static int maxActiveThreads = 0;

    /**
     * Mapping of java.lang.Thread objects to SimulationThreads.
     */
    private static final Map<Thread, SimulationThread> simThreadMap = new IdentityHashMap<Thread, SimulationThread>();

    /**
     * Adds a Thread to SimulationThread mapping.
     *
     * @param javaThread the Java Thread
     * @param simThread the SimulationThread
     */
    private static void addThreadMapping(final Thread javaThread, final SimulationThread simThread) {
        synchronized (simThreadMap) {
            simThreadMap.put(javaThread, simThread);
        }
    }

    /**
     * Removes a Thread to SimulationThread mapping.
     *
     * @param javaThread the Java Thread
     */
    private static void removeThreadMapping(final Thread javaThread) {
        synchronized (simThreadMap) {
            simThreadMap.remove(javaThread);
        }
    }

    /**
     * Returns the current simulation thread. This method throws an
     * IllegalThreadException if the current thread is not associated with a
     * simulation thread.
     *
     * @return the SimulationThread currently running
     * @throws IllegalThreadException
     */
    public static SimulationThread currentThread() {
        return forThread(Thread.currentThread());
    }

    /**
     * Returns the current simulation thread, or null if the current thread is
     * not a simulation thread.
     *
     * @return the SimulationThread currently running or null
     */
    public static SimulationThread currentThreadOrNull() {
        return forThreadOrNull(Thread.currentThread());
    }

    /**
     * Returns the simulation thread object corresponding to the given Java
     * thread. This method throws an IllegalThreadException if the given thread
     * is not associated with a simulation thread.
     *
     * @param javaThread a Java thread
     * @return the SimulationThread currently running
     * @throws IllegalThreadException
     */
    public static SimulationThread forThread(Thread javaThread) {
        final SimulationThread t = forThreadOrNull(javaThread);
        if (t == null) {
            throw new IllegalThreadException(javaThread.getName() + " is not a simulation thread");
        }
        return t;
    }

    /**
     * Returns the simulation thread object corresponding to the given Java
     * thread, or null if the given thread is not a simulation thread.
     *
     * @param javaThread a Java thread
     * @return the SimulationThread currently running or null
     */
    public static SimulationThread forThreadOrNull(Thread javaThread) {
        synchronized (simThreadMap) {
            return simThreadMap.get(javaThread);
        }
    }

    final SimulationManager manager;

    final SimulationThread parent;

    private final List<SimulationThread> children;

    final Runnable target;

    PRNG random;

    PRNGFactory randomFactory;

    ThreadState state;

    boolean terminating;

    final Event joinEvent;

    final Thread thread;

    Event blockingEvent;

    Throwable unhandledException;

    /**
     * Stack trace at time of fork/construction
     */
    public StackTraceElement[] forkStack;

    /**
     * Constructs a new simulation thread.
     *
     * @param manager the SimulationManager managing this thread
     * @param parent the SimulationThread creating this thread (or null if none)
     * @param target the code to run in this thread
     * @param random the random number generator for this thread
     * @param randomFactory the factory used to create random number generators
     *            for child threads
     * @param name the name of this thread (for debugging purposes)
     */
    SimulationThread(final SimulationManager manager, final SimulationThread parent, final Runnable target, final PRNG random, final PRNGFactory randomFactory, final String name) {
        this.manager = manager;
        this.parent = parent;
        this.children = new LinkedList<SimulationThread>();
        this.target = target;
        this.random = random;
        this.randomFactory = randomFactory;
        this.state = ThreadState.STARTING;
        this.joinEvent = new StepEvent("JoinEvent[" + name + "]");
        this.forkStack = new Throwable().getStackTrace();
        thread = new Thread(new ThreadWrapper(), name);
        registerThread();
        try {
            thread.start();
        } catch (RuntimeException e) {
            unregisterThread();
            throw e;
        } catch (Error e) {
            unregisterThread();
            throw e;
        }
    }

    private void registerThread() {
        if (logger.getLevel() == Level.FINER) {
            logger.finer(this + ": registerThread()");
        }
        addThreadMapping(thread, this);
        if (parent != null) {
            parent.addChild(this);
        }
        manager.addActiveThread(this);
        activeThreads++;
        totalThreads++;
        if (activeThreads > maxActiveThreads) {
            maxActiveThreads = activeThreads;
        }
    }

    void unregisterThread() {
        if (logger.getLevel() == Level.FINER) {
            logger.finer(this + ": unregisterThread()");
        }
        if (parent != null) {
            parent.removeChild(SimulationThread.this);
            parent.addChildren(children);
        }
        removeThreadMapping(thread);
        manager.notifyOf(joinEvent);
        setState(ThreadState.TERMINATED);
        manager.removeActiveThread(this);
        activeThreads--;
    }

    private class ThreadWrapper implements Runnable {

        public void run() {
            try {
                setState(ThreadState.PENDING);
                waitForState(ThreadState.RUNNING, false);
                target.run();
            } catch (ThreadTerminatedException e) {
            } catch (Throwable t) {
                unhandledException = t;
            } finally {
                unregisterThread();
            }
        }
    }

    /**
     * Returns the name of this thread.
     *
     * @return the name of this thread.
     */
    public String getName() {
        return thread.getName();
    }

    /**
     * Returns the SimulationManager that manages this thread.
     *
     * @return the SimulationManager for this thread
     */
    public SimulationManager getManager() {
        return manager;
    }

    /**
     * Returns the parent thread of this simulation thread, or null if this is a
     * top-level thread (created by a non-simulation thread).
     *
     * @return the parent RealSimulationThread or null
     */
    public SimulationThread getParent() {
        return parent;
    }

    /**
     * Adds a new child thread to this thread's list of child threads.
     *
     * @param thread the new child thread
     */
    private void addChild(SimulationThread thread) {
        synchronized (children) {
            children.add(thread);
        }
    }

    /**
     * Adds a collection of child threads to this thread's list of child
     * threads.
     *
     * @param threads Collection of SimulationThread
     */
    private void addChildren(Collection<SimulationThread> threads) {
        synchronized (children) {
            children.addAll(threads);
        }
    }

    /**
     * Removes a terminated child thread from the parent's list of child
     * threads.
     *
     * @param thread the terminated child thread
     */
    private void removeChild(SimulationThread thread) {
        synchronized (children) {
            children.remove(thread);
        }
    }

    /**
     * Returns a list of child threads (i.e. threads directly created by this
     * thread). The returned list is a snapshot of the current child threads at
     * the time of the call.
     *
     * @return a List of SimulationThreads
     */
    public List<SimulationThread> getChildren() {
        List<SimulationThread> snapshot;
        synchronized (children) {
            snapshot = new ArrayList<SimulationThread>(children.size());
            snapshot.addAll(children);
        }
        return snapshot;
    }

    /**
     * Returns the current state of this thread.
     *
     * @return the current state of this thread
     */
    public ThreadState getState() {
        return state;
    }

    /**
     * Changes the state of this thread and notifies any threads waiting on its
     * monitor.
     *
     * @param state the new state
     */
    void setState(ThreadState state) {
        synchronized (this) {
            if (logger.getLevel() == Level.FINER) {
                logger.finer(this + ": setState(" + state + ")");
            }
            this.state = state;
            notifyAll();
        }
    }

    /**
     * Suspends this thread until it is in the given state, or if invert is
     * true, not in the given state. This method returns the observed state that
     * caused it to complete.
     *
     * @param waitState the state to wait for
     * @param invert indicates waiting to leave the given state
     * @return the new state of the thread
     * @throws InterruptedException
     */
    private ThreadState waitForStateChecked(ThreadState waitState, boolean invert) throws InterruptedException {
        ThreadState curState;
        synchronized (this) {
            while (((curState = state) == waitState) == invert) {
                wait();
            }
        }
        assert ((curState == waitState) != invert);
        return curState;
    }

    /**
     * Wrapper for waitForStateChecked() that converts InterruptedException to
     * either ThreadInterruptedException or ThreadTerminatedException.
     *
     * @param waitState the state to wait for
     * @param invert indicates waiting to leave the given state
     * @return the new state of the thread
     * @throws ThreadTerminatedException
     * @throws ThreadInterruptedException
     */
    ThreadState waitForState(ThreadState waitState, boolean invert) throws ThreadTerminatedException, ThreadInterruptedException {
        try {
            return waitForStateChecked(waitState, invert);
        } catch (InterruptedException e) {
            if (terminating) {
                throw new ThreadTerminatedException(e);
            } else {
                throw new ThreadInterruptedException(e);
            }
        }
    }

    /**
     * Returns the event that will be notified when this thread terminates.
     *
     * @return the join event for this thread
     */
    public Event getJoinEvent() {
        return joinEvent;
    }

    /**
     * Returns the random number generator for this thread.
     *
     * @return the PRNG instance for this thread
     */
    public PRNG getRandom() {
        return random;
    }

    /**
     * Sets the random number generator for this thread.
     *
     * @param random a new PRNG instance for this thread
     */
    public void setRandom(PRNG random) {
        this.random = random;
    }

    /**
     * Returns the factory used to create random number generators for child
     * threads.
     *
     * @return the PRNGFactory instance for this thread
     */
    public PRNGFactory getRandomFactory() {
        return randomFactory;
    }

    /**
     * Sets the factory used to create random number generators for child
     * threads.
     *
     * @param randomFactory a new PRNGFactory instance for this thread
     */
    public void setRandomFactory(PRNGFactory randomFactory) {
        this.randomFactory = randomFactory;
    }

    /**
     * Suspends this simulation thread until all other currently runnable
     * threads have had a chance to run.
     *
     * <p>This method must be called from the Java thread associated with this
     * simulation thread.
     */
    public void yield() {
        logger.entering(getClass().getName(), "yield");
        assert (Thread.currentThread() == thread);
        manager.pushPendingThread(this);
        setState(ThreadState.PENDING);
        waitForState(ThreadState.RUNNING, false);
        logger.exiting(getClass().getName(), "yield");
    }

    /**
     * Waits until the given event occurs.
     *
     * <p>This method must be called from the Java thread associated with this
     * simulation thread.
     *
     * @param e the event to wait for
     */
    public void waitFor(Event e) {
        logger.entering(getClass().getName(), "waitFor", e);
        assert (Thread.currentThread() == thread);
        boolean blocked = false;
        synchronized (manager.eventThreadsMap) {
            if (!e.hasOccurred()) {
                manager.eventThreadsMap.addThread(e, this);
                if (e instanceof MetaEvent) {
                    synchronized (manager.metaEventMap) {
                        manager.metaEventMap.addMetaEvent((MetaEvent) e);
                    }
                }
                e.preWait();
                blocked = true;
                blockingEvent = e;
                setState(ThreadState.BLOCKED);
            }
        }
        if (blocked) {
            try {
                waitForState(ThreadState.RUNNING, false);
            } catch (ThreadInterruptedException exception) {
                cancelEvent();
                setState(ThreadState.RUNNING);
                throw exception;
            } finally {
                e.postWait();
            }
        }
        logger.exiting(getClass().getName(), "waitFor", e);
    }

    /**
     * If this thread is waiting on an event, this method removes it from the
     * event mapping tables.
     */
    private void cancelEvent() {
        synchronized (manager.eventThreadsMap) {
            if (blockingEvent != null) {
                final boolean noMoreThreads = manager.eventThreadsMap.removeThread(blockingEvent, this);
                if (noMoreThreads && blockingEvent instanceof MetaEvent) {
                    synchronized (manager.metaEventMap) {
                        manager.metaEventMap.removeMetaEvent((MetaEvent) blockingEvent);
                    }
                }
                blockingEvent = null;
            }
        }
    }

    /**
     * Waits until any of the given events have occurred.
     *
     * <p>This method must be called from the Java thread associated with this
     * simulation thread.
     *
     * @param events an array of events to wait on
     */
    public void waitForAny(Event... events) {
        waitForAny(Arrays.asList(events));
    }

    /**
     * Waits until any of the events in the given collection have occurred.
     *
     * <p>This method must be called from the Java thread associated with this
     * simulation thread.
     *
     * @param events a collection of events to wait on
     */
    public void waitForAny(Collection<? extends Event> events) {
        if (!events.isEmpty()) {
            waitFor(new AnyEvent(events, false));
        }
    }

    /**
     * Waits until all of the given events have occurred.
     * <p>
     * This method must be called from the Java thread associated with this
     * simulation thread.
     *
     * @param events an array of events to wait on
     */
    public void waitForAll(Event... events) {
        waitForAll(Arrays.asList(events));
    }

    /**
     * Waits until all of the events in the given collection have occurred.
     * <p>
     * This method must be called from the Java thread associated with this
     * simulation thread.
     *
     * @param events a collection of events to wait on
     */
    public void waitForAll(Collection<? extends Event> events) {
        if (!events.isEmpty()) {
            waitFor(new AllEvent(events, false));
        }
    }

    /**
     * Waits until this thread terminates.
     * <p>
     * This method must be called from a Java thread other than the one
     * associated with this simulation thread.
     */
    public void join() {
        final SimulationThread t = currentThread();
        assert (t != this);
        t.waitFor(joinEvent);
    }

    /**
     * Waits until all of the current child threads of this thread (and their
     * descendents) have terminated. Specifically, this method performs a
     * depth-first recursive join of each thread in the tree of descendents.
     * For each thread, it first takes a snapshot of the current list of
     * children, before recursively joining them. This implies that if a thread
     * creates new threads while it is being joined, those threads will not be
     * joined.
     */
    public void joinChildren() {
        final List<SimulationThread> curChildren = getChildren();
        for (final SimulationThread child : curChildren) {
            child.joinChildren();
            child.join();
        }
    }

    /**
     * Waits until the given thread has terminated.
     * <p>
     * This method must be called from the Java thread associated with this
     * simulation thread.
     *
     * @param thread the thread to wait on
     */
    public void join(SimulationThread thread) {
        waitFor(thread.getJoinEvent());
    }

    /**
     * Waits until any of the given threads have terminated.
     * <p>
     * This method must be called from the Java thread associated with this
     * simulation thread.
     *
     * @param threads an array of threads to join
     */
    public void joinAny(SimulationThread... threads) {
        waitForAny(getJoinEvents(threads));
    }

    /**
     * Returns an array of join events corresponding to each of the threads in
     * the given array of SimulationThreads.
     *
     * @param threads an array of SimulationThreads
     * @return an array of join Events
     */
    private Event[] getJoinEvents(SimulationThread[] threads) {
        final int count = threads.length;
        final Event[] events = new Event[count];
        for (int i = 0; i < count; ++i) {
            events[i] = threads[i].getJoinEvent();
        }
        return events;
    }

    /**
     * Waits until any of the threads in the given collection have terminated.
     * <p>
     * This method must be called from the Java thread associated with this
     * simulation thread.
     *
     * @param threads a collection of threads to join
     */
    public void joinAny(Collection<SimulationThread> threads) {
        waitForAny(getJoinEvents(threads));
    }

    /**
     * Returns a collection of join events corresponding to each of the threads
     * in the given collection of SimulationThreads.
     *
     * @param threads a collection of SimulationThreads
     * @return a collection of join Events
     */
    private Collection<Event> getJoinEvents(Collection<SimulationThread> threads) {
        final Collection<Event> events = new ArrayList<Event>(threads.size());
        for (final SimulationThread thread : threads) {
            events.add(thread.getJoinEvent());
        }
        return events;
    }

    /**
     * Waits until all of the given threads have terminated.
     * <p>
     * This method must be called from the Java thread associated with this
     * simulation thread.
     *
     * @param threads an array of threads to join
     */
    public void joinAll(SimulationThread... threads) {
        waitForAll(getJoinEvents(threads));
    }

    /**
     * Waits until all of the threads in the given collection have terminated.
     * <p>
     * This method must be called from the Java thread associated with this
     * simulation thread.
     *
     * @param threads a collection of threads to join
     */
    public void joinAll(Collection<SimulationThread> threads) {
        waitForAll(getJoinEvents(threads));
    }

    /**
     * Terminates this thread, either immediately if it currently PENDING or
     * BLOCKED, or at its next synchronization point if it is RUNNING. This
     * method does nothing if this thread is already TERMINATED or is
     * terminating.
     *
     * @param waitForTermination indicates whether to block the current thread
     *            until this simulation thread enters the TERMINATED state
     */
    void terminate(boolean waitForTermination) {
        if (!terminating && state != ThreadState.TERMINATED) {
            terminating = true;
            thread.interrupt();
            if (waitForTermination) {
                waitForState(ThreadState.TERMINATED, false);
            }
        }
    }

    /**
     * Terminates this thread, either immediately if it currently PENDING or
     * BLOCKED, or at its next synchronization point if it is RUNNING, and waits
     * for the thread to become TERMINATED. This method does nothing if this
     * thread is already TERMINATED or is terminating.
     * <p>
     * This method may be called from any thread.
     */
    public void terminate() {
        terminate(true);
    }

    /**
     * Terminates all of the child threads of this thread (and their
     * descendents) by calling the terminate() method on them. Specifically,
     * this method performs a pre-order recursive terminate of each thread in
     * the tree of descendents.
     * <p>
     * For each thread, it first takes a snapshot of the current list of
     * children, before recursively terminating them. This implies that if a
     * thread creates new threads before it terminates, those threads will not
     * be terminated.
     * <p>
     * This method may be called from any thread.
     */
    public void terminateChildren() {
        final List<SimulationThread> curChildren = getChildren();
        for (final SimulationThread child : curChildren) {
            child.terminate();
            child.terminateChildren();
        }
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(80);
        buf.append(thread.getName());
        buf.append('[');
        buf.append(state);
        if (blockingEvent != null) {
            buf.append(':');
            buf.append(blockingEvent);
        }
        buf.append(']');
        return buf.toString();
    }

    public static final int getActiveThreads() {
        return activeThreads;
    }

    public static final int getMaxActiveThreads() {
        return maxActiveThreads;
    }

    public static final int getTotalThreads() {
        return totalThreads;
    }

    /**
     * Dumps state for all simulation threads including
     * a java stack trace for running threads.
     * @param out
     * @return true if threads still exist
     */
    public static boolean dumpSimThreadState(PrintStream out) {
        boolean threadsExist = false;
        synchronized (simThreadMap) {
            out.println("Dumping JOVE thread state");
            for (Thread thread : simThreadMap.keySet()) {
                threadsExist = true;
                SimulationThread simulationThread = simThreadMap.get(thread);
                out.println("JOVE-Thread-" + simulationThread);
                if (simulationThread.state == ThreadState.RUNNING) {
                    StackTraceElement[] stack = thread.getStackTrace();
                    for (StackTraceElement stackElement : stack) {
                        out.println("  " + stackElement);
                    }
                }
            }
        }
        return threadsExist;
    }

    /**
     * Equivalent to dumpSimThreadState, except for all java threads
     * 
     * @param out
     * @return
     */
    public static void dumpJavaThreadState(PrintStream out) {
        Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
        out.println("Dumping JAVA thread State");
        for (Thread thread : stackTraces.keySet()) {
            State threadState = thread.getState();
            out.println("Java-Thread-" + thread.getName() + " STATE=" + threadState + " DAEMON: " + thread.isDaemon());
            if (threadState != State.WAITING) {
                for (StackTraceElement stackTraceElement : stackTraces.get(thread)) {
                    out.println("   " + stackTraceElement);
                }
            }
        }
    }
}
