package goose.roles;

import goose.BasicGooseAction;
import goose.model.Role;
import goose.service.IRoleManager;

/**
 * 
 */
public class AddRoleAction extends BasicGooseAction {

    private static final long serialVersionUID = -3243050552787643923L;

    private String name;

    public String execute() throws Exception {
        if (isInvalid(getName())) return INPUT;
        try {
            Role role = new Role(name);
            IRoleManager roleManager = gooseService.getRoleManager();
            roleManager.addRole(role);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return INPUT;
    }

    private boolean isInvalid(String value) {
        return (value == null || value.length() == 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
