package edu.vt.middleware.ldap;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

/**
 * Contains functions that run before and after all tests.
 *
 * @author  Middleware Services
 * @version  $Revision: 2165 $
 */
public class TestControl {

    /** DN to block on. */
    public static final String DN = "uid=1,ou=test,dc=vt,dc=edu";

    /** Attribute to block on. */
    public static final LdapAttribute ATTR_IDLE = new LdapAttribute("mail", "test-idle@vt.edu");

    /** Attribute to block on. */
    public static final LdapAttribute ATTR_RUNNING = new LdapAttribute("mail", "test-running@vt.edu");

    /** Time to wait before checking if lock is available. */
    public static final int WAIT_TIME = 5000;

    /**
   * Obtains the lock before running all tests.
   *
   * @param  ignoreLock  whether to check for the global test lock
   *
   * @throws Exception on test failure
   */
    @BeforeSuite(alwaysRun = true)
    @Parameters("ldapTestsIgnoreLock")
    public void setup(final String ignoreLock) throws Exception {
        if (!Boolean.valueOf(ignoreLock)) {
            final Connection conn = TestUtil.createSetupConnection();
            try {
                conn.open();
                final CompareOperation compare = new CompareOperation(conn);
                int i = 1;
                while (!compare.execute(new CompareRequest(DN, ATTR_IDLE)).getResult()) {
                    System.err.println("Waiting for test lock...");
                    Thread.sleep(WAIT_TIME * i++);
                }
                final ModifyOperation modify = new ModifyOperation(conn);
                modify.execute(new ModifyRequest(DN, new AttributeModification[] { new AttributeModification(AttributeModificationType.REPLACE, ATTR_RUNNING) }));
            } finally {
                conn.close();
            }
        }
    }

    /**
   * Releases the lock after running all tests.
   *
   * @throws Exception on test failure
   */
    @AfterSuite(alwaysRun = true)
    public void teardown() throws Exception {
        final Connection conn = TestUtil.createSetupConnection();
        try {
            conn.open();
            final ModifyOperation modify = new ModifyOperation(conn);
            modify.execute(new ModifyRequest(DN, new AttributeModification[] { new AttributeModification(AttributeModificationType.REPLACE, ATTR_IDLE) }));
        } finally {
            conn.close();
        }
    }
}
