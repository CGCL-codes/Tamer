package util;

import java.util.ArrayList;

/**
 * @author Jesper Nordenberg
 * @version $Revision: 1.1 $ $Date: 2002/06/07 06:36:52 $
 */
public class TTaskManager implements ITask, Runnable {

    private static final TTaskManager instance = new TTaskManager();

    private ArrayList tasks = new ArrayList();

    private ArrayList eventQueue = new ArrayList();

    private TTaskManager() {
    }

    public static final TTaskManager getInstance() {
        return instance;
    }

    public synchronized void addEvent(ITask task) {
        eventQueue.add(task);
    }

    public synchronized void addTask(ITask task) {
        tasks.add(task);
    }

    public synchronized void removeTask(ITask task) {
        tasks.remove(task);
    }

    public void runTasks() {
        runTask();
    }

    public void runTask() {
        ITask r[];
        synchronized (this) {
            r = (ITask[]) eventQueue.toArray(new ITask[eventQueue.size()]);
            eventQueue.clear();
        }
        for (int i = 0; i < r.length; i++) r[i].runTask();
        synchronized (this) {
            r = (ITask[]) tasks.toArray(new ITask[tasks.size()]);
        }
        for (int i = 0; i < r.length; i++) r[i].runTask();
    }

    public void run() {
        while (true) {
            TTaskManager.getInstance().runTasks();
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
    }
}
