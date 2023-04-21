package de.z8bn.ircg;

import java.util.concurrent.*;
import java.util.*;

/**
 *
 * @author Administrator
 */
public class TaskPool {

    public static final String QUEUE_ASYNC = "async";

    private Map<String, ScheduledExecutorService> execServices = new HashMap<String, ScheduledExecutorService>();

    private ThreadFactory tf = new IThreadFactory();

    /** Creates a new instance of TaskPool */
    public TaskPool() {
        execServices.put(QUEUE_ASYNC, Executors.newScheduledThreadPool(3, tf));
    }

    public void addTask(Task tsk, String queue) {
        ScheduledExecutorService ses = execServices.get(queue);
        if (ses == null) execServices.put(queue, ses = Executors.newSingleThreadScheduledExecutor(tf));
        ses.schedule(tsk, 0, TimeUnit.MILLISECONDS);
    }

    public void scheduleTask(Task tsk, long delay, String queue) {
        ScheduledExecutorService ses = execServices.get(queue);
        if (ses == null) execServices.put(queue, ses = Executors.newSingleThreadScheduledExecutor(tf));
        ses.schedule(tsk, delay, TimeUnit.MILLISECONDS);
    }

    public void scheduleTaskWithFixedDelay(Task tsk, long initialDelay, long delay, String queue) {
        ScheduledExecutorService ses = execServices.get(queue);
        if (ses == null) execServices.put(queue, ses = Executors.newSingleThreadScheduledExecutor(tf));
        ses.schedule(new PeriodicRunnable(tsk, delay, queue), initialDelay, TimeUnit.MILLISECONDS);
    }

    private class PeriodicRunnable implements Runnable {

        private Task tsk;

        private long delay;

        private String queue;

        public PeriodicRunnable(Task tsk, long delay, String queue) {
            this.tsk = tsk;
            this.delay = delay;
            this.queue = queue;
        }

        public void run() {
            if (tsk.call()) execServices.get(queue).schedule(this, delay, TimeUnit.MILLISECONDS);
        }
    }

    class IThreadFactory implements ThreadFactory, Thread.UncaughtExceptionHandler {

        private ThreadGroup tg = new ThreadGroup("TaskPool-Threads");

        public Thread newThread(Runnable r) {
            Thread t = new Thread(tg, r);
            t.setUncaughtExceptionHandler(this);
            t.setDaemon(true);
            return t;
        }

        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
        }
    }
}
