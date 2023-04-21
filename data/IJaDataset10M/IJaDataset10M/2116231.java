package com.winterwar.service.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.winterwar.base.User;
import com.winterwar.dao.UserDAO;
import com.winterwar.service.UserManager;

public class UserManagerImpl implements UserManager {

    public User get(String userID) {
        User user = dao.get(Integer.valueOf(userID));
        if (user == null) {
            log.warn("User " + userID + " does not exist in the database.");
        }
        return user;
    }

    public List getAll() {
        return dao.getAll();
    }

    public void remove(String userID) {
        dao.remove(Integer.valueOf(userID));
    }

    public User save(User user) {
        dao.save(user);
        return user;
    }

    public void setDAO(UserDAO dao) {
        this.dao = dao;
    }

    private UserDAO dao;

    private static Log log = LogFactory.getLog(UserManager.class);

    public User login(String username, String password) {
        List users = dao.getLogin(username, password);
        if (users == null || users.size() != 1 || !((User) users.get(0)).getPassword().equals(password)) {
            return null;
        } else {
            return (User) users.get(0);
        }
    }

    public User getByUserName(String username) {
        return dao.getByUserName(username);
    }

    @Override
    public List getByEmail(String eMail) {
        return dao.getByEmail(eMail);
    }

    public User getByFirstAndLast(String first, String last) {
        return dao.getByFirstAndLast(first, last);
    }
}
