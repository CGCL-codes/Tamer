package net.sourceforge.velai.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.sourceforge.velai.TaskFuture;
import net.sourceforge.velai.TaskListener;

public class TasksMonitor {

    private final TaskExecutionEventListener<Object> immediateEventListener = new ImmediateEventListener();

    private final TaskExecutionEventListener<Object> deferredEventListener = new DeferredEventListener();

    private ReentrantReadWriteLock upToDateStatsLock = new ReentrantReadWriteLock();

    private int activeTaskCount;

    private int completedTaskCount;

    private int cancellingTaskCount;

    private ReentrantReadWriteLock delayedStatsLock = new ReentrantReadWriteLock();

    private List<TaskFuture<?>> activeTasks;

    public TasksMonitor() {
        this.activeTasks = new ArrayList<TaskFuture<?>>();
    }

    /**
     * Gets an instance of {@link TaskExecutionEventListener} that can be notified
     * immediately of events, within the task's or caller's thread.
     * @return
     */
    public TaskExecutionEventListener<Object> asImmediateTaskExecutionEventListener() {
        return immediateEventListener;
    }

    /**
     * Gets an instance of {@link TaskExecutionEventListener} that should be
     * notified asynchronously within the monitor thread.
     * @return
     */
    public TaskExecutionEventListener<Object> asDeferredTaskExecutionEventListener() {
        return deferredEventListener;
    }

    /**
     * @return Returns the activeThreadCount.
     */
    public int getActiveThreadCount() {
        upToDateStatsLock.readLock().lock();
        try {
            return activeTaskCount;
        } finally {
            upToDateStatsLock.readLock().unlock();
        }
    }

    /**
     * @return Returns the completedThreadCount.
     */
    public int getCompletedThreadCount() {
        upToDateStatsLock.readLock().lock();
        try {
            return completedTaskCount;
        } finally {
            upToDateStatsLock.readLock().unlock();
        }
    }

    /**
     * @return Returns the activeTasks.
     */
    public List<TaskFuture<?>> getActiveTasks() {
        delayedStatsLock.readLock().lock();
        try {
            return activeTasks;
        } finally {
            delayedStatsLock.readLock().unlock();
        }
    }

    /**
     * Listens to immediately notified monitor events.
     */
    private class ImmediateEventListener implements TaskExecutionEventListener<Object> {

        @Override
        public void taskStarted(ExecutionState<Object> executionState) {
            if (executionState.isAsynchronousExecution()) {
                upToDateStatsLock.writeLock().lock();
                try {
                    activeTaskCount++;
                } finally {
                    upToDateStatsLock.writeLock().unlock();
                }
            }
        }

        @Override
        public void taskCancellationRequested(ExecutionState<Object> executionState, boolean interrupted) {
            if (executionState.isAsynchronousExecution()) {
                upToDateStatsLock.writeLock().lock();
                try {
                    if (executionState.isExecuting()) {
                        cancellingTaskCount++;
                    }
                } finally {
                    upToDateStatsLock.writeLock().unlock();
                }
            }
        }

        @Override
        public void taskCompleted(ExecutionState<Object> executionState) {
            if (executionState.isAsynchronousExecution()) {
                upToDateStatsLock.writeLock().lock();
                try {
                    if (executionState.isCancellationRequested()) {
                        cancellingTaskCount--;
                    }
                    activeTaskCount--;
                    completedTaskCount++;
                } finally {
                    upToDateStatsLock.writeLock().unlock();
                }
            }
        }

        @Override
        public void taskCreated(ExecutionState<Object> executionState) {
        }

        @Override
        public void taskPauseRequested(ExecutionState<Object> executionState) {
        }

        @Override
        public void taskUnpauseRequested(ExecutionState<Object> executionState) {
        }

        @Override
        public void addTaskListener(TaskListener<? super Object> listener, boolean notifyImmediately) {
        }

        @Override
        public void removeTaskListener(TaskListener<? super Object> listener) {
        }
    }

    /**
     * Listens to deferred monitor events.
     */
    private class DeferredEventListener implements TaskExecutionEventListener<Object> {

        @Override
        public void taskStarted(ExecutionState<Object> executionState) {
        }

        @Override
        public void taskCompleted(ExecutionState<Object> executionState) {
        }

        @Override
        public void taskCreated(ExecutionState<Object> executionState) {
        }

        @Override
        public void taskPauseRequested(ExecutionState<Object> executionState) {
        }

        @Override
        public void taskUnpauseRequested(ExecutionState<Object> executionState) {
        }

        @Override
        public void taskCancellationRequested(ExecutionState<Object> executionState, boolean interrupted) {
        }

        @Override
        public void addTaskListener(TaskListener<? super Object> listener, boolean notifyImmediately) {
        }

        @Override
        public void removeTaskListener(TaskListener<? super Object> listener) {
        }
    }
}
