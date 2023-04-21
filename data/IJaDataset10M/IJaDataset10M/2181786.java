package com.asoft.common.sysframe.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.asoft.common.base.web.service.AbstractListService;
import com.asoft.common.sysframe.define.RoleStatus;
import com.asoft.common.sysframe.manager.RoleManager;
import com.asoft.common.sysframe.view.foreveryone.RoleList4EveryOne;

/**
 * <p>Title: 角色</p>
 * <p>Description: 角色 Role </p>
 * <p>Copyright: Copyright (c) 2004-2006</p>
 * <p>Company: asoft</p>
 * @ $Author: amon.lei $
 * @ $Date: 2007-2-20 $
 * @ $Revision: 1.0 $
 * @ created in 2007-2-20
 *
 */
public class FindRolesService extends AbstractListService {

    static Logger logger = Logger.getLogger(FindRolesService.class);

    private RoleManager roleManager;

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    /** 获取列表的models */
    public List getList(HttpServletRequest request) {
        logger.debug("1. 获取查询条件.....");
        String code = this.getValueFromRequestParameter(request, "code");
        String superRoleId = this.getValueFromRequestParameter(request, "superRoleId");
        String name = this.getValueFromRequestParameter(request, "name");
        String orderBy = this.getValueFromRequestParameter(request, "orderBy", "role.pri");
        String sortType = this.getValueFromRequestParameter(request, "sortType", "asc");
        logger.debug("2. 获取查询条件完毕,开始查询.....");
        return this.roleManager.findRoles(code, superRoleId, name, Integer.toString(RoleStatus.VALID), orderBy, sortType);
    }

    /** model -> viewer */
    public Object getViewer(HttpServletRequest request) {
        return new RoleList4EveryOne();
    }
}
