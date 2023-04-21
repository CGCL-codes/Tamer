package hypercast.adapters;

import java.io.*;
import java.util.*;

/** 
 * This class provides synchronized access for a thread to a TimerEventQueue.
 * It also provides for waiting until an event. 
 *
 * NOTE: Calls to this object use delays, while the times in the TimerEventQueue
 * are absolute!!!
 *
 * @author HyperCast Team
 * @author Dongwen Wang
 * @author Michael Nahas
 * @version	2.0, Feb. 28, 2001
 */
public class Timer {

    /** The queue of events. */
    TimerEventQueue TimerEventQueue;

    /**
	 * Constructs a Timer object which contains a TimerEventQueue.
	 */
    public Timer() {
        TimerEventQueue = new TimerEventQueue();
    }

    /**
	 * Adds a TimerEvent to the TimerEventQueue.
	 * @param	timeID		the id of a time event
	 * @param	delay		the time value in ms
	 */
    public synchronized void setTimer(Object timeID, long delay) {
        TimerEventQueue.remove(timeID);
        long time = System.currentTimeMillis() + delay;
        TimerEvent te = new TimerEvent(timeID, time);
        if (TimerEventQueue.isEmpty() || TimerEventQueue.getMinEventTime() > time) notify();
        TimerEventQueue.insert(te);
    }

    /** Return delay until a timer goes off.  */
    public synchronized long getTimer(Object timeID) {
        TimerEvent te = TimerEventQueue.getEvent(timeID);
        if (te == null) return -1;
        long t = te.getTime();
        long delay = t - System.currentTimeMillis();
        if (delay < 0) delay = 0;
        return delay;
    }

    /**
	 * Removes a TimerEvent from the TimerEventQueue.
	 * @param	timeID		the id of the time event to be removed
	 */
    public synchronized void clearTimer(Object timeID) {
        TimerEvent te = TimerEventQueue.remove(timeID);
        if (te != null && !TimerEventQueue.isEmpty() && te.getTime() <= TimerEventQueue.getMinEventTime()) notify();
    }

    /**
	 * Returns after a certain amount of time. 
	 */
    public synchronized Object sleepUntilNextEvent() throws InterruptedException {
        Object resultID;
        while (true) {
            long sleepTime = 10000;
            if (!TimerEventQueue.isEmpty()) sleepTime = TimerEventQueue.getMinEventTime() - System.currentTimeMillis();
            if (sleepTime > 0) {
                wait(sleepTime);
            }
            if (!TimerEventQueue.isEmpty() && TimerEventQueue.getMinEventTime() <= System.currentTimeMillis()) {
                TimerEvent te = TimerEventQueue.removeTop();
                resultID = te.getTimeID();
                break;
            }
        }
        return resultID;
    }

    public static void main(String args[]) {
        Timer t = new Timer();
        TimerTestObject to = new TimerTestObject(t);
        Thread th = new Thread(to);
        th.start();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        System.out.println("Insert to infinite sleeping thread.");
        System.out.println("Correct response: 0:200");
        to.resetStartTime();
        t.setTimer(new Integer(0), 200);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        System.out.println();
        System.out.println("Insert of two timers first follows second.");
        System.out.println("Correct response: 1:200 0:400");
        to.resetStartTime();
        t.setTimer(new Integer(0), 400);
        t.setTimer(new Integer(1), 200);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        System.out.println();
        System.out.println("Insert of two timers second follows first.");
        System.out.println("Correct response: 0:200 1:400");
        to.resetStartTime();
        t.setTimer(new Integer(0), 200);
        t.setTimer(new Integer(1), 400);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        System.out.println();
        System.out.println("Insert of two timers with same expiration time.");
        System.out.println("Correct response: 0:200 1:200 or 1:200 0:200");
        to.resetStartTime();
        t.setTimer(new Integer(0), 200);
        t.setTimer(new Integer(1), 200);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        System.out.println();
        System.out.println("Insert of two timers, clearing second.");
        System.out.println("Correct response: 0:200");
        to.resetStartTime();
        t.setTimer(new Integer(0), 200);
        t.setTimer(new Integer(1), 400);
        t.clearTimer(new Integer(1));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        System.out.println();
        System.out.println("Insert of two timers, clearing first.");
        System.out.println("Correct response: 1:400");
        to.resetStartTime();
        t.setTimer(new Integer(0), 200);
        t.setTimer(new Integer(1), 400);
        t.clearTimer(new Integer(0));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        to.die();
    }
}

/** Simple object used to test Timer object. */
class TimerTestObject implements Runnable {

    /** Timer being tested.*/
    private Timer t;

    /** Time at which the current test started.  */
    private long startTime;

    /** True if the function die() has been called. */
    private boolean dying;

    /** Thread performing test. */
    private Thread myThread;

    TimerTestObject(Timer timer) {
        t = timer;
        dying = false;
    }

    /** resets start time to current time. */
    void resetStartTime() {
        startTime = System.currentTimeMillis();
    }

    /** Kills <code>myThread</code> indirectly. */
    void die() {
        dying = true;
        myThread.interrupt();
    }

    /** Waits until next event and then prints out the ID and time of the event. */
    public void run() {
        myThread = Thread.currentThread();
        while (!dying) {
            try {
                Integer j = (Integer) t.sleepUntilNextEvent();
            } catch (InterruptedException ie) {
            }
        }
    }
}
