package org.itsocial.framework.model.userInformation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

/**
 *
 * @author mookiah
 */
@Entity
public class Roles implements Serializable {

    @Id
    @Column(length = 10)
    String roleName;

    @OneToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "RolesAndTaskPermissions", joinColumns = @JoinColumn(name = "roleName"), inverseJoinColumns = @JoinColumn(name = "taskPermissionName"))
    List<TaskPermission> taskPermissions;

    Boolean disabled;

    public Roles() {
    }

    public Roles(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<TaskPermission> getTaskPermissions() {
        return taskPermissions;
    }

    public void setTaskPermissions(List<TaskPermission> permissions) {
        this.taskPermissions = permissions;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void addTaskPermission(TaskPermission permission) {
        if (this.taskPermissions == null) {
            this.taskPermissions = new ArrayList<TaskPermission>();
        }
        this.taskPermissions.add(permission);
    }

    public void removeTaskPermission(TaskPermission permission) {
        if (this.taskPermissions != null) {
            this.taskPermissions.remove(permission);
        }
    }
}
