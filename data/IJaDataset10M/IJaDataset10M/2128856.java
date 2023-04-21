package es.ehrflex.core.service;

import es.ehrflex.core.jdo.EHRflexException;
import es.ehrflex.core.jdo.User;

/**
 * Service, for all kind of user requests
 * 
 * @author Anton Brass
 * @version 1.0, 11.05.2009
 * 
 */
public interface UserService {

    /**
     * Checks if the combination of username and password exists and delivers the user if so.
     * 
     * @param username
     *            username
     * @param password
     *            password (not encrypted, will be done in this method)
     * 
     * @return user with given combination
     * @throws EHRflexException
     *             - if there is no user with this combination or other exception
     */
    public User loginUser(String username, String password) throws EHRflexException;

    /**
     * Saves the given user in the database. But first checking if there's already an user with the same login. If so an expcetion will be thrown.
     * 
     * @param user
     *            user to save in database
     * 
     * @return created user
     * 
     * @throws EHRflexException
     *             if there is already a user with the given username (in case of new User) or other exception
     */
    public User createUser(User user) throws EHRflexException;

    /**
     * Updating the given user in the database
     * 
     * @param user
     *            user to save in database
     * 
     * @return created user
     * 
     * @throws EHRflexException
     *             if there is already a user with the given username (in case of new User) or other exception
     */
    public User updateUser(User user) throws EHRflexException;
}
