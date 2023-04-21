package com.frameworkset.commons.dbcp.datasources;

import java.io.Serializable;

/**
 * Holds a username, password pair.
 * @version $Revision: 479137 $ $Date: 2006-11-25 08:51:48 -0700 (Sat, 25 Nov 2006) $
 */
class UserPassKey implements Serializable {

    private String password;

    private String username;

    UserPassKey(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Get the value of password.
     * @return value of password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the value of username.
     * @return value of username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return <code>true</code> if the username and password fields for both 
     * objects are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserPassKey)) {
            return false;
        }
        UserPassKey key = (UserPassKey) obj;
        boolean usersEqual = (this.username == null ? key.username == null : this.username.equals(key.username));
        boolean passwordsEqual = (this.password == null ? key.password == null : this.password.equals(key.password));
        return (usersEqual && passwordsEqual);
    }

    public int hashCode() {
        return (this.username != null ? this.username.hashCode() : 0);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(50);
        sb.append("UserPassKey(");
        sb.append(username).append(", ").append(password).append(')');
        return sb.toString();
    }
}
