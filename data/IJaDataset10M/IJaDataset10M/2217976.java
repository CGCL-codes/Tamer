package org.isurf.spmiddleware.scheduling;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.isurf.spmiddleware.SPClientProfile;
import org.isurf.spmiddleware.SystemException;
import org.isurf.spmiddleware.callback.ALECallback;
import org.isurf.spmiddleware.callback.EPCISCallback;
import org.isurf.spmiddleware.dao.ReaderProfileDAO;
import org.isurf.spmiddleware.dao.SubscriptionDAO;
import org.isurf.spmiddleware.fac.ECReportsFactory;
import org.isurf.spmiddleware.fac.EPCISEventFactory;
import org.isurf.spmiddleware.fac.Grouper;
import org.isurf.spmiddleware.model.Subscription;
import org.isurf.spmiddleware.model.ale.ECReports;
import org.isurf.spmiddleware.model.ale.ECReportsTerminationCondition;
import org.isurf.spmiddleware.model.ale.ECSpec;
import org.isurf.spmiddleware.model.epcis.EPCISDocument;
import org.isurf.spmiddleware.model.observation.AbstractObservation;
import org.isurf.spmiddleware.reader.ReaderManager;
import org.isurf.spmiddleware.utils.ECSpecUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.quartz.Trigger;

/**
 * {@link EventCycle} is the basic unit of work of the Smart Product Middleware. {@link EventCycle}s execute one or many
 * {@link ReadCycle}s, build the results into ALE ({@link ECReports}) and EPCIS events {@link EPCISDocument} and send the
 * results to the subscriber.
 * <P>
 * {@link EventCycle}s may execute synchronously or asynchronously.
 */
public class EventCycle implements StatefulJob {

    public static final String EC_SPEC_KEY = "EC_SPEC_KEY";

    public static final String EC_SPEC_NAME = "EC_SPEC_NAME";

    public static final String PRIOR_EVENTS = "PRIOR_EVENTS";

    private static final Logger logger = Logger.getLogger(EventCycle.class);

    private ReaderManager readerManager;

    private SubscriptionDAO subscriptionDAO;

    private Date creationTime;

    private String eventCycleName;

    private SPClientProfile spClientProfile;

    private ALECallback aleCallback;

    private EPCISCallback epcisCallback;

    private ReaderProfileDAO readerProfileDAO;

    private ECReports reports;

    private EPCISDocument epcisEvent;

    private ECReportsTerminationCondition terminationCondition;

    private Grouper grouper;

    /**
	 * States of an EventCycle.
	 */
    private enum State {

        ACTIVE, REQUESTED, UNREQUESTED
    }

    ;

    private State state;

    /**
	 * Constructs an EventCycle.
	 */
    public EventCycle() {
    }

    /**
	 * Constructs an EventCycle.
	 *
	 * @param readerManager The ReaderManager.
	 * @param eventCycleName The eventCycleName.
	 * @param spClientProfile The profile.
	 * @param aleCallback The callback service.
	 * @param readerProfileDAO The {@link ReaderProfileDAO}.
	 * @param subscriptionDAO The {@link SubscriptionDAO}.
	 */
    public EventCycle(ReaderManager readerManager, String eventCycleName, SPClientProfile spClientProfile, ALECallback aleCallback, EPCISCallback epcisCallback, ReaderProfileDAO readerProfileDAO, SubscriptionDAO subscriptionDAO, Grouper grouper) {
        this.readerManager = readerManager;
        this.eventCycleName = eventCycleName;
        this.spClientProfile = spClientProfile;
        this.aleCallback = aleCallback;
        this.epcisCallback = epcisCallback;
        this.readerProfileDAO = readerProfileDAO;
        this.subscriptionDAO = subscriptionDAO;
        this.grouper = grouper;
        creationTime = new Date();
        state = State.UNREQUESTED;
    }

    /**
	 * Executes the EventCycle.
	 *
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.debug("execute: creationTime = " + creationTime);
            synchronized (this) {
                logger.debug("execute: eventCycleName = " + eventCycleName + "; state = " + state + "; this = " + this);
                if (state != State.ACTIVE) {
                    state = State.ACTIVE;
                    logger.debug("execute: EventCycle " + eventCycleName + " is ready for execution, state = " + state);
                    JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
                    ECSpec ecSpec = (ECSpec) jobDataMap.get(EC_SPEC_KEY);
                    String startTrigger = getStartTrigger(context);
                    read(ecSpec, jobDataMap, startTrigger);
                    EPCISEventFactory epcisEventFactory = new EPCISEventFactory(reports, spClientProfile, readerProfileDAO);
                    epcisEvent = epcisEventFactory.createEPCISDocument();
                    callback();
                    state = State.REQUESTED;
                    logger.debug("execute: EventCycle " + eventCycleName + " is ready for execution, state = " + state);
                } else {
                    logger.debug("execute: EventCycle " + eventCycleName + " was already executing, state = " + state);
                    if (state == State.UNREQUESTED) {
                        state = State.REQUESTED;
                    }
                }
            }
            logger.debug("execute: end");
        } catch (Exception e) {
            state = State.REQUESTED;
            logger.error("execute: " + e.getMessage(), e);
            throw new SystemException(SystemException.ErrorCode.CREATION_ERROR, e.getMessage(), e);
        }
    }

    /**
	 * Gets the start trigger.
	 *
	 * @param context The job context.
	 * @return the start trigger.
	 */
    private String getStartTrigger(JobExecutionContext context) {
        String startTrigger = null;
        Trigger trigger = context.getTrigger();
        if (trigger instanceof CronTrigger) {
            CronTrigger cronTrigger = (CronTrigger) trigger;
            startTrigger = cronTrigger.getCronExpression();
        }
        return startTrigger;
    }

    /**
	 * Sends the results to all subscribers the event cycle specification.
	 */
    private void callback() {
        List<Subscription> subscriptions = subscriptionDAO.findByECSpecName(eventCycleName);
        for (Subscription subscription : subscriptions) {
            callback(subscription);
        }
    }

    /**
	 * Sends the results to the subscriber of the event cycle specification.
	 *
	 * @param subscription The Subscription to call back to.
	 */
    private void callback(Subscription subscription) {
        aleCallback.aleCallback(subscription.getNotificationURI(), reports);
        if (epcisCallback != null) {
            epcisCallback.epcisCallback(epcisEvent);
        }
    }

    /**
	 * Reads the informatino from the Readers as defined in the ECSpec.
	 *
	 * @param ecSpec The event cycle specification.
	 * @param jobDataMap The data map.
	 * @param startTrigger The trigger which initiated the event cycle
	 */
    public void read(ECSpec ecSpec, JobDataMap jobDataMap, String startTrigger) {
        logger.debug("read: eventCycleName = " + eventCycleName);
        try {
            long startTime = System.currentTimeMillis();
            List<String> readers = ecSpec.getLogicalReaders().getLogicalReaders();
            Set<ReadCycle> readCycles = createReadCycles(ecSpec, readers);
            Set<AbstractObservation> currentEvents = invokeReadCycles(ecSpec, readCycles);
            long endTime = System.currentTimeMillis();
            long eventCycleTime = endTime - startTime;
            Set<AbstractObservation> priorEvents = null;
            if (jobDataMap != null) {
                priorEvents = (Set<AbstractObservation>) jobDataMap.get(PRIOR_EVENTS);
            } else {
                priorEvents = new HashSet<AbstractObservation>();
            }
            ECReportsFactory factory = new ECReportsFactory(ecSpec, currentEvents, priorEvents, grouper);
            reports = factory.createECReports(spClientProfile.getProfileName(), eventCycleName, eventCycleTime, startTrigger, terminationCondition, null);
            if (jobDataMap != null) {
                jobDataMap.put(PRIOR_EVENTS, currentEvents);
            }
            logger.debug("read: reports = " + reports);
        } catch (Exception e) {
            logger.error("read: ", e);
            throw new SystemException(SystemException.ErrorCode.SCHEDULING_ERROR, " Error occured executing event cycle", e);
        }
    }

    /**
	 * Creates the Set of ReadCycles to be run.
	 *
	 * @param ecSpec The ECSpec.
	 * @param readers The readers.
	 * @return The ReadCycles.
	 */
    private Set<ReadCycle> createReadCycles(ECSpec ecSpec, List<String> readers) {
        logger.debug("createReadCycles: readers = " + readers);
        Set<ReadCycle> readCycles = new HashSet<ReadCycle>();
        for (String readerName : readers) {
            ReadCycle readCycle = new ReadCycle(ecSpec, readerName, readerManager);
            readCycles.add(readCycle);
        }
        return readCycles;
    }

    /**
	 * Invokes all ReadCycles.
	 *
	 * @param ecSpec The ECSpec.
	 * @param readCycles The ReadCycles
	 * @return The results.
	 * @throws InterruptedException
	 */
    private Set<AbstractObservation> invokeReadCycles(ECSpec ecSpec, Set<ReadCycle> readCycles) throws InterruptedException {
        Set<AbstractObservation> readCycleResults = new HashSet<AbstractObservation>();
        ExecutorService exec = Executors.newCachedThreadPool();
        long duration = ECSpecUtils.getDuration(ecSpec);
        try {
            Map<Future<List<? extends AbstractObservation>>, ReadCycle> tasks = new HashMap<Future<List<? extends AbstractObservation>>, ReadCycle>();
            for (ReadCycle readCycle : readCycles) {
                Future<List<? extends AbstractObservation>> future = exec.submit(readCycle);
                tasks.put(future, readCycle);
                logger.debug("invokeReadCycles: future = " + future + "; readCycle = " + readCycle);
            }
            StringBuffer report = new StringBuffer("");
            long startTime = System.currentTimeMillis();
            if (duration != 0) {
                executeForFiniteDuration(duration, tasks, report, startTime);
            } else {
                terminationCondition = ECReportsTerminationCondition.UNREQUEST;
            }
            for (Future<List<? extends AbstractObservation>> future : tasks.keySet()) {
                List<? extends AbstractObservation> observations = future.get();
                readCycleResults.addAll(observations);
                logger.debug("invokeReadCycles: observations size = " + observations.size());
            }
            logger.debug("invokeReadCycles: report " + report);
        } catch (ExecutionException ee) {
            logger.error("invokeReadCycles: error occurred executing task", ee);
            throw new SystemException(SystemException.ErrorCode.SCHEDULING_ERROR, ee.getMessage(), ee);
        } finally {
            exec.shutdownNow();
        }
        return readCycleResults;
    }

    /**
	 * Executes the tasks for the specified duration.
	 *
	 * @param duration The duration to execute the tasks in milliseconds
	 * @param readCycles The tasks to be executed.
	 * @param report The report for debugging.
	 * @param startTime The start time
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
    private void executeForFiniteDuration(long duration, Map<Future<List<? extends AbstractObservation>>, ReadCycle> readCycles, StringBuffer report, long startTime) throws InterruptedException, ExecutionException {
        Thread.sleep(duration);
        for (ReadCycle readCycle : readCycles.values()) {
            long currentTime = System.currentTimeMillis() - startTime;
            report.append("time = " + currentTime + "\n");
            logger.debug("invokeReadCycles: cancelling...");
            readCycle.stop();
            logger.debug("invokeReadCycles: cancelled readCycle");
        }
        terminationCondition = ECReportsTerminationCondition.DURATION;
    }

    /**
	 * Gets the ALE event created.
	 *
	 * @return the ALE.
	 */
    public ECReports getReports() {
        return reports;
    }
}
