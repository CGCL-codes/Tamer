package com.google.code.facebookwebapp;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;
import com.google.code.facebookwebapp.model.dao.UserDao;

/**
 * Tests the integrity of all the functionality in the {@code UserDao}.
 * 
 * @author Cesar Arevalo
 * @since 0.2
 */
@ContextConfiguration(locations = { "classpath:ITApplicationContext.xml" })
public class UserDaoTest extends AbstractJUnit38SpringContextTests {

    private UserDao userDao;

    /**
	 * Test the creation of a user. This method will store a user in the
	 * database.
	 */
    public void testCreate() {
        String facebookUserId = "1";
        String sessionKey = "1";
        User user = userDao.createUser(facebookUserId, sessionKey);
        assertNotNull(user);
    }

    @Required
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
