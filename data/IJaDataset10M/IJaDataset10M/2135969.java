package net.sf.hippopotam.framework.background;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.SwingUtilities;
import net.sf.hippopotam.util.WeakEventListenerList;
import org.apache.log4j.Logger;

/**
 * This class is the background queue implementation. Starts its own thread and handle tasks from queue.
 * The "task" is the any class that implements BackgroundTask interface.
 * This class is intended for work with Swing.
 * <p/>
 * <br>Author: Dmitry Ermakov dim_er@mail.ru
 * <br>Date: 28.05.2007
 */
public class BackgroundQueue extends Thread implements BackgroundQueueInterface {

    private static final Logger LOG = Logger.getLogger(BackgroundQueue.class);

    private enum NeedExecuteResult {

        YES, NO, DESTROY
    }

    private boolean stopped = true;

    private boolean wasStopped = false;

    private LinkedList<BackgroundTask> taskList = new LinkedList<BackgroundTask>();

    private BackgroundTask currentTask;

    private boolean eventDispatchThreadWait = false;

    private AbstractInvokeHandler currentInvokeHandler;

    private WeakEventListenerList<TaskExecutionListener> listenerList;

    public BackgroundQueue() {
        this(null);
    }

    public BackgroundQueue(WeakEventListenerList<TaskExecutionListener> listenerList) {
        super();
        this.listenerList = listenerList == null ? new WeakEventListenerList<TaskExecutionListener>() : listenerList;
    }

    public void addTaskExecutionListener(TaskExecutionListener l) {
        listenerList.addListener(l);
    }

    public void removeTaskExecutionListener(TaskExecutionListener l) {
        listenerList.removeListener(l);
    }

    public void addExternalEventListenerList(WeakEventListenerList<TaskExecutionListener> externalList) {
        listenerList.addExternalEventListenerList(externalList);
    }

    public void removeExternalEventListenerList(WeakEventListenerList<TaskExecutionListener> externalList) {
        listenerList.removeExternalEventListenerList(externalList);
    }

    /**
     * Starts the thread for queue.
     */
    public void start() {
        if (wasStopped) {
            throw new IllegalStateException("Can not start previously stopped queue");
        }
        if (!stopped) return;
        setDaemon(true);
        int priority = Thread.NORM_PRIORITY - 1;
        if (priority < Thread.MIN_PRIORITY) priority = Thread.MIN_PRIORITY;
        setPriority(priority);
        stopped = false;
        super.start();
    }

    /**
     * Stops the background thread and destroys task queue.
     */
    public synchronized void stopBackgroundQueue() {
        stopped = true;
        wasStopped = true;
        notifyAll();
    }

    /**
     * Returns true if queue was stopped.
     *
     * @return true if queue was stopped.
     */
    public synchronized boolean isStopped() {
        return stopped;
    }

    /**
     * Returns the size of the queue.
     *
     * @return size of the queue.
     */
    public synchronized int getTaskListSize() {
        return taskList.size() + (currentTask == null ? 0 : 1);
    }

    /**
     * Returns true if task queue is empty.
     *
     * @return true if task queue is empty.
     */
    public synchronized boolean isTaskListEmpty() {
        return (currentTask == null) && (taskList.size() == 0);
    }

    /**
     * Tries to add task to the task queue. If queue is stopped (isStopped() == true)
     * or task.canAdd() returns false or task.canAddAfterTask(previosTask) returns false at least one time
     * then the task will not be added.
     *
     * @param task the task instance
     * @return true, if task was added.
     */
    public synchronized boolean addTask(BackgroundTask task) {
        boolean res = addTaskToTaskList(task);
        if (res) notifyAll();
        return res;
    }

    /**
     * Tries to add collection of the tasks to the task queue.
     *
     * @param tasks collection of the tasks.
     * @return the count of added tasks.
     */
    public synchronized int addTasks(Collection<? extends BackgroundTask> tasks) {
        if (stopped || (tasks == null) || tasks.isEmpty()) {
            return 0;
        }
        int res = 0;
        for (BackgroundTask task : tasks) {
            if (addTaskToTaskList(task)) {
                res++;
            }
        }
        if (res > 0) notifyAll();
        return res;
    }

    private boolean addTaskToTaskList(BackgroundTask task) {
        if (stopped || (task == null)) {
            return false;
        }
        if (!invokeCanAdd(task)) {
            return false;
        }
        boolean canAdd = true;
        if (task.isNeedInspectionOfPreviosTasks()) {
            if (currentTask != null) {
                canAdd = task.canAddAfterTask(currentTask);
            }
            if (canAdd) {
                Iterator<BackgroundTask> iterator = taskList.descendingIterator();
                while (iterator.hasNext()) {
                    BackgroundTask previosTask = iterator.next();
                    if (!task.canAddAfterTask(previosTask)) {
                        canAdd = false;
                        break;
                    }
                }
            }
        }
        if (canAdd) {
            taskList.add(task);
            invokeTaskWasAdded(task);
        }
        return canAdd;
    }

    /**
     * Clear the task queue except the task that is in process now.
     */
    public synchronized void clearTaskListExceptExecutingTask() {
        clearTaskList();
        notifyAll();
    }

    private void clearTaskList() {
        if (taskList.size() == 0) {
            return;
        }
        LinkedList<BackgroundTask> toRemoveList = new LinkedList<BackgroundTask>(taskList);
        taskList.clear();
        for (BackgroundTask task : toRemoveList) {
            invokeTaskWasRemoved(task, false);
        }
    }

    /**
     * Remove the tasks from the queue for which taskKiller returns true (except the task "in process").
     *
     * @param taskKiller BackgroundTaskKiller instance
     * @return the count of removed tasks
     */
    public synchronized int killTask(BackgroundTaskKiller taskKiller) {
        if ((taskList.size() == 0) || (taskKiller == null)) {
            return 0;
        }
        LinkedList<BackgroundTask> toRemoveList = new LinkedList<BackgroundTask>();
        Iterator<BackgroundTask> iterator = taskList.iterator();
        while (iterator.hasNext()) {
            BackgroundTask task = iterator.next();
            if (taskKiller.killTask(task)) {
                toRemoveList.add(task);
                iterator.remove();
            }
        }
        notifyAll();
        for (BackgroundTask task : toRemoveList) {
            invokeTaskWasRemoved(task, false);
        }
        return toRemoveList.size();
    }

    /**
     * Wait for ending of all tasks in the queue.
     *
     * @throws DeadlockDetectedException deadlock detected. When you try to invoke this method from
     * the "safe" methods of the task.
     */
    public synchronized void waitForEndingTaskQueue() throws DeadlockDetectedException {
        if (SwingUtilities.isEventDispatchThread()) {
            if (DeadlockDetectionService.getInstance().isDeadlockDetected()) {
                throw new DeadlockDetectedException();
            }
            eventDispatchThreadWait = true;
            try {
                while ((currentTask != null) || (taskList.size() > 0)) {
                    if (currentInvokeHandler != null) {
                        currentInvokeHandler.run();
                    }
                    if ((currentTask != null) || (taskList.size() > 0)) {
                        wait();
                    }
                }
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            } finally {
                eventDispatchThreadWait = false;
            }
        } else {
            try {
                while ((currentTask != null) || (taskList.size() > 0)) {
                    wait();
                }
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Remove all tasks from the queue (except "in process" task) and wait for ending "in process" task.
     *
     * @throws DeadlockDetectedException deadlock detected. When you try to invoke this method from
     * the "safe" methods of the task.
     */
    public synchronized void clearTaskListAndWaitForEndingTaskQueue() throws DeadlockDetectedException {
        clearTaskList();
        notifyAll();
        waitForEndingTaskQueue();
    }

    private BackgroundTask getNextTask() {
        BackgroundTask task = null;
        if (taskList.size() > 0) {
            task = taskList.remove(0);
        }
        return task;
    }

    public void run() {
        boolean needToClearTaskList = false;
        currentTask = null;
        while (true) {
            try {
                synchronized (this) {
                    if (stopped) {
                        clearTaskList();
                        notifyAll();
                        break;
                    }
                    while (!stopped && (taskList.size() == 0)) {
                        wait();
                    }
                    if (stopped) {
                        clearTaskList();
                        notifyAll();
                        break;
                    }
                    currentTask = getNextTask();
                    if (currentTask == null) {
                        clearTaskList();
                        notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
            if (currentTask == null) continue;
            try {
                NeedExecuteResult res = invokeIsNeedExecuteTask(currentTask);
                needToClearTaskList = (res == NeedExecuteResult.DESTROY);
                if (res == NeedExecuteResult.YES) {
                    try {
                        try {
                            invokeBeforeExecuteTask(currentTask);
                            try {
                                try {
                                    currentTask.executeTask();
                                } catch (DestroyQueueException e) {
                                    needToClearTaskList = true;
                                }
                            } finally {
                                invokeFinallyExecuteTaskBeforeHandleException(currentTask);
                            }
                            invokeAfterExecuteTask(currentTask);
                        } catch (Throwable e) {
                            needToClearTaskList = invokeHandleTaskException(currentTask, e);
                        }
                    } finally {
                        invokeFinallyExecuteTaskAfterHandleException(currentTask);
                    }
                }
            } catch (Throwable exception) {
                LOG.error(exception.getMessage(), exception);
            }
            synchronized (this) {
                BackgroundTask task = currentTask;
                currentTask = null;
                invokeTaskWasRemoved(task, true);
                if (needToClearTaskList) {
                    clearTaskList();
                }
                needToClearTaskList = false;
                notifyAll();
            }
        }
    }

    private synchronized void invoikeAndWait(AbstractInvokeHandler invokeHandler) {
        try {
            while (currentInvokeHandler != null) {
                wait();
            }
            currentInvokeHandler = invokeHandler;
            try {
                if (!eventDispatchThreadWait) {
                    SwingUtilities.invokeLater(invokeHandler);
                }
                notifyAll();
                while (!invokeHandler.isHandlerExecuted()) {
                    wait();
                }
            } finally {
                currentInvokeHandler = null;
                invokeHandler.clearInvokeHandler();
                notifyAll();
            }
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private boolean invokeCanAdd(BackgroundTask task) {
        if (task == null) {
            return false;
        }
        if (!task.isNeedSafeCall(BackgroundTask.CAN_ADD)) {
            return canAdd(task);
        }
        if (SwingUtilities.isEventDispatchThread()) {
            return canAdd(task);
        } else {
            InvokeCanAddHandler invokeCanAddHandler = new InvokeCanAddHandler(task);
            invoikeAndWait(invokeCanAddHandler);
            return invokeCanAddHandler.getResult();
        }
    }

    private void invokeTaskWasAdded(BackgroundTask task) {
        if (task == null) {
            return;
        }
        if (!task.isNeedSafeCall(BackgroundTask.TASK_WAS_ADDED)) {
            taskWasAdded(task);
            return;
        }
        if (SwingUtilities.isEventDispatchThread()) {
            taskWasAdded(task);
        } else {
            invoikeAndWait(new InvokeTaskWasAddedHandler(task));
        }
    }

    private void invokeTaskWasRemoved(BackgroundTask task, boolean wasInProgress) {
        if (task == null) {
            return;
        }
        if (!task.isNeedSafeCall(BackgroundTask.TASK_WAS_REMOVED)) {
            taskWasRemoved(task, wasInProgress);
            return;
        }
        if (SwingUtilities.isEventDispatchThread()) {
            taskWasRemoved(task, wasInProgress);
        } else {
            invoikeAndWait(new InvokeTaskWasRemovedHandler(task, wasInProgress));
        }
    }

    private NeedExecuteResult invokeIsNeedExecuteTask(BackgroundTask task) {
        if (task == null) {
            return NeedExecuteResult.NO;
        }
        if (!task.isNeedSafeCall(BackgroundTask.IS_NEED_EXECUTE_TASK)) {
            return isNeedExecuteTask(task);
        }
        if (SwingUtilities.isEventDispatchThread()) {
            return isNeedExecuteTask(task);
        } else {
            InvokeIsNeedExecuteTaskHandler invokeIsNeedExecuteTaskHandler = new InvokeIsNeedExecuteTaskHandler(task);
            invoikeAndWait(invokeIsNeedExecuteTaskHandler);
            return invokeIsNeedExecuteTaskHandler.getResult();
        }
    }

    private void invokeBeforeExecuteTask(BackgroundTask task) {
        if (task == null) {
            return;
        }
        if (!task.isNeedSafeCall(BackgroundTask.BEFORE_EXECUTE_TASK)) {
            beforeExecuteTask(task);
            return;
        }
        if (SwingUtilities.isEventDispatchThread()) {
            beforeExecuteTask(task);
        } else {
            invoikeAndWait(new InvokeBeforeExecuteTaskHandler(task));
        }
    }

    private void invokeFinallyExecuteTaskBeforeHandleException(BackgroundTask task) {
        if (task == null) {
            return;
        }
        if (!task.isNeedSafeCall(BackgroundTask.FINALLY_EXECUTE_TASK_BEFORE_HANDLE_EXCEPTION)) {
            finallyExecuteTaskBeforeHandleException(task);
            return;
        }
        if (SwingUtilities.isEventDispatchThread()) {
            finallyExecuteTaskBeforeHandleException(task);
        } else {
            invoikeAndWait(new InvokeFinallyExecuteTaskBeforeHandleExceptionHandler(task));
        }
    }

    private void invokeAfterExecuteTask(BackgroundTask task) {
        if (task == null) {
            return;
        }
        if (!task.isNeedSafeCall(BackgroundTask.AFTER_EXECUTE_TASK)) {
            afterExecuteTask(task);
            return;
        }
        if (SwingUtilities.isEventDispatchThread()) {
            afterExecuteTask(task);
        } else {
            invoikeAndWait(new InvokeAfterExecuteTaskHandler(task));
        }
    }

    private boolean invokeHandleTaskException(BackgroundTask task, Throwable e) {
        if (task == null) {
            return false;
        }
        if (!task.isNeedSafeCall(BackgroundTask.HANDLE_TASK_EXCEPTION)) {
            return handleTaskException(task, e);
        }
        if (SwingUtilities.isEventDispatchThread()) {
            return handleTaskException(task, e);
        } else {
            InvokeHandleTaskExceptionHandler invokeHandleTaskExceptionHandler = new InvokeHandleTaskExceptionHandler(task, e);
            invoikeAndWait(invokeHandleTaskExceptionHandler);
            return invokeHandleTaskExceptionHandler.getResult();
        }
    }

    private void invokeFinallyExecuteTaskAfterHandleException(BackgroundTask task) {
        if (task == null) {
            return;
        }
        if (!task.isNeedSafeCall(BackgroundTask.FINALLY_EXECUTE_TASK_AFTER_HANDLE_EXCEPTION)) {
            finallyExecuteTaskAfterHandleException(task);
            return;
        }
        if (SwingUtilities.isEventDispatchThread()) {
            finallyExecuteTaskAfterHandleException(task);
        } else {
            invoikeAndWait(new InvokeFinallyExecuteTaskAfterHandleExceptionHandler(task));
        }
    }

    private abstract class AbstractInvokeHandler implements Runnable {

        private boolean handlerExecuted = false;

        protected BackgroundTask task;

        public AbstractInvokeHandler(BackgroundTask taskForExecuting) {
            super();
            task = taskForExecuting;
        }

        public void clearInvokeHandler() {
            task = null;
        }

        public void run() {
            if (isHandlerExecuted()) {
                return;
            }
            try {
                DeadlockDetectionService.getInstance().setDeadlockDetected(true);
                if (task != null) {
                    execute();
                }
            } catch (Throwable e) {
                LOG.error(e.getMessage(), e);
            } finally {
                DeadlockDetectionService.getInstance().setDeadlockDetected(false);
                setHandlerExecuted(true);
                synchronized (BackgroundQueue.this) {
                    BackgroundQueue.this.notifyAll();
                }
            }
        }

        public synchronized boolean isHandlerExecuted() {
            return handlerExecuted;
        }

        private synchronized void setHandlerExecuted(boolean value) {
            handlerExecuted = value;
        }

        protected abstract void execute();
    }

    private class InvokeCanAddHandler extends AbstractInvokeHandler {

        private boolean result = false;

        public InvokeCanAddHandler(BackgroundTask taskForExecuting) {
            super(taskForExecuting);
        }

        public boolean getResult() {
            return result;
        }

        protected void execute() {
            result = canAdd(task);
        }
    }

    private class InvokeTaskWasAddedHandler extends AbstractInvokeHandler {

        public InvokeTaskWasAddedHandler(BackgroundTask taskForExecuting) {
            super(taskForExecuting);
        }

        protected void execute() {
            taskWasAdded(task);
        }
    }

    private class InvokeTaskWasRemovedHandler extends AbstractInvokeHandler {

        private boolean wasInProgress;

        public InvokeTaskWasRemovedHandler(BackgroundTask taskForExecuting, boolean wasInProgress) {
            super(taskForExecuting);
            this.wasInProgress = wasInProgress;
        }

        protected void execute() {
            taskWasRemoved(task, wasInProgress);
        }
    }

    private class InvokeIsNeedExecuteTaskHandler extends AbstractInvokeHandler {

        protected NeedExecuteResult result = NeedExecuteResult.NO;

        public InvokeIsNeedExecuteTaskHandler(BackgroundTask taskForExecuting) {
            super(taskForExecuting);
        }

        public NeedExecuteResult getResult() {
            return result;
        }

        protected void execute() {
            result = isNeedExecuteTask(task);
        }
    }

    private class InvokeBeforeExecuteTaskHandler extends AbstractInvokeHandler {

        public InvokeBeforeExecuteTaskHandler(BackgroundTask taskForExecuting) {
            super(taskForExecuting);
        }

        protected void execute() {
            beforeExecuteTask(task);
        }
    }

    private class InvokeFinallyExecuteTaskBeforeHandleExceptionHandler extends AbstractInvokeHandler {

        public InvokeFinallyExecuteTaskBeforeHandleExceptionHandler(BackgroundTask taskForExecuting) {
            super(taskForExecuting);
        }

        protected void execute() {
            finallyExecuteTaskBeforeHandleException(task);
        }
    }

    private class InvokeAfterExecuteTaskHandler extends AbstractInvokeHandler {

        public InvokeAfterExecuteTaskHandler(BackgroundTask taskForExecuting) {
            super(taskForExecuting);
        }

        protected void execute() {
            afterExecuteTask(task);
        }
    }

    private class InvokeHandleTaskExceptionHandler extends AbstractInvokeHandler {

        private Throwable exception;

        private boolean result = false;

        public InvokeHandleTaskExceptionHandler(BackgroundTask taskForExecuting, Throwable e) {
            super(taskForExecuting);
            exception = e;
        }

        public boolean getResult() {
            return result;
        }

        protected void execute() {
            result = handleTaskException(task, exception);
        }

        public void clearInvokeHandler() {
            exception = null;
            super.clearInvokeHandler();
        }
    }

    private class InvokeFinallyExecuteTaskAfterHandleExceptionHandler extends AbstractInvokeHandler {

        public InvokeFinallyExecuteTaskAfterHandleExceptionHandler(BackgroundTask taskForExecuting) {
            super(taskForExecuting);
        }

        protected void execute() {
            finallyExecuteTaskAfterHandleException(task);
        }
    }

    private boolean canAdd(BackgroundTask task) {
        boolean res1 = fireCanAdd(task);
        boolean res2 = task.canAdd();
        return res1 && res2;
    }

    private void taskWasAdded(BackgroundTask task) {
        fireTaskWasAdded(task);
        task.taskWasAdded();
    }

    private void taskWasRemoved(BackgroundTask task, boolean wasInProgress) {
        task.taskWasRemoved(wasInProgress);
        fireTaskWasRemoved(task, wasInProgress);
    }

    private NeedExecuteResult isNeedExecuteTask(BackgroundTask task) {
        NeedExecuteResult res1 = fireIsNeedExecuteTask(task);
        NeedExecuteResult res2;
        try {
            res2 = task.isNeedExecuteTask() ? NeedExecuteResult.YES : NeedExecuteResult.NO;
        } catch (DestroyQueueException e) {
            res2 = NeedExecuteResult.DESTROY;
        }
        return NeedExecuteResult.values()[Math.max(res1.ordinal(), res2.ordinal())];
    }

    private void beforeExecuteTask(BackgroundTask task) {
        fireBeforeExecuteTask(task);
        task.beforeExecuteTask();
    }

    private void finallyExecuteTaskBeforeHandleException(BackgroundTask task) {
        task.finallyExecuteTaskBeforeHandleException();
        fireFinallyExecuteTaskBeforeHandleException(task);
    }

    private void afterExecuteTask(BackgroundTask task) {
        task.afterExecuteTask();
        fireAfterExecuteTask(task);
    }

    private boolean handleTaskException(BackgroundTask task, Throwable exception) {
        boolean res1 = false;
        try {
            task.handleTaskException(exception);
        } catch (DestroyQueueException e) {
            res1 = true;
        }
        boolean res2 = fireHandleTaskException(task, exception);
        return res1 || res2;
    }

    private void finallyExecuteTaskAfterHandleException(BackgroundTask task) {
        task.finallyExecuteTaskAfterHandleException();
        fireFinallyExecuteTaskAfterHandleException(task);
    }

    private boolean fireCanAdd(BackgroundTask task) {
        boolean res = true;
        TaskExecutionEvent taskExecutionEvent = null;
        for (TaskExecutionListener listener : listenerList) {
            if (taskExecutionEvent == null) {
                taskExecutionEvent = new TaskExecutionEvent(this, task, BackgroundTask.CAN_ADD);
            }
            try {
                listener.callingCanAdd(taskExecutionEvent);
            } catch (TaskExecutionVetoException e) {
                res = false;
            }
        }
        return res;
    }

    private void fireTaskWasAdded(BackgroundTask task) {
        TaskExecutionEvent taskExecutionEvent = null;
        for (TaskExecutionListener listener : listenerList) {
            if (taskExecutionEvent == null) {
                taskExecutionEvent = new TaskExecutionEvent(this, task, BackgroundTask.TASK_WAS_ADDED);
            }
            listener.callingTaskWasAdded(taskExecutionEvent);
        }
    }

    private void fireTaskWasRemoved(BackgroundTask task, boolean wasInProgress) {
        TaskExecutionEvent taskExecutionEvent = null;
        for (TaskExecutionListener listener : listenerList) {
            if (taskExecutionEvent == null) {
                taskExecutionEvent = new TaskExecutionEvent(this, task, wasInProgress);
            }
            listener.callingTaskWasRemoved(taskExecutionEvent);
        }
    }

    private NeedExecuteResult fireIsNeedExecuteTask(BackgroundTask task) {
        NeedExecuteResult res = NeedExecuteResult.YES;
        TaskExecutionEvent taskExecutionEvent = null;
        for (TaskExecutionListener listener : listenerList) {
            if (taskExecutionEvent == null) {
                taskExecutionEvent = new TaskExecutionEvent(this, task, BackgroundTask.IS_NEED_EXECUTE_TASK);
            }
            try {
                listener.callingIsNeedExecuteTask(taskExecutionEvent);
            } catch (TaskExecutionVetoException e) {
                if (res != NeedExecuteResult.DESTROY) {
                    res = NeedExecuteResult.NO;
                }
            } catch (DestroyQueueException e) {
                res = NeedExecuteResult.DESTROY;
            }
        }
        return res;
    }

    private void fireBeforeExecuteTask(BackgroundTask task) {
        TaskExecutionEvent taskExecutionEvent = null;
        for (TaskExecutionListener listener : listenerList) {
            if (taskExecutionEvent == null) {
                taskExecutionEvent = new TaskExecutionEvent(this, task, BackgroundTask.BEFORE_EXECUTE_TASK);
            }
            listener.callingBeforeExecuteTask(taskExecutionEvent);
        }
    }

    private void fireFinallyExecuteTaskBeforeHandleException(BackgroundTask task) {
        TaskExecutionEvent taskExecutionEvent = null;
        for (TaskExecutionListener listener : listenerList) {
            if (taskExecutionEvent == null) {
                taskExecutionEvent = new TaskExecutionEvent(this, task, BackgroundTask.FINALLY_EXECUTE_TASK_BEFORE_HANDLE_EXCEPTION);
            }
            listener.callingFinallyExecuteTaskBeforeHandleException(taskExecutionEvent);
        }
    }

    private void fireAfterExecuteTask(BackgroundTask task) {
        TaskExecutionEvent taskExecutionEvent = null;
        for (TaskExecutionListener listener : listenerList) {
            if (taskExecutionEvent == null) {
                taskExecutionEvent = new TaskExecutionEvent(this, task, BackgroundTask.AFTER_EXECUTE_TASK);
            }
            listener.callingAfterExecuteTask(taskExecutionEvent);
        }
    }

    private boolean fireHandleTaskException(BackgroundTask task, Throwable exception) {
        boolean res = false;
        TaskExecutionEvent taskExecutionEvent = null;
        for (TaskExecutionListener listener : listenerList) {
            if (taskExecutionEvent == null) {
                taskExecutionEvent = new TaskExecutionEvent(this, task, exception);
            }
            try {
                listener.callingHandleTaskException(taskExecutionEvent);
            } catch (DestroyQueueException e) {
                res = true;
            }
        }
        return res;
    }

    private void fireFinallyExecuteTaskAfterHandleException(BackgroundTask task) {
        TaskExecutionEvent taskExecutionEvent = null;
        for (TaskExecutionListener listener : listenerList) {
            if (taskExecutionEvent == null) {
                taskExecutionEvent = new TaskExecutionEvent(this, task, BackgroundTask.FINALLY_EXECUTE_TASK_AFTER_HANDLE_EXCEPTION);
            }
            listener.callingFinallyExecuteTaskAfterHandleException(taskExecutionEvent);
        }
    }

    public static void emulateTaskExcecution(BackgroundTask task) throws Throwable {
        if (task == null) {
            return;
        }
        if (!task.canAdd()) {
            return;
        }
        Throwable exception = null;
        task.taskWasAdded();
        try {
            if (task.isNeedExecuteTask()) {
                try {
                    try {
                        task.beforeExecuteTask();
                        try {
                            try {
                                task.executeTask();
                            } catch (DestroyQueueException exp) {
                            }
                        } finally {
                            task.finallyExecuteTaskBeforeHandleException();
                        }
                        task.afterExecuteTask();
                    } catch (Throwable e) {
                        exception = e;
                        try {
                            task.handleTaskException(e);
                        } catch (DestroyQueueException exp) {
                        }
                    }
                } finally {
                    task.finallyExecuteTaskAfterHandleException();
                }
            }
        } finally {
            task.taskWasRemoved(true);
        }
        if (exception != null) throw exception;
    }
}
