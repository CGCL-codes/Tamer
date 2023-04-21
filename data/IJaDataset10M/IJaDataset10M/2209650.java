package com.bizosys.oneline.services.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import com.bizosys.oneline.conf.Configuration;
import com.bizosys.oneline.services.Request;
import com.bizosys.oneline.services.Response;
import com.bizosys.oneline.services.Service;
import com.bizosys.oneline.services.ServiceMetaData;

public class SchedulerService implements Service, ISchedulerService {

    static SchedulerService instance = null;

    public static SchedulerService getInstance() {
        if (null != instance) return instance;
        synchronized (SchedulerService.class) {
            if (null != instance) return instance;
            instance = new SchedulerService();
        }
        return instance;
    }

    protected Configuration conf;

    private Timer timer = null;

    public Map<String, ScheduleTask> scheduledTasks = new HashMap<String, ScheduleTask>();

    private SchedulerService() {
    }

    /**
	 * Create the table if it does not exist yet.
	 */
    public boolean init(Configuration conf, ServiceMetaData meta) {
        this.conf = conf;
        timer = new Timer(true);
        try {
            SchedulerLog.l.info("Scheduler Service has Started Sucessfully.");
            return true;
        } catch (Exception ex) {
            SchedulerLog.l.fatal("Error on starting the scheduler service.");
            SchedulerLog.l.fatal(ex);
            return false;
        }
    }

    /**
     * Stopping the service
     */
    public void stop() {
        timer.cancel();
    }

    /**
	 * A task is added to the timer.
	 */
    public boolean putTask(ScheduleTask task) {
        String taskId = task.task.getJobName();
        if (scheduledTasks.containsKey(taskId)) {
            ScheduleTask existingTask = scheduledTasks.get(taskId);
            if (null != existingTask) existingTask.refresh(task);
            return false;
        } else {
            scheduledTasks.put(taskId, task);
            try {
                task.schedule();
            } catch (Exception ex) {
                SchedulerLog.l.fatal(ex);
            }
            return true;
        }
    }

    /**
	 * Once executed, again it is configured for next window
	 * Not to conflict the parallel processing/ always clone and add.
	 * @param aTask
	 */
    public void putTaskNextTime(String jobId) {
        if (SchedulerLog.l.isDebugEnabled()) SchedulerLog.l.debug("scheduler.SchedulerService >> Putting Task For Next Time");
        ScheduleTask aTask = scheduledTasks.get(jobId);
        if (null == aTask) {
            if (SchedulerLog.l.isDebugEnabled()) SchedulerLog.l.debug("scheduler.SchedulerService >> The job might have been discontinued");
            return;
        }
        try {
            Date startTime = aTask.getNextWindow();
            if (null == startTime) {
                aTask.purge();
                scheduledTasks.remove(jobId);
                return;
            }
            if ((null != aTask.endDate) && startTime.after(aTask.endDate)) {
                if (SchedulerLog.l.isDebugEnabled()) SchedulerLog.l.debug("scheduler.SchedulerService >> " + jobId + " : Job is expired.");
                aTask.purge();
                scheduledTasks.remove(jobId);
                return;
            } else {
                if (SchedulerLog.l.isDebugEnabled()) SchedulerLog.l.debug("scheduler.SchedulerService >> " + jobId + " >> Scheduled at : " + startTime);
                this.timer.schedule(aTask.clone(), startTime);
            }
        } catch (Exception ex) {
            SchedulerLog.l.fatal("scheduler.SchedulerService >> ", ex);
        }
    }

    public void process(Request context, Response response) {
    }

    public String getName() {
        return "SchedulerService";
    }
}
