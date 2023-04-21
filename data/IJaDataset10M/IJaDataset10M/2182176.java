package com.pioneer.app.proview;

/**
 * RoleProcess entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class RoleProcess implements java.io.Serializable {

    private Integer id;

    private Integer roleId;

    private String processId;

    /** default constructor */
    public RoleProcess() {
    }

    /** full constructor */
    public RoleProcess(Integer roleId, String processId) {
        this.roleId = roleId;
        this.processId = processId;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getProcessId() {
        return this.processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
}
