package com.zl.tutor.service.impl;

import com.zl.tutor.dao.UserDao;
import com.zl.tutor.domain.User;
import com.zl.tutor.service.UserService;
import com.zl.tutor.web.dto.UserDto;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import java.util.List;

/**
 * User: mkk
 * Date: 11-8-4
 * Time: 下午10:57
 */
public class UserServiceImpl implements UserService, InitializingBean {

    private UserDao userDao;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userDao);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDto queryAvailableUsers(int start, int rows) {
        UserDto dto = new UserDto();
        List<User> list = userDao.findByPage(start, rows);
        dto.setDatas(list);
        return dto;
    }
}
