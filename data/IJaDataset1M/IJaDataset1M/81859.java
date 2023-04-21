package org.exist.scheduler;

import java.util.Map;
import org.exist.storage.BrokerPool;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Class to represent a System Job
 * Should be extended by all classes wishing to
 * schedule as a Job that perform core system functions
 * 
 * Classes extending SystemJob may only have a Single Instance
 * running in the scheduler at once, intersecting schedules will be queued.
 * 
 * @author Adam Retter <adam.retter@devon.gov.uk>
 */
public abstract class SystemJob implements JobDescription, Job {

    public static final String JOB_GROUP = "eXist.System";

    public final String getGroup() {
        return JOB_GROUP;
    }

    /**
	 *	The execute method as called by the Quartz Scheduler
	 *
	 * @param jec	The execution context of the executing job
	 *
	 * @throws JobExecutionException if there was a problem with the job,
	 * this also describes to Quartz how to cleanup the job
	 */
    public final void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDataMap jobDataMap = jec.getJobDetail().getJobDataMap();
        BrokerPool pool = (BrokerPool) jobDataMap.get("brokerpool");
        Map params = (Map) jobDataMap.get("params");
        try {
            execute(pool, params);
        } catch (JobException je) {
            je.cleanupJob();
        }
    }

    /**
	 * Function that is executed by the Scheduler
	 * 
	 * @param brokerpool	The BrokerPool for the Scheduler of this job 
	 * @param params	Any parameters passed to the job or null otherwise
	 * 
	 * @throws JobException if there is a problem with the job.
	 * cleanupJob() should then be called, which will adjust the
	 * jobs scheduling appropriately
	 */
    public abstract void execute(BrokerPool brokerpool, Map params) throws JobException;
}
