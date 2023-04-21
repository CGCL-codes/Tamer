package com.cci.bmc.action.role;

import java.util.HashSet;
import java.util.List;
import com.cci.bmc.domain.Permission;
import com.cci.bmc.domain.Role;
import com.cci.bmc.service.SecurityService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.Validateable;

public class CreateRole extends ActionSupport implements ModelDriven<Role>, Preparable, Validateable {

    private static final long serialVersionUID = 5580260177114495099L;

    public String execute() throws Exception {
        securityService.save(getModel());
        return SUCCESS;
    }

    public void prepare() {
        setModel(new Role());
        setAvailablePermissions(securityService.listPermissions());
        getModel().setPermissions(new HashSet<Permission>(securityService.listPermissions(getSelectedPermissions())));
    }

    public void validate() {
        Role dbRole = securityService.getRole(getModel().getName());
        if (dbRole != null) {
            addFieldError("name", "This name is already in use.");
        }
    }

    private SecurityService securityService;

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    private Role role;

    public Role getModel() {
        return role;
    }

    public void setModel(Role role) {
        this.role = role;
    }

    private List<Permission> availablePermissions;

    public List<Permission> getAvailablePermissions() {
        return availablePermissions;
    }

    public void setAvailablePermissions(List<Permission> availablePermissions) {
        this.availablePermissions = availablePermissions;
    }

    private List<Long> selectedPermissions;

    public List<Long> getSelectedPermissions() {
        return selectedPermissions;
    }

    public void setSelectedPermissions(List<Long> selectedPermissions) {
        this.selectedPermissions = selectedPermissions;
    }
}
