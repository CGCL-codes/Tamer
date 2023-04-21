package org.dataminx.dts.wn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionException;

/**
 * Stratey to restart job immediately.
 * @author hnguyen
 */
public class ImmediateJobRestartStrategy implements JobRestartStrategy {

    /** Internal application logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ImmediateJobRestartStrategy.class);

    /** internal restart implementation */
    private final WorkerNodeManager mWorkerNodeManager;

    public ImmediateJobRestartStrategy(WorkerNodeManager manager) {
        this.mWorkerNodeManager = manager;
    }

    @Override
    public void restartJob(final String jobName) throws Exception {
        Long instanceId;
        try {
            instanceId = mWorkerNodeManager.getJobInstances(jobName, 0, 1).get(0);
            final Long executionId = mWorkerNodeManager.getExecutions(instanceId).get(0);
            mWorkerNodeManager.restart(executionId);
        } catch (JobExecutionException ex) {
            LOG.debug("Error restarting job " + jobName + " immediately", ex);
            throw ex;
        }
    }
}
