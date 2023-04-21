package net.onlinepresence.opos.domain.beans;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import net.onlinepresence.opos.domain.Application;
import net.onlinepresence.opos.domain.Membership;
import net.onlinepresence.opos.domain.Person;

public class PersonBean implements Person {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private Set<Membership> applicationMemberships = new HashSet<Membership>();

    /**
	 * @param firstName
	 * @param lastName
	 * @param username
	 */
    public PersonBean(String firstName, String lastName, String username, String password) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    /**
	 */
    public PersonBean() {
    }

    /**
	 * @return the firstName
	 */
    public String getFirstName() {
        return firstName;
    }

    /**
	 * @param firstName the firstName to set
	 */
    public void setFirstName(String firstName) {
        if (firstName == null) throw new IllegalArgumentException("Name is not valid");
        this.firstName = firstName;
    }

    /**
	 * @return the lastName
	 */
    public String getLastName() {
        return lastName;
    }

    /**
	 * @param lastName the lastName to set
	 */
    public void setLastName(String lastName) {
        if (lastName == null) throw new IllegalArgumentException("Last name is not valid");
        this.lastName = lastName;
    }

    /**
	 * @return the username
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @param username the username to set
	 */
    public void setUsername(String username) {
        if (username == null || username.length() < 4) throw new IllegalArgumentException("Username is not valid");
        this.username = username;
    }

    /**
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @param password the password to set
	 */
    public void setPassword(String password) {
        if (password == null || password.length() < 4) throw new IllegalArgumentException("Password is not valid");
        this.password = password;
    }

    /**
	 * @return the association
	 */
    public Set<Membership> getApplicationMemberships() {
        return applicationMemberships;
    }

    public void setApplicationMemberships(Set<Membership> applicationMemberships) {
        this.applicationMemberships = applicationMemberships;
    }

    /**
	 * @param association the association to add to the list of users associations
	 */
    public boolean addApplicationMembership(Membership membership) {
        if (hasMembership(membership)) return false;
        return applicationMemberships.add(membership);
    }

    public Membership deleteApplicationMembership(String appUrl) {
        for (Membership membership : applicationMemberships) {
            if (membership.getApplication().getWebAddress().equals(appUrl)) {
                applicationMemberships.remove(membership);
                return membership;
            }
        }
        return null;
    }

    public boolean hasMembership(Membership membership) {
        for (Membership m : getApplicationMemberships()) {
            if (m.getApplication().getWebAddress().equals(membership.getApplication().getWebAddress()) && m.getUsername().equals(membership.getUsername())) return true;
        }
        return false;
    }

    public void updateMembership(Membership membership) {
        for (Membership m : applicationMemberships) {
            if (m.getApplication().getWebAddress().toString().equals(membership.getApplication().getWebAddress().toString())) {
                m.setUsername(membership.getUsername());
                m.setReceiveFrom(membership.isReceiveFrom());
                m.setSendTo(membership.isSendTo());
            }
        }
    }

    public void deleteApplicationMembership(Membership membership) {
        applicationMemberships.remove(membership);
    }
}
