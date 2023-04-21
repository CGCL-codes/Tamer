package org.oss.owasp.ca;

import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.owasp.oss.TestBase;
import org.owasp.oss.ca.UserManager;
import org.owasp.oss.ca.model.User;
import org.owasp.oss.httpserver.OSSHttpServer;

public class UserManagerTest extends TestBase {

    protected void setUp() throws Exception {
        super.setUp();
        OSSHttpServer.getInstance().start();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        OSSHttpServer.getInstance().stop();
    }

    public static Test suite() {
        return new TestSuite(UserManagerTest.class);
    }

    public void testStoreDeleteUser() {
        UserManager um = UserManager.getInstance();
        User test1 = new User();
        test1.setUserName("test1");
        test1.setResourceName("test1");
        test1.setResourcePath("");
        um.storeUser(test1);
        User test2 = um.getUser("test1");
        assertEquals(test1.getUserName(), test2.getUserName());
        assertEquals(test1.getResourceName(), test2.getResourceName());
        um.deleteUser(test2);
    }

    public void testStoreDeleteUserByName() {
        UserManager um = UserManager.getInstance();
        User test1 = new User();
        test1.setUserName("test1");
        test1.setResourceName("test1");
        test1.setResourcePath("");
        um.storeUser(test1);
        User test2 = um.getUser("test1");
        assertEquals(test1.getUserName(), test2.getUserName());
        assertEquals(test1.getResourceName(), test2.getResourceName());
        int ret = um.deleteUserByName("test1");
        assertEquals(1, ret);
    }

    public void testGetAllUserInPath() {
        UserManager um = UserManager.getInstance();
        User test1 = new User();
        test1.setUserName("test1");
        test1.setResourceName("root/test1");
        test1.setResourcePath("root");
        um.storeUser(test1);
        User test2 = new User();
        test2.setUserName("test2");
        test2.setResourceName("root/test1/test2");
        test2.setResourcePath("root");
        um.storeUser(test2);
        List<User> users = um.getAllUsersInPath("root/test1/test2");
        assertNotNull(users);
        assertTrue(users.get(0).getUserName().equals("root"));
        assertTrue(users.get(1).getUserName().equals(test1.getUserName()));
        assertTrue(users.get(2).getUserName().equals(test2.getUserName()));
    }
}
