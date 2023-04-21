package com.birdpiss.commons.dao.interfaces;

import java.util.List;
import com.birdpiss.commons.domain.User;

/**
 * The Interface UserDao.
 *
 * @author mark
 */
public interface UserDao {

    /**
	 * Gets the all users.
	 *
	 * @return the all users
	 */
    public List<User> getAllUsers();

    /**
	 * Gets the user.
	 *
	 * @param userId the user id
	 *
	 * @return the user
	 */
    public User getUser(Integer userId);
}
