package org.ikasan.consumer.quartz;

import java.util.Map;
import org.ikasan.scheduler.ScheduledJobFactory;
import org.ikasan.spec.component.endpoint.Consumer;
import org.ikasan.spec.event.EventFactory;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;

/**
 * Scheduled based consumer Factory provides
 * consumer instances using a single scheduler and job factory
 * for coordination.
 * 
 * @author Ikasan Development Team
 */
public class ScheduledConsumerFactory {

    /** Quartz Scheduler */
    private Scheduler scheduler;

    /** event factory */
    private EventFactory eventFactory;

    /**
     * Constructor
     * @param scheduler
     * @param scheduledConsumerJobFactory
     */
    public ScheduledConsumerFactory(Scheduler scheduler, EventFactory eventFactory) {
        this.scheduler = scheduler;
        if (scheduler == null) {
            throw new IllegalArgumentException("scheduler cannot be 'null'");
        }
        this.eventFactory = eventFactory;
        if (eventFactory == null) {
            throw new IllegalArgumentException("eventFactory cannot be 'null'");
        }
    }

    public Consumer getScheduledConsumer(String flowName, String moduleName) {
        JobDetail jobDetail = new JobDetail();
        jobDetail.setJobClass(ScheduledConsumer.class);
        jobDetail.setName(flowName);
        jobDetail.setGroup(moduleName);
        ScheduledConsumer scheduledConsumer = new ScheduledConsumer(this.scheduler, jobDetail, this.eventFactory);
        Map<String, Job> jobs = ScheduledJobFactory.getInstance().getScheduledJobs();
        jobs.put(flowName + moduleName, (Job) scheduledConsumer);
        return scheduledConsumer;
    }
}
