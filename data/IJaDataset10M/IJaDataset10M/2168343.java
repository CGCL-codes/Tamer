package com.shuetech.usermanagement.entity;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = UserAccount.USERNAME_FIELDNAME))
public class UserAccount implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Integer userAccountId;

    private String username;

    public static final String USERNAME_FIELDNAME = "username";

    private String passwordHash;

    private Set<Group> groups;

    private String firstname;

    public static final String FIRSTNAME_FIELDNAME = "firstname";

    private String lastname;

    private boolean enabled;

    private String email;

    @Id
    @GeneratedValue
    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @ManyToMany(targetEntity = Group.class)
    @JoinTable(name = "UserAccountGroups", joinColumns = @JoinColumn(name = "UserAccountId"), inverseJoinColumns = @JoinColumn(name = "GroupId"))
    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
