package net.sourceforge.velai.utils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.RejectedExecutionException;
import net.sourceforge.velai.ExecutionContext;
import net.sourceforge.velai.ProgressState;
import net.sourceforge.velai.TaskFuture;
import net.sourceforge.velai.annotations.Context;
import net.sourceforge.velai.annotations.Init;
import net.sourceforge.velai.annotations.OnCancel;
import net.sourceforge.velai.annotations.OnPauseStateChanged;
import net.sourceforge.velai.annotations.Progress;

/**
 * Task that executes multiple child tasks in sequence, using
 * a single thread, passing results from one task to the next, and
 * returning the result of the last task.
 * The result of this task is either the result of the last task
 * to be executed, or the first exception thrown by any of the child tasks.
 * 
 * <p> This class is abstract. To use it, it must be extended and
 * the {@link #pipe(TaskFuture,Callable)} method implemented.
 * 
 * <p> Supports cancellation and pausing.
 * 
 * @param <V> common value of child tasks, use Object or ? if no common type 
 * @created 14/05/2011
 * @author malcolm.lett
 */
public abstract class PipedTaskGroup<V> implements TaskGroup<V>, Callable<V> {

    private SequentialTaskGroup<V> delegate;

    public PipedTaskGroup() {
        delegate = new SequentialTaskGroup<V>();
    }

    /**
     * Note: a copy is made of the tasks array, so subsequent changes to it will
     * have no effect on the operation of this class.
     * @param tasks
     */
    public PipedTaskGroup(Callable<? extends V>... tasks) {
        delegate = new SequentialTaskGroup<V>(tasks);
    }

    /**
     * Note: a copy is made of the tasks array, so subsequent changes to it will
     * have no effect on the operation of this class.
     * @param tasks
     */
    public PipedTaskGroup(List<? extends Callable<? extends V>> tasks) {
        delegate = new SequentialTaskGroup<V>(tasks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Callable<? extends V> task) {
        delegate.add(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAll(Collection<? extends Callable<? extends V>> tasks) {
        delegate.addAll(tasks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskFuture<V>> getCompleted() {
        return delegate.getCompleted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskFuture<V>> getFutures() {
        return delegate.getFutures();
    }

    /**
     * Executes the child tasks in the order they were supplied.
     * @returns the ordered list of result objects from each child task
     * @throws InterruptedException if interrupted while executing or if any
     *         of the child tasks throw it
     * @throws Exception on any exception thrown by a child task
     */
    @Override
    public V call() throws InterruptedException, Exception {
        List<V> results = delegate.call();
        return results.get(results.size() - 1);
    }

    @Init
    protected void init() {
        delegate.init();
    }

    /**
     * Injection point for execution context.
     * Using public setter instead of private field to support
     * running when a Security Manager is installed.
     * @param context
     */
    @Context
    protected void setExecutionContext(ExecutionContext execution) throws RejectedExecutionException {
        delegate.setExecutionContext(execution);
    }

    /**
     * Sub-classes may override this method to prepare the next task,
     * usually based on the outcome of the previous task.
     * Takes the future of the previously completed task and the instance of
     * the task about to be executed, and returns a task for execution. Usually
     * the same task supplied is returned, after it is prepared, but this is
     * not required.
     * 
     * <p> Called with a {@code null previousFuture} when the first task is about
     * to be executed, and called with a {@code null nextTask} after the last
     * task has executed.
     * 
     * <p> The implementation may return {@code null} at any time,
     * in which case the current task is skipped and the task following, if any
     * remain, is executed instead - after first calling this method again
     * for the new task (the same previousFuture is supplied again).
     * 
     * <p> The implementation may return a different task to the
     * one supplied, including returning a non-<code>null</code> task
     * instance where {@code nextTask} is {@code null}.
     * 
     * @param previousFuture the previously completed task
     * @param nextTask the proposed task for next execution
     * @return the next task for execution, or null to skip to the
     *         next task, if available, or otherwise to stop.
     * @throws InterruptedException may be thrown if thread is interrupted during execution
     * @throws CancellationException may be thrown to cancel execution
     * @throws Exception on any exception that should be thrown by this task group
     */
    protected abstract Callable<V> pipe(TaskFuture<V> previousFuture, Callable<V> nextTask) throws InterruptedException, CancellationException, Exception;

    /**
     * Cancels the current task if executing.
     * @param mayInterruptIfRunning
     */
    @OnCancel
    public void onCancel(boolean mayInterruptIfRunning) {
        delegate.onCancel(mayInterruptIfRunning);
    }

    /**
     * Pauses the current task if executing.
     */
    @OnPauseStateChanged
    public void onPause() {
        delegate.onPause();
    }

    /**
     * Combines the progress of all child tasks and returns the
     * result.
     * Where one or more tasks do not identify a progress fraction,
     * they are treated as having a {@code 0.0} fraction if incomplete,
     * and a {@code 1.0} fraction if complete.
     * @return
     */
    @Progress
    public ProgressState getProgress() {
        return delegate.getProgress();
    }

    /**
     * Internal implementation to link {@link #pipe(TaskFuture, Callable)} method
     * to delegate's {@code pipe()} method.
     */
    private class DelegateTaskGroup extends SequentialTaskGroup<V> {

        public DelegateTaskGroup() {
            super();
        }

        public DelegateTaskGroup(Callable<? extends V>... tasks) {
            super(tasks);
        }

        public DelegateTaskGroup(List<? extends Callable<? extends V>> tasks) {
            super(tasks);
        }

        protected Callable<V> pipe(TaskFuture<V> previousFuture, Callable<V> nextTask) throws InterruptedException, CancellationException, Exception {
            return PipedTaskGroup.this.pipe(previousFuture, nextTask);
        }
    }
}
