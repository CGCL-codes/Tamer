package com.markatta.hund.core;

import com.google.inject.Singleton;
import java.util.Date;
import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

/**
 * Creates a scheduler factory instance
 * to make scheduled jobs happen.
 * 
 * @author johan
 */
@Singleton
public class SchedulerManager {

    private static final Logger logger = Logger.getLogger(SchedulerManager.class);

    private Scheduler scheduler;

    public SchedulerManager() {
        logger.info("Initializing nightwatch schedule");
        SchedulerFactory factory = new StdSchedulerFactory();
        try {
            scheduler = factory.getScheduler();
            scheduler.start();
        } catch (SchedulerException ex) {
            logger.error("Failed to start scheduler", ex);
        }
    }

    public void setJobFactory(JobFactory jobFactory) {
        try {
            scheduler.setJobFactory(jobFactory);
        } catch (SchedulerException ex) {
            logger.error("Could not replace scheduler job factory", ex);
        }
    }

    public void start() {
        try {
            JobDetail detail = new JobDetail("5 minute trigger", null, WorkItemCreator.class);
            Trigger trigger = TriggerUtils.makeMinutelyTrigger(5);
            trigger.setStartTime(new Date());
            trigger.setName("5 minute trigger");
            scheduler.scheduleJob(detail, trigger);
        } catch (SchedulerException ex) {
            logger.error("Failed to schedule 5 minute check", ex);
        }
    }

    public void shutdown() {
        logger.debug("Stopping nightwatch schedule");
        try {
            scheduler.shutdown();
        } catch (SchedulerException ex) {
            logger.error("Failed to stop scheduler", ex);
        }
    }
}
