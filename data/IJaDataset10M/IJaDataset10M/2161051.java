package com.ipolyglot.service.impl;

import com.ipolyglot.dao.UserDAOExt;
import com.ipolyglot.model.User;
import com.ipolyglot.service.UserManagerExt;

/**
 * @author mishag
 */
public class UserManagerExtImpl extends UserManagerImpl implements UserManagerExt {

    private UserDAOExt dao;

    /**
   * Set the DAO for communication with the data layer.
   * @param dao
   */
    public void setUserDAOExt(UserDAOExt dao) {
        this.dao = dao;
    }

    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        return dao.getUserByUsernameOrEmail(usernameOrEmail);
    }
}
