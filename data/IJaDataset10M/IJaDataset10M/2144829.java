package allensoft.bug;

import java.rmi.*;
import java.net.*;
import java.util.*;
import allensoft.bug.server.*;
import allensoft.gui.*;
import allensoft.util.*;

/** Submits a bug report by connecting to a <code>BugServer</code> object using RMI. */
public class RMIBugSubmitter extends WizardBasedBugSubmitter {

    public RMIBugSubmitter(String sServerName) {
        m_sServerName = sServerName;
    }

    protected Wizard createWizard(BugReport bugReport) {
        return new RMIBugReportWizard(bugReport);
    }

    protected class RMIBugReportWizard extends SubmitBugReportWizard {

        public RMIBugReportWizard(BugReport bugReport) {
            super(bugReport);
        }

        protected void submit() throws BugSubmissionException {
            try {
                BugServer server = (BugServer) Naming.lookup(m_sServerName);
                server.processBugReport(getBugReport());
            } catch (MalformedURLException e) {
                throw new BugSubmissionException(e);
            } catch (NotBoundException e) {
                throw new BugSubmissionException(e);
            } catch (RemoteException e) {
                throw new BugSubmissionException(e);
            } catch (NestingException e) {
                throw new BugSubmissionException(e);
            }
        }
    }

    private String m_sServerName;
}
