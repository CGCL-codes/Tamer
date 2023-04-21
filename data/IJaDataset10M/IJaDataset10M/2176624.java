package org.red5.server.scheduling;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.red5.server.api.scheduling.IScheduledJob;
import org.red5.server.api.scheduling.ISchedulingService;

/**
 * Scheduled job that is registered in the Quartz scheduler. 
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Joachim Bauch (jojo@struktur.de)
 */
public class QuartzSchedulingServiceJob implements Job {

    protected static final String SCHEDULING_SERVICE = "scheduling_service";

    protected static final String SCHEDULED_JOB = "scheduled_job";

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        ISchedulingService service = (ISchedulingService) arg0.getJobDetail().getJobDataMap().get(SCHEDULING_SERVICE);
        IScheduledJob job = (IScheduledJob) arg0.getJobDetail().getJobDataMap().get(SCHEDULED_JOB);
        job.execute(service);
    }
}
