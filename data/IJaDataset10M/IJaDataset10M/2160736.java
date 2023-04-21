package com.vayoodoot.server;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: Dec 27, 2006
 * Time: 8:39:11 PM
 * This class stores information about an online user.
 */
public class OnlineUser {

    private String userName;

    private String userType;

    private String userIP;

    private int userPort;

    private boolean isPowerUser;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserIP() {
        return userIP;
    }

    public void setUserIP(String userIP) {
        this.userIP = userIP;
    }

    public int getUserPort() {
        return userPort;
    }

    public void setUserPort(int userPort) {
        this.userPort = userPort;
    }

    public boolean isPowerUser() {
        return isPowerUser;
    }

    public void setPowerUser(boolean powerUser) {
        isPowerUser = powerUser;
    }
}
