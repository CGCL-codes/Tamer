package gnu.testlet.java2.util.logging.Handler;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;
import java.util.logging.ErrorManager;

/**
 * @author <a href="mailto:brawer@dandelis.ch">Sascha Brawer</a>
 */
public class getErrorManager implements Testlet {

    private final TestSecurityManager sec = new TestSecurityManager();

    private final TestHandler handler = new TestHandler();

    private final ErrorManager mgr = new ErrorManager();

    public void test(TestHarness th) {
        Throwable caught;
        sec.install();
        try {
            sec.setGrantLoggingControl(false);
            caught = null;
            try {
                handler.getErrorManager();
            } catch (Exception ex) {
                caught = ex;
            }
            th.check(caught instanceof SecurityException);
            sec.setGrantLoggingControl(true);
            caught = null;
            try {
                handler.setErrorManager(mgr);
            } catch (Exception ex) {
                caught = ex;
            }
            th.check(caught, null);
            th.check(handler.getErrorManager() == mgr);
        } finally {
            sec.uninstall();
        }
    }
}
