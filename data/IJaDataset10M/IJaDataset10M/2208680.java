package net.sourceforge.velai;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import net.sourceforge.velai.internal.ExecutionState;
import net.sourceforge.velai.internal.TaskExecutionEventHandler;
import net.sourceforge.velai.internal.TaskMonitoringAndNotificationManager;

public class TaskExecutor {

    private static AtomicInteger nextThreadId = new AtomicInteger(1);

    private static final TaskMonitoringAndNotificationManager manager = new TaskMonitoringAndNotificationManager();

    /**
     * Executes the given callable within the current thread.
     * A default generated task name is assigned, based on the task's type.
     * @param <V> the return value
     * @param callable the task to execute
     * @return the result from executing the task
     * @throws ExecutionException on any checked exceptions thrown during execution
     * @throws RejectedExecutionException if not started due to errors
     * @throws CancellationException if result could not be retrieved because the task was cancelled
     * @throws InterruptedException if the task execution was interrupted
     * @throws RuntimeException if thrown by the task
     * @throws Error if thrown by the task
     */
    public static <V> V execute(Callable<V> callable) throws RejectedExecutionException, ExecutionException, InterruptedException {
        ExecutionState<V> execution = newExecutionState(null, callable);
        return execution.execute();
    }

    /**
     * Executes the given callable within the current thread.
     * @param <V> the return value
     * @param name presented within some error messages and available from {@link TaskFuture#taskName()}.
     * @param callable the task to execute
     * @return the result from executing the task
     * @throws ExecutionException on any checked exceptions thrown during execution
     * @throws RejectedExecutionException if not started due to errors
     * @throws CancellationException if result could not be retrieved because the task was cancelled
     * @throws InterruptedException if the task execution was interrupted
     * @throws RuntimeException if thrown by the task
     * @throws Error if thrown by the task
     */
    public static <V> V execute(String name, Callable<V> callable) throws RejectedExecutionException, ExecutionException, InterruptedException {
        ExecutionState<V> execution = newExecutionState(name, callable);
        return execution.execute();
    }

    /**
     * Starts a new thread and executes the given callable within it.
     * Returns a {@link TaskFuture} that may be used to monitor
     * its execution progress, wait for it to complete, or
     * to cancel it.
     * @param <V> the return value
     * @param callable the task to execute
     * @return a future for the task
     * @throws RejectedExecutionException if thrown by an @Init method of the task
     * @throws RuntimeException if thrown by an @Init method of the task
     * @throws Error if thrown by an @Init method of the task
     */
    public static <V> TaskFuture<V> start(Callable<V> callable) throws RejectedExecutionException {
        ExecutionState<V> execution = newExecutionState(null, callable);
        return execution.start();
    }

    /**
     * Starts a new thread and executes the given callable within it.
     * Returns a {@link TaskFuture} that may be used to monitor
     * its execution progress, wait for it to complete, or
     * to cancel it.
     * @param <V> the return value
     * @param name presented within some error messages and available from {@link TaskFuture#taskName()}.
     * @param callable the task to execute
     * @return a future for the task
     * @throws RejectedExecutionException if thrown by an @Init method of the task
     * @throws RuntimeException if thrown by an @Init method of the task
     * @throws Error if thrown by an @Init method of the task
     */
    public static <V> TaskFuture<V> start(String name, Callable<V> callable) throws RejectedExecutionException {
        ExecutionState<V> execution = newExecutionState(name, callable);
        return execution.start();
    }

    /**
     * Prepares the task for synchronous or asynchronous execution, but does
     * not start it executing. The returned {@link StartableTaskFuture} can be
     * used to start the execution.
     * 
     * <p> This method is provided for more advanced executions of tasks.
     * Examples uses include when the {@link TaskFuture} is needed for
     * synchronous execution so that it can be cancelled or paused by a monitor
     * thread; and when the caller needs to allocate all threads prior
     * to starting execution, so that all threads may start immediately upon
     * a "gun-shot".
     * 
     * <p> Extra care must be taken when using this advanced approach.
     * For example, if the task is never started, a call to {@link TaskFuture#get()}
     * will never return.
     * Subsequent calls to {@link #execute(Callable)}, {@link #start(Callable)} and
     * {@link #prepare(boolean, Callable)} may cause exceptions to be thrown until
     * the task has been started and completed execution.
     * However exceptions may not be thrown if the executor cannot detect that
     * previous execution attempt.
     * 
     * @param <V> the return value
     * @param asynchronousExecution whether the task will execute asynchronously
     *        once started
     * @param callable the task to prepare for execution
     * @return a startable future for the task
     * @throws RejectedExecutionException if thrown by an @Init method of the task
     * @throws RuntimeException if thrown by an @Init method of the task
     * @throws Error if thrown by an @Init method of the task
     */
    public static <V> StartableTaskFuture<V> prepare(boolean asynchronousExecution, Callable<V> callable) throws RejectedExecutionException {
        ExecutionState<V> execution = newExecutionState(null, callable);
        return execution.prepare(asynchronousExecution);
    }

    /**
     * Mostly same as {@link #prepare(boolean,Callable)}.
     * Assigns the supplied task name.
     * 
     * @param <V> the return value
     * @param asynchronousExecution whether the task will execute asynchronously
     *        once started
     * @param name presented within some error messages and available from {@link TaskFuture#taskName()}.
     * @param callable the task to prepare for execution
     * @return a startable future for the task
     * @throws RejectedExecutionException if thrown by an @Init method of the task
     * @throws RuntimeException if thrown by an @Init method of the task
     * @throws Error if thrown by an @Init method of the task
     */
    public static <V> StartableTaskFuture<V> prepare(boolean asynchronousExecution, String name, Callable<V> callable) throws RejectedExecutionException {
        ExecutionState<V> execution = newExecutionState(name, callable);
        return execution.prepare(asynchronousExecution);
    }

    /**
     * Creates a new execution state.
     * @param <V>
     * @param name optional name
     * @param callable task
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <V> ExecutionState<V> newExecutionState(String name, Callable<V> callable) {
        TaskExecutionEventHandler<V> eventHandler = (TaskExecutionEventHandler<V>) manager.newEventHandler();
        return new ExecutionState<V>(popNextThreadId(), name, callable, eventHandler);
    }

    /**
     * Gets the futures of all tasks that are currently executing.
     * @return
     */
    public static TaskFuture<Object> getExecuting() {
        throw new UnsupportedOperationException("to do");
    }

    private static int popNextThreadId() {
        return nextThreadId.getAndIncrement();
    }
}
