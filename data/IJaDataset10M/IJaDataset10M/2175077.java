package net.sourceforge.velai.internal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import net.sourceforge.velai.ExecutionContext;
import net.sourceforge.velai.TaskCancellationRequestEvent;
import net.sourceforge.velai.TaskEvent;
import net.sourceforge.velai.TaskFuture;
import net.sourceforge.velai.TaskListener;
import net.sourceforge.velai.annotations.Context;
import net.sourceforge.velai.annotations.Daemon;
import net.sourceforge.velai.internal.annotations.InternalTask;

/**
 * Handles events during the execution of a single task.
 * This involves notifying listeners on that particular task,
 * and listeners on all tasks.
 * 
 * @param <V> the value of the task
 * @created 1/05/2011
 * @author malcolm.lett
 */
public class TaskExecutionEventHandler<V> {

    private TaskExecutionEventListener<Object> immediateMonitorListener = (TaskExecutionEventListener<Object>) TaskExecutionEventListener.NULL_LISTENER;

    private TaskExecutionEventListener<Object> deferredMonitorListener = (TaskExecutionEventListener<Object>) TaskExecutionEventListener.NULL_LISTENER;

    /**
     * Listeners to be notified within the task's thread
     * immediately that an event occurs.
     */
    private TaskListenerList<V> immediateTaskListeners = new TaskListenerList<V>();

    /**
     * Listeners to be asynchronously notified within a notifier thread
     * after an event occurs.
     */
    private TaskListenerList<V> deferredTaskListeners = new TaskListenerList<V>();

    /**
     * Queue of events for monitor thread(s): these only
     * update the internal monitor state.
     * Events are moved to the 'notification' queue once
     * removed from here.
     */
    private BlockingQueue<EventAction<V>> monitorEventQueue;

    public static BlockingQueue<EventAction<Object>> newEventQueue() {
        return new LinkedBlockingQueue<EventAction<Object>>();
    }

    public static MonitorUpdateTask newMonitorUpdateTask(BlockingQueue<EventAction<Object>> inputQueue, BlockingQueue<EventAction<Object>> outputQueue) {
        return new MonitorUpdateTask(inputQueue, outputQueue);
    }

    public static NotifierTask newNotifierTask(BlockingQueue<EventAction<Object>> inputQueue) {
        return new NotifierTask(inputQueue);
    }

    public TaskExecutionEventHandler(BlockingQueue<EventAction<V>> monitorEventQueue, TasksMonitor taskMonitor) {
        this.monitorEventQueue = monitorEventQueue;
        this.immediateMonitorListener = taskMonitor.asImmediateTaskExecutionEventListener();
        this.deferredMonitorListener = taskMonitor.asDeferredTaskExecutionEventListener();
    }

    /**
     * Returns a listener implementation to be passed around.
     * @return
     */
    public TaskExecutionEventListener<V> asListener() {
        return new TaskExecutionEventListener<V>() {

            @SuppressWarnings("unchecked")
            @Override
            public void taskCreated(ExecutionState<V> executionState) {
                if (!executionState.isInternalTask()) {
                    immediateMonitorListener.taskCreated((ExecutionState<Object>) executionState);
                    immediateHandleEventAction(new TaskCreationEventAction<V>(executionState, deferredMonitorListener));
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public void taskStarted(ExecutionState<V> executionState) {
                if (!executionState.isInternalTask()) {
                    immediateMonitorListener.taskStarted((ExecutionState<Object>) executionState);
                    immediateHandleEventAction(new TaskStartedEventAction<V>(executionState, deferredMonitorListener));
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public void taskPauseRequested(ExecutionState<V> executionState) {
                if (!executionState.isInternalTask()) {
                    immediateMonitorListener.taskPauseRequested((ExecutionState<Object>) executionState);
                    immediateHandleEventAction(new TaskPauseRequestedEventAction<V>(executionState, deferredMonitorListener, deferredTaskListeners));
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public void taskUnpauseRequested(ExecutionState<V> executionState) {
                if (!executionState.isInternalTask()) {
                    immediateMonitorListener.taskUnpauseRequested((ExecutionState<Object>) executionState);
                    immediateHandleEventAction(new TaskUnpauseRequestedEventAction<V>(executionState, deferredMonitorListener, deferredTaskListeners));
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public void taskCancellationRequested(ExecutionState<V> executionState, boolean interrupted) {
                if (!executionState.isInternalTask()) {
                    immediateMonitorListener.taskCancellationRequested((ExecutionState<Object>) executionState, interrupted);
                    immediateHandleEventAction(new TaskCancellationRequestedEventAction<V>(executionState, deferredMonitorListener, deferredTaskListeners, interrupted));
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public void taskCompleted(ExecutionState<V> executionState) {
                if (!executionState.isInternalTask()) {
                    immediateMonitorListener.taskCompleted((ExecutionState<Object>) executionState);
                    immediateHandleEventAction(new TaskCompletionEventAction<V>(executionState, deferredMonitorListener, deferredTaskListeners));
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public void addTaskListener(TaskListener<? super V> listener, boolean notifyImmediately) {
                if (notifyImmediately) {
                    immediateTaskListeners.add((TaskListener<V>) listener);
                } else {
                    deferredTaskListeners.add((TaskListener<V>) listener);
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public void removeTaskListener(TaskListener<? super V> listener) {
                immediateTaskListeners.remove((TaskListener<V>) listener);
                deferredTaskListeners.remove((TaskListener<V>) listener);
            }
        };
    }

    /**
     * Handle an event action within the thread that caused the event.
     * This has the task of executing anything that must be performed
     * within this thread, and then delegating asynchronous
     * executions to worker threads.
     * Does not throw any exceptions.
     * @param future
     */
    private void immediateHandleEventAction(EventAction<V> action) {
        try {
            action.notifyListeners(immediateTaskListeners);
        } catch (Throwable t) {
            System.err.println("[" + Thread.currentThread().getName() + "]: Exception thrown within event action " + action + ":");
            t.printStackTrace(System.err);
        }
        try {
            monitorEventQueue.add(action);
        } catch (Throwable t) {
            System.err.println("[" + Thread.currentThread().getName() + "]: Exception thrown within event action " + action + ":");
            t.printStackTrace(System.err);
        }
    }

    /**
     * Internal task that calls {@link EventAction#asynchronouslyExecute}
     * on event actions from the 'monitor' queue,
     * and then transfers the event actions to the 'notification' queue.
     * <p> One to many instances of this internal task may be created
     * across the whole application.
     */
    @InternalTask
    @Daemon
    public static class MonitorUpdateTask implements Callable<Void> {

        private static AtomicInteger nextThreadId = new AtomicInteger((int) System.currentTimeMillis());

        private String name;

        /**
         * Queue of events for monitor thread(s): these only
         * update the internal monitor state.
         * Events are moved to the 'notification' queue once
         * removed from here.
         */
        private BlockingQueue<EventAction<Object>> monitorEventQueue;

        /**
         * Queue of events for notifier thread(s): these notify
         * external listeners.
         * They execute in separate threads than for internal
         * monitoring, so that internal monitoring is not
         * affected by slow-executing external listeners.
         */
        private BlockingQueue<EventAction<Object>> notificationEventQueue;

        @Context
        private ExecutionContext context;

        public MonitorUpdateTask(BlockingQueue<EventAction<Object>> monitorEventQueue, BlockingQueue<EventAction<Object>> notificationEventQueue) {
            this.monitorEventQueue = monitorEventQueue;
            this.notificationEventQueue = notificationEventQueue;
            this.name = "TaskMonitor-" + nextThreadId.getAndIncrement();
        }

        public String getName() {
            return name;
        }

        @Override
        public Void call() throws Exception {
            while (!context.isCancellationRequested()) {
                EventAction<Object> action = monitorEventQueue.take();
                try {
                    action.doDeferredMonitorUpdates();
                } catch (Throwable t) {
                    System.err.println("[" + Thread.currentThread().getName() + "]: Exception thrown within event action " + action + ":");
                    t.printStackTrace(System.err);
                }
                try {
                    notificationEventQueue.add(action);
                } catch (Throwable t) {
                    System.err.println("[" + Thread.currentThread().getName() + "]: Exception thrown within event action " + action + ":");
                    t.printStackTrace(System.err);
                }
            }
            return null;
        }
    }

    /**
     * Internal task that calls {@link EventAction#notifyListeners}
     * on event actions from the 'notification' queue.
     * <p> One to many instances of this internal task may be created
     * across the whole application.
     */
    @InternalTask
    @Daemon
    public static class NotifierTask implements Callable<Void> {

        private static AtomicInteger nextThreadId = new AtomicInteger((int) System.currentTimeMillis());

        private String name;

        /**
         * Queue of events for notifier thread(s): these notify
         * external listeners.
         * They execute in separate threads than for internal
         * monitoring, so that internal monitoring is not
         * affected by slow-executing external listeners.
         */
        private BlockingQueue<EventAction<Object>> notificationEventQueue;

        @Context
        private ExecutionContext context;

        public NotifierTask(BlockingQueue<EventAction<Object>> notificationEventQueue) {
            this.notificationEventQueue = notificationEventQueue;
            this.name = "TaskEventNotifier-" + nextThreadId.getAndIncrement();
        }

        public String getName() {
            return name;
        }

        @Override
        public Void call() throws Exception {
            while (!context.isCancellationRequested()) {
                EventAction<Object> action = notificationEventQueue.take();
                try {
                    action.notifyDeferredListeners();
                } catch (Throwable t) {
                    System.err.println("[" + Thread.currentThread().getName() + "]: Exception thrown within event action " + action + ":");
                    t.printStackTrace(System.err);
                }
            }
            return null;
        }
    }

    /**
     * Base class for event actions that don't need to asynchronously notify
     * listeners but do need to asynchronusly notify the monitor.
     */
    private abstract static class DeferrableMonitorableEventAction<T> implements EventAction<T> {

        protected ExecutionState<T> executionState;

        protected TaskExecutionEventListener<Object> deferredMonitorListener;

        private DeferrableMonitorableEventAction(ExecutionState<T> executionState, TaskExecutionEventListener<Object> deferredMonitorListener) {
            this.executionState = executionState;
            this.deferredMonitorListener = deferredMonitorListener;
        }
    }

    /**
     * Base class for event actions that need to asynchronously notify
     * listeners as well as the monitor.
     */
    private abstract static class DeferrableNotifiableMonitorableEventAction<T> extends DeferrableMonitorableEventAction<T> {

        protected TaskListenerList<T> deferredListeners;

        private DeferrableNotifiableMonitorableEventAction(ExecutionState<T> executionState, TaskExecutionEventListener<Object> deferredMonitorListener, TaskListenerList<T> deferredListeners) {
            super(executionState, deferredMonitorListener);
            this.deferredListeners = deferredListeners;
        }

        @Override
        public void notifyDeferredListeners() {
            notifyListeners(deferredListeners);
        }
    }

    /**
     * Event action for handling when the task is created and prepared
     * for execution, but not yet started.
     */
    private static class TaskCreationEventAction<T> extends DeferrableMonitorableEventAction<T> implements EventAction<T> {

        public TaskCreationEventAction(ExecutionState<T> executionState, TaskExecutionEventListener<Object> deferredMonitorListener) {
            super(executionState, deferredMonitorListener);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void doDeferredMonitorUpdates() {
            deferredMonitorListener.taskCreated((ExecutionState<Object>) executionState);
        }

        @Override
        public void notifyListeners(TaskListenerList<T> listeners) {
        }

        @Override
        public void notifyDeferredListeners() {
        }
    }

    /**
     * Event action for handling when execution of the task starts,
     * after it has been created and prepared.
     */
    private static class TaskStartedEventAction<T> extends DeferrableMonitorableEventAction<T> implements EventAction<T> {

        public TaskStartedEventAction(ExecutionState<T> executionState, TaskExecutionEventListener<Object> deferredMonitorListener) {
            super(executionState, deferredMonitorListener);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void doDeferredMonitorUpdates() {
            deferredMonitorListener.taskStarted((ExecutionState<Object>) executionState);
        }

        @Override
        public void notifyListeners(TaskListenerList<T> listeners) {
        }

        @Override
        public void notifyDeferredListeners() {
        }
    }

    /**
     * Event action for handling when a request is made to pause the task.
     */
    private static class TaskPauseRequestedEventAction<T> extends DeferrableNotifiableMonitorableEventAction<T> implements EventAction<T> {

        public TaskPauseRequestedEventAction(ExecutionState<T> executionState, TaskExecutionEventListener<Object> deferredMonitorListener, TaskListenerList<T> deferredListeners) {
            super(executionState, deferredMonitorListener, deferredListeners);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void doDeferredMonitorUpdates() {
            deferredMonitorListener.taskPauseRequested((ExecutionState<Object>) executionState);
        }

        @Override
        public void notifyListeners(TaskListenerList<T> listeners) {
        }
    }

    /**
     * Event action for handling when a request is made to unpause the task.
     */
    private static class TaskUnpauseRequestedEventAction<T> extends DeferrableNotifiableMonitorableEventAction<T> implements EventAction<T> {

        public TaskUnpauseRequestedEventAction(ExecutionState<T> executionState, TaskExecutionEventListener<Object> deferredMonitorListener, TaskListenerList<T> deferredListeners) {
            super(executionState, deferredMonitorListener, deferredListeners);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void doDeferredMonitorUpdates() {
            deferredMonitorListener.taskUnpauseRequested((ExecutionState<Object>) executionState);
        }

        @Override
        public void notifyListeners(TaskListenerList<T> listeners) {
        }
    }

    /**
     * Event action for handling when a request is made to cancel the task.
     */
    private static class TaskCancellationRequestedEventAction<T> extends DeferrableNotifiableMonitorableEventAction<T> implements EventAction<T> {

        private boolean interrupted;

        public TaskCancellationRequestedEventAction(ExecutionState<T> executionState, TaskExecutionEventListener<Object> deferredMonitorListener, TaskListenerList<T> deferredListeners, boolean interrupted) {
            super(executionState, deferredMonitorListener, deferredListeners);
            this.interrupted = interrupted;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void doDeferredMonitorUpdates() {
            deferredMonitorListener.taskCancellationRequested((ExecutionState<Object>) executionState, interrupted);
        }

        @Override
        public void notifyListeners(TaskListenerList<T> listeners) {
            TaskFuture<T> nonBlockingFuture = new NonBlockingTaskFutureImpl<T>(executionState.getFuture());
            TaskCancellationRequestEvent<T> event = new TaskCancellationRequestEventImpl<T>(null, executionState, nonBlockingFuture, interrupted);
            listeners.cancellationRequested(event);
        }
    }

    /**
     * Event action for handling when the task completes.
     */
    private static class TaskCompletionEventAction<T> extends DeferrableNotifiableMonitorableEventAction<T> implements EventAction<T> {

        public TaskCompletionEventAction(ExecutionState<T> executionState, TaskExecutionEventListener<Object> deferredMonitorListener, TaskListenerList<T> deferredListeners) {
            super(executionState, deferredMonitorListener, deferredListeners);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void doDeferredMonitorUpdates() {
            deferredMonitorListener.taskCompleted((ExecutionState<Object>) executionState);
        }

        @Override
        public void notifyListeners(TaskListenerList<T> listeners) {
            TaskEvent<T> event = new TaskEventImpl<T>(null, executionState, executionState.getFuture());
            listeners.taskCompleted(event);
        }
    }
}
