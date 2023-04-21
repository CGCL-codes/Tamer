package org.systemsbiology.apps.gui.server.provider.user;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.systemsbiology.apps.gui.domain.User;
import org.systemsbiology.apps.gui.server.dao.DAOException;
import org.systemsbiology.apps.gui.server.dao.UserDAO;

/**
 * Singleton class to provide information on the Users
 * @author Mark Christiansen
 *
 * @see User
 */
public class UserInfoProvider implements IUserInfoProvider {

    private static final Logger log = Logger.getLogger(UserInfoProvider.class.getName());

    private static UserInfoProvider instance = new UserInfoProvider();

    private UserInfoProvider() {
    }

    /**
	 * @return singleton instance of user information provider
	 */
    public static IUserInfoProvider instance() {
        return instance;
    }

    public User getUser(String userLoginName) {
        User user = null;
        try {
            UserDAO userDao = new UserDAO();
            log.debug("UserInfoProvider - Getting user: " + userLoginName);
            user = userDao.get(userLoginName);
            log.debug("Done getting user");
        } catch (DAOException de) {
            log.error("UserInfoProvider - getUser DAOException " + de.getMessage());
            log.error(de.getCause());
        }
        return user;
    }

    @Override
    public boolean changeUserPassword(User user, String newPassword) {
        boolean passwordChanged = false;
        try {
            UserDAO userDao = new UserDAO();
            log.debug("UserInfoProvider -changepassword-  Getting user: " + user.getUserName());
            user = userDao.get(user.getUserName());
            passwordChanged = true;
            log.debug("Done getting user");
            String encrypted = PasswordEncrypter.encryptPassword(newPassword);
            user.setPassword(encrypted);
            userDao.save(user);
        } catch (DAOException de) {
            log.error("UserInfoProvider - DAOException " + de.getMessage());
            log.error(de.getCause());
        }
        return passwordChanged;
    }

    public User authenticateUser(String userLoginName, String password) {
        log.debug("UserInfoProvider - Authenticating user entry: " + userLoginName);
        User user = null;
        try {
            UserDAO userDao = new UserDAO();
            user = userDao.get(userLoginName);
        } catch (DAOException de) {
            log.error("UserInfoProvider - authenticateUser DAOException " + de.getMessage());
            log.error(de.getCause());
        }
        log.debug("UserInfoProvider - Done getting user");
        if (user == null) {
            String encrypted = PasswordEncrypter.encryptPassword(password);
            log.debug("UserInfoProvider - Encrypted password is: " + encrypted);
            log.debug("UserInfoProvider - User is null! returning...");
            return null;
        }
        log.debug("UserInfoProvider - User retrieved " + user.getUserName());
        String encrypted = PasswordEncrypter.encryptPassword(password);
        log.debug("UserInfoProvider - Encrypted password is: " + encrypted);
        if (user.getPassword().equals(encrypted)) return user;
        return null;
    }

    @Override
    public boolean validatePassword(User user, String password) {
        return (authenticateUser(user.getUserName(), password) != null);
    }

    @Override
    public ArrayList<User> getUsers() {
        ArrayList<User> users = null;
        try {
            UserDAO userDao = new UserDAO();
            log.debug("UserInfoProvider - Getting all users: ");
            users = userDao.getAll();
            log.debug("Done getting all users");
        } catch (DAOException de) {
            log.error("UserInfoProvider - DAOException " + de.getMessage());
            log.error(de.getCause());
        }
        return users;
    }

    @Override
    public boolean addUser(User user) {
        boolean userAdded = false;
        try {
            UserDAO userDao = new UserDAO();
            log.debug("UserInfoProvider - Adding user: " + user.getUserName());
            user = userDao.create(user);
            userAdded = true;
            log.debug("Done creating user");
        } catch (DAOException de) {
            log.error("UserInfoProvider - DAOException " + de.getMessage());
            log.error(de.getCause());
        }
        return userAdded;
    }

    @Override
    public boolean deleteUser(User userLoginName) {
        boolean isDeleted = false;
        try {
            UserDAO userDao = new UserDAO();
            log.debug("UserInfoProvider - Deleting user: " + userLoginName);
            userDao.delete(userLoginName);
            log.debug("Done deleting user");
            isDeleted = true;
        } catch (DAOException de) {
            log.error("UserInfoProvider - deleteUser DAOException " + de.getMessage());
            log.error(de.getCause());
        }
        return isDeleted;
    }
}
