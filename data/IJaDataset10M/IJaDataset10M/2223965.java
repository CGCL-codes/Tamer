package org.go.threadpool;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import org.go.CronExpression;
import org.go.Trigger;
import org.go.Work;
import org.go.expcetion.SchedulerConfigException;
import org.go.work.WorkRunShell;

/**
 * 
 * @author hejie
 *
 */
public class GoThreadPoolImpl implements Serializable, GoThreadPool {

    private static final long serialVersionUID = 5349832498496506645L;

    static MyScheduledThreadPoolExecutor exec;

    private String id;

    private int threadcount;

    public GoThreadPoolImpl() {
    }

    public GoThreadPoolImpl(int minThreadCount) {
        this.threadcount = minThreadCount;
    }

    @Override
    public int blockForAvailableThreads() {
        int active = exec.getActiveCount();
        int max = exec.getMaximumPoolSize();
        return (max - active);
    }

    public void execute(Runnable runnable) {
        exec.execute(runnable);
    }

    @Override
    public int getCorePoolSize() {
        return exec.getCorePoolSize();
    }

    @Override
    public String getPoolId() {
        return getThreadPoolId();
    }

    @Override
    public String getThreadPoolId() {
        return id;
    }

    @Override
    public void initialize() throws SchedulerConfigException {
        exec = new MyScheduledThreadPoolExecutor(this.threadcount);
    }

    @Override
    public boolean isPaused() {
        return exec.isPaused();
    }

    @Override
    public void pause() {
        exec.pause();
    }

    @Override
    public void resume() {
        exec.resume();
    }

    @Override
    public boolean runInThread(Runnable runnable) {
        if (runnable == null) {
            return false;
        }
        exec.execute(runnable);
        return true;
    }

    @Override
    public ScheduledFuture<?> scheduleWithCronExp(Runnable command, CronExpression cronExpression) {
        return exec.scheduleWithCronExp(command, cronExpression);
    }

    @Override
    public ScheduledFuture<?> scheduleWithCronExp(Work work, Trigger trigger) throws Exception {
        System.err.println(exec.getActiveCount());
        return exec.scheduleWithCronExp(new WorkRunShell(work.getGo(), trigger), trigger);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void shutdown() {
        exec.shutdown();
    }

    @Override
    public void shutdown(boolean waitForJobsToComplete) {
        if (waitForJobsToComplete) {
            exec.shutdown();
        } else {
            exec.shutdownNow();
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        return exec.shutdownNow();
    }

    public void start(Runnable run) {
        exec.execute(run);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return exec.submit(task);
    }

    @Override
    public void setThreadCount(int threadcount) {
        this.threadcount = threadcount;
    }
}
