package org.webical.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.webical.User;
import org.webical.dao.DaoException;
import org.webical.dao.UserDao;
import org.webical.manager.UserManager;
import org.webical.manager.WebicalException;

public class UserManagerImpl implements UserManager, InitializingBean {

    private static final String USER_MANAGER_IMPL_NEEDS_USER_DAO = "UserManagerImpl needs UserDao";

    private static Log log = LogFactory.getLog(UserManagerImpl.class);

    private UserDao userDao;

    public User getUser(String userId) throws WebicalException {
        try {
            return userDao.getUser(userId);
        } catch (DaoException e) {
            throw new WebicalException("Could not retrieve User", e);
        }
    }

    public void removeUser(User user) throws WebicalException {
        try {
            userDao.removeUser(user);
        } catch (DaoException e) {
            throw new WebicalException("Could not remove User", e);
        }
    }

    public void storeUser(User user) throws WebicalException {
        try {
            userDao.storeUser(user);
        } catch (DaoException e) {
            throw new WebicalException("Could not store an User", e);
        }
    }

    public void afterPropertiesSet() throws Exception {
        if (userDao == null) {
            throw new ExceptionInInitializerError(USER_MANAGER_IMPL_NEEDS_USER_DAO);
        }
        if (log.isDebugEnabled()) {
            log.debug("Class of UserDao set by Spring: " + userDao.getClass());
        }
    }

    /**
	 * @return userDao
	 */
    public UserDao getUserDao() {
        return userDao;
    }

    /**
	 * @param userDao
	 */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
