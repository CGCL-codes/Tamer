package j2me.concurrent;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import alvis.general.Util;

public class ThreadPool {

    private String name;

    private int maxQueueSize;

    private boolean running = true;

    private Thread[] threads;

    private LinkedList tasks = new LinkedList();

    private Timer timer = new Timer(true);

    private AtomicInteger usingThreads = new AtomicInteger(0);

    private synchronized boolean isRunning() {
        return running;
    }

    private synchronized void stopRunning() {
        running = false;
    }

    private synchronized PoolTask getTask() {
        while (tasks.isEmpty() && isRunning()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (tasks.isEmpty()) return null;
        return (PoolTask) tasks.removeFirst();
    }

    private synchronized void addTaskNoDelay(PoolTask task) {
        tasks.addLast(task);
        notifyAll();
    }

    /**
	 * Add a task in the queue with a delay that is got from
	 * the delay method of the task.
	 * 
	 * @param task
	 */
    private void addTask(final PoolTask task) {
        long delay = task.delay();
        if (delay == 0) {
            addTaskNoDelay(task);
        } else {
            timer.schedule(new TimerTask() {

                public void run() {
                    addTaskNoDelay(task);
                }
            }, delay);
        }
    }

    private synchronized int numTasks() {
        return tasks.size();
    }

    private class PoolThread extends Thread {

        public void run() {
            Util.poolThreads.inc();
            while (isRunning()) {
                PoolTask task = getTask();
                if (task == null) {
                    Util.poolThreads.dec();
                    return;
                }
                usingThreads.inc();
                task.run();
                usingThreads.dec();
                if (!task.finish()) {
                    addTask(task);
                }
            }
            Util.poolThreads.dec();
        }
    }

    public ThreadPool(String name, int size, int taskQueueSize) {
        this(name, size, taskQueueSize, Thread.NORM_PRIORITY);
    }

    /**
	 * 
	 * @param name: the name of the thread pool
	 * @param size: the size of the thread pool, meaning the number of threads in the pool.
	 * @param taskQueueSize: the max number of tasks the thread pool can keep.
	 * @param priority: the priority of the threads in the pool.
	 */
    public ThreadPool(String name, int size, int taskQueueSize, int priority) {
        this.name = name;
        maxQueueSize = taskQueueSize;
        threads = new Thread[size];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new PoolThread();
            threads[i].setPriority(priority);
            threads[i].start();
        }
    }

    /**
	 * Get a task, insert it in the queue and wait for the later execution.
	 * 
	 * @param task
	 * @return
	 */
    public synchronized boolean executor(PoolTask task) {
        if (!isRunning()) {
            return false;
        }
        if (numTasks() >= maxQueueSize) {
            return false;
        }
        addTask(task);
        return true;
    }

    public synchronized void destroy() {
        stopRunning();
        timer.cancel();
        this.notifyAll();
    }

    public synchronized void printSnapshot() {
        Util.log.info("Pool " + name + " using threads: " + usingThreads + ", remaining tasks: " + tasks.size());
    }
}
