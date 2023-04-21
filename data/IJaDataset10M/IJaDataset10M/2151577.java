package org.icenigrid.gridsam.core.plugin.connector.broker;

import java.net.URI;
import org.icenigrid.gridsam.core.plugin.JobContext;

/**
 * Interface defining a pluggable strategy to make brokering decision
 */
public interface BrokeringStrategy {

    /**
     * make a decision on which GridSAM endpoint to use to submit the job
     * 
     * @param pJob
     *            the job to make a decision on
     * @return URI the endpoint address of the selected GridSAM instance
     */
    public URI decide(JobContext pJob) throws BrokeringDecisionException;
}
