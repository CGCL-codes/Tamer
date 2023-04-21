package org.icenigrid.gridsam.core.plugin.connector.globus;

import java.net.MalformedURLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gram.Gram;
import org.globus.gram.GramException;
import org.globus.gram.GramJob;
import org.icenigrid.gridsam.core.JobManagerException;
import org.icenigrid.gridsam.core.JobState;
import org.icenigrid.gridsam.core.SubmissionException;
import org.icenigrid.gridsam.core.plugin.JobContext;
import org.icenigrid.gridsam.core.plugin.connector.common.TerminalStage;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

/**
 * DRMConnector for terminating a GRAM job
 */
public class GRAMTerminateStage extends TerminalStage {

    private static final Log sLog = LogFactory.getLog(GRAMTerminateStage.class);

    /**
     * handle job termination. This implementation performs nothing. Sub-class
     * can provide cleanup task by overriding this method
     * 
     * @param pContext
     *            the job to be terminated
     */
    public void terminate(JobContext pContext) {
        try {
            try {
                String xGlobusID = (String) pContext.getJobInstance().getProperties().get("urn:gridsam:globus:id");
                if (xGlobusID == null) {
                    return;
                }
                if (pContext.getJobInstance().wasInStage(JobState.EXECUTED)) {
                    return;
                }
                GSSCredential xCredential = null;
                try {
                    xCredential = GlobusCredentialSupport.getCredential(pContext);
                } catch (JobManagerException xEx) {
                    throw new SubmissionException("unable to load credential to query job: " + xEx.getMessage(), xEx);
                }
                GramJob xJob = new GramJob((String) pContext.getJobInstance().getProperties().get("urn:gridsam:globus:rsl"));
                xJob.setCredentials(xCredential);
                try {
                    xJob.setID(xGlobusID);
                } catch (MalformedURLException xEx) {
                    throw new SubmissionException("unable to rebind to job: " + xEx.getMessage(), xEx);
                }
                Gram.cancel(xJob);
            } catch (GSSException xEx) {
                throw new SubmissionException("the credential is not allowed to cancel the job - " + xEx.getMessage(), xEx);
            } catch (GramException xEx) {
                throw new SubmissionException("the job cannot be cancelled - " + xEx.getMessage(), xEx);
            }
        } catch (SubmissionException xEx) {
            pContext.getLog().debug(xEx.getMessage(), xEx);
            pContext.getLog().warn(xEx.getMessage());
        }
    }
}
