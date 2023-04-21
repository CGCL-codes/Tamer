package accesscontrol;

import hrm.Coder;
import hrm.Manager;
import junit.framework.TestCase;

/**
 * Tests the hrm system access control
 * All these tests should pass
 * @author Johan �stlund
 */
public class AccessControlTestCase extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AccessControlTestCase.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testIsManagerLoggedInFail() {
        Coder c1 = new Coder("Coder1", "codepwd1", 2000, 200, "ruby");
        AccessController.getAccessController().login(c1);
        assertFalse(AccessController.getAccessController().isManagerLoggedIn());
    }

    public void testIsManagerLoggedInSuccess() {
        Manager m1 = new Manager("Boss1", "bosspwd1", 5000, 160);
        AccessController.getAccessController().login(m1);
        assertTrue(AccessController.getAccessController().isManagerLoggedIn());
    }

    public void testAnonymousAccessFail() {
        AccessController.getAccessController().logout();
        Coder c1 = new Coder("Coder1", "codepwd1", 2000, 200, "ruby");
        try {
            c1.getSalary();
            fail("Anonymouns access not allowed");
        } catch (SecurityException se) {
        }
    }

    public void testAnonymousAccessSuccess() {
        AccessController.getAccessController().logout();
        Coder c1 = new Coder("Coder1", "codepwd1", 2000, 200, "ruby");
        Coder c2 = new Coder("Coder2", "codepwd2", 2500, 180, "java");
        AccessController.getAccessController().login(c1);
        try {
            c2.getSalary();
        } catch (SecurityException se) {
            fail("Access should be ok when logged in");
        } finally {
            AccessController.getAccessController().logout();
        }
    }

    public void testManagerSetSalarySuccess() {
        Coder c1 = new Coder("Coder1", "codepwd1", 2000, 200, "java");
        Manager m1 = new Manager("Boss1", "bosspwd1", 5000, 160);
        AccessController.getAccessController().login(m1);
        m1.add(c1);
        try {
            c1.setSalary(0);
        } catch (SecurityException se) {
            fail("The manager of an employee should be able" + " to set the salary of that employee");
        } finally {
            AccessController.getAccessController().logout();
        }
    }

    public void testManagerSetSalaryFail() {
        Coder c1 = new Coder("Coder1", "codepwd1", 2000, 200, "java");
        Manager m1 = new Manager("Boss1", "bosspwd1", 5000, 160);
        AccessController.getAccessController().login(m1);
        try {
            c1.setSalary(0);
            fail("Only the manager of an employee should be able" + " to set the salary of that employee");
        } catch (SecurityException se) {
        } finally {
            AccessController.getAccessController().logout();
        }
    }

    public void testAccessSecretFail() {
        AccessController.getAccessController().logout();
        Coder c1 = new Coder("Coder1", "codepwd1", 2000, 200, "ruby");
        Coder c2 = new Coder("Coder2", "codepwd2", 2500, 180, "java");
        try {
            ((SecretEmployee) c2).setSecret(true);
        } catch (ClassCastException cce) {
            fail("Employees should implement SecretEmployee");
        }
        AccessController.getAccessController().login(c1);
        try {
            c2.getSalary();
            fail("Should not be allowed to access secret employee");
        } catch (SecurityException se) {
        }
    }

    public void testAccessSecretSuccess() {
        AccessController.getAccessController().logout();
        Coder c1 = new Coder("Coder1", "codepwd1", 2000, 200, "ruby");
        Coder c2 = new Coder("Coder2", "codepwd2", 2500, 180, "java");
        AccessController.getAccessController().login(c1);
        try {
            c2.getSalary();
        } catch (SecurityException se) {
            fail("Accessing non secret employee should be ok");
        }
    }
}
