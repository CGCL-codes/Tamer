package com.creawor.hz_market.t_role;

import java.io.Serializable;
import java.util.Set;

public class t_role implements Serializable {

    public t_role() {
        id = 0;
    }

    public t_role(int id) {
        this.id = 0;
        setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int parm) {
        id = parm;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String parm) {
        role_name = parm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String parm) {
        description = parm;
    }

    public Set getUsers() {
        return users;
    }

    public void setUsers(Set users) {
        this.users = users;
    }

    private int id;

    private String role_name;

    private String description;

    private Set users;

    private Set functions;

    public Set getFunctions() {
        return functions;
    }

    public void setFunctions(Set functions) {
        this.functions = functions;
    }
}
