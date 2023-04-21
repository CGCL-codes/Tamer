package de.akquinet.jbosscc.needle.example.dao;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import de.akquinet.jbosscc.needle.example.model.User;

@Stateless
@Local(UserDeletionService.class)
public class UserDeletionServiceBean implements UserDeletionService {

    @EJB
    private UserDao _userDao;

    @Override
    public void deleteUser(final Long userId) {
        final User user = _userDao.load(userId);
        _userDao.delete(user);
    }
}
