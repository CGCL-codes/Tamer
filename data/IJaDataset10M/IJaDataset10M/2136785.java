package edu.umd.cs.piccolox.swt;

import org.eclipse.swt.widgets.Display;

/**
 * @author Lance Good
 */
public class SWTTimerQueue implements Runnable {

    static SWTTimerQueue instance;

    Display display = null;

    SWTTimer firstTimer;

    boolean running;

    /**
     * Constructor for TimerQueue.
     */
    public SWTTimerQueue(Display display) {
        super();
        this.display = display;
        start();
    }

    public static SWTTimerQueue sharedInstance(Display display) {
        if (instance == null) {
            instance = new SWTTimerQueue(display);
        }
        return instance;
    }

    synchronized void start() {
        if (running) {
            throw new RuntimeException("Can't start a TimerQueue " + "that is already running");
        } else {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    Thread timerThread = new Thread(SWTTimerQueue.this, "TimerQueue");
                    timerThread.setDaemon(true);
                    timerThread.setPriority(Thread.NORM_PRIORITY);
                    timerThread.start();
                }
            });
            running = true;
        }
    }

    synchronized void stop() {
        running = false;
        notify();
    }

    synchronized void addTimer(SWTTimer timer, long expirationTime) {
        SWTTimer previousTimer;
        SWTTimer nextTimer;
        if (timer.running) {
            return;
        }
        previousTimer = null;
        nextTimer = firstTimer;
        while (nextTimer != null) {
            if (nextTimer.expirationTime > expirationTime) break;
            previousTimer = nextTimer;
            nextTimer = nextTimer.nextTimer;
        }
        if (previousTimer == null) {
            firstTimer = timer;
        } else {
            previousTimer.nextTimer = timer;
        }
        timer.expirationTime = expirationTime;
        timer.nextTimer = nextTimer;
        timer.running = true;
        notify();
    }

    synchronized void removeTimer(SWTTimer timer) {
        SWTTimer previousTimer;
        SWTTimer nextTimer;
        boolean found;
        if (!timer.running) return;
        previousTimer = null;
        nextTimer = firstTimer;
        found = false;
        while (nextTimer != null) {
            if (nextTimer == timer) {
                found = true;
                break;
            }
            previousTimer = nextTimer;
            nextTimer = nextTimer.nextTimer;
        }
        if (!found) return;
        if (previousTimer == null) {
            firstTimer = timer.nextTimer;
        } else {
            previousTimer.nextTimer = timer.nextTimer;
        }
        timer.expirationTime = 0;
        timer.nextTimer = null;
        timer.running = false;
    }

    synchronized boolean containsTimer(SWTTimer timer) {
        return timer.running;
    }

    /**
     * If there are a ton of timers, this method may never return. It loops
     * checking to see if the head of the Timer list has expired. If it has, it
     * posts the Timer and reschedules it if necessary.
     */
    synchronized long postExpiredTimers() {
        SWTTimer timer;
        long currentTime;
        long timeToWait;
        do {
            timer = firstTimer;
            if (timer == null) return 0;
            currentTime = System.currentTimeMillis();
            timeToWait = timer.expirationTime - currentTime;
            if (timeToWait <= 0) {
                try {
                    timer.postOverride();
                } catch (SecurityException e) {
                }
                removeTimer(timer);
                if (timer.isRepeats()) {
                    addTimer(timer, currentTime + timer.getDelay());
                }
                try {
                    wait(1);
                } catch (InterruptedException e) {
                }
            }
        } while (timeToWait <= 0);
        return timeToWait;
    }

    public synchronized void run() {
        long timeToWait;
        try {
            while (running) {
                timeToWait = postExpiredTimers();
                try {
                    wait(timeToWait);
                } catch (InterruptedException e) {
                }
            }
        } catch (ThreadDeath td) {
            running = false;
            SWTTimer timer = firstTimer;
            while (timer != null) {
                timer.cancelEventOverride();
                timer = timer.nextTimer;
            }
            display.asyncExec(new SWTTimerQueueRestart(display));
            throw td;
        }
    }

    public synchronized String toString() {
        StringBuffer buf;
        SWTTimer nextTimer;
        buf = new StringBuffer();
        buf.append("TimerQueue (");
        nextTimer = firstTimer;
        while (nextTimer != null) {
            buf.append(nextTimer.toString());
            nextTimer = nextTimer.nextTimer;
            if (nextTimer != null) buf.append(", ");
        }
        buf.append(")");
        return buf.toString();
    }

    /**
     * Runnable that will message the shared instance of the Timer Queue to
     * restart.
     */
    protected static class SWTTimerQueueRestart implements Runnable {

        boolean attemptedStart;

        Display display = null;

        public SWTTimerQueueRestart(Display display) {
            this.display = display;
        }

        public synchronized void run() {
            if (!attemptedStart) {
                SWTTimerQueue q = SWTTimerQueue.sharedInstance(display);
                synchronized (q) {
                    if (!q.running) q.start();
                }
                attemptedStart = true;
            }
        }
    }
}
