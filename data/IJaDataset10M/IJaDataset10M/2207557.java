package net.zcarioca.zscrum.domain.dao.impl;

import static net.zcarioca.zscrum.domain.EntityAssert.assertEntityEquals;
import static org.junit.Assert.assertEquals;
import java.util.Date;
import net.zcarioca.zscrum.domain.DBCleaner;
import net.zcarioca.zscrum.domain.SystemRole;
import net.zcarioca.zscrum.domain.User;
import net.zcarioca.zscrum.domain.UserSession;
import net.zcarioca.zscrum.domain.dao.UserDAO;
import net.zcarioca.zscrum.domain.dao.UserSessionDAO;
import net.zcarioca.zscrum.domain.dao.exceptions.DAOReadException;
import net.zcarioca.zscrum.domain.dao.exceptions.DAOWriteException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the {@link UserSessionDAO}.
 * 
 * 
 * @author zcarioca
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/app-config.xml")
public class UserSessionDAOImplTest {

    @Autowired
    private UserDAO userDao;

    @Autowired
    private UserSessionDAO userSessionDao;

    private User user;

    @Autowired
    private DBCleaner cleaner;

    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setEmailAddress("admin@test.com");
        user.setFirstname("test");
        user.setLastname("tester");
        user.setPassword("1234");
        user.setSystemRole(SystemRole.USER);
        user.setUsername("username1");
        user.setActive(true);
        userDao.save(user);
    }

    @After
    public void tearDown() throws Exception {
        cleaner.cleanUsers();
    }

    @Test(expected = DAOWriteException.class)
    public void testSaveWithBadData() throws Exception {
        UserSession session = new UserSession(user);
        session.setUser(null);
        userSessionDao.save(session);
    }

    @Test
    public void testSave() throws Exception {
        assertEquals(0, userSessionDao.loadAll().size());
        UserSession session = new UserSession(user);
        userSessionDao.save(session);
        assertEquals(1, userSessionDao.loadAll().size());
    }

    @Test(expected = DAOReadException.class)
    public void testLoadById() throws Exception {
        userSessionDao.loadById(1);
    }

    @Test
    public void testLoadBySessionId() throws Exception {
        UserSession session = new UserSession(user);
        userSessionDao.save(session);
        userSessionDao.save(new UserSession(user));
        assertEquals(2, userSessionDao.loadAll().size());
        assertEntityEquals(session, userSessionDao.loadBySessionId(session.getSessionId()));
    }

    @Test
    public void testLoadAllActive() throws Exception {
        userSessionDao.save(new UserSession(user));
        userSessionDao.save(new UserSession(user));
        userSessionDao.save(new UserSession(user));
        UserSession session = new UserSession(user);
        session.setEndTime(new Date(System.currentTimeMillis() - 100));
        userSessionDao.save(session);
        assertEquals(4, userSessionDao.loadAll().size());
        assertEquals(String.format("Found %d sessions", userSessionDao.loadAllActive().size()), 3, userSessionDao.loadAllActive().size());
    }
}
