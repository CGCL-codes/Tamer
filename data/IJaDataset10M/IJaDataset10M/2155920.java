package uk.org.brindy.jwebdoc.user;

import java.util.List;

/**
 * @author brindy
 */
public final class UserManager {

    private UserManager() {
    }

    /**
     * Login.
     * @param user the user to try and login
     * @return a User
     * @throws UserException if the login details are not valid
     */
    public static User login(User user) throws UserException {
        User loggedInUser = UserDAO.login(user);
        if (null == loggedInUser) {
            throw new UserException("user.login.failed");
        }
        return loggedInUser;
    }

    /** Get a list of users. 
     * @return a list of User objects
     */
    public static List getUserList() {
        return UserDAO.getUserList();
    }

    /**
     * Create a user.
     * @param user the user to create
     * @throws UserException if the user can't be created
     */
    public static void createUser(User user) throws UserException {
        if (!UserDAO.createUser(user)) {
            throw new UserException("user.create.failed");
        }
    }

    /**
     * Delete the specified user.
     * @param id the id of the user
     * @throws UserException if the user cannot be deleted
     */
    public static void deleteUser(int id) throws UserException {
        if (!UserDAO.deleteUser(id)) {
            throw new UserException("user.delete.failed");
        }
    }

    /**
     * Get a user by their id.
     * @param id the id
     * @return a User
     * @throws UserException if the id is invalid
     */
    public static User getUser(int id) throws UserException {
        User user;
        if (null == (user = UserDAO.getUser(id))) {
            throw new UserException("user.get.failed");
        }
        return user;
    }

    /**
     * Update the user.
     * @param user the user to update
     * @throws UserException if the user can't be updated
     */
    public static void updateUser(User user) throws UserException {
        if (user.getId() == 0) {
            throw new UserException("user.update.failed.invalid");
        }
        if (!UserDAO.updateUser(user)) {
            throw new UserException("user.update.failed");
        }
    }

    /** Update the user's password.
     * 
     * @param user the user who's password needs updating
     * @throws UserException if the user can't be updated
     */
    public static void updatePassword(User user) throws UserException {
        if (user.getId() == 0) {
            throw new UserException("user.update.password.failed.invalid");
        }
        if (!UserDAO.updatePassword(user)) {
            throw new UserException("user.update.password.failed");
        }
    }
}
