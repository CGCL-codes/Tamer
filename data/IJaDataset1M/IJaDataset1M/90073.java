package com.agil.photoalbum.db.entities;

import java.util.HashSet;
import java.util.Set;

public class Role implements java.io.Serializable {

    private long roleId;

    private String roleType;

    private Set<User> users = new HashSet<User>(0);

    public Role() {
    }

    public Role(long roleId, String roleType) {
        this.roleId = roleId;
        this.roleType = roleType;
    }

    public Role(long roleId, String roleType, Set<User> userses) {
        this.roleId = roleId;
        this.roleType = roleType;
        this.users = userses;
    }

    public long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleType() {
        return this.roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> userses) {
        this.users = userses;
    }
}
