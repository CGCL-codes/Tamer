package de.hybris.storm.ejb;

import junit.framework.*;
import java.util.Hashtable;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;

/**
 * @version $Revision: 1.2 $, $Date: 2001/06/26 13:08:34 $
 */
public class UserTest extends StormTest {

    /**
	 * This constructor has to be there so the compiler won't bark.
	 * @param name the name of the test case when used inside a test suite
	 */
    public UserTest(String name) {
        super(name);
    }

    /**
	 * The fixture set up called before every test method.
	 */
    public void setUp() throws Exception {
        System.out.println("UserTest.setUp() begin");
        super.setUp();
        System.out.println("UserTest.setUp() end");
    }

    /**
	 * The fixture clean up called after every test method.
	 */
    public void tearDown() {
    }

    public void testCreate() throws Exception {
        System.out.println("UserTest.testCreate() begin");
        assertEquals(0, userHome.findAll().size());
        UserRemote user = userHome.create("user created by UserTest", "".getBytes());
        assertEquals(1, userHome.findAll().size());
        assertEquals(user, userHome.findByPrimaryKey("user created by UserTest"));
        user.remove();
        assertEquals(0, userHome.findAll().size());
        System.out.println("UserTest.testCreate() end");
    }

    public static Test suite() {
        return new TestSuite(UserTest.class);
    }
}
