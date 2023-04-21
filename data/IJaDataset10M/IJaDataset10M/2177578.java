package it.inrich.enterprise.users;

import junit.framework.*;
import it.inrich.enterprise.users.User;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author riccardo
 */
public class UsersListTest extends TestCase {

    public UsersListTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(UsersListTest.class);
        return suite;
    }

    /**
     * Test of addUser method, of class it.inrich.enterprise.users.UsersList.
     */
    public void testAddUser() {
        System.out.println("addUser");
        User user = null;
        UsersList instance = new UsersList();
        instance.addUser(user);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUser method, of class it.inrich.enterprise.users.UsersList.
     */
    public void testGetUser() {
        System.out.println("getUser");
        String uname = "";
        String pwd = "";
        UsersList instance = new UsersList();
        User expResult = null;
        User result = instance.getUser(uname, pwd);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of authenticate method, of class it.inrich.enterprise.users.UsersList.
     */
    public void testAuthenticate() {
        System.out.println("authenticate");
        String name = "";
        String pass = "";
        UsersList instance = new UsersList();
        User expResult = null;
        User result = instance.authenticate(name, pass);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}
