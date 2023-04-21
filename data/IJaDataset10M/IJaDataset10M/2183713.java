package com.ncs.crm.server.service;

import com.isomorphic.datasource.DSRequest;
import com.isomorphic.datasource.DSResponse;
import com.ncs.crm.client.entity.User;
import com.ncs.crm.server.dao.UserDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Service("crmUserService")
public class UserService {

    Logger log = Logger.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public DSResponse fetch(DSRequest dsRequest, HttpSession httpSession) {
        log.info("运行 fetch()...");
        return userDao.fetch(dsRequest);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @SuppressWarnings("unchecked")
    public DSResponse update(DSRequest dsRequest, Map newValues) {
        log.info("运行 update()...");
        return userDao.update(dsRequest, newValues);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DSResponse add(DSRequest dsRequest, User user, HttpSession httpSession) {
        log.info("运行 add()...");
        log.info("add()运行结束...");
        return userDao.add(dsRequest, user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User remove(User user) {
        log.info("运行 remove()...");
        return userDao.remove(user);
    }
}
