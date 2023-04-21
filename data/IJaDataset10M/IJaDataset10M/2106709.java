package com.jvantage.ce.acl;

import org.apache.commons.lang.*;
import java.io.*;

/**
 * @author  Brent Clay
 */
public class ACLPrincipal implements java.io.Serializable, java.lang.Comparable {

    private ACLPrincipalLoginName loginName = null;

    private String password = null;

    private String userFullName = null;

    /** Creates new ACLPrincipal */
    public ACLPrincipal() {
    }

    public ACLPrincipal(ACLPrincipalLoginName loginName) {
        setLoginName(loginName);
    }

    public ACLPrincipal(ACLPrincipalLoginName loginName, String fullName) {
        setLoginName(loginName);
        setUserFullName(fullName);
    }

    public ACLPrincipal(ACLPrincipalLoginName loginName, String fullName, String password) {
        setLoginName(loginName);
        setUserFullName(fullName);
        setPassword(password);
    }

    public int compareTo(java.lang.Object obj) {
        if (obj == null) {
            return -1;
        }
        ACLPrincipal argPrincipal = (ACLPrincipal) obj;
        ACLPrincipalLoginName lhs = this.getLoginName();
        ACLPrincipalLoginName rhs = argPrincipal.getLoginName();
        if ((lhs == null) && (rhs != null)) {
            return -1;
        } else if ((lhs != null) && (rhs == null)) {
            return 1;
        } else if ((lhs == null) && (rhs == null)) {
            return 0;
        }
        return lhs.getLoginName().compareToIgnoreCase(rhs.getLoginName());
    }

    public ACLPrincipalLoginName getLoginName() {
        return loginName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public boolean isValid() {
        return !((getLoginName() == null) || getLoginName().isNull() || (getUserFullName() == null) || (getPassword() == null));
    }

    public void setLoginName(ACLPrincipalLoginName name) {
        loginName = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserFullName(String userName) {
        userFullName = userName;
    }

    public String toString() {
        final String ls = SystemUtils.LINE_SEPARATOR;
        StringBuffer msg = new StringBuffer();
        msg.append("ACLPrincipal").append(ls).append("       loginName [").append(getLoginName().toString()).append("]").append(ls).append("    userFullName [").append(getUserFullName()).append("]").append(ls).append("        password [").append(StringUtils.isBlank(getPassword()) ? "<<Undefined>>]" : "<<Defined>>]").append("]");
        return msg.toString();
    }
}
