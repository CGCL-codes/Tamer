package org.brainypdm.interfaces;

import org.brainypdm.dto.scheduler.JobInstance;

/**
 * @author <a href="mailto:thomas@brainypdm.org">Thomas Buffagni</a>
 */
public interface JobThreadInterface {

    /***
	 * 
	 * @return id of menaged job
	 */
    public Long getJobId();

    /***
	 * 
	 * @return JobInterface - menaged job instance
	 */
    public JobInstance getJobInstance();
}
