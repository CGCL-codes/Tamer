package jwebapp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 * Remote user information.
 */
public class RemoteUser implements Serializable {

    private static final long serialVersionUID = 100L;

    private String userId, salutation, firstName, lastName;

    private Set<String> roleSet = new HashSet<String>();

    private Map<String, Boolean> roleMap = new HashMap<String, Boolean>();

    public RemoteUser(String userId, String salutation, String firstName, String lastName, Set<String> roleSet) {
        this.userId = userId;
        this.salutation = salutation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleSet = roleSet;
        if (roleSet != null) {
            Iterator<String> i = roleSet.iterator();
            while (i.hasNext()) roleMap.put(i.next(), Boolean.TRUE);
        }
    }

    public static void setRemoteUser(HttpServletRequest request, RemoteUser remoteUser) {
        request.getSession().setAttribute("jwaRemoteUser", remoteUser.getUserId());
        request.getSession().setAttribute("jwaRemoteUserInformation", remoteUser);
    }

    /**
     * Returns the user Id.
     * @return the user Id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the users salutation (Mr., Mrs., Miss, ...).  May be null.
     * @return the users salutation
     */
    public String getSalutation() {
        return salutation;
    }

    /**
     * Returns the users first name.  May be null.
     * @return the users first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the users last name.  May be null.
     * @return the users last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns a set with all the roles the user is in.
     * @return an instance of Set.  May be empty.
     */
    @Deprecated
    public Set<String> getRoles() {
        return roleSet;
    }

    /**
     * Returns a set with all the roles the user is in.
     * @return an instance of Set.  May be empty.
     */
    public Set<String> getRoleSet() {
        return roleSet;
    }

    /**
     * Returns a map that's suitable for EL with all the roles the user is in.
     * @return an instance of map.  May be empty.
     * @deprecated Use getRoleMap() instead.
     */
    @Deprecated
    public Map<String, Boolean> getRoleHash() {
        return roleMap;
    }

    /**
     * Returns a map that's suitable for EL with all the roles the user is in.
     * @return an instance of map.  May be empty.
     */
    public Map<String, Boolean> getRoleMap() {
        return roleMap;
    }

    public String toString() {
        return "UserId=" + userId + ", Name=" + salutation + " " + firstName + " " + lastName + ", Roles=" + roleSet;
    }
}
