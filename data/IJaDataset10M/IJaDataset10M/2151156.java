package dev.cinema.model;

import java.io.Serializable;

/**
 *
 * @author brushtyler
 */
public class UserCredentials implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private int role;

    public UserCredentials() {
    }

    public UserCredentials(String username) {
        this.username = username;
    }

    public UserCredentials(String username, String password, int role) {
        this.username = username;
        this.password = password;
        this.role = role;
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserCredentials)) {
            return false;
        }
        UserCredentials other = (UserCredentials) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.UserCredentials[ username=" + username + " ]";
    }
}
