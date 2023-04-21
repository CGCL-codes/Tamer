package com.asoft.common.sysframe.service;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.asoft.common.base.web.service.AbstractTreeService;
import com.asoft.common.sysframe.manager.RoleManager;
import com.asoft.common.sysframe.model.Role;
import com.asoft.common.sysframe.view.foreveryone.RoleNodeOfRoleTree4EveryOne;
import com.asoft.common.sysframe.view.foreveryone.RootNodeOfRoleTree4EveryOne;

/**
 * <p>Title:  - 查询角色,将结果通过树图表现</p>
 * <p>Description:
 *      如果有多种节点类型(根节点除外),必须传
 *      递多个根节点实体
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: asoft</p>
 * @ $Author: author $
 * @ $Date: 2005/02/15 09:14:59 $
 * @ $Revision: 1.7 $
 * @ created in 2005-11-14
 *
 */
public class FindRolesForTreeViewService extends AbstractTreeService {

    /** 节点类型－根结点 */
    public static String ROOTNODE = "0";

    /** 节点类型－ 角色：非根节点 */
    public static String ROLE = "1";

    static Logger logger = Logger.getLogger(FindRolesForTreeViewService.class);

    private RoleManager roleManager;

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    /** 获取树节点对应的models */
    public void createModels(Map modelsMap, HttpServletRequest request) {
        logger.debug("1. 获取查询条件.....");
        String roleId = this.getValueFromRequestParameter(request, "roleId", null);
        logger.debug("2. 获取查询条件完毕,开始查询.....");
        Role rootRole;
        if (roleId == null) {
            Set roles = new LinkedHashSet(this.roleManager.findRoles(null));
            rootRole = new Role();
            rootRole.setSubRoles(roles);
        } else {
            rootRole = (Role) this.roleManager.get(roleId);
        }
        modelsMap.put(ROOTNODE, rootRole);
        modelsMap.put(ROLE, rootRole.getAllSubRoles());
    }

    /** 设置nodeModel's views */
    public void setNodeViews(Map viewsMap, HttpServletRequest request) {
        viewsMap.put(ROOTNODE, new RootNodeOfRoleTree4EveryOne());
        viewsMap.put(ROLE, new RoleNodeOfRoleTree4EveryOne());
    }
}
