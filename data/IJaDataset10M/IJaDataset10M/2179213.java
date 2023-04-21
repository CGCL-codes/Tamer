package org.jmage.pool;

import org.apache.log4j.Logger;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * WorkerPoolImpl
 */
public class WorkerPoolImpl implements WorkerPool {

    protected int timeoutSeconds = 0;

    protected static final int DEFAULT_TIMEOUT_SECONDS = 10;

    public static final int MAX_JOBS = 1;

    protected Set workerPool;

    protected static Logger log = Logger.getLogger(WorkerPoolImpl.class.getName());

    private static final String WORKER_HIRED = " hired next available worker: ";

    private static final String WORKER_FREED = " freed worker: ";

    private static final String WORKER_ADDED = " added worker to pool: ";

    private static final String WORKER_REMOVED = " removed worker from pool: ";

    private static final String FREE_ERROR = "unable to free worker, not part of this pool: ";

    private static final String ADD_ERROR = "unable to add worker to pool, worker already a member: ";

    private static final String REMOVE_ERROR = "unable to remove worker from pool, worker not a pool member: ";

    private static final String TIMEOUT_ERROR = "unable to set timeout period less than one second";

    private static final String TIMEOUT_SUCCESS = " settimeout seconds for WorkerPool to: ";

    private static final String RESET_WORKER = " reset dozing worker for new assignment: ";

    public WorkerPoolImpl() {
        workerPool = new HashSet();
        timeoutSeconds = DEFAULT_TIMEOUT_SECONDS;
    }

    public int getTimeoutSeconds() throws WorkerException {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) throws WorkerException {
        if (timeoutSeconds < 1) {
            throw new WorkerException(TIMEOUT_ERROR);
        }
        this.timeoutSeconds = timeoutSeconds;
        if (log.isDebugEnabled()) log.debug(TIMEOUT_SUCCESS + this.timeoutSeconds);
    }

    public void addWorker(Worker worker) throws WorkerException {
        if (workerPool.contains(worker)) {
            throw new WorkerException(ADD_ERROR + worker);
        }
        worker.setLastReport(new Date(System.currentTimeMillis()));
        workerPool.add(worker);
        if (log.isDebugEnabled()) log.debug(WORKER_ADDED + worker);
    }

    public void removeWorker(Worker worker) throws WorkerException {
        if (!workerPool.contains(worker)) {
            throw new WorkerException(REMOVE_ERROR + worker);
        }
        workerPool.remove(worker);
        if (log.isDebugEnabled()) log.debug(WORKER_REMOVED + worker);
    }

    public Worker hireWorker() throws WorkerException {
        while (true) {
            Object[] workerPoolArray = workerPool.toArray();
            for (int i = 0; i < workerPoolArray.length; i++) {
                Worker worker = (Worker) workerPoolArray[i];
                if (worker.getJobCount() < MAX_JOBS) {
                    worker.incJobCount();
                    worker.setLastReport(new Date(System.currentTimeMillis()));
                    if (log.isDebugEnabled()) log.debug(WORKER_HIRED + worker);
                    return worker;
                } else {
                    if (this.hasDozedOff(worker)) {
                        this.resetWorker(worker);
                    }
                }
            }
        }
    }

    public Set getAllWorkers() throws WorkerException {
        return workerPool;
    }

    public void freeWorker(Worker worker) throws WorkerException {
        if (workerPool.contains(worker)) {
            worker.decJobCount();
            worker.setLastReport(new Date(System.currentTimeMillis()));
            if (log.isDebugEnabled()) log.debug(WORKER_FREED + worker);
        } else {
            throw new WorkerException(FREE_ERROR + worker);
        }
    }

    public void freeWorkerFor(Object object) throws WorkerException {
        Iterator it = workerPool.iterator();
        while (it.hasNext()) {
            Worker worker = (Worker) it.next();
            if (object.equals(worker.getObject()) || object.hashCode() == worker.getObject().hashCode()) {
                this.freeWorker(worker);
            }
        }
    }

    protected boolean hasDozedOff(Worker worker) {
        return ((System.currentTimeMillis() - worker.getLastReport().getTime()) / 1000l) >= timeoutSeconds && worker.getJobCount() >= MAX_JOBS;
    }

    protected void resetWorker(Worker worker) {
        worker.setJobCount(0);
        worker.setLastReport(new Date(System.currentTimeMillis()));
        if (log.isDebugEnabled()) log.debug(RESET_WORKER + worker);
    }
}
