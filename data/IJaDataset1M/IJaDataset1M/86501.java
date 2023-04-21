package org.vrforcad.lib.network.beans;

import java.io.Serializable;

/**
 * Login bean.
 * @version 1.0 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class POLogin implements PassingObject, Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String userPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
