package net.coolcoders.showcase.service;

import net.coolcoders.showcase.dao.UserDao;
import net.coolcoders.showcase.model.User;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 *
 * @author andreas
 */
@Stateless
public class UserService extends AbstractGenericService<User, String> {

    @EJB
    private UserDao userDao;

    public UserService() {
    }

    @PostConstruct
    public void init() {
        abstractGenericDao = userDao;
    }

    public Long countUsersYouFollow(String userId) {
        return userDao.countUsersYouFollow(userId);
    }

    public List<User> listUsersYouFollow(String userId) {
        return userDao.listUsersYouFollow(userId);
    }
}
