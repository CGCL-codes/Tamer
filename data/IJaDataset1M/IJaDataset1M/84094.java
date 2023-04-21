package org.gearman.impl.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A utility that schedules tasks to run on a {@link ExecutorService} defined by the user such that
 * it abides by the {@link ScheduledExecutorService} documentation.
 * 
 * @author isaiah
 */
public class Scheduler implements ScheduledExecutorService {

    /**
	 * Defines a scheduled task type
	 * @author isaiah
	 */
    private static enum ScheduleType {

        /**
		 * A one-time delay task that will not be re-scheduled
		 */
        SCHEDULED, /**
		 * A task that will be re-scheduled to run at a fixed rate.
		 */
        FIXED_RATE, /**
		 * A task that will be re-scheduled with a specified delay after the
		 * task has completed executing
		 */
        FIXED_DELAY
    }

    ;

    /**
	 * Pulls jobs from the DelayQueue and executes them on the ExecutorService. The Driver is executed
	 * on a thread from the given ThreadFactory. Depending on the state of the Scheduler, it may or
	 * may not timeout when no scheduled jobs are available. 
	 * 
	 * @author isaiah
	 */
    private class Driver implements Runnable {

        @Override
        public void run() {
            try {
                while (!Scheduler.this.isShutdown()) {
                    if (Scheduler.this.queue.isEmpty()) {
                        final long startTime = System.nanoTime();
                        long remainder;
                        synchronized (this) {
                            while (Scheduler.this.queue.isEmpty()) {
                                if (!Scheduler.this.isThreadTimeout) {
                                    this.wait();
                                } else if ((remainder = Scheduler.this.threadTimeout - (System.nanoTime() - startTime)) > 0) {
                                    TimeUnit.NANOSECONDS.timedWait(this, remainder);
                                } else {
                                    return;
                                }
                            }
                        }
                    }
                    assert !Scheduler.this.queue.isEmpty();
                    Scheduler.this.executor.execute(Scheduler.this.queue.take());
                }
            } catch (RejectedExecutionException ree) {
                Scheduler.this.shutdown();
            } catch (InterruptedException ie) {
            } finally {
                assert Scheduler.this.isShutdown();
                synchronized (this) {
                    Scheduler.this.thread = null;
                    Scheduler.this.clean();
                }
            }
        }
    }

    /**
	 * A scheduled task
	 * @author isaiah
	 *
	 * @param <X> The return type
	 */
    private class ScheduledFutureTask<X> extends FutureTask<X> implements RunnableScheduledFuture<X> {

        private long time;

        private final long period;

        private final ScheduleType type;

        public ScheduledFutureTask(Runnable runnable, X result, long initialDelay, long period, ScheduleType type) {
            super(runnable, result);
            this.time = initialDelay + System.nanoTime();
            this.period = period;
            this.type = type;
        }

        public ScheduledFutureTask(Callable<X> callable, long initialDelay, long period, ScheduleType type) {
            super(callable);
            this.time = initialDelay + System.nanoTime();
            this.period = period;
            this.type = type;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(time - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            assert o instanceof ScheduledFutureTask<?>;
            return compareTo((ScheduledFutureTask<?>) o);
        }

        public final int compareTo(final ScheduledFutureTask<?> o) {
            if (time - o.time < 0) return 1; else if (time - o.time == 0) return 0; else return 1;
        }

        @Override
        public final boolean isPeriodic() {
            return !this.type.equals(ScheduleType.SCHEDULED);
        }

        @Override
        public final void run() {
            switch(this.type) {
                case SCHEDULED:
                    super.run();
                    break;
                case FIXED_RATE:
                    if (super.runAndReset()) this.reschedule(this.time + this.period);
                    break;
                case FIXED_DELAY:
                    if (super.runAndReset()) this.reschedule(System.nanoTime() + period);
                    break;
            }
        }

        private final void reschedule(final long time) {
            this.time = time;
            Scheduler.this.queue.add(this);
            Scheduler.this.newEvent();
        }
    }

    /** The {@link ExecutorService} defined by the user  */
    private final ExecutorService executor;

    /** The queue that hold scheduled tasks*/
    private final DelayQueue<RunnableScheduledFuture<?>> queue = new DelayQueue<RunnableScheduledFuture<?>>();

    /** The driver that submits tasks to the executor */
    private final Driver driver = new Driver();

    /** Tests if this Scheduler is shutdown */
    private boolean isShutdown = false;

    /** The thread running the driver. If null, nothing is executing the driver */
    private Thread thread = null;

    /** The factory for creating threads */
    private ThreadFactory threadFactory;

    /** The amount of time, in nanoseconds, a thread can be ideal before being terminated */
    private long threadTimeout = 60000000000L;

    /** If true the driving thread can timeout, otherwise it cannot */
    private boolean isThreadTimeout = true;

    public final void allowSchedulerThreadTimeOut(boolean isThreadTimeout) {
        this.isThreadTimeout = isThreadTimeout;
        this.newEvent();
    }

    public final void setThreadTimeout(long timeout, TimeUnit value) {
        this.threadTimeout = value.toNanos(timeout);
        this.newEvent();
    }

    /**
	 * Creates a new Scheduler with the given {@link ExecutorService}.<br>
	 * <br>
	 * It is presumed the Scheduler will have complete control over the {@link ExecutorService}
	 * and the given {@link ExecutorService} will not be directly shutdown.<br>
	 * <br>
	 * Calling the {@link Scheduler#shutdown()} method will shutdown both the Scheduler and the underlying
	 * {@link ExecutorService}
	 * 
	 * @param executor
	 * 		The {@link ExecutorService} that tasks will be submitted to.
	 */
    public Scheduler(final ExecutorService executor) {
        this(executor, Executors.defaultThreadFactory());
    }

    public Scheduler(final ExecutorService executor, ThreadFactory threadfactory) {
        if (executor == null || executor.isShutdown()) throw new IllegalArgumentException("invalid executor");
        this.executor = executor;
        this.threadFactory = threadfactory;
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        if (this.isShutdown) throw new RejectedExecutionException();
        final RunnableScheduledFuture<?> sf = new ScheduledFutureTask<Object>(command, null, unit.toNanos(delay), 0, ScheduleType.SCHEDULED);
        this.queue.add(sf);
        this.newEvent();
        return sf;
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        if (this.isShutdown) throw new RejectedExecutionException();
        final RunnableScheduledFuture<V> sf = new ScheduledFutureTask<V>(callable, unit.toNanos(delay), 0, ScheduleType.SCHEDULED);
        this.queue.add(sf);
        this.newEvent();
        return sf;
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        if (this.isShutdown) throw new RejectedExecutionException();
        final RunnableScheduledFuture<?> sf = new ScheduledFutureTask<Object>(command, null, unit.toNanos(initialDelay), unit.toNanos(period), ScheduleType.FIXED_RATE);
        this.queue.add(sf);
        assert this.queue.contains(sf);
        this.newEvent();
        return sf;
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        if (this.isShutdown) throw new RejectedExecutionException();
        final RunnableScheduledFuture<?> sf = new ScheduledFutureTask<Object>(command, null, unit.toNanos(initialDelay), unit.toNanos(delay), ScheduleType.FIXED_DELAY);
        this.queue.add(sf);
        this.newEvent();
        return sf;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.executor.awaitTermination(timeout, unit);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.executor.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return this.executor.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return this.executor.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.executor.invokeAny(tasks, timeout, unit);
    }

    @Override
    public boolean isShutdown() {
        return this.isShutdown;
    }

    @Override
    public boolean isTerminated() {
        return this.executor.isTerminated();
    }

    @Override
    public void shutdown() {
        synchronized (this.driver) {
            if (this.isShutdown) return;
            this.isShutdown = true;
            if (thread != null) thread.interrupt();
        }
        this.executor.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        synchronized (this.driver) {
            if (!this.isShutdown) {
                this.isShutdown = true;
                if (thread != null) thread.interrupt();
            }
        }
        return this.executor.shutdownNow();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return this.executor.submit(task);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return this.executor.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return this.executor.submit(task, result);
    }

    @Override
    public void execute(Runnable command) {
        this.executor.execute(command);
    }

    private final void newEvent() {
        synchronized (this.driver) {
            this.driver.notify();
            if (this.thread == null) {
                this.thread = this.threadFactory.newThread(this.driver);
                this.thread.start();
            }
        }
    }

    private final void clean() {
        for (Future<?> f : this.queue) {
            f.cancel(false);
        }
        this.queue.clear();
    }
}
