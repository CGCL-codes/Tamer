package com.jcorporate.expresso.core.security;

import com.jcorporate.expresso.core.db.DBException;

public interface UserListener {

    /**
     * This method is called just after a new user is added
     * Creation date: (5/12/2001 2:26:17 PM)
     * @param user com.jcorporate.expresso.core.security.User
     * @throws DBException
     */
    public void addedUser(User user) throws DBException;

    /**
     * This method is called just before a user is deleted
     * Creation date: (5/12/2001 2:26:17 PM)
     * @param user com.jcorporate.expresso.core.security.User
     * @throws DBException
     */
    public void deletedUser(User user) throws DBException;

    /**
     * This method is called just before a user is logged off
     * Creation date: (5/12/2001 2:26:17 PM)
     * @param user com.jcorporate.expresso.core.security.User
     * @throws DBException
     */
    public void loggedOffUser(User user) throws DBException;

    /**
     * This method is called just after a user is logged on
     * Creation date: (5/12/2001 2:26:17 PM)
     * @param user com.jcorporate.expresso.core.security.User
     * @throws DBException
     */
    public void loggedOnUser(User user) throws DBException;

    /**
     * This method is called just after a user is modified
     * Creation date: (5/12/2001 2:26:17 PM)
     * @param user com.jcorporate.expresso.core.security.User
     * @throws DBException
     */
    public void modifiedUser(User user) throws DBException;
}
