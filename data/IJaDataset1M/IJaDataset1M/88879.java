package com.pk.platform.business.privilege.web;

import com.pk.platform.business.core.web.GenericAction;
import com.pk.platform.business.privilege.service.IPrivilegeService;
import com.pk.platform.business.privilege.vo.PrivilegeVO;
import com.pk.platform.domain.privilege.Privilege;
import com.pk.platform.domain.privilege.Role;
import com.pk.platform.util.constant.Constant;

public class PrivilegeAction extends GenericAction {

    private IPrivilegeService privilegeService;

    private Privilege privilege = new Privilege();

    private PrivilegeVO pvo = new PrivilegeVO();

    private String tree;

    private boolean isPrivilege = false;

    private Privilege parentPrivilege = new Privilege();

    private boolean addPrivilegeFlag = false;

    private boolean delPrivilegeFlag = false;

    private String[] privilegeCb;

    private Role role = new Role();

    private String hasPrivilegeIds;

    public String toAddPrivilege() {
        parentPrivilege = privilegeService.queryPrivilegeById(parentPrivilege.getId());
        return SUCCESS;
    }

    public String addPrivilege() {
        addPrivilegeFlag = privilegeService.addPrivilege(privilege);
        return SUCCESS;
    }

    public String toUpdatePrivilege() {
        privilege = privilegeService.queryPrivilegeById(privilege.getId());
        parentPrivilege = privilegeService.queryPrivilegeById(privilege.getParentId());
        return SUCCESS;
    }

    public String updatePrivilege() {
        privilegeService.updatePrivilege(privilege);
        return SUCCESS;
    }

    public String delPrivilege() {
        Privilege p = privilegeService.queryPrivilegeById(privilege.getId());
        if (Constant.PRIVILEGE.equals(p.getType())) {
            delPrivilegeFlag = true;
            privilegeService.delPrivilege(privilege.getId());
        }
        return SUCCESS;
    }

    public String queryAllPrivilegeTree() {
        tree = privilegeService.getAllPrivilegeTree();
        return SUCCESS;
    }

    public String updateCheck() {
        Privilege p = privilegeService.queryPrivilegeById(privilege.getId());
        if (Constant.PRIVILEGE.equals(p.getType())) {
            isPrivilege = true;
        }
        return SUCCESS;
    }

    public String queryAssignPrivilegeTree() {
        hasPrivilegeIds = privilegeService.hasPrivilegeIds(role.getId());
        tree = privilegeService.getAssignPrivilegeTree();
        return SUCCESS;
    }

    public String assignPrivilege() {
        privilegeService.assignPrivilege(role.getId(), privilegeCb);
        return SUCCESS;
    }

    public IPrivilegeService getPrivilegeService() {
        return privilegeService;
    }

    public void setPrivilegeService(IPrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public PrivilegeVO getPvo() {
        return pvo;
    }

    public void setPvo(PrivilegeVO pvo) {
        this.pvo = pvo;
    }

    public String getTree() {
        return tree;
    }

    public void setTree(String tree) {
        this.tree = tree;
    }

    public boolean getIsPrivilege() {
        return isPrivilege;
    }

    public void setIsPrivilege(boolean isPrivilege) {
        this.isPrivilege = isPrivilege;
    }

    public Privilege getParentPrivilege() {
        return parentPrivilege;
    }

    public void setParentPrivilege(Privilege parentPrivilege) {
        this.parentPrivilege = parentPrivilege;
    }

    public boolean isAddPrivilegeFlag() {
        return addPrivilegeFlag;
    }

    public void setAddPrivilegeFlag(boolean addPrivilegeFlag) {
        this.addPrivilegeFlag = addPrivilegeFlag;
    }

    public boolean isDelPrivilegeFlag() {
        return delPrivilegeFlag;
    }

    public void setDelPrivilegeFlag(boolean delPrivilegeFlag) {
        this.delPrivilegeFlag = delPrivilegeFlag;
    }

    public String[] getPrivilegeCb() {
        return privilegeCb;
    }

    public void setPrivilegeCb(String[] privilegeCb) {
        this.privilegeCb = privilegeCb;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getHasPrivilegeIds() {
        return hasPrivilegeIds;
    }

    public void setHasPrivilegeIds(String hasPrivilegeIds) {
        this.hasPrivilegeIds = hasPrivilegeIds;
    }
}
