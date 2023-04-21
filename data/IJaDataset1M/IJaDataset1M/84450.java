package uk.ac.ncl.cs.instantsoap.servicedp.impl;

import uk.ac.ncl.cs.instantsoap.job.JobManager;
import uk.ac.ncl.cs.instantsoap.servicedp.AsynchronousServiceDispatcher;
import uk.ac.ncl.cs.instantsoap.servicedp.IllegalInvocationStateException;
import uk.ac.ncl.cs.instantsoap.servicedp.InvocationState;
import uk.ac.ncl.cs.instantsoap.strategydp.StrategyDispatcher;
import uk.ac.ncl.cs.instantsoap.strategydp.StrategyDispatcherListener;
import uk.ac.ncl.cs.instantsoap.wsapi.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A simple implementation of AsynchronousServiceDispatcher that delegates all
 * the hard work on to a StartegyDispatcher.
 *
 * @author Louis
 * @author Matthew Pocock
 */
public class AsynchronousServiceDispatcherImpl implements AsynchronousServiceDispatcher {

    private final Logger logger = Logger.getLogger("logger");

    private final JobManager jobManager;

    private final StrategyDispatcher strategyDispatcher;

    public AsynchronousServiceDispatcherImpl(StrategyDispatcher dispatcher, JobManager jobManager) {
        this.strategyDispatcher = dispatcher;
        this.jobManager = jobManager;
    }

    public List<String> listApplications() {
        logger.info("Searching for list of mounted applications");
        return strategyDispatcher.listApplications();
    }

    public MetaData describeApplication(String appName) throws UnknownApplicationException {
        logger.info("Describing application: " + appName);
        return strategyDispatcher.describeApplication(appName);
    }

    public UUID invoke(JobSpecification jobSpec) throws InvalidJobSpecificationException, JobExecutionException, UnknownApplicationException {
        UUID publicID = jobManager.newJob();
        jobManager.setJobSubmissionTime(publicID, new Date());
        UUID internalID;
        synchronized (strategyDispatcher) {
            StrategyDispatcher.ValidatedJob vj = strategyDispatcher.validateJob(jobSpec);
            internalID = vj.dispatch(new StrategyDispatcherListenerImpl(publicID));
        }
        return publicID;
    }

    public InvocationState pollState(UUID id) throws UnknownUuidException, JobExecutionException {
        if (jobManager.isFailure(id)) {
            throw new JobExecutionException(jobManager.getFailureReason(id));
        }
        return jobManager.getInvocationState(id);
    }

    public NonBlockingInvocationResponse getExecutionResult(UUID id) throws UnknownUuidException, IllegalInvocationStateException, JobExecutionException {
        InvocationState state = pollState(id);
        switch(state) {
            case WAITING:
                throw new IllegalInvocationStateException("The jobManager is not executed yet. Try it later");
            case IN_PROGRESS:
                throw new IllegalInvocationStateException("The jobManager execution is in progress. Try it later");
            case COMPLETED:
                return new NonBlockingInvocationResponse(id, jobManager.getResult(id));
            default:
                throw new Error("Unknown state: " + state);
        }
    }

    public Set<MetaData> getInputs(String application) throws UnknownApplicationException {
        return strategyDispatcher.getInputs(application);
    }

    public Set<MetaData> getOutputs(String application) throws UnknownApplicationException {
        return strategyDispatcher.getOutputs(application);
    }

    /**
     * StrategyDispatcherListener is implemented as an inner class.
     * Therefore only small piece of code has to passed down to the
     * lower layer to provide callback function
     */
    public class StrategyDispatcherListenerImpl implements StrategyDispatcherListener {

        private final UUID id;

        public StrategyDispatcherListenerImpl(UUID id) {
            this.id = id;
        }

        public void jobStarted() {
            jobManager.setInvocationState(id, InvocationState.IN_PROGRESS);
            jobManager.setJobExecutionStartTime(id, new Date());
        }

        public void jobCompletedSuccessfully(Map<String, String> result) throws JobExecutionException {
            jobManager.setResult(id, result);
            jobManager.setInvocationState(id, InvocationState.COMPLETED);
            jobManager.setJobExecutionEndTime(id, new Date());
        }

        public void jobFailed(Throwable failureReason) {
            try {
                jobManager.setFailureReason(id, failureReason);
            } catch (JobExecutionException e) {
                throw new Error(e);
            } finally {
                jobManager.setFailure(id, true);
                jobManager.setJobExecutionEndTime(id, new Date());
            }
        }
    }
}
