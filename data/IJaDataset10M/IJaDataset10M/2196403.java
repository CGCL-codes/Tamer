package net.sourceforge.iwii.db.dev.bo.user;

import net.sourceforge.iwii.db.dev.bo.AbstractConvertableBO;
import net.sourceforge.iwii.db.dev.bo.IBusinessObject;
import net.sourceforge.iwii.db.dev.bo.project.ProjectBO;
import net.sourceforge.iwii.db.dev.common.enumerations.ProjectRoleTypes;

/**
 * Class represents user role in project.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class ProjectRoleBO extends AbstractConvertableBO<Long> {

    private ProjectRoleTypes role;

    private ProjectBO project;

    private UserBO user;

    private UserGroupBO userGroup;

    public ProjectRoleBO() {
    }

    public UserBO getUser() {
        return user;
    }

    public void setUser(UserBO user) {
        this.user = user;
    }

    public UserGroupBO getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroupBO userGroup) {
        this.userGroup = userGroup;
    }

    public ProjectBO getProject() {
        return project;
    }

    public void setProject(ProjectBO project) {
        this.project = project;
    }

    public ProjectRoleTypes getRole() {
        return role;
    }

    public void setRole(ProjectRoleTypes role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "business-object://convertable/" + this.getClass().getName() + "[databaseId=" + this.getDatabaseId() + "]";
    }

    @Override
    public void initWithOtherBO(IBusinessObject otherBO) {
        super.initWithOtherBO(otherBO);
        if (otherBO instanceof ProjectRoleBO) {
            ProjectRoleBO bo = (ProjectRoleBO) otherBO;
            this.role = bo.role;
            this.project = bo.project;
            this.user = bo.user;
            this.userGroup = bo.userGroup;
        }
    }
}
