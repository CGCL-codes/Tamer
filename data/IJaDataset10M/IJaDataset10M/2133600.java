package gnu.testlet.java2.util.logging.Handler;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;
import java.util.logging.ErrorManager;

/**
 * @author <a href="mailto:brawer@dandelis.ch">Sascha Brawer</a>
 */
public class reportError implements Testlet {

    private final TestSecurityManager sec = new TestSecurityManager();

    private final TestHandler handler = new TestHandler();

    private final TestErrorManager mgr = new TestErrorManager();

    private final Exception somex = new IllegalStateException();

    public void test(TestHarness th) {
        sec.install();
        try {
            sec.setGrantLoggingControl(true);
            handler.setErrorManager(mgr);
            sec.setGrantLoggingControl(false);
            handler.invokeReportError("foo", somex, ErrorManager.FLUSH_FAILURE);
            th.check(mgr.getLastMessage(), "foo");
            th.check(mgr.getLastException() == somex);
            th.check(mgr.getLastErrorCode(), ErrorManager.FLUSH_FAILURE);
            handler.invokeReportError(null, somex, ErrorManager.OPEN_FAILURE);
            th.check(mgr.getLastMessage(), null);
            th.check(mgr.getLastException() == somex);
            th.check(mgr.getLastErrorCode(), ErrorManager.OPEN_FAILURE);
            handler.invokeReportError(null, null, ErrorManager.CLOSE_FAILURE);
            th.check(mgr.getLastMessage(), null);
            th.check(mgr.getLastException(), null);
            th.check(mgr.getLastErrorCode(), ErrorManager.CLOSE_FAILURE);
            handler.invokeReportError("foobar", null, -12345);
            th.check(mgr.getLastMessage(), "foobar");
            th.check(mgr.getLastException(), null);
            th.check(mgr.getLastErrorCode(), -12345);
        } finally {
            sec.uninstall();
        }
    }
}
