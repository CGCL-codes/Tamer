package org.jinvent.service.businesslogic.impl;

import org.jinvent.persistence.dao.IUserDao;
import org.jinvent.persistence.entity.User;
import org.jinvent.service.businesslogic.ILoginService;
import org.ploin.utils.service.ISecureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService implements ILoginService {

    private ISecureService secureService;

    private IUserDao userDao;

    @Transactional
    public User getByLoginInformation(final String username, final String password) {
        String pass = secureService.generateDigest(password, null);
        User user = userDao.getByLoginInformation(username, pass);
        return user;
    }

    @Autowired
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setSecureService(ISecureService secureService) {
        this.secureService = secureService;
    }
}
