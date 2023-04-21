package org.ikasan.consumer.quartz;

import java.text.ParseException;
import java.util.Date;
import org.apache.log4j.Logger;
import org.ikasan.spec.component.endpoint.Consumer;
import org.ikasan.spec.configuration.ConfiguredResource;
import org.ikasan.spec.event.EventFactory;
import org.ikasan.spec.event.EventListener;
import org.ikasan.spec.flow.FlowEvent;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.StatefulJob;
import org.quartz.Trigger;

/**
 * This test class supports the <code>Consumer</code> class.
 * 
 * @author Ikasan Development Team
 */
public class ScheduledConsumer implements Consumer<EventListener>, ConfiguredResource<ScheduledConsumerConfiguration>, StatefulJob {

    /** logger */
    private static Logger logger = Logger.getLogger(ScheduledConsumer.class);

    /** Scheduler */
    private Scheduler scheduler;

    /** consumer event factory */
    private EventFactory<FlowEvent<?, ?>> flowEventFactory;

    /** consumer event listener */
    private EventListener eventListener;

    /** configuredResourceId */
    private String configuredResourceId;

    /** consumer configuration */
    private ScheduledConsumerConfiguration consumerConfiguration;

    /** quartz job detail */
    private JobDetail jobDetail;

    /**
     * Constructor
     * @param scheduler
     * @param jobDetail
     * @param flowEventFactory
     */
    public ScheduledConsumer(Scheduler scheduler, JobDetail jobDetail, EventFactory<FlowEvent<?, ?>> flowEventFactory) {
        this.scheduler = scheduler;
        if (scheduler == null) {
            throw new IllegalArgumentException("scheduler cannot be 'null'");
        }
        this.jobDetail = jobDetail;
        if (jobDetail == null) {
            throw new IllegalArgumentException("jobDetail cannot be 'null'");
        }
        this.flowEventFactory = flowEventFactory;
        if (flowEventFactory == null) {
            throw new IllegalArgumentException("flowEventFactory cannot be 'null'");
        }
    }

    /**
     * Start the underlying tech
     */
    public void start() {
        try {
            Trigger trigger = getCronTrigger(jobDetail.getName(), jobDetail.getGroup(), this.consumerConfiguration.getCronExpression());
            Date scheduledDate = scheduler.scheduleJob(jobDetail, trigger);
            logger.info("Scheduled consumer for flow [" + jobDetail.getName() + "] module [" + jobDetail.getGroup() + "] starting at [" + scheduledDate + "]");
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stop the scheduled job and triggers
     */
    public void stop() {
        try {
            this.scheduler.deleteJob(jobDetail.getName(), jobDetail.getGroup());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Is the underlying tech actively running
     * @return isRunning
     */
    public boolean isRunning() {
        try {
            JobDetail jobDetail = this.scheduler.getJobDetail(this.jobDetail.getName(), this.jobDetail.getGroup());
            if (jobDetail == null) {
                return false;
            }
            return this.scheduler.isStarted();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set the consumer event listener
     * @param eventListener
     */
    public void setListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Callback from the scheduler.
     * @param context
     */
    public void execute(JobExecutionContext context) {
        String uniqueId = context.getJobDetail().getFullName();
        FlowEvent<?, ?> flowEvent = this.flowEventFactory.newEvent(uniqueId, context);
        this.eventListener.invoke(flowEvent);
    }

    public ScheduledConsumerConfiguration getConfiguration() {
        return consumerConfiguration;
    }

    public String getConfiguredResourceId() {
        return this.configuredResourceId;
    }

    public void setConfiguration(ScheduledConsumerConfiguration consumerConfiguration) {
        this.consumerConfiguration = consumerConfiguration;
    }

    public void setConfiguredResourceId(String configuredResourceId) {
        this.configuredResourceId = configuredResourceId;
    }

    /**
     * Method factory for creating a new job detail
     * @return jobDetail
     */
    protected JobDetail getJobDetail() {
        return new JobDetail();
    }

    /**
     * Method factory for creating a cron trigger
     * @return jobDetail
     * @throws ParseException 
     */
    protected Trigger getCronTrigger(String name, String group, String cronExpression) throws ParseException {
        return new CronTrigger(name, group, cronExpression);
    }
}
