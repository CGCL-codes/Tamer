package org.apache.axis2.deployment.scheduler;

import java.security.PrivilegedAction;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.axis2.java.security.AccessController;

public class Scheduler {

    private final Timer timer = (Timer) AccessController.doPrivileged(new PrivilegedAction() {

        public Object run() {
            return new Timer(true);
        }
    });

    private void reschedule(SchedulerTask schedulerTask, DeploymentIterator iterator) {
        Date time = iterator.next();
        if (time == null) {
            schedulerTask.cancel();
        } else {
            synchronized (schedulerTask.lock) {
                if (schedulerTask.state != SchedulerTask.CANCELLED) {
                    schedulerTask.timerTask = new SchedulerTimerTask(schedulerTask, iterator);
                    timer.schedule(schedulerTask.timerTask, time);
                }
            }
        }
    }

    /**
     * Schedules the specified task for execution according to the specified schedule.
     * If times specified by the <code>ScheduleIterator</code> are in the past they are
     * scheduled for immediate execution.
     *
     * @param schedulerTask task to be scheduled
     * @param iterator      iterator that describes the schedule
     * @throws IllegalStateException if task was already scheduled or cancelled,
     *                               scheduler was cancelled, or scheduler thread terminated.
     */
    public void schedule(SchedulerTask schedulerTask, DeploymentIterator iterator) {
        Date time = iterator.next();
        if (time == null) {
            schedulerTask.cancel();
        } else {
            synchronized (schedulerTask.lock) {
                schedulerTask.state = SchedulerTask.SCHEDULED;
                schedulerTask.timerTask = new SchedulerTimerTask(schedulerTask, iterator);
                timer.schedule(schedulerTask.timerTask, time);
            }
        }
    }

    public void cleanup() {
        timer.cancel();
    }

    public class SchedulerTimerTask extends TimerTask {

        private DeploymentIterator iterator;

        private SchedulerTask schedulerTask;

        public SchedulerTimerTask(SchedulerTask schedulerTask, DeploymentIterator iterator) {
            this.schedulerTask = schedulerTask;
            this.iterator = iterator;
        }

        public void run() {
            schedulerTask.run();
            reschedule(schedulerTask, iterator);
        }
    }
}
