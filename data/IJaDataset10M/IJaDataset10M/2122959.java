package li.kaeppe.travel.tracker.service;

import java.util.List;
import li.kaeppe.travel.tracker.dao.UserDao;
import li.kaeppe.travel.tracker.domain.User;
import li.kaeppe.travel.tracker.domain.UserRole;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserServiceImpl implements UserService {

    UserDao userDao;

    public void deleteUser(User user) {
        this.getUserDao().deleteUser(user);
    }

    public List<User> getAllUsers() {
        return this.getUserDao().getAllUsers();
    }

    public User saveUser(User user) {
        return this.getUserDao().saveUser(user);
    }

    public List<UserRole> getAllRoles() {
        return this.getUserDao().getAllRoles();
    }

    public UserRole getRoleById(long id) {
        return this.getUserDao().getRoleById(id);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
