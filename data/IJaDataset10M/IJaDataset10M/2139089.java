package goose.security.ori;

import goose.dao.UserDAO;
import goose.model.User;
import goose.security.impl.SecurityModule;
import ori.provider.IUserAuthenticator;

public class GooseUserAuthenticator implements IUserAuthenticator {

    private UserDAO userDAO;

    public boolean authenticate(Object id, String encodedPassword) {
        try {
            long userId = (Long) id;
            if (SecurityModule.ADMIN_ID.equals(userId) && SecurityModule.ADMIN_MD5_PASSWORD.equals(encodedPassword)) {
                return true;
            }
            if (SecurityModule.GUEST_ID.equals(userId) && SecurityModule.GUEST_MD5_PASSWORD.equals(encodedPassword)) {
                return true;
            }
            User user = userDAO.getById(userId);
            if (user != null && encodedPassword.equals(user.getPassword())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void logout(Object arg0) {
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
