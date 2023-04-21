package org.jsecurity.ri.authc.module.dao;

import org.jsecurity.authc.module.AuthenticationInfo;
import java.security.Permission;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Simple implementation of the {@link org.jsecurity.authc.module.AuthenticationInfo} interface that
 * contains all necessary information as instance variables and exposes them
 * via getters and setters using standard JavaBean notation.
 *
 * @see org.jsecurity.authc.module.AuthenticationInfo
 *
 * @since 0.1
 * @author Jeremy Haile
 */
public class SimpleAuthenticationInfo implements AuthenticationInfo {

    private List<Principal> principals;

    private Object credentials;

    /**
     * The collection of roles that apply to this user.
     */
    private Collection<String> roles;

    /**
     * The collection of permissions that apply to this user.
     */
    private Collection<Permission> permissions;

    /**
     * True if the user's account is locked, false otherwise.
     */
    private boolean accountLocked = false;

    /**
     * True if the user's credentials are expired, false otherwise.
     */
    private boolean credentialsExpired = false;

    /**
     * True if the user is allowed to log in concurrently from two
     * separate locations, false otherwise.
     */
    private boolean concurrentLoginsAllowed = true;

    public SimpleAuthenticationInfo() {
        this.principals = new ArrayList<Principal>();
        this.roles = new HashSet<String>();
        this.permissions = new HashSet<Permission>();
    }

    public SimpleAuthenticationInfo(List<Principal> principals, Object credentials) {
        this.principals = principals;
        this.credentials = credentials;
    }

    public SimpleAuthenticationInfo(Principal principal, Object credentials, Collection<String> roles) {
        this.principals = new ArrayList<Principal>();
        this.principals.add(principal);
        this.credentials = credentials;
        this.roles = roles;
    }

    public SimpleAuthenticationInfo(Principal principal, Object credentials, Collection<String> roles, Collection<Permission> permissions) {
        this.principals = new ArrayList<Principal>();
        this.principals.add(principal);
        this.credentials = credentials;
        this.roles = roles;
        this.permissions = permissions;
    }

    public SimpleAuthenticationInfo(Principal principal, Object credentials, Collection<String> roles, boolean accountLocked, boolean credentialsExpired, boolean concurrentLoginsAllowed) {
        this.principals = new ArrayList<Principal>();
        this.principals.add(principal);
        this.credentials = credentials;
        this.roles = roles;
        this.accountLocked = accountLocked;
        this.credentialsExpired = credentialsExpired;
        this.concurrentLoginsAllowed = concurrentLoginsAllowed;
    }

    public SimpleAuthenticationInfo(Principal principal, Object credentials, Collection<String> roles, Collection<Permission> permissions, boolean accountLocked, boolean credentialsExpired, boolean concurrentLoginsAllowed) {
        this.principals = new ArrayList<Principal>();
        this.principals.add(principal);
        this.credentials = credentials;
        this.roles = roles;
        this.permissions = permissions;
        this.accountLocked = accountLocked;
        this.credentialsExpired = credentialsExpired;
        this.concurrentLoginsAllowed = concurrentLoginsAllowed;
    }

    public SimpleAuthenticationInfo(List<Principal> principals, Object credentials, Collection<String> roles) {
        this.principals = principals;
        this.credentials = credentials;
        this.roles = roles;
    }

    public SimpleAuthenticationInfo(List<Principal> principals, Object credentials, Collection<String> roles, Collection<Permission> permissions) {
        this.principals = principals;
        this.credentials = credentials;
        this.roles = roles;
        this.permissions = permissions;
    }

    public SimpleAuthenticationInfo(List<Principal> principals, Object credentials, Collection<String> roles, boolean accountLocked, boolean credentialsExpired, boolean concurrentLoginsAllowed) {
        this.principals = principals;
        this.credentials = credentials;
        this.roles = roles;
        this.accountLocked = accountLocked;
        this.credentialsExpired = credentialsExpired;
        this.concurrentLoginsAllowed = concurrentLoginsAllowed;
    }

    public SimpleAuthenticationInfo(List<Principal> principals, Object credentials, Collection<String> roles, Collection<Permission> permissions, boolean accountLocked, boolean credentialsExpired, boolean concurrentLoginsAllowed) {
        this.principals = principals;
        this.credentials = credentials;
        this.roles = roles;
        this.permissions = permissions;
        this.accountLocked = accountLocked;
        this.credentialsExpired = credentialsExpired;
        this.concurrentLoginsAllowed = concurrentLoginsAllowed;
    }

    public Principal getPrincipal() {
        return this.principals.get(0);
    }

    public List<Principal> getPrincipals() {
        return principals;
    }

    public void setPrincipals(List<Principal> principals) {
        this.principals = principals;
    }

    public void addPrincipal(Principal principal) {
        this.principals.add(principal);
    }

    public Object getCredentials() {
        return credentials;
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    public void addRole(String roleName) {
        this.roles.add(roleName);
    }

    public Collection<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public boolean isConcurrentLoginsAllowed() {
        return concurrentLoginsAllowed;
    }

    public void setConcurrentLoginsAllowed(boolean concurrentLoginsAllowed) {
        this.concurrentLoginsAllowed = concurrentLoginsAllowed;
    }

    public String toString() {
        return "Authentication information for user [" + getPrincipals() + "]";
    }
}
