package grouprec.job;

import grouprec.service.ServiceRecommender;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author ricardol
 */
public class ServiceRecommenderJob implements Job {

    public ServiceRecommenderJob() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        ServiceRecommender serviceRecommender = (ServiceRecommender) dataMap.get("ServiceRecommenderJob");
        serviceRecommender.computeRecommendationsOneByOne();
    }
}
