package org.itsocial.framework.model.userInformation;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author mookiah
 */
@Entity
public class TaskPermission implements Serializable {

    @Id
    @Column(length = 20)
    String taskPermissionName;

    Boolean locked;

    public TaskPermission() {
    }

    public TaskPermission(String string) {
        taskPermissionName = string;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getTaskPermissionName() {
        return taskPermissionName;
    }

    public void setTaskPermissionName(String taskPermissionName) {
        this.taskPermissionName = taskPermissionName;
    }
}
