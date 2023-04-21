package net.sf.mzmine.taskcontrol.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import net.sf.mzmine.desktop.preferences.MZminePreferences;
import net.sf.mzmine.desktop.preferences.NumOfThreadsParameter;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.taskcontrol.Task;
import net.sf.mzmine.taskcontrol.TaskControlListener;
import net.sf.mzmine.taskcontrol.TaskController;
import net.sf.mzmine.taskcontrol.TaskPriority;
import net.sf.mzmine.taskcontrol.TaskStatus;

/**
 * Task controller implementation
 */
public class TaskControllerImpl implements TaskController, Runnable {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    ArrayList<TaskControlListener> listeners = new ArrayList<TaskControlListener>();

    /**
     * Update the task progress window every 300 ms
     */
    private final int TASKCONTROLLER_THREAD_SLEEP = 300;

    private Thread taskControllerThread;

    private TaskQueue taskQueue;

    private TaskProgressWindow taskWindow;

    /**
     * This vector contains references to all running threads of NORMAL
     * priority. Maximum number of concurrent threads is specified in the
     * preferences dialog.
     */
    private Vector<WorkerThread> runningThreads;

    /**
     * Initialize the task controller
     */
    public void initModule() {
        taskQueue = new TaskQueue();
        runningThreads = new Vector<WorkerThread>();
        taskControllerThread = new Thread(this, "Task controller thread");
        taskControllerThread.setPriority(Thread.MIN_PRIORITY);
        taskControllerThread.start();
        taskWindow = new TaskProgressWindow();
    }

    TaskQueue getTaskQueue() {
        return taskQueue;
    }

    public void addTask(Task task) {
        addTasks(new Task[] { task }, TaskPriority.NORMAL);
    }

    public void addTask(Task task, TaskPriority priority) {
        addTasks(new Task[] { task }, priority);
    }

    public void addTasks(Task tasks[]) {
        addTasks(tasks, TaskPriority.NORMAL);
    }

    public void addTasks(Task tasks[], TaskPriority priority) {
        if ((tasks == null) || (tasks.length == 0)) return;
        for (Task task : tasks) {
            WrappedTask newQueueEntry = new WrappedTask(task, priority);
            taskQueue.addWrappedTask(newQueueEntry);
        }
        synchronized (this) {
            this.notifyAll();
        }
        if (MZmineCore.getDesktop().getMainFrame() != null) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (taskWindow.getParent() == null) {
                        MZmineCore.getDesktop().addInternalFrame(taskWindow);
                        SwingUtilities.updateComponentTreeUI(taskWindow);
                    }
                    taskWindow.setVisible(true);
                }
            });
        }
    }

    /**
     * Task controller thread main method.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        int previousQueueSize = -1;
        while (true) {
            int currentQueueSize = taskQueue.getNumOfWaitingTasks();
            if (currentQueueSize != previousQueueSize) {
                previousQueueSize = currentQueueSize;
                for (TaskControlListener listener : listeners) listener.numberOfWaitingTasksChanged(currentQueueSize);
            }
            synchronized (this) {
                while (taskQueue.isEmpty()) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (taskQueue.allTasksFinished()) {
                if (MZmineCore.getDesktop().getMainFrame() != null) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            taskWindow.setVisible(false);
                        }
                    });
                }
                taskQueue.clear();
                continue;
            }
            Iterator<WorkerThread> threadIterator = runningThreads.iterator();
            while (threadIterator.hasNext()) {
                WorkerThread thread = threadIterator.next();
                if (thread.isFinished()) threadIterator.remove();
            }
            WrappedTask[] queueSnapshot = taskQueue.getQueueSnapshot();
            NumOfThreadsParameter parameter = MZmineCore.getConfiguration().getPreferences().getParameter(MZminePreferences.numOfThreads);
            int maxRunningThreads;
            if (parameter.isAutomatic() || (parameter.getValue() == null)) maxRunningThreads = Runtime.getRuntime().availableProcessors(); else maxRunningThreads = parameter.getValue();
            for (WrappedTask task : queueSnapshot) {
                if (task.isAssigned() || (task.getActualTask().getStatus() == TaskStatus.CANCELED)) continue;
                if ((task.getPriority() == TaskPriority.HIGH) || (runningThreads.size() < maxRunningThreads)) {
                    WorkerThread newThread = new WorkerThread(task);
                    if (task.getPriority() == TaskPriority.NORMAL) {
                        runningThreads.add(newThread);
                    }
                    newThread.start();
                }
            }
            taskQueue.refresh();
            try {
                Thread.sleep(TASKCONTROLLER_THREAD_SLEEP);
            } catch (InterruptedException e) {
            }
        }
    }

    public void setTaskPriority(Task task, TaskPriority priority) {
        WrappedTask currentQueue[] = taskQueue.getQueueSnapshot();
        for (WrappedTask wrappedTask : currentQueue) {
            if (wrappedTask.getActualTask() == task) {
                logger.finest("Setting priority of task \"" + task.getTaskDescription() + "\" to " + priority);
                wrappedTask.setPriority(priority);
                taskQueue.refresh();
            }
        }
    }

    @Override
    public void addTaskControlListener(TaskControlListener listener) {
        listeners.add(listener);
    }
}
