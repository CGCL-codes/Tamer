package de.fau.cs.dosis.model;

import java.util.List;

public interface UserManager {

    /**
	 * retrieves user by username
	 * return null if user does not exist
	 * @param username
	 * @return
	 */
    public User getUserByUsername(String username);

    /**
	 * retrieves user by email
	 * return null if user does not exist
	 * @param username
	 * @return
	 */
    public User getUserByEmail(String email);

    /**
	 * Returns a list with all users in the system.
	 * @return
	 */
    public List<User> getUserList();

    /**
	 * Retrieves user by sessionKey.
	 * Returns null if user does not exist
	 * @param sessionKey
	 * @return
	 */
    public User getUserBySessionKey(String sessionKey);

    /**
	 * stores given user
	 * @param user
	 * @return
	 * generated userid
	 */
    public int storeUser(User user);

    /**
	 * updates existing user
	 * @param user
	 */
    public void updateUser(User user);

    /**
	 * Assigns the sessionKey to the user.
	 * If the key is used by somebody else this key is hijacked
	 * @param user
	 * @param sessionKey
	 */
    public void assignSessionKey(User user, String sessionKey);

    /**
	 * Assigns the cookieKey to the given user. 
	 * @param user
	 * @param cookieKey
	 */
    public void assignCookieKey(User user, String cookieKey);

    /**
	 * Deletes the cookieKey from its owner.
	 * @param cookieKey
	 */
    public void deleteCookieKey(String cookieKey);

    /**
	 * Retrieves a user identified by its cookieKey
	 * @param cookieKey
	 * @return
	 */
    public User getUserByCookie(String cookieKey);

    /**
	 * Returns all users who have requested reviewer status
	 * @return
	 */
    public List<User> getRequestedReviewerStatusUsers();

    /** Delete user from database.
	 * TODO some hibernate guru please fix this
	 * WARNING: untested
	 * @param user
	 */
    @Deprecated
    public void deleteUser(User user);
}
