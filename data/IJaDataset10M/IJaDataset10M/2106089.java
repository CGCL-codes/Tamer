package com.googlecode.openmpis.persistence.ibatis.dao;

import com.googlecode.openmpis.dto.User;
import com.googlecode.openmpis.util.Pagination;
import java.sql.SQLException;
import java.util.List;

/**
 * The UserDAO interface provides the ability to add, edit, delete, update and
 * retrieve administrators, encoders and investigators.
 * 
 * @author  <a href="mailto:rvbabilonia@gmail.com">Rey Vincent Babilonia</a>
 * @version 1.0
 */
public interface UserDAO {

    /**
     * Retrieves all users.
     *
     * @param pagination    the pagination context
     * @return              the list of users
     * @throws java.sql.SQLException
     */
    public List<User> getAllUsers(Pagination pagination) throws SQLException;

    /**
     * Retrieves all investigators according to agency then last name.
     *
     * @return              the list of investigators
     * @throws java.sql.SQLException
     */
    public List<User> listInvestigators() throws SQLException;

    /**
     * Retrieves all active investigators according to agency then last name.
     *
     * @return              the list of active investigators
     * @throws java.sql.SQLException
     */
    public List<User> listActiveInvestigators() throws SQLException;

    /**
     * Retrieves all suspended investigators according to agency then last name.
     *
     * @return              the list of suspended investigators
     * @throws java.sql.SQLException
     */
    public List<User> listSuspendedInvestigators() throws SQLException;

    /**
     * Retrieves all administrators according to agency then last name.
     *
     * @return              the list of administrators
     * @throws java.sql.SQLException
     */
    public List<User> listAdministrators() throws SQLException;

    /**
     * Retrieves all active administrators according to agency then last name.
     *
     * @return              the list of active administrators
     * @throws java.sql.SQLException
     */
    public List<User> listActiveAdministrators() throws SQLException;

    /**
     * Retrieves all suspended administrators according to agency then last name.
     *
     * @return              the list of suspended administrators
     * @throws java.sql.SQLException
     */
    public List<User> listSuspendedAdministrators() throws SQLException;

    /**
     * Retrieves all encoders according to agency then last name.
     *
     * @return              the list of encoders
     * @throws java.sql.SQLException
     */
    public List<User> listEncoders() throws SQLException;

    /**
     * Retrieves all active encoders according to agency then last name.
     *
     * @return              the list of active encoders
     * @throws java.sql.SQLException
     */
    public List<User> listActiveEncoders() throws SQLException;

    /**
     * Retrieves all suspended encoders according to agency then last name.
     *
     * @return              the list of suspended encoders
     * @throws java.sql.SQLException
     */
    public List<User> listSuspendedEncoders() throws SQLException;

    /**
     * Retrieves a user given his ID.
     * 
     * @param id            the user ID
     * @return              the user
     * @throws java.sql.SQLException
     */
    public User getUserById(Integer id) throws SQLException;

    /**
     * Retrieves a user given his username.
     * 
     * @param username      the username
     * @return              the user
     * @throws java.sql.SQLException
     */
    public User getUserByUsername(String username) throws SQLException;

    /**
     * Inserts a new user.
     * 
     * @param user          the new user
     * @return              <code>true</code> if the user was successfully inserted; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    public boolean insertUser(User user) throws SQLException;

    /**
     * Updates an existing user.
     * 
     * @param user          the existing user
     * @return              <code>true</code> if the user was successfully updated; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    public boolean updateUser(User user) throws SQLException;

    /**
     * Updates a user's last log in date and IP address.
     * 
     * @param user          the user who logged in
     * @return              <code>true</code> if the log in date and IP address were successfully updated; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    public boolean updateLogin(User user) throws SQLException;

    /**
     * Resets a user's password.
     * 
     * @param user          the user who supplied the correct security question and answer
     * @return              <code>true</code> if the password was successfully reset; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    public boolean updatePassword(User user) throws SQLException;

    /**
     * Deletes a user with the specified ID.
     * 
     * @param id            the ID of the user to be deleted
     * @return              <code>true</code> if the user was successfully deleted; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    public boolean deleteUser(Integer id) throws SQLException;

    /**
     * Checks if a username is unique.
     * 
     * @param user          the existing user
     * @return              <code>true</code> if the username is unique; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    public boolean isUniqueUsername(User user) throws SQLException;

    /**
     * Checks if an email address is unique.
     * 
     * @param user          the existing user
     * @return              <code>true</code> if the email address is unique; <code>false</code> otherwise
     * @throws java.sql.SQLException
     */
    public boolean isUniqueEmail(User user) throws SQLException;

    /**
     * Returns the total number of users.
     *
     * @return              the total number of users
     * @throws java.sql.SQLException
     */
    public int countAllUsers() throws SQLException;

    /**
     * Returns the total number of administrators.
     *
     * @return              the total number of administrators
     * @throws java.sql.SQLException
     */
    public int countAdministrators() throws SQLException;

    /**
     * Returns the total number of active administrators.
     *
     * @return              the total number of active administrators
     * @throws java.sql.SQLException
     */
    public int countActiveAdministrators() throws SQLException;

    /**
     * Returns the total number of suspended administrators.
     *
     * @return              the total number of suspended administrators
     * @throws java.sql.SQLException
     */
    public int countSuspendedAdministrators() throws SQLException;

    /**
     * Returns the total number of encoders.
     *
     * @return              the total number of encoders
     * @throws java.sql.SQLException
     */
    public int countEncoders() throws SQLException;

    /**
     * Returns the total number of active encoders.
     *
     * @return              the total number of active encoders
     * @throws java.sql.SQLException
     */
    public int countActiveEncoders() throws SQLException;

    /**
     * Returns the total number of suspended encoders.
     *
     * @return              the total number of suspended encoders
     * @throws java.sql.SQLException
     */
    public int countSuspendedEncoders() throws SQLException;

    /**
     * Returns the total number of investigators.
     *
     * @return              the total number of investigators
     * @throws java.sql.SQLException
     */
    public int countInvestigators() throws SQLException;

    /**
     * Returns the total number of active investigators.
     *
     * @return              the total number of active investigators
     * @throws java.sql.SQLException
     */
    public int countActiveInvestigators() throws SQLException;

    /**
     * Returns the total number of suspended investigators.
     *
     * @return              the total number of suspended investigators
     * @throws java.sql.SQLException
     */
    public int countSuspendedInvestigators() throws SQLException;

    /**
     * Returns the total number of active users.
     *
     * @return              the total number of active users
     * @throws java.sql.SQLException
     */
    public int countActiveUsers() throws SQLException;

    /**
     * Returns the total number of suspended users.
     *
     * @return              the total number of suspended users
     * @throws java.sql.SQLException
     */
    public int countSuspendedUsers() throws SQLException;

    /**
     * Returns the total number of users encoded by a given user.
     *
     * @param creatorId     the user ID
     * @return              the total number of users encoded by a given user
     * @throws java.sql.SQLException
     */
    public int countEncodedUsers(Integer creatorId) throws SQLException;
}
