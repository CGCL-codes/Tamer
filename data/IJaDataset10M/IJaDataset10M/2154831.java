package com.casheen.spring.annotation.service;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.casheen.spring.annotation.model.User;
import com.casheen.spring.annotation.persistence.UserDao;

@Service
public class UserManagerImpl implements UserManager {

    private UserDao userDao;

    @Resource
    public void setUserDao(UserDao userDao) {
        System.out.println("Inject the userDaoImpl.");
        this.userDao = userDao;
    }

    public User get(Integer id) {
        return userDao.get(id);
    }

    public List<User> list() {
        return userDao.list();
    }

    public void saveOrUpdate(User user) {
        userDao.saveOrUpdate(user);
    }
}
