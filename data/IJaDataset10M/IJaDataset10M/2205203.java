package com.fsj.spring.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.fsj.spring.dao.IUserDao;
import com.fsj.spring.model.TUser;
import com.fsj.spring.service.IUserService;
import com.fsj.spring.util.DataGridModel;

@Service("userService")
public class UserServiceImpl implements IUserService {

    private IUserDao userDao;

    public IUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    public Map<String, Object> getPageListByExemple(DataGridModel dgm, TUser user) throws Exception {
        return userDao.getPageListByExemple(dgm, user);
    }

    public Map<String, Object> getPageList(DataGridModel dgm, TUser user) throws Exception {
        return userDao.getPageList(dgm, user);
    }

    public TUser getUserById(int id) throws Exception {
        return userDao.findById(id);
    }

    public TUser getUserByName(String name) throws Exception {
        List<TUser> list = userDao.findByName(name);
        return list == null || list.size() == 0 ? null : (TUser) list.get(0);
    }

    public void addOrUpdate(TUser user) throws Exception {
        userDao.addOrUpdate(user);
    }

    public void deleteUsers(List<Integer> usersId) throws Exception {
        userDao.deleteUsers(usersId);
    }
}
