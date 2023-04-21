package org.isurf.spmiddleware.scheduling;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;
import org.isurf.spmiddleware.SPClientProfile;
import org.isurf.spmiddleware.SystemException;
import org.isurf.spmiddleware.callback.ALECallback;
import org.isurf.spmiddleware.callback.EPCISCallback;
import org.isurf.spmiddleware.dao.ReaderProfileDAO;
import org.isurf.spmiddleware.dao.SPClientProfileDAO;
import org.isurf.spmiddleware.dao.SubscriptionDAO;
import org.isurf.spmiddleware.fac.Grouper;
import org.isurf.spmiddleware.model.ale.ECBoundarySpec;
import org.isurf.spmiddleware.model.ale.ECSpec;
import org.isurf.spmiddleware.reader.ReaderManager;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Spring managed {@link EventCycleScheduler} implemented using Quartz.
 */
public class QuartzScheduler implements EventCycleScheduler {

    private static final String EVENT_CYCLE_GROUP = "Observation Cycle Group";

    private static final Logger logger = Logger.getLogger(QuartzScheduler.class);

    private org.quartz.Scheduler scheduler;

    private EventCycleFactory eventCycleFactory;

    /**
	 * Constructs and starts a QuartzScheduler.
	 *
	 * @param sfb The Spring Quartz {@link SchedulerFactoryBean}.
	 * @param readerManager The reader manager.
	 * @param spClientProfileDAO The {@link SPClientProfileDAO}
	 * @param callback The {@link ALECallback}.
	 * @param readerProfileDAO The {@link ReaderProfileDAO}.
	 * @param subscriptionDAO The {@link SubscriptionDAO}.
	 */
    @Autowired
    public QuartzScheduler(SchedulerFactoryBean sfb, ReaderManager readerManager, SPClientProfileDAO spClientProfleDAO, ALECallback aleCallback, EPCISCallback epcisCallback, ReaderProfileDAO readerProfileDAO, SubscriptionDAO subscriptionDAO, Grouper grouper) {
        try {
            SPClientProfile profile = spClientProfleDAO.find();
            eventCycleFactory = new EventCycleFactory(profile, readerManager, aleCallback, epcisCallback, readerProfileDAO, subscriptionDAO, grouper);
            scheduler = sfb.getScheduler();
            scheduler.setJobFactory(eventCycleFactory);
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error("define: " + e.getMessage(), e);
            throw new SystemException(SystemException.ErrorCode.SCHEDULING_ERROR, e.getMessage(), e);
        }
    }

    public void schedule(String eventCycleName, ECSpec ecSpec) {
        try {
            logger.debug("schedule: eventCycleName = " + eventCycleName + "; ecSpec = " + ecSpec);
            JobDetail jobDetail = new JobDetail(eventCycleName, null, EventCycle.class);
            jobDetail.getJobDataMap().put(EventCycle.EC_SPEC_KEY, ecSpec);
            jobDetail.getJobDataMap().put(EventCycle.EC_SPEC_NAME, eventCycleName);
            scheduleJob(jobDetail, eventCycleName, ecSpec);
        } catch (Exception e) {
            logger.error("schedule: " + e.getMessage(), e);
            throw new SystemException(SystemException.ErrorCode.SCHEDULING_ERROR, e.getMessage(), e);
        }
    }

    /**
	 * Unschedules an ECSpec from execution.
	 *
	 * @param eventCycleName The name of the ECSpec.
	 */
    public void unschedule(String eventCycleName) {
        try {
            scheduler.deleteJob(eventCycleName, null);
        } catch (SchedulerException e) {
            logger.error("unschedule: " + e.getMessage(), e);
            throw new SystemException(SystemException.ErrorCode.SCHEDULING_ERROR, e.getMessage(), e);
        }
    }

    /**
	 * Creates the trigger as defined in the ECSpec.
	 *
	 * @param ecSpec The ECSpec.
	 * @return The trigger.
	 */
    private void scheduleJob(JobDetail jobDetail, String eventCycleName, ECSpec ecSpec) throws ParseException, SchedulerException {
        ECBoundarySpec boundarySpec = ecSpec.getBoundarySpec();
        List<String> startTriggers = null;
        if (boundarySpec.getExtension() != null && boundarySpec.getExtension().getStartTriggerList() != null) {
            startTriggers = boundarySpec.getExtension().getStartTriggerList().getStartTriggers();
        }
        if (boundarySpec.getStartTrigger() != null) {
            scheduleJobUsingStartTrigger(jobDetail, eventCycleName, boundarySpec.getStartTrigger());
        } else if (startTriggers != null && !startTriggers.isEmpty()) {
            scheduleJobUsingStartTriggers(jobDetail, eventCycleName, startTriggers);
        } else if (boundarySpec.getRepeatPeriod() != null && boundarySpec.getRepeatPeriod().getValue() != 0) {
            scheduleJobUsingRepeatPeriod(jobDetail, eventCycleName, boundarySpec.getRepeatPeriod().getValue());
        } else {
            scheduleJobUsingRepeatPeriod(jobDetail, eventCycleName, 0);
        }
    }

    /**
	 * Schedules a Job using a CronTrigger based on the startTrigger of the
	 * ECBoundarySpec.
	 */
    private void scheduleJobUsingStartTrigger(JobDetail jobDetail, String eventCycleName, String startTrigger) throws ParseException, SchedulerException {
        logger.debug("scheduleJobUsingStartTrigger: eventCycleName = " + eventCycleName);
        CronTrigger trigger = new CronTrigger(eventCycleName, EVENT_CYCLE_GROUP);
        trigger.setCronExpression(startTrigger);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
	 * Schedules a Job using the startTriggerList of the ECBoundarySpec.
	 *
	 * @param jobDetail
	 * @param eventCycleName
	 * @param startTriggers
	 * @throws ParseException
	 * @throws SchedulerException
	 */
    private void scheduleJobUsingStartTriggers(JobDetail jobDetail, String eventCycleName, List<String> startTriggers) throws ParseException, SchedulerException {
        logger.debug("scheduleJobUsingStartTriggers: eventCycleName = " + eventCycleName);
        for (String startTrigger : startTriggers) {
            String name = eventCycleName + startTrigger;
            jobDetail.setName(name);
            scheduleJobUsingStartTrigger(jobDetail, name, startTrigger);
        }
    }

    /**
	 * Schedules a Job using a repeat period.
	 *
	 * @param jobDetail
	 * @param eventCycleName
	 * @param repeatPeriod
	 * @throws ParseException
	 * @throws SchedulerException
	 */
    private void scheduleJobUsingRepeatPeriod(JobDetail jobDetail, String eventCycleName, long repeatPeriod) throws ParseException, SchedulerException {
        SimpleTrigger trigger = new SimpleTrigger(eventCycleName, EVENT_CYCLE_GROUP);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        trigger.setRepeatInterval(repeatPeriod);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
	 * Shuts the EventCycleScheduler down.
	 *
	 * @param waitForJobsToComplete if true waits for currently executing jobs to be terminated before shutting down, if false shutsdown immediately.
	 */
    public void shutdown(boolean waitForJobsToComplete) {
        try {
            scheduler.shutdown(waitForJobsToComplete);
        } catch (SchedulerException e) {
            logger.error("shutdown: error occured trying to shutdown scheduler", e);
            throw new SystemException(SystemException.ErrorCode.SCHEDULING_ERROR, "error occured trying to shutdown scheduler", e);
        }
    }
}
