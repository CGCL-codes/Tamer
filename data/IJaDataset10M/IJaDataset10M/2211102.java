package protoj.system.internal.acme;

import java.io.File;
import org.junit.Assert;
import protoj.shell.ArgRunnable;
import protoj.shell.ProjectLayout;
import protoj.system.internal.ScriptSession;

/**
 * These assertions trigger when both the 'junit' and 'test' commands are
 * executed, which are aliases of each other. So they should both do the same
 * thing, which is create a test report containing a success message.
 * 
 * @author Ashley Williams
 * 
 */
final class AssertJunit implements ArgRunnable<ScriptSession> {

    private final AcmeSession session;

    public AssertJunit(AcmeSession acmeSession) {
        this.session = acmeSession;
    }

    public void run(ScriptSession projectSession) {
        ProjectLayout acmeLayout = session.getAcmeProject().getLayout();
        String log = session.getAcmeProject().loadLog();
        File testReport = new File(acmeLayout.getJunitReportsDir(), "TEST-org.acme.SampleTest.txt");
        Assert.assertTrue(testReport.exists());
        Assert.assertTrue(log.contains("junit"));
        Assert.assertTrue(projectSession.getCurrentExec().isSuccess());
        acmeLayout.getLogFile().delete();
    }
}
