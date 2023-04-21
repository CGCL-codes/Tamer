package org.jaffa.security;

import org.jaffa.security.SecurityManager;
import java.security.PrivilegedAction;
import org.apache.log4j.Logger;
import java.security.AccessControlException;
import org.jaffa.security.PolicyManager;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author  paule
 * @version
 */
public class TestThread {

    /** Set up Logging for Log4J */
    private static Logger log = Logger.getLogger(TestThread.class);

    private static String FUNC_1 = "payroll.viewSalary";

    private static String FUNC_2 = "payroll.updateSalary";

    public void test1() {
        System.out.println("Start: Test 1...");
        System.out.println("Run some code protected by business function '" + FUNC_1 + "'");
        try {
            SecurityManager.runFunction(FUNC_1, new PrivilegedAction() {

                public Object run() {
                    System.out.println("*** Hello, I'm now running " + FUNC_1 + "");
                    return null;
                }
            });
        } catch (AccessControlException e) {
            System.out.println("*** Access Denied!");
        }
        System.out.println("End: Test 1...");
    }

    public void test2() {
        System.out.println("Start: Test 2...");
        System.out.println("Run some code protected by business function '" + FUNC_2 + "'");
        try {
            SecurityManager.runFunction(FUNC_2, new PrivilegedAction() {

                public Object run() {
                    System.out.println("*** Hello, I'm now running " + FUNC_2 + "");
                    return null;
                }
            });
        } catch (AccessControlException e) {
            System.out.println("*** Access Denied!");
        }
        System.out.println("End: Test 2...");
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        BasicConfigurator.configure();
        PolicyManager.printPolicy();
        try {
            HttpServletRequest request = new FakeRequest("PAUL");
            System.out.println("Conext: " + request.getUserPrincipal().getName());
            SecurityManager.runWithContext(request, new TestThread(), "test1", null);
            SecurityManager.runWithContext(request, new TestThread(), "test2", null);
        } catch (Exception e) {
            log.error("Test Failed", e);
        }
        try {
            HttpServletRequest request = new FakeRequest("MAHESH");
            System.out.println("Conext: " + request.getUserPrincipal().getName());
            SecurityManager.runWithContext(request, new TestThread(), "test1", null);
            SecurityManager.runWithContext(request, new TestThread(), "test2", null);
        } catch (Exception e) {
            log.error("Test Failed", e);
        }
    }
}
