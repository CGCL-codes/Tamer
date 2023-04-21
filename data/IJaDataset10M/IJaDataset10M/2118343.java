package org.codeandmagic.affected.service.impl;

import java.util.List;
import org.codeandmagic.affected.persistence.UserDao;
import org.codeandmagic.affected.service.UserService;
import org.codeandmagic.affected.svn.SvnException;
import org.codeandmagic.affected.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Service
public class UserServiceImpl implements UserService {

    private UserDao dao;

    @Required
    @Autowired
    public void setDao(UserDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public User get(String username) throws SvnException {
        return dao.get(username);
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return dao.getAll();
    }

    @Transactional(readOnly = false)
    public User create(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        save(user);
        return user;
    }

    @Transactional(readOnly = false)
    public boolean save(User user) {
        return dao.save(user);
    }

    @Transactional(readOnly = false)
    public boolean delete(String username) throws SvnException {
        return dao.delete(dao.get(username));
    }
}
