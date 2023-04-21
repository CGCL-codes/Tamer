package org.vqwiki.users;

import java.util.List;

/**
 * Usergroup, if there is no usergroup. This class implements the
 * default wiki behavior.
 *
 * @version $Revision: 636 $ - $Date: 2006-04-20 02:14:40 +0200 (do, 20 apr 2006) $
 * @author SinnerSchrader (tobsch)
 */
public class NoUsergroup extends Usergroup {

    /**
     * Get an instance of the user group class.
     * @return Instance to the user group class
     * @see vqwiki.users.Usergroup#getInstance()
     */
    public static Usergroup getInstance() {
        return new NoUsergroup();
    }

    /**
     * Get the full name of an user by its user-ID
     * @param uid The user-ID of this user
     * @return The full name of this user
     * @see vqwiki.users.Usergroup#getFullnameById(java.lang.String)
     */
    public String getFullnameById(String uid) {
        return uid;
    }

    /**
     * Get the email address of an user by its user-ID
     * @param uid The user-ID of this user
     * @return The email address of this user
     * @see vqwiki.users.Usergroup#getKnownEmailById(java.lang.String)
     */
    public String getKnownEmailById(String user) {
        return null;
    }

    /**
     * Get a list of all users.
     * @return List of all users. The list contains SelectorBeans with the user-ID as key and the full
     * username as label.
     * @see vqwiki.servlets.beans.SelectorBean
     * @see vqwiki.users.Usergroup#getListOfAllUsers()
     */
    public List getListOfAllUsers() {
        return null;
    }

    /**
     * Get the user details of this user by its user-ID. The user details is a string, which is
     * set in the admin section. It contains some placeholders, which are replaced by values from
     * the user repository
     * @param uid The user-ID of this user
     * @return The user details section
     * @see vqwiki.users.Usergroup#getUserDetails(java.lang.String)
     */
    public String getUserDetails(String uid) {
        return null;
    }

    /**
     * Contains the repository valid (already confirmed) email addresses?
     * If yes, then we can skip the registration process and the user is automatically registered.
     * @return true, if so. false otherwise.
     * @see vqwiki.users.Usergroup#isEmailValidated()
     */
    public boolean isEmailValidated() {
        return false;
    }
}
