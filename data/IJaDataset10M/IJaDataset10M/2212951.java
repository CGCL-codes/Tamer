package net.sourceforge.symba.web.server.database.security;

public class UserPassword {

    private String username;

    private String password;

    private String endID;

    public UserPassword(String username, String password, String endID) {
        this.username = username;
        this.password = password;
        this.endID = endID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEndID() {
        return endID;
    }

    public void setEndID(String endID) {
        this.endID = endID;
    }
}
