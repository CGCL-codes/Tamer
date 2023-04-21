package it.inrich.enterprise.users;

import junit.framework.*;
import javax.ejb.Local;

/**
 *
 * @author riccardo
 */
public class UsersManagerLocalTest extends TestCase {

    public UsersManagerLocalTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(UsersManagerLocalTest.class);
        return suite;
    }

    /**
     * Test of getUserFromUID method, of class it.inrich.enterprise.users.UsersManagerLocal.
     */
    public void testGetUserFromUID() {
        System.out.println("getUserFromUID");
        int uid = 0;
        UsersManagerLocal instance = new UsersManagerLocal();
        User expResult = null;
        User result = instance.getUserFromUID(uid);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Generated implementation of abstract class it.inrich.enterprise.users.UsersManagerLocal. Please fill dummy bodies of generated methods.
     */
    private class UsersManagerLocalImpl implements UsersManagerLocal {

        public it.inrich.enterprise.users.User getUserFromUID(int uid) {
            return null;
        }
    }
}
