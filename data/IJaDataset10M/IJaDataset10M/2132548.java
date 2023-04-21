package net.sf.viwow.seda;

import java.util.*;
import java.util.logging.*;

/*********************************************************
 * Stage - the stage in the processing pipeline.
 * Every Stage has its own input queue containing
 * one or more event objects.
 * Stage has its thread pool, fetch the event object
 * from the queen, allocate a thread to process the
 * object, then push the processed object to the next
 * stage's input queue.
 * The key point is how to let user add their event
 * handler to stage, to let the thread pool's thread
 * to execute the handler.
 * Current implementation is clone a pool of EventHandler
 * thread and let the EventHandler's processEvent to handle
 * the Event
 *
 *  @author yanyanshen taowen
 */
public class Stage extends Thread {

    protected LinkedList inEventQueue;

    protected LinkedList outEventQueue;

    protected int inEventLimit = 100;

    protected int outEventLimit = 100;

    protected EventHandler handler;

    protected ThreadPool threadPool;

    protected Object notifyObj = new Object();

    protected int stageID;

    protected String stageName;

    protected StageManager stageManager;

    protected Logger logger = Logger.getLogger(Stage.class.getName());

    public void setStageManager(StageManager sm) {
        stageManager = sm;
    }

    public StageManager getStageManager() {
        return stageManager;
    }

    public void setStageID(int id) {
        stageID = id;
    }

    public void setStageName(String name) {
        stageName = name;
    }

    public int getStageID() {
        return stageID;
    }

    public String getStageName() {
        return stageName;
    }

    public void setInEventLimit(int limit) {
        this.inEventLimit = limit;
    }

    public void setOutEventLimit(int limit) {
        this.outEventLimit = limit;
    }

    public int getInEventLimit() {
        return inEventLimit;
    }

    public int getOutEventLimit() {
        return outEventLimit;
    }

    public Event addInQueue(Event event) {
        if (inEventQueue.size() == inEventLimit) {
            return event;
        }
        inEventQueue.add(event);
        synchronized (notifyObj) {
            notifyObj.notifyAll();
        }
        return null;
    }

    public Event removeInQueue() {
        if (inEventQueue.size() > 0) {
            Event event = (Event) inEventQueue.removeFirst();
            addOutQueue(event);
            return event;
        }
        return null;
    }

    public void addOutQueue(Event event) {
        outEventQueue.add(event);
    }

    public Event removeOutQueue() {
        if (outEventQueue.size() > 0) return (Event) outEventQueue.removeFirst();
        return null;
    }

    public Stage(EventHandler handler) {
        this.handler = handler;
        this.inEventQueue = new LinkedList();
        this.outEventQueue = new LinkedList();
        this.threadPool = new ThreadPool(10, 5, handler);
    }

    public Stage(EventHandler handler, int id, String name) {
        this.handler = handler;
        this.stageID = id;
        this.stageName = name;
        this.inEventQueue = new LinkedList();
        this.outEventQueue = new LinkedList();
        this.threadPool = new ThreadPool(10, 5, handler);
    }

    public void action() {
    }

    public void sleep() {
        synchronized (notifyObj) {
            try {
                notifyObj.wait();
            } catch (Exception e) {
                logger.warning(e.toString());
            }
        }
    }

    public void wakeup() {
        synchronized (notifyObj) {
            notifyObj.notify();
        }
    }

    public void processInEventQueue() {
        if (inEventQueue.size() == 0) return;
    }

    public void run() {
        while (true) {
            if (inEventQueue.size() == 0) {
                synchronized (notifyObj) {
                    try {
                        notifyObj.wait();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
                continue;
            }
            Event event = removeInQueue();
            if (event == null) {
                synchronized (notifyObj) {
                    try {
                        notifyObj.wait();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
                continue;
            }
            if (!threadPool.allocateThread(event)) {
                outEventQueue.remove(event);
                addInQueue(event);
                synchronized (notifyObj) {
                    try {
                        notifyObj.wait();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }
        }
    }
}
