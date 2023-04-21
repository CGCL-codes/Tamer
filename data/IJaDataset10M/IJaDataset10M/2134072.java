package org.hlj.commons.thread;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定间任务操作封装类
 * @author WD
 * @since JDK5
 * @version 1.0 2010-01-25
 */
public final class TaskEngine {

    private static final Timer TIMER;

    /**
	 * 静态初始化
	 */
    static {
        TIMER = new Timer(true);
    }

    /**
	 * 获得Timer
	 * @return Timer
	 */
    public static final Timer getTimer() {
        return TIMER;
    }

    /**
	 * 安排在指定的时间执行指定的任务。
	 * @param task 任务
	 * @param time 时间
	 */
    public static final void schedule(Runnable task, Date time) {
        schedule(getTimerTask(task), time);
    }

    /**
	 * 安排指定的任务在指定的时间开始进行重复的固定延迟执行。
	 * @param task 任务
	 * @param firstTime 开始时间
	 * @param period 间隔多长时间运行一次
	 */
    public static final void schedule(Runnable task, Date firstTime, long period) {
        schedule(getTimerTask(task), firstTime, period);
    }

    /**
	 * 安排在指定延迟后执行指定的任务。
	 * @param task 任务
	 * @param delay 延迟执行时间
	 */
    public static final void schedule(Runnable task, long delay) {
        schedule(getTimerTask(task), delay);
    }

    /**
	 * 安排指定的任务从指定的延迟后开始进行重复的固定延迟执行。
	 * @param task 任务
	 * @param delay 延迟执行时间
	 * @param period 间隔多长时间运行一次
	 */
    public static final void schedule(Runnable task, long delay, long period) {
        schedule(getTimerTask(task), delay, period);
    }

    /**
	 * 安排指定的任务在指定的时间开始进行重复的固定速率执行。
	 * @param task 任务
	 * @param firstTime 开始时间
	 * @param period 间隔多长时间运行一次
	 */
    public static final void scheduleAtFixedRate(Runnable task, Date firstTime, long period) {
        scheduleAtFixedRate(getTimerTask(task), firstTime, period);
    }

    /**
	 * 安排指定的任务在指定的延迟后开始进行重复的固定速率执行。
	 * @param task 任务
	 * @param delay 延迟执行时间
	 * @param period 间隔多长时间运行一次
	 */
    public static final void scheduleAtFixedRate(Runnable task, long delay, long period) {
        scheduleAtFixedRate(getTimerTask(task), delay, period);
    }

    /**
	 * 安排在指定的时间执行指定的任务。
	 * @param task 任务
	 * @param time 时间
	 */
    public static final void schedule(TimerTask task, Date time) {
        TIMER.schedule(task, time);
    }

    /**
	 * 安排指定的任务在指定的时间开始进行重复的固定延迟执行。
	 * @param task 任务
	 * @param firstTime 开始时间
	 * @param period 间隔多长时间运行一次
	 */
    public static final void schedule(TimerTask task, Date firstTime, long period) {
        TIMER.schedule(task, firstTime, period);
    }

    /**
	 * 安排在指定延迟后执行指定的任务。
	 * @param task 任务
	 * @param delay 延迟执行时间
	 */
    public static final void schedule(TimerTask task, long delay) {
        TIMER.schedule(task, delay);
    }

    /**
	 * 安排指定的任务从指定的延迟后开始进行重复的固定延迟执行。
	 * @param task 任务
	 * @param delay 延迟执行时间
	 * @param period 间隔多长时间运行一次
	 */
    public static final void schedule(TimerTask task, long delay, long period) {
        TIMER.schedule(task, delay, period);
    }

    /**
	 * 安排指定的任务在指定的时间开始进行重复的固定速率执行。
	 * @param task 任务
	 * @param firstTime 开始时间
	 * @param period 间隔多长时间运行一次
	 */
    public static final void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
        TIMER.scheduleAtFixedRate(task, firstTime, period);
    }

    /**
	 * 安排指定的任务在指定的延迟后开始进行重复的固定速率执行。
	 * @param task 任务
	 * @param delay 延迟执行时间
	 * @param period 间隔多长时间运行一次
	 */
    public static final void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        TIMER.scheduleAtFixedRate(task, delay, period);
    }

    /**
	 * 停止所有任务
	 */
    public static final void shutdown() {
        TIMER.cancel();
    }

    /**
	 * 根据Runnable获得getTimerTask
	 * @param task Runnable
	 * @return TimerTask
	 */
    private static TimerTask getTimerTask(Runnable task) {
        if (task instanceof TimerTask) {
            return (TimerTask) task;
        } else {
            return new BasicTimerTask(task);
        }
    }

    /**
	 * 私有构造
	 */
    private TaskEngine() {
    }
}
