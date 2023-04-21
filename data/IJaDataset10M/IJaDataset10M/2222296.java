package goose.roles;

import java.util.Collection;
import java.util.Map;
import goose.BasicGooseAction;
import goose.model.PermissionList;
import goose.service.IPermissionManager;

/**
 * 
 */
public class SaveRolePermissionListAction extends BasicGooseAction {

    private static final long serialVersionUID = -3243050552787643923L;

    private Map<String, String[]> permissions;

    private long roleId;

    public String execute() throws Exception {
        IPermissionManager permissionManager = gooseService.getPermissionManager();
        PermissionList pl = new PermissionList();
        if (permissions != null) {
            Collection<String[]> cole = permissions.values();
            for (String[] moduleAndActions : cole) {
                for (String moduleAndAction : moduleAndActions) {
                    String[] temp = moduleAndAction.split("_");
                    pl.addActionToModule(temp[0], temp[1]);
                }
            }
            permissionManager.saveRolePermissions(roleId, pl);
        }
        return SUCCESS;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public Map<String, String[]> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, String[]> permissions) {
        this.permissions = permissions;
    }
}
