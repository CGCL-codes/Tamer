package com.cleangwt.showcase.server.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cleangwt.server.application.GenericBO;
import com.cleangwt.server.application.GenericDAO;
import com.cleangwt.showcase.server.dao.UserDAO;
import com.cleangwt.showcase.server.entity.User;

/**
 * @author Jess
 * @date 2011/12/2
 */
@Service
public class UserBO extends GenericBO<User> {

    @Autowired
    private UserDAO userDAO;

    @Override
    protected GenericDAO<User> getGenericDAO() {
        return userDAO;
    }
}
