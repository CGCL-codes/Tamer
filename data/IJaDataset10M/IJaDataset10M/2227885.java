package org.openremote.modeler.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.openremote.modeler.client.Constants;

/**
 * The Class User.
 * 
 * @author Dan 2009-7-7
 */
@Entity
@Table(name = "user")
public class User extends BusinessEntity {

    private static final long serialVersionUID = 6064996041309363949L;

    private String username;

    private String password;

    private String rawPassword;

    private String email;

    private boolean valid;

    private transient Timestamp registerTime;

    /** The account containing all business entities. */
    private Account account;

    private List<Role> roles;

    private String token;

    /**
    * Instantiates a new user.
    */
    public User() {
        account = new Account();
        valid = false;
        registerTime = new Timestamp(System.currentTimeMillis());
        roles = new ArrayList<Role>();
    }

    public User(Account account) {
        this.account = account;
        valid = false;
        registerTime = new Timestamp(System.currentTimeMillis());
        roles = new ArrayList<Role>();
    }

    /**
    * Gets the username.
    * 
    * @return the username
    */
    @Column(unique = true, nullable = false)
    public String getUsername() {
        return username;
    }

    /**
    * Sets the username.
    * 
    * @param username the new username
    */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
    * Gets the password.
    * 
    * @return the password
    */
    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    /**
    * Sets the password.
    * 
    * @param password the new password
    */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
    * Gets the account.
    * 
    * @return the account
    */
    @ManyToOne
    public Account getAccount() {
        return account;
    }

    /**
    * Sets the account.
    * 
    * @param account the new account
    */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
    * Gets the roles.
    * 
    * @return the roles
    */
    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_oid") }, inverseJoinColumns = { @JoinColumn(name = "role_oid") })
    public List<Role> getRoles() {
        return roles;
    }

    /**
    * Sets the roles.
    * 
    * @param roles the new roles
    */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
    * Adds the role.
    * 
    * @param role the role
    */
    public void addRole(Role role) {
        roles.add(role);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Transient
    public String getRegisterTimeAsString() {
        return registerTime.toString().replaceAll("\\.\\d+", "");
    }

    @Column(name = "register_time")
    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    @Transient
    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Transient
    public String getRole() {
        List<String> roleStrs = new ArrayList<String>();
        for (Role role : roles) {
            roleStrs.add(role.getName());
        }
        String userRole = null;
        if (roleStrs.contains(Role.ROLE_ADMIN)) {
            userRole = Constants.ROLE_ADMIN_DISPLAYNAME;
        } else if (roleStrs.contains(Role.ROLE_MODELER) && roleStrs.contains(Role.ROLE_DESIGNER)) {
            userRole = Constants.ROLE_MODELER_DESIGNER_DISPLAYNAME;
        } else if (roleStrs.contains(Role.ROLE_MODELER)) {
            userRole = Constants.ROLE_MODELER_DISPLAYNAME;
        } else if (roleStrs.contains(Role.ROLE_DESIGNER)) {
            userRole = Constants.ROLE_DESIGNER_DISPLAYNAME;
        }
        return userRole;
    }
}
