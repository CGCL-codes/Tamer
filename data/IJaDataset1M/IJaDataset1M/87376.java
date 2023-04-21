package net.godcode.olivenotes.entities;

/**
 * UserDAO
 * 
 * @author Chris Lewis Jan 2, 2008 <chris@thegodcode.net>
 * @version $Id: UserDAO.java 11 2008-01-02 01:21:40Z burningodzilla $
 */
public interface UserDAO extends GenericDAO<User, Long> {

    /**
	 * Find a user by user name.
	 * @param userName The user name.
	 * @return The user or <code>null</code> if no user by this name exists.
	 */
    public User findByUserName(String userName);
}
