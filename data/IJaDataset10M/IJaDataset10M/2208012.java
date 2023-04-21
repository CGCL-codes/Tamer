package org.javenue.util.process.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.javenue.util.process.Process;
import org.javenue.util.process.ProcessManager;
import org.javenue.util.process.ProcessState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the <tt>ProcessManager</tt> interface.
 * <p>
 * This implementation is simply backed by a <tt>ThreadPoolExecutor</tt>.
 * <p>
 * <tt>Process</tt>es are executed in an asynchronous fashion on another thread.
 * 
 * @author Benjamin Possolo
 * Created on Jul 24, 2007
 */
public final class ThreadPoolExecutorProcessManager extends ThreadPoolExecutor implements ProcessManager {

    /**
	 * A logger.
	 */
    private static final Logger log = LoggerFactory.getLogger(ThreadPoolExecutorProcessManager.class);

    /**
	 * TODO
	 */
    private boolean processesEnabled = true;

    /**
	 * TODO
	 */
    private Lock processesEnabledLock = new ReentrantLock();

    /**
	 * TODO
	 */
    private Condition processesEnabledCondition = processesEnabledLock.newCondition();

    /**
	 * TODO
	 */
    private Lock mainLock = new ReentrantLock();

    /**
	 * TODO
	 */
    private Map<Runnable, Thread> threads = new HashMap<Runnable, Thread>();

    /**
	 * TODO
	 */
    private Map<Runnable, Lock> locks = new HashMap<Runnable, Lock>();

    /**
	 * TODO
	 */
    private Map<Runnable, Condition> conditions = new HashMap<Runnable, Condition>();

    /**
	 * Constructs a ProcessManager backed by a ThreadPoolExecutor
	 * using the <tt>ThreadPoolExecutor</tt>'s corresponding constructor.
	 * 
	 * @see ThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit, BlockingQueue)
	 */
    public ThreadPoolExecutorProcessManager(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
	 * Implementation of superclass hook for performing preparation operations before
	 * a <tt>Process</tt> begins executing.
	 * <p>
	 * @see ThreadPoolExecutor
	 */
    @Override
    protected void beforeExecute(Thread thread, Runnable runnable) {
        super.beforeExecute(thread, runnable);
        if (log.isDebugEnabled()) log.debug("Thread [" + Thread.currentThread().getName() + "] checking if starting of processes is enabled");
        processesEnabledLock.lock();
        try {
            while (!processesEnabled) processesEnabledCondition.await();
            log.debug("Thread [" + Thread.currentThread().getName() + "] detected starting of processes is enabled");
        } catch (InterruptedException e) {
            log.warn("Thread [" + Thread.currentThread().getName() + "] interrupted while waiting for starting of processes to be enabled");
            thread.interrupt();
        } finally {
            processesEnabledLock.unlock();
        }
        Lock processLock = new ReentrantLock();
        Condition processEnded = processLock.newCondition();
        mainLock.lock();
        try {
            threads.put(runnable, thread);
            locks.put(runnable, processLock);
            conditions.put(runnable, processEnded);
        } finally {
            mainLock.unlock();
        }
    }

    /**
	 * Implementation of superclass hook for performing cleanup operations after
	 * a <tt>Process</tt> finishes executing.
	 * <p>
	 * @see ThreadPoolExecutor
	 */
    @Override
    protected void afterExecute(Runnable runnable, Throwable throwable) {
        super.afterExecute(runnable, throwable);
        Lock processLock;
        Condition processEnded;
        if (log.isDebugEnabled()) log.debug("Thread [" + Thread.currentThread().getName() + "] : process [" + ((Process<?>) runnable).getName() + "] has completed. Removing lock and condition variables from maps.");
        mainLock.lock();
        try {
            threads.remove(runnable);
            processLock = locks.remove(runnable);
            processEnded = conditions.remove(runnable);
        } finally {
            mainLock.unlock();
        }
        if (log.isDebugEnabled()) log.debug("Thread [" + Thread.currentThread().getName() + "] signaling to waiting threads that process [" + ((Process<?>) runnable).getName() + "] compeleted");
        processLock.lock();
        try {
            processEnded.signalAll();
        } finally {
            processLock.unlock();
        }
    }

    /**
	 * @see ProcessManager#waitForProcessToFinish(Process)
	 */
    public void waitForProcessToFinish(Process<? extends ProcessState> process) {
        Lock processLock;
        Condition processEnded;
        mainLock.lock();
        try {
            if (isProcessRunning(process)) {
                if (log.isDebugEnabled()) log.debug("Thread [" + Thread.currentThread().getName() + "] retrieving lock and condition variable for process [" + process.getName() + "]");
                processLock = locks.get(process);
                processEnded = conditions.get(process);
            } else {
                if (log.isDebugEnabled()) log.debug("Thread [" + Thread.currentThread().getName() + "] detected process has already finished.");
                return;
            }
        } finally {
            mainLock.unlock();
        }
        processLock.lock();
        try {
            while (isProcessRunning(process)) {
                if (log.isDebugEnabled()) log.debug("Thread [" + Thread.currentThread().getName() + "] waiting for process [" + process.getName() + "] to end");
                processEnded.await();
            }
        } catch (InterruptedException e) {
            log.warn("Thread [" + Thread.currentThread().getName() + "] interrupted while waiting for process [" + process.getName() + "] to end");
        } finally {
            processLock.unlock();
        }
    }

    /**
	 * @see ProcessManager#execute(Process)
	 */
    public void execute(Process<? extends ProcessState> process) {
        super.execute(process);
    }

    /**
	 * @see ProcessManager#interrupt(Process)
	 */
    public void interrupt(Process<? extends ProcessState> process) {
        mainLock.lock();
        try {
            Thread t = threads.get(process);
            if (t != null) {
                if (log.isDebugEnabled()) log.debug("Thread [" + Thread.currentThread().getName() + "] interrupting thread [" + t.getName() + "]");
                t.interrupt();
            }
        } finally {
            mainLock.unlock();
        }
    }

    /**
	 * @see ProcessManager#stop(Process, boolean)
	 */
    public void stop(Process<? extends ProcessState> process, boolean interrupt) {
        if (process == null) throw new IllegalArgumentException("process is null");
        process.stop();
        if (interrupt) interrupt(process);
    }

    /**
     * @see ProcessManager#allProcessesAreStopped()
     */
    public boolean allProcessesAreStopped() {
        mainLock.lock();
        try {
            return threads.isEmpty();
        } finally {
            mainLock.unlock();
        }
    }

    /**
	 * @see ProcessManager#isProcessRunning(Process)
	 */
    public boolean isProcessRunning(Process<? extends ProcessState> process) {
        if (process == null) throw new IllegalArgumentException("process is null");
        mainLock.lock();
        try {
            if (log.isDebugEnabled()) log.debug("Thread [" + Thread.currentThread().getName() + "] checking if process [" + process.getName() + "] is running");
            boolean running = threads.containsKey(process);
            if (log.isDebugEnabled()) log.debug("Thread [" + Thread.currentThread().getName() + "] detected process [" + process.getName() + "] is " + (running ? "" : "not ") + "running");
            return running;
        } finally {
            mainLock.unlock();
        }
    }

    /**
	 * @see ProcessManager#allProcessesAreRunning(Process[])
	 */
    public boolean allProcessesAreRunning(Process<? extends ProcessState>[] processes) {
        if (processes == null) throw new IllegalArgumentException("processes array is null");
        mainLock.lock();
        try {
            for (Process<? extends ProcessState> process : processes) {
                if (process == null) throw new IllegalArgumentException("array element is null");
                if (!isProcessRunning(process)) return false;
            }
            return true;
        } finally {
            mainLock.unlock();
        }
    }

    /**
	 * @see ProcessManager#allProcessesAreStopped(Process[])
	 */
    public boolean allProcessesAreStopped(Process<? extends ProcessState>[] processes) {
        if (processes == null) throw new IllegalArgumentException("processes array is null");
        mainLock.lock();
        try {
            for (Process<? extends ProcessState> process : processes) {
                if (process == null) throw new IllegalArgumentException("array element is null");
                if (isProcessRunning(process)) return false;
            }
            return true;
        } finally {
            mainLock.unlock();
        }
    }

    /**
	 * @see ProcessManager#isAnyProcessRunning(Process[])
	 */
    public boolean isAnyProcessRunning(Process<? extends ProcessState>[] processes) {
        if (processes == null) throw new IllegalArgumentException("processes array is null");
        mainLock.lock();
        try {
            for (Process<? extends ProcessState> process : processes) {
                if (process == null) throw new IllegalArgumentException("array element is null");
                if (isProcessRunning(process)) return true;
            }
            return false;
        } finally {
            mainLock.unlock();
        }
    }

    /**
	 * @see ProcessManager#allProcessesAreStopped(String)
	 */
    public boolean allProcessesAreStopped(String processGroup) {
        mainLock.lock();
        try {
            for (Process<? extends ProcessState> process : getAllProcessesWithGroupName(processGroup)) if (isProcessRunning(process)) return false;
            return true;
        } finally {
            mainLock.unlock();
        }
    }

    /**
	 * @see ProcessManager#disableStartingOfAllProcesses()
	 */
    public void disableStartingOfAllProcesses() {
        log.debug("Thread [" + Thread.currentThread().getName() + "] disabling starting of all processes");
        processesEnabledLock.lock();
        try {
            processesEnabled = false;
        } finally {
            processesEnabledLock.unlock();
        }
    }

    /**
	 * @see ProcessManager#disableStartingOfProcessesInGroup(String)
	 */
    public void disableStartingOfProcessesInGroup(String processGroup) {
        throw new UnsupportedOperationException("method not yet implemented");
    }

    /**
	 * @see ProcessManager#enableStartingOfAllProcesses()
	 */
    public void enableStartingOfAllProcesses() {
        log.debug("Thread [" + Thread.currentThread().getName() + "] enabling starting of all processes");
        processesEnabledLock.lock();
        try {
            processesEnabled = true;
            log.debug("Thread [" + Thread.currentThread().getName() + "] notifying threads that starting of processes has been enabled");
            processesEnabledCondition.signalAll();
        } finally {
            processesEnabledLock.unlock();
        }
    }

    /**
	 * @see ProcessManager#enableStartingOfProcessesInGroup(String)
	 */
    public void enableStartingOfProcessesInGroup(String processGroup) {
        throw new UnsupportedOperationException("method not yet implemented");
    }

    /**
	 * @see ProcessManager#isAnyProcessRunning(String)
	 */
    public boolean isAnyProcessRunning(String processGroup) {
        mainLock.lock();
        try {
            for (Process<? extends ProcessState> process : getAllProcessesWithGroupName(processGroup)) if (isProcessRunning(process)) return true;
            return false;
        } finally {
            mainLock.unlock();
        }
    }

    /**
	 * Retrieves a list of all running <tt>Process</tt>es that belong to a particular group.
	 * 
	 * @param groupName the name of the group
	 * @return a list of <tt>Process</tt>es
	 * @throws IllegalArgumentException if <code>groupName</code> is null or empty string
	 */
    private LinkedList<Process<? extends ProcessState>> getAllProcessesWithGroupName(String groupName) {
        if (groupName == null || "".equals(groupName.trim())) throw new IllegalArgumentException("groupName is null or empty string");
        LinkedList<Process<? extends ProcessState>> relevantProcesses = new LinkedList<Process<? extends ProcessState>>();
        mainLock.lock();
        try {
            Iterator<Runnable> i = threads.keySet().iterator();
            while (i.hasNext()) {
                Process<?> p = (Process<?>) i.next();
                if (groupName.equals(p.getProcessGroup())) relevantProcesses.add(p);
            }
        } finally {
            mainLock.unlock();
        }
        return relevantProcesses;
    }
}
