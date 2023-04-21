package org.icenigrid.gridsam.core.plugin.connector.drmaa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.events.RegistryShutdownListener;
import org.ggf.drmaa.DrmaaException;
import org.ggf.drmaa.NoActiveSessionException;
import org.ggf.drmaa.Session;
import org.icenigrid.gridsam.core.ConfigurationException;
import org.icenigrid.gridsam.core.ControlException;
import org.icenigrid.gridsam.core.JobManagerException;
import org.icenigrid.gridsam.core.plugin.DRMConnector;
import org.icenigrid.gridsam.core.plugin.JobContext;
import org.icenigrid.gridsam.core.plugin.connector.common.AbstractDRMConnectorManager;
import org.icenigrid.gridsam.core.plugin.connector.common.MultiStageDRMConnector;

/**
 * DRMConnectorBuilder implementation for the shell-based condor integration
 */
public class DrmaaDRMConnectorManager extends AbstractDRMConnectorManager implements RegistryShutdownListener {

    /**
     * logger
     */
    private static Log sLog = LogFactory.getLog(DrmaaDRMConnectorManager.class);

    /**
     * static session
     */
    private static Session sSession;

    static {
        try {
            getDrmaaSession();
        } catch (DrmaaException xEx) {
            sLog.fatal("DRMAA DRMConnector fails to initialise. Please consult the log for advice.");
        }
    }

    /**
     * get the singleton Drmaa Session
     * 
     * @throws DrmaaException
     *             if the Drmaa Session cannot be retrieved
     */
    public static final synchronized Session getDrmaaSession() throws DrmaaException {
        if (sSession == null) {
            try {
                sSession = org.ggf.drmaa.SessionFactory.getFactory().getSession();
                sLog.info("DRMAA session created");
                sSession.init(null);
                sLog.info("DRMAA session initialised");
                sLog.info("DRMAA system = " + sSession.getContact() + " - " + sSession.getVersion() + " - " + sSession.getDrmSystem() + " - " + sSession.getDrmaaImplementation());
            } catch (DrmaaException xEx) {
                sLog.debug("failed to initialise DRMAA subsystem - " + xEx.getMessage(), xEx);
                sLog.error("failed to initialise DRMAA subsystem - " + xEx.getMessage());
                sSession = null;
            } catch (UnsatisfiedLinkError xEx) {
                sLog.debug("failed to load the DRMAA library - " + xEx.getMessage(), xEx);
                sLog.error("failed to load the DRMAA library - " + xEx.getMessage());
                sSession = null;
            } catch (Throwable xEx) {
                sLog.debug("unknown error when loading the DRMAA library - " + xEx.getMessage(), xEx);
                sLog.error("unknown error when loading the DRMAA library - " + xEx.getMessage());
                sSession = null;
            }
            if (sSession == null) {
                throw new NoActiveSessionException("DRMAA sub-system is not configured properly. Please contact your system administrator.");
            }
        }
        return sSession;
    }

    /**
     * called by the Job Manager to terminate the job previously handled by this
     * DRMConnectorManager
     * 
     * @param pJobContext
     *            the job
     * @throws org.icenigrid.gridsam.core.ControlException
     *             the job cannot be terminated
     */
    public void terminate(JobContext pJobContext) throws ControlException {
        try {
            MultiStageDRMConnector xRemainingStages = new MultiStageDRMConnector();
            xRemainingStages.setNext("drmaa.TerminateStage", MultiStageDRMConnector.ALWAYS_PROGRESS_CRITERIA).setNext("gridsam.TerminalStage", MultiStageDRMConnector.ALWAYS_PROGRESS_CRITERIA).setNext("fork.FileSystemCleanupStage", MultiStageDRMConnector.ALWAYS_PROGRESS_CRITERIA);
            pJobContext.schedule(xRemainingStages);
        } catch (JobManagerException xEx) {
            throw new ControlException("unable to schedule 'gridsam.TerminalStage' " + xEx.getMessage(), xEx);
        }
    }

    /**
     * handle shutdown
     */
    public synchronized void registryDidShutdown() {
        if (sSession != null) {
            try {
                sSession.exit();
            } catch (Throwable xEx) {
                sLog.warn("error occurred while exiting the DRMAA session - " + xEx.getMessage());
            }
            sSession = null;
        }
        sLog.info("DrmaaDRMConnectorManager shutdown completed");
    }

    /**
     * return a DRMConnector instance based on the configuration
     * 
     * @return DRMConnector instance that can be used for launching job
     * 
     * @throws org.icenigrid.gridsam.core.ConfigurationException
     *             if the instance cannot be built
     */
    public DRMConnector getDRMConnector() throws ConfigurationException {
        return getJobManagerContext().getDRMConnector("drmaa.DRMConnector");
    }
}
