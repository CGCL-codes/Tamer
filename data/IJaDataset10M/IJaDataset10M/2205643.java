package net.sourceforge.fluxion.portal.impl;

import net.sourceforge.fluxion.portal.User;

/**
 * Javadocs go here!
 *
 * @author Tony Burdett
 * @date 26-Sep-2008
 */
public class KnownUser implements User {

    private String userID;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}
