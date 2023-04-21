package com.angel.architecture.services.impl;

import org.apache.log4j.Logger;
import com.angel.architecture.daos.UserDAO;
import com.angel.architecture.exceptions.BusinessException;
import com.angel.architecture.persistence.beans.User;
import com.angel.architecture.services.UserService;

public class UserServiceImpl extends GenericServiceImpl implements UserService {

    private static Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    public User findUniqueByName(String userName) {
        User user = this.getUserDAO().findUniqueByUserName(userName);
        if (user == null) {
            LOGGER.error("No se encontro el usuario [" + userName + "].");
            throw new BusinessException("No se encontro el usuario.");
        }
        return user;
    }

    protected UserDAO getUserDAO() {
        return (UserDAO) super.getGenericDAO();
    }
}
