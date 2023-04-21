package com.jcorporate.expresso.core.security;

import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.LookupInterface;
import java.util.Vector;

/**
 *
 * UserInfo defines the interface to an object that provides information about a user
 * this object may get it's information from LDAP, Database, or any other source
 *
 * @version        $Revision: 3 $  $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 * @author        Michael Nash
 */
public interface UserInfo extends LookupInterface {

    /**
     * Adds the current UserInfo object into the repository
     *
     * @throws    DBException If the add fails
     */
    public void add() throws DBException;

    /**
     * Deletes the current UserInfo object from the repository
     *
     * @throws    DBException If the delete fails
     */
    public void delete() throws DBException;

    /**
     * Find a UserInfo object in the repository that matches the non-empty properties filled in the current UserInfo object
     *
     * @throws    DBException If the find fails
     * @return true if the user is found
     */
    public boolean find() throws DBException;

    /**
     * Retrieve the current account status. Valid values are "A" (active), "I" (inactive), "D" (disabled)
     *
     * @throws    DBException If the find fails
     * @return    java.lang.String
     */
    public String getAccountStatus() throws DBException;

    /**
     * Returns a Vector of all the UserInfo objects in the repository
     *
     * @return java.util.Vector
     * @throws DBException If there is an error during the retrieval
     */
    public Vector getAllUsers() throws DBException;

    /**
     * Returns the date that this UserInfo object was created
     *
     * @return java.lang.String
     * @throws DBException If there is an error during the retrieval
     */
    public String getCreateDate() throws DBException;

    /**
     * Returns the email address for this user
     *
     * @return java.lang.String
     * @throws DBException If there is an error during the retrieval
     */
    public String getEmail() throws DBException;

    /**
     * Returns the email auth code previously set for this user
     *
     * @return java.lang.String
     * @throws DBException If there is an error during the retrieval
     */
    public String getEmailAuthCode() throws DBException;

    /**
     * Retrieve the validation code required for authorization by email
     *
     * @throws    DBException If the find fails
     * @return    java.lang.String
     */
    public String getEmailValCode() throws DBException;

    /**
     * Return a vector of the group names that this user belongs to
     *
     * @param   fieldName The field to retrieve
     * @return    Vector Group names that this user belongs to
     * @throws    DBException If an error occurs when the group info is read
     * @deprecated Use the direct getLoginName, getEmail, getPassword, etc. methods instead
     */
    public String getField(String fieldName) throws DBException;

    /**
     * Return a vector of the group names that this user belongs to
     *
     * @return    Vector Group names that this user belongs to
     * @throws    DBException If an error occurs when the group info is read
     */
    public Vector getGroups() throws DBException;

    /**
     * Returns the login name of this user
     *
     * @return java.lang.String
     * @throws DBException If there is an error during the retrieval
     */
    public String getLoginName() throws DBException;

    /**
     * Returns the password for this user
     *
     * @return java.lang.String
     * @throws DBException If there is an error during the retrieval
     */
    public String getPassword() throws DBException;

    /**
     * Returns the status of whether extended registration has been completed or not
     * Valid values are "Y" or "N"
     *
     * @return java.lang.String
     * @throws DBException If there is an error during the retrieval
     */
    public boolean getRegComplete() throws DBException;

    /**
     * Returns the unique integer for the registration domain that this user belongs to
     *
     * @throws com.jcorporate.expresso.core.db.DBException If the underlying User implementation throws the same
     * @return java.lang.String
     */
    public String getRegistrationDomain() throws DBException;

    /**
     * Returns the user id for this user
     *
     * @return java.lang.String
     * @throws DBException If there is an error during the retrieval
     */
    public int getUid() throws DBException;

    /**
     * Returns the date that this UserInfo object was last modified
     *
     * @return java.lang.String
     * @throws DBException If there is an error during the retrieval
     */
    public String getUpdateDate() throws DBException;

    /**
     * Returns the descriptive string for this user
     *
     * @return java.lang.String
     * @throws DBException If there is an error during the retrieval
     */
    public String getUserName() throws DBException;

    /**
     * Checks if the given password equals what we have on file.  We don't
     * directly compare userInfo fields because often we store the password
     * in a hash instead of a normal string so we have to take the testPassword,
     * hash it and compare the two hashes.
     *
     * @param   testPassword The string to test if it's a correct password
     * @return  true if the testPassword equals the password on file.
     * @throws    DBException If an error occurs when the group info is read
     */
    public boolean passwordEquals(String testPassword) throws DBException;

    /**
     * Retrieves the current user from the repository
     *
     * @throws    DBException If the add fails
     */
    public void retrieve() throws DBException;

    /**
     *
     *
     * @throws  DBException
     */
    public void sendAuthEmail() throws DBException;

    /**
     *
     *
     * @throws  DBException
     */
    public void sendFollowUpEmail() throws DBException;

    /**
     * Sets the current status of the account - "A" (active), "D" (disabled), "I" (inactive)
     *
     * @param accountStatus java.lang.String
     * @throws DBException If there is an error
     */
    public void setAccountStatus(String accountStatus) throws DBException;

    /**
     * Sets the DB context
     *
     * @param newDBName java.lang.String
     * @throws DBException If there is an error
     */
    public void setDBName(String newDBName) throws DBException;

    /**
     * gets the DB context; can return null
     */
    public String getDBName();

    /**
     * gets the DB context; can return null
     */
    public String getDataContext();

    /**
     * Sets the email address
     *
     * @param email java.lang.String
     * @throws DBException If there is an error
     */
    public void setEmail(String email) throws DBException;

    /**
     * Sets the code required for auth. via email
     * @param code java.lang.String
     * @throws DBException If there is an error
     */
    public void setEmailValCode(String code) throws DBException;

    /**
     * Sets the login name
     *
     * @param loginName java.lang.String
     * @throws DBException If there is an error
     */
    public void setLoginName(String loginName) throws DBException;

    /**
     * Sets the password
     *
     * @param password java.lang.String
     * @throws DBException If there is an error
     */
    public void setPassword(String password) throws DBException;

    /**
     * Sets the extended registration complete flag - "Y" or "N"
     *
     * @param status java.lang.String
     * @throws DBException If there is an error
     */
    public void setRegComplete(boolean status) throws DBException;

    /**
     * Sets the registration domain
     *
     * @param id java.lang.String
     * @throws DBException If there is an error
     * @see com.jcorporate.expresso.services.dbobj.RegistrationDomain
     */
    public void setRegistrationDomain(String id) throws DBException;

    /**
     * Sets the user UID
     *
     * @param uid The uid of the user
     * @throws DBException If there is an error
     */
    public void setUid(int uid) throws DBException;

    /**
     * Sets the user name
     *
     * @param name java.lang.String
     * @throws DBException If there is an error
     */
    public void setUserName(String name) throws DBException;

    /**
     * Update the user in the repository with modified properties
     *
     * @throws    DBException If the add fails
     */
    public void update() throws DBException;

    /**
     * the primary group of this user is appropriate for unix-like purposes,
     * such as setting the group for a file permission
     * @return name of the primary group of this user; null if no group is found
     *
     */
    public String getPrimaryGroup() throws DBException;
}
