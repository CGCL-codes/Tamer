package org.jtv.backend;

import java.io.IOException;
import java.util.Iterator;
import java.util.PriorityQueue;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Timer extends Thread {

    private final Logger logger = Logger.getLogger(this.getClass());

    public static class Task implements Comparable<Task> {

        private final Logger logger = Logger.getLogger(this.getClass());

        private long start;

        private int priority;

        public Task(long start) {
            this(start, 0);
        }

        public Task(long start, int priority) {
            super();
            this.start = start;
            this.priority = priority;
        }

        public void execute(Timer timer) {
            logger.warn("timer: nothing to execute for " + this);
        }

        public final long getStart() {
            return start;
        }

        public final int getPriority() {
            return priority;
        }

        public final int compareTo(Task task) {
            int startCompare = (int) (getStart() - task.getStart());
            if (startCompare == 0) {
                return getPriority() - task.getPriority();
            } else {
                return startCompare;
            }
        }

        public String toString() {
            return "{task start: " + getStart() + "}";
        }
    }

    public static class Condition {

        public boolean check(Task task) {
            return true;
        }
    }

    private boolean active;

    private PriorityQueue<Task> queue;

    public Timer() {
        super("timer");
        queue = new PriorityQueue<Task>();
    }

    public synchronized void close() {
        active = false;
        notify();
    }

    public synchronized void schedule(Task task) {
        queue.offer(task);
        notify();
    }

    public synchronized void run() {
        active = true;
        int waitTime = 1000;
        while (active) {
            try {
                wait(waitTime);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            waitTime = checkTask();
        }
    }

    private int checkTask() {
        int waitTime;
        final Task task = queue.peek();
        if (task == null) {
            return 0;
        }
        long startTime = task.getStart();
        long currentTime = System.currentTimeMillis();
        if (currentTime >= startTime - 100) {
            queue.poll();
            new Thread("timerTaskExecute") {

                public void run() {
                    try {
                        task.execute(Timer.this);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }

                ;
            }.start();
            logger.debug("timer: " + task + ", startdiff = " + (currentTime - startTime));
            return checkTask();
        } else {
            waitTime = (int) (startTime - currentTime - 50);
        }
        return waitTime;
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        long time = System.currentTimeMillis();
        Timer timer = new Timer();
        timer.start();
        timer.schedule(new Timer.Task(time + 2000));
        timer.schedule(new Timer.Task(time + 4000));
        timer.schedule(new Timer.Task(time + 4000));
        timer.schedule(new Timer.Task(time + 4000));
        timer.schedule(new Timer.Task(time + 4000));
        timer.schedule(new Timer.Task(time + 4000));
        timer.schedule(new Timer.Task(time + 4000));
        timer.schedule(new Timer.Task(time + 4000));
        timer.schedule(new Timer.Task(time + 6000));
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        timer.close();
    }

    public synchronized void clear() {
        queue.clear();
    }

    public synchronized void clear(Condition condition) {
        Iterator<Task> iter = queue.iterator();
        while (iter.hasNext()) {
            Task task = iter.next();
            if (condition.check(task)) {
                iter.remove();
            }
        }
    }
}
