package org.jgentleframework.services.threadpooling;

import java.util.ArrayList;
import java.util.List;
import org.jgentleframework.services.queuedbeans.BlockingQueue;

/**
 * @author LE QUOC CHUNG - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Jun 27, 2008
 */
public class ThreadPool {

    private BlockingQueue taskQueue = null;

    private List<PoolThread> threads = new ArrayList<PoolThread>();

    private boolean isStopped = false;

    public ThreadPool(int noOfThreads, int maxNoOfTasks) {
        taskQueue = new BlockingQueue(maxNoOfTasks);
        for (int i = 0; i < noOfThreads; i++) {
            threads.add(new PoolThread(taskQueue));
        }
        for (PoolThread thread : threads) {
            thread.start();
        }
    }

    public synchronized void execute(Runnable task) throws InterruptedException {
        if (this.isStopped) throw new IllegalStateException("ThreadPool is stopped");
        this.taskQueue.enqueue(task);
    }

    public synchronized void stop() {
        this.isStopped = true;
        for (PoolThread thread : threads) {
            thread.interrupt();
        }
    }
}
