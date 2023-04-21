package com.extentech.security;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Stateless user factory essentially is pass through user handling for 
 * integrations in systems where security is handled 
 * with a separate implementation.  
 * 
 * Security is not managed through these users, unless it it handled in an Auth class.
 * 
 * In essence this primarily allows multiple unique users to exist even though they are not managed
 */
public class StatelessUserFactory extends UserFactory {

    /** connect or reconnect the factory to the database

     * 
     * @author John McMahon [ Apr 27, 2008 ]
     * @throws Exception
     */
    public void init() throws Exception {
    }

    String userUserField = "stateless", userPasswordField = "stateless", userTableName = "stateless", userIdeField = "stateless";

    /** Instantiates and returns a user object
     * 
     *  The returned User object needs to login after creation to instantiate permissions and data.
     * 
     * @return
     */
    public User getUser() {
        User thisUser = new StatelessUser();
        thisUser.setUserField(userUserField);
        thisUser.setPasswordField(userPasswordField);
        thisUser.setTableName(userTableName);
        thisUser.setKeyCol(userIdeField);
        thisUser.setFactory(this);
        return thisUser;
    }

    /**
     * Implement this, but how?
     * 
     * overriding super so we dont hit the dbconnection
     *  */
    public boolean isMemeOwner(User u, String resource) {
        boolean ret = false;
        return ret;
    }
}
