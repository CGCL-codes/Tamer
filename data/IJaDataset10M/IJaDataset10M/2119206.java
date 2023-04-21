package org.icenigrid.gridsam.core.plugin.connector.common;

import org.icenigrid.gridsam.core.JobState;
import org.icenigrid.gridsam.core.plugin.DRMConnector;
import org.icenigrid.gridsam.core.plugin.JobContext;

/**
 * A DRMConnector that always fail.
 */
public class AlwaysFailDRMConnector implements DRMConnector {

    /**
     * serial version ID
     */
    private static final long serialVersionUID = 1321970954864024975L;

    /**
     * execute a job through this DRMConnector
     * 
     * @param pContext
     *            the context in which this DRMConnector executes
     */
    public void execute(JobContext pContext) {
        pContext.getJobInstance().advanceJobState(JobState.FAILED, "GridSAM is not accepting any submission");
    }
}
