package org.template.springbatch;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;

public class BatchMain {

    private static final Logger logger = LogManager.getLogger(BatchMain.class);

    public static void main(String[] args) throws JobInstanceAlreadyCompleteException, JobRestartException, JobExecutionAlreadyRunningException {
        logger.info("Starting Spring batch");
        ClassPathXmlApplicationContext sharedContext = new ClassPathXmlApplicationContext(new String[] { "applicationcontext.xml" });
        logger.info("Started Spring batch template");
        JobLauncher launcher = (JobLauncher) sharedContext.getBean("jobLauncher");
        Job job = (Job) sharedContext.getBean("helloWorldJob");
        JobExecution execution = launcher.run(job, new JobParameters());
    }
}
