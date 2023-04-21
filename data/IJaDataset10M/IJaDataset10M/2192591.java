package gr.bluecore.webwatch;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class ListenJob implements JobListener {

    public String getName() {
        return "myURLMonitor";
    }

    public void jobToBeExecuted(JobExecutionContext arg0) {
        System.out.println("Executing Job ...");
    }

    public void jobExecutionVetoed(JobExecutionContext arg0) {
    }

    public void jobWasExecuted(JobExecutionContext arg0, JobExecutionException arg1) {
        System.out.println("Job exetuted.");
    }
}
