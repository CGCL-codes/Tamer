package org.jsmtpd.generic.threadpool;

import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A generic, fixed-size thread pooler
 * each not busy thread are kept waiting
 * 
 * Things to do to add auto-grow ability :
 * Record number of active th over time (at each assign free th, or a controling thread)
 * if thread pool is inactive during a while, and curent size is bigger than min size,
 * try to remove free thds from the pool. => synchronize the collection
 * 
 * 
 * Add a max thread int
 * add min thread int
 * 
 * 
 * Instead of throwing an exception directly when pool is exhausted
 * try to increase by 10% the number of thd, if under max th., then re assign a thread.
 * 
 * 
 * 
 * @author Jean-Francois POUX
 * @see org.jsmtpd.generic.threadpool.IThreadedClass
 */
public class GenericThreadPool implements ThreadPool {

    private Log log = LogFactory.getLog(GenericThreadPool.class);

    private LinkedList<ThreadWorker> threads = new LinkedList<ThreadWorker>();

    /**
     * 
     * @param numThreads number of threads to be spawned
     * @param threadClassName name of the class to be threaded, must impletement IThreadedClass
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public GenericThreadPool(int numThreads, String threadClassName, String displayThreadName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        ThreadWorker tmp;
        IThreadedClass cls;
        log.debug("Starting a fixed pool of " + numThreads + " threads");
        for (int i = 0; i < numThreads; i++) {
            tmp = new ThreadWorker();
            cls = (IThreadedClass) Class.forName(threadClassName).newInstance();
            tmp.setWorker(cls);
            tmp.setName(displayThreadName + "#" + tmp.getId());
            tmp.start();
            while (!tmp.isFree()) {
                Thread.yield();
                log.debug("Thread " + tmp.getName() + " ready");
            }
            threads.add(tmp);
        }
    }

    /**
     * Will gracefully shutdown each running thread
     *
     */
    public void gracefullShutdown() {
        log.debug("Gracefull shutdown ...");
        ThreadWorker tmp;
        for (int i = 0; i < threads.size(); i++) {
            tmp = (ThreadWorker) threads.get(i);
            tmp.gracefullShutdown();
        }
    }

    /**
     * Will force each thread to shutdown
     *
     */
    public void forceShutdown() {
        log.debug("Forcing shutdown ...");
        ThreadWorker tmp;
        for (int i = 0; i < threads.size(); i++) {
            tmp = (ThreadWorker) threads.get(i);
            tmp.forceShutdown();
        }
    }

    /**
     * 
     * @return true if any free thread
     */
    public synchronized boolean hasFreeThread() {
        for (ThreadWorker element : threads) {
            if (element.isFree()) return true;
        }
        return false;
    }

    /**
     * 
     */
    public synchronized int countFreeThread() {
        int count = 0;
        for (ThreadWorker element : threads) {
            if (element.isFree()) count++;
        }
        return count;
    }

    /**
     * passes the obj parameter to the thread instance, and runs its doJob mehtod
     * @param obj the object to pass
     * @throws BusyThreadPoolException when the pool is exhausted
     */
    public synchronized void assignFreeThread(Object obj) throws BusyThreadPoolException {
        int i = 0;
        for (ThreadWorker element : threads) {
            if (element.isFree()) {
                log.debug("Worker " + element.getName() + " is free, assigning job");
                element.setParam(obj);
                element.wake();
                return;
            }
            i++;
        }
        log.warn("Thread pool exhausted !");
        throw new BusyThreadPoolException();
    }
}
