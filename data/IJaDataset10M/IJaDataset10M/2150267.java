package com.tspp.dao.dto;

/**
 * Transfer object for user table(utility table for role distribution)
 * @author Maks
 */
public class User {

    /** Represents user's login*/
    private String login;

    /** Represents user's password*/
    private String password;

    /**
     * get the user's login
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * set the user's login
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * get the usser's password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set the user's password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
