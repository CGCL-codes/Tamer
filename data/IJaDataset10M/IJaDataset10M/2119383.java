package com.berd.components.security.web;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.berd.components.security.model.Menu;
import com.berd.components.security.model.MenuRole;
import com.berd.components.security.model.Role;
import com.berd.components.security.service.MenuRoleManager;
import com.berd.components.security.service.RoleManager;
import com.berd.components.security.utils.TreeFactory;
import com.berd.core.exception.BusinessException;
import com.berd.core.web.StrutsEntityAction;
import com.berd.core.utils.StringUtil;

public class MenuRoleManageAction extends StrutsEntityAction<Menu, MenuRoleManager> {

    private static final long serialVersionUID = 16978L;

    private String type;

    private String roleId;

    private String menuId;

    private String itemselect;

    private List<Role> roles;

    private RoleManager roleManager;

    private TreeFactory treeFactory;

    private MenuRoleManager menuRoleManager;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemselect() {
        return itemselect;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setItemselect(String itemselect) {
        this.itemselect = itemselect;
    }

    public void setMenuRoleManager(MenuRoleManager menuRoleManager) {
        this.menuRoleManager = menuRoleManager;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void setTreeFactory(TreeFactory treeFactory) {
        this.treeFactory = treeFactory;
    }

    public String list() {
        try {
            getRequest().setAttribute("roles", roleManager.getAllRole());
            String roleId = this.getRoleId();
            if (!StringUtil.isEmpty(roleId)) {
                this.setRoleId(roleId);
            } else {
                this.setRoleId("");
                return SUCCESS;
            }
            creatorTree();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            saveError("树处理错误！");
            return INPUT;
        }
        return SUCCESS;
    }

    public void buildTree() {
        try {
            this.setRoles(roleManager.getAllRole());
            String roleId = this.getRoleId();
            if (StringUtils.isNotBlank(roleId)) {
                this.setRoleId(roleId);
            } else {
                this.setRoleId("");
            }
            creatorTree();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            saveError("树处理错误！");
        }
    }

    private void creatorTree() {
        String type = "checkbox";
        int treeType = TreeFactory.CHECKBOX_TREE;
        if (StringUtils.isNotBlank(type) && StringUtils.isNumeric(type)) treeType = new Integer(type);
        String roleId = this.getRoleId();
        String iRoleId = null;
        if (StringUtils.isNotBlank(roleId)) {
            iRoleId = roleId;
        }
        String menuId = this.getMenuId();
        String iMenuId = null;
        if (StringUtils.isNotBlank(menuId)) {
            iMenuId = menuId;
        }
        String treeText = (String) treeFactory.creator(treeType, iRoleId, iMenuId);
        getRequest().setAttribute("treeText", treeText);
    }

    public String save() {
        try {
            String roleId = this.getRoleId();
            if (StringUtil.isEmpty(roleId)) {
                throw new BusinessException("请选择角色组");
            }
            List menus = menuRoleManager.loadByRoleId(roleId);
            String[] itemlist = StringUtils.split(this.getItemselect(), ",");
            if (itemlist != null) {
                for (int k = 0; k < itemlist.length; k++) {
                    MenuRole menuRole = new MenuRole();
                    menuRole.setMenuId(itemlist[k]);
                    menuRole.setRoleId(roleId);
                    if (menus.contains(menuRole)) {
                        menus.remove(menuRole);
                    } else {
                        menuRoleManager.save(menuRole);
                    }
                }
            }
            for (int k = 0; k < menus.size(); k++) {
                menuRoleManager.remove(menus.get(k));
            }
            this.setRoleId(roleId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            saveError(e.getMessage());
            refrenceData(getRequest());
            return INPUT;
        }
        return list();
    }
}
