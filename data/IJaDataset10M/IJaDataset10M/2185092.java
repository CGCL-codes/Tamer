package com.djb.brewery.dao.impl;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.djb.brewery.dao.LoginDao;
import com.djb.brewery.exception.AppException;
import com.djb.brewery.vo.User;

public class LoginDaoImpl extends HibernateDaoSupport implements LoginDao {

    public LoginDaoImpl() {
        super();
    }

    public User login(User user) throws AppException {
        Object[] args = new Object[] { user.getUserName(), user.getPassword() };
        try {
            user = (User) getHibernateTemplate().findByExample(user).get(0);
        } catch (RuntimeException e) {
            throw new AppException("Invalid user credentials.");
        }
        return user;
    }
}
