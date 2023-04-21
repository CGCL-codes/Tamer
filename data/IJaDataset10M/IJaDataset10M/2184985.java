package com.fdv.template.web.action.administration;

import java.util.List;
import ar.com.fdvs.bean2bean.Bean2Bean;
import ar.com.fdvs.bean2bean.annotations.CopyTo;
import com.fdv.template.domain.persistence.GroupDao;
import com.fdv.template.domain.persistence.UserRoleDao;
import com.fdv.template.domain.persistence.filter.GroupFilter;
import com.fdv.template.domain.user.PermitType;
import com.fdv.template.domain.user.UserGroup;
import com.fdv.template.domain.user.UserRole;
import com.fdv.template.service.UserGroupService;
import com.fdv.template.web.action.BasicAction;

/**
 * Esta clase permite implementar el ABM de grupos
 * 
 * @author Dario Garcia
 */
public class GroupAbmAction extends BasicAction {

    private static final long serialVersionUID = 8893040769137919623L;

    private GroupDao groupDao;

    private UserRoleDao userRoleDao;

    private GroupFilter filter;

    private UserGroup selectedGroup;

    private PermitType[] allPermits;

    private List<UserRole> allRoles;

    private UserGroupService groupService;

    @CopyTo(destinationProperty = UserGroup.groupPermits_FIELD)
    private PermitType[] selectedPermits;

    @CopyTo(destinationProperty = UserGroup.roles_FIELD)
    private UserRole[] selectedRoles;

    /**
	 * Es una lista por que está ordenada
	 */
    private List<UserGroup> userGroups;

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public GroupFilter getFilter() {
        if (filter == null) {
            filter = new GroupFilter();
        }
        return filter;
    }

    public String doSearch() {
        userGroups = getGroupDao().findGroupsFor(this.getFilter());
        return SUCCESS;
    }

    public PermitType[] getAllPermits() {
        return allPermits;
    }

    public void setAllPermits(PermitType[] allPermits) {
        this.allPermits = allPermits;
    }

    public String loadCombos() {
        allPermits = PermitType.values();
        allRoles = getUserRoleDao().list();
        return SUCCESS;
    }

    public String doSave() {
        Bean2Bean.getInstance().copyPropertiesTo(this.getSelectedGroup(), this);
        getGroupService().save(this.getSelectedGroup());
        return SUCCESS;
    }

    public PermitType[] getSelectedPermits() {
        return selectedPermits;
    }

    public void setSelectedPermits(PermitType[] selectedPermits) {
        this.selectedPermits = selectedPermits;
    }

    public String doRemove() {
        this.getGroupService().delete(this.getSelectedGroup());
        return SUCCESS;
    }

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public UserGroup getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(UserGroup selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public UserGroupService getGroupService() {
        return groupService;
    }

    public void setGroupService(UserGroupService groupService) {
        this.groupService = groupService;
    }

    public void setFilter(GroupFilter filter) {
        this.filter = filter;
    }

    public List<UserRole> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(List<UserRole> allRoles) {
        this.allRoles = allRoles;
    }

    public UserRoleDao getUserRoleDao() {
        return userRoleDao;
    }

    public void setUserRoleDao(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    public UserRole[] getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(UserRole[] selectedRoles) {
        this.selectedRoles = selectedRoles;
    }
}
