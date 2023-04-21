package tudu.domain.dao;

import tudu.domain.model.User;

/**
 * DAO for the User table.
 * 
 * @author Julien Dubois
 */
public interface UserDAO {

    /**
     * Get a specific user.
     * 
     * @param login
     *            The user login
     * @return A user
     */
    User getUser(String login);

    /**
     * Update a user.
     * 
     * @param user
     *            The user value object
     */
    void updateUser(User user);

    /**
     * Save a user.
     * 
     * @param user
     *            The user value object
     */
    void saveUser(User user);
}
