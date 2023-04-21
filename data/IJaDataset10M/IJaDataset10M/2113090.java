package com.adam.framework.util;

import java.util.*;

/**
 * A class to manage the execution of tasks in this system. A TaskEngine
 * object accepts Runnable objects and queues them for execution by
 * worker threads.<p>
 *
 * Note: the current implementation of this class ignores priority. This may
 * be changed in the future, however.
 */
public class TaskEngine {

    /**
     * A queue of tasks to be executed.
     */
    private static LinkedList taskList = null;

    /**
     * An array of worker threads.
     */
    private static TaskEngineWorker[] workers = null;

    /**
     * A Timer to perform periodic tasks.
     */
    private static Timer taskTimer = null;

    private static Object lock = new Object();

    static {
        taskTimer = new Timer(true);
        workers = new TaskEngineWorker[7];
        taskList = new LinkedList();
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new TaskEngineWorker();
            workers[i].setDaemon(true);
            workers[i].start();
        }
    }

    private TaskEngine() {
    }

    /**
     * Adds a task to the task queue with normal priority. The task will be
     * executed immediately provided there is a free worker thread to execute
     * it. Otherwise, it will execute as soon as a worker thread becomes
     * available.
     */
    public static void addTask(Runnable r) {
        addTask(r, Thread.NORM_PRIORITY);
    }

    /**
     * Adds a task to the task queue. The task will be executed immediately
     * provided there is a free worker thread to execute it. Otherwise, it
     * will execute as soon as a worker thread becomes available.<p>
     *
     * The priority of the task can be specified and must be one of the
     * following values:<ul>
     *      <li> Thread.MAX_PRIORITY
     *      <li> Thread.NORM_PRIORITY
     *      <li> Thread.MIN_PRIORITY
     * </ul>
     *
     * @param task the task to execute
     */
    public static void addTask(Runnable task, int priority) {
        synchronized (lock) {
            taskList.addFirst(task);
            lock.notifyAll();
        }
    }

    /**
     * Schedules a task to periodically run. This is useful for tasks such as
     * updating search indexes, deleting old data at periodic intervals, etc.
     *
     * @param task task to be scheduled.
     * @param delay delay in milliseconds before task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @return a TimerTask object which can be used to track executions of the
     *      task and to cancel subsequent executions.
     */
    public static TimerTask scheduleTask(Runnable task, long delay, long period) {
        TimerTask timerTask = new ScheduledTask(task);
        taskTimer.scheduleAtFixedRate(timerTask, delay, period);
        return timerTask;
    }

    /**
     * Schedules a task to periodically run. This is useful for tasks such as
     * updating search indexes, deleting old data at periodic intervals, etc.
     *
     * @param task task to be scheduled.
     * @param priority the priority the periodic task should run at.
     * @param delay delay in milliseconds before task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @return a TimerTask object which can be used to track executions of the
     *      task and to cancel subsequent executions.
     */
    public static TimerTask scheduleTask(Runnable task, int priority, long delay, long period) {
        TimerTask timerTask = new ScheduledTask(task, priority);
        taskTimer.scheduleAtFixedRate(timerTask, delay, period);
        return timerTask;
    }

    /**
     * Return the next task in the queue. If no task is available, this method
     * will block until a task is added to the queue.
     *
     * @return a <code>Task</code> object
     */
    private static Runnable nextTask() {
        synchronized (lock) {
            while (taskList.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException ie) {
                }
            }
            return (Runnable) taskList.removeLast();
        }
    }

    /**
     * A worker thread class which executes <code>Task</code> objects.
     */
    private static class TaskEngineWorker extends Thread {

        private boolean done = false;

        /**
         * Get tasks from the task engine. The call to get another task will
         * block until there is an available task to execute.
         */
        public void run() {
            while (!done) {
                try {
                    nextTask().run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * A subclass of TimerClass that passes along a Runnable to the task engine
     * when the scheduled task is run.
     */
    private static class ScheduledTask extends TimerTask {

        private Runnable task;

        private int priority;

        public ScheduledTask(Runnable task) {
            this(task, Thread.NORM_PRIORITY);
        }

        public ScheduledTask(Runnable task, int priority) {
            this.task = task;
            this.priority = priority;
        }

        public void run() {
            addTask(task);
        }
    }
}
