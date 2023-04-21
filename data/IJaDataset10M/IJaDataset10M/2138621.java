package de.objectcode.openk.soa.auth.store.api;

import java.io.Serializable;
import java.util.Set;

public class RoleData implements Serializable {

    private static final long serialVersionUID = 1L;

    String roleId;

    String name;

    String description;

    Set<String> partOf;

    Set<String> contains;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getPartOf() {
        return partOf;
    }

    public void setPartOf(Set<String> partOf) {
        this.partOf = partOf;
    }

    public Set<String> getContains() {
        return contains;
    }

    public void setContains(Set<String> contains) {
        this.contains = contains;
    }
}
