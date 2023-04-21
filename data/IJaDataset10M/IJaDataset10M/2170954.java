package org.gaofa.web.vo;

import java.util.List;

public class ResourceVO {

    private int id;

    private String url;

    private String note;

    private boolean isResourceInRole = false;

    private List<RoleVO> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isResourceInRole() {
        return isResourceInRole;
    }

    public void setResourceInRole(boolean isResourceInRole) {
        this.isResourceInRole = isResourceInRole;
    }

    public List<RoleVO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleVO> roles) {
        this.roles = roles;
    }
}
