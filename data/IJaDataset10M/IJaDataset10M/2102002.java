package de.folt.models.usermodel;

import java.util.Hashtable;

/**
 * @author klemens
 *

 */
public interface User {

    /**
     * createUser creates a new OpenTMS user
     * @param username the new user name
     * @param parameters the parameters of the new users
     * @return hashtable with user parameters
     */
    public Hashtable<String, Hashtable<String, String>> createUser(String username, Hashtable<String, String> parameters);

    /**
     * modifyUser mdifies the values of a user
     * @param username the user to modify
     * @param parameters
     * @return hash table with user parameters
     */
    public Hashtable<String, Hashtable<String, String>> modifyUser(String username, Hashtable<String, String> parameters);

    /**
     * removeUser removes a user from OpenTMS
     * @param username the user to remove
     * @return hash table with user parameters
     */
    public Hashtable<String, Hashtable<String, String>> removeUser(String username);
}
