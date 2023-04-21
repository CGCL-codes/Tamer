package org.oss.owasp.ca.dao;

import java.util.Iterator;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.hibernate.Transaction;
import org.oss.owasp.crypto.KeyStoreTest;
import org.owasp.oss.OSSMain;
import org.owasp.oss.TestBase;
import org.owasp.oss.ca.dao.DaoFactory;
import org.owasp.oss.ca.dao.UserDao;
import org.owasp.oss.ca.model.User;

public class DaoTest extends TestBase {

    public static Test suite() {
        return new TestSuite(DaoTest.class);
    }

    private static void listUsers() {
        DaoFactory factory = DaoFactory.getInstance();
        UserDao userDao = factory.getUserDao();
        List<User> users = userDao.loadIssuers();
        Iterator<User> iter = users.iterator();
        while (iter.hasNext()) {
            User user = iter.next();
            System.out.println("ID: " + user.getId() + " name: " + user.getUserName() + " password: " + user.getPassword() + "Issuer:" + user.isIssuer());
        }
    }

    public void testUserDao() {
        Long id = null;
        DaoFactory factory = DaoFactory.getInstance();
        UserDao userDao = factory.getUserDao();
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setPassword("password1");
        user1.setUserName("userName1");
        user1.setFirstName("firstName1");
        user1.setLastName("lastName1");
        user1.setResourceName("root/user1");
        user1.setResourcePath("root");
        user1.setEmailAddress("email1@address.com");
        user1.setIssuer(true);
        user2.setPassword("password2");
        user2.setUserName("userName2");
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setResourceName("root/user2");
        user2.setResourcePath("root");
        user2.setEmailAddress("email2@address.com");
        user2.setIssuer(true);
        user3.setPassword("password3");
        user3.setUserName("userName3");
        user3.setFirstName("firstName3");
        user3.setLastName("lastName3");
        user3.setResourceName("root/user3");
        user3.setResourcePath("root");
        user3.setEmailAddress("email3@address.com");
        Transaction tx = factory.getSession().beginTransaction();
        Long id1 = userDao.store(user1).getId();
        Long id2 = userDao.store(user2).getId();
        Long id3 = userDao.store(user3).getId();
        tx.commit();
        User user1t = userDao.get(id1);
        User user2t = userDao.get(id2);
        User user3t = userDao.get(id3);
        assertEquals(user1.getUserName(), user1t.getUserName());
        assertEquals(user1.getFirstName(), user1t.getFirstName());
        assertEquals(user1.getLastName(), user1t.getLastName());
        assertEquals(user1.getPassword(), user1t.getPassword());
        assertEquals(user1.getResourceName(), user1t.getResourceName());
        assertEquals(user1.getResourcePath(), user1t.getResourcePath());
        assertEquals(user1.getEmailAddress(), user1t.getEmailAddress());
        assertNotNull(user2t);
        assertNotNull(user2t);
        List<User> users = userDao.getAll();
        assertTrue(users.size() == 3);
        List<User> issuers = userDao.loadIssuers();
        assertTrue(issuers.size() == 2);
    }

    public void testLoadSubEntities() {
        DaoFactory factory = DaoFactory.getInstance();
        UserDao userDao = factory.getUserDao();
        List<User> users = userDao.loadSubEntities("root");
        assertNotNull(users);
        assertTrue(users.get(0).getUserName().equals("userName1"));
        assertTrue(users.get(1).getUserName().equals("userName2"));
        assertTrue(users.get(2).getUserName().equals("userName3"));
    }

    public void testDeleteEntity() {
        DaoFactory factory = DaoFactory.getInstance();
        UserDao userDao = factory.getUserDao();
        User user1 = userDao.loadByUserName("userName1");
        Transaction tx = factory.getSession().beginTransaction();
        userDao.delete(user1);
        tx.commit();
    }
}
