package rec2s.core.scheduler;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job {

    public HelloJob() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Hello!  HelloJob is executing." + new Date().toString());
    }
}
