package org.progeeks.mapview;

import java.util.*;
import java.util.concurrent.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.progeeks.util.*;
import org.progeeks.util.log.*;

/**
 *  Runs layer tasks in the background.
 *
 *  @version   $Revision: 3944 $
 *  @author    Paul Speed
 */
public class LayerTaskManager {

    static Log log = Log.getLog();

    private ThreadPoolExecutor executor;

    private Map<MapLayer, List<TaskRunnable>> tasks = new HashMap<MapLayer, List<TaskRunnable>>();

    private ListenerList<ChangeListener> listeners = ListenerSupport.getListenerSupport(ChangeListener.class);

    private ChangeEvent changeEvent = new ChangeEvent(this);

    public LayerTaskManager() {
        this.executor = new ThreadPoolExecutor(8, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new DaemonFactory());
    }

    public void terminate() {
        executor.shutdownNow();
    }

    /**
     *  Returns true if there are tasks executing or waiting
     *  to be executed.
     */
    public synchronized boolean isBusy() {
        return getTaskCount() > 0;
    }

    public int getTaskCount() {
        int size = 0;
        for (List<TaskRunnable> l : tasks.values()) size += l.size();
        return size;
    }

    public void addChangeListener(ChangeListener l) {
        listeners.addListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        listeners.removeListener(l);
    }

    protected void fireStateChanged() {
        ((ChangeListener) listeners).stateChanged(changeEvent);
    }

    /**
     *  Should only be called from a synchronized method.
     */
    protected List<TaskRunnable> getTasks(MapLayer layer) {
        List<TaskRunnable> list = tasks.get(layer);
        if (list == null) {
            list = new ArrayList<TaskRunnable>();
            tasks.put(layer, list);
        }
        return list;
    }

    public synchronized void execute(MapLayer layer, LayerTask task) {
        TaskRunnable r = new TaskRunnable(layer, task);
        getTasks(layer).add(r);
        executor.execute(r);
        fireStateChanged();
    }

    public synchronized void cancelTasks(MapLayer layer) {
        if (log.isDebugEnabled()) log.debug("Canceling tasks for:" + layer);
        List<TaskRunnable> list = getTasks(layer);
        for (TaskRunnable r : list) {
            if (log.isDebugEnabled()) log.debug("-------Canceling:" + r + "    state:" + r.getState());
            executor.remove(r);
            r.cancel();
        }
        executor.purge();
        list.clear();
        fireStateChanged();
    }

    protected synchronized void taskDone(TaskRunnable r, Exception error) {
        if (getTasks(r.layer).remove(r)) {
            javax.swing.SwingUtilities.invokeLater(new TaskApply(r.layer, r.task, error));
        } else {
            if (r.getState() != TaskState.CANCELED) log.warn("******** Dropping results for:" + r); else if (log.isDebugEnabled()) log.debug("******** Task was canceled:" + r);
        }
    }

    /**
     *  We want our threads to be daemon so we'll override the
     *  normal thread factory.
     */
    private static class DaemonFactory implements ThreadFactory {

        private ThreadFactory delegate = Executors.defaultThreadFactory();

        public Thread newThread(Runnable r) {
            Thread t = delegate.newThread(r);
            t.setDaemon(true);
            return t;
        }
    }

    private enum TaskState {

        STARTING, RUNNING, CANCELED, DONE
    }

    ;

    private class TaskRunnable implements Runnable {

        private MapLayer layer;

        private LayerTask task;

        private volatile TaskState state = TaskState.STARTING;

        private Thread runner;

        public TaskRunnable(MapLayer layer, LayerTask task) {
            this.layer = layer;
            this.task = task;
        }

        public synchronized void cancel() {
            if (state == TaskState.DONE || state == TaskState.CANCELED) return;
            if (state == TaskState.RUNNING) {
            }
            state = TaskState.CANCELED;
        }

        public TaskState getState() {
            return state;
        }

        public void run() {
            this.runner = Thread.currentThread();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.warn("Interrupted during sleep", e);
            }
            synchronized (this) {
                if (state != TaskState.STARTING) {
                    if (log.isDebugEnabled()) log.debug("<<< Stopped with state:" + state);
                    taskDone(this, null);
                    return;
                }
                state = TaskState.RUNNING;
            }
            try {
                if (log.isDebugEnabled()) log.debug(">>>> running:" + this);
                task.performTask();
                synchronized (this) {
                    state = TaskState.DONE;
                }
                taskDone(this, null);
            } catch (Exception e) {
                synchronized (this) {
                    state = TaskState.CANCELED;
                }
                taskDone(this, e);
            } finally {
                if (log.isDebugEnabled()) log.debug("<<< done:" + this);
            }
        }

        public String toString() {
            return "Task:" + layer + " -> " + task;
        }
    }

    private class TaskApply implements Runnable {

        private MapLayer layer;

        private LayerTask task;

        private Exception error;

        public TaskApply(MapLayer layer, LayerTask task, Exception error) {
            this.layer = layer;
            this.task = task;
            this.error = error;
        }

        public void run() {
            if (error != null) log.error("Task failed, Layer:" + layer + " task:" + task, error); else task.applyResults(layer);
            fireStateChanged();
        }
    }
}
