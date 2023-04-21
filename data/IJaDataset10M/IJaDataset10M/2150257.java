package com.companyname.common.sysframe.valid;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.companyname.common.sysframe.define.RoleStatus;
import com.companyname.common.sysframe.manager.RoleManager;
import com.companyname.common.sysframe.model.Role;
import com.companyname.common.sysframe.view.foreveryone.RoleList4EveryOne;
import com.companyname.common.util.mvc.valid.UserValidator;
import com.companyname.common.util.mvc.valid.ValidatedResult;

/**
 * <p>Title: 角色</p>
 * <p>Description: 角色 Role </p>
 * <p>Copyright: Copyright (c) 2004-2006</p>
 * <p>Company: 公司名</p>
 * @ $Author: 作者名 $
 * @ $Date: 创建日期 $
 * @ $Revision: 1.0 $
 * @ created in 创建日期
 *
 */
public class AddRoleValidator extends UserValidator {

    static Logger logger = Logger.getLogger(AddRoleValidator.class);

    /**
         * 新增角色
         *
         * @ param: HttpServletRequest request
         * @ param: ValidatedResult vr 校验结果存放器
         * @ param: String[] params 配置参数
         */
    public void validing(HttpServletRequest request, ValidatedResult vr, String[] params) {
        logger.debug("开始进行新增角色前校验");
        RoleManager roleManager = (RoleManager) this.getBean("roleManager");
        String code = request.getParameter("code");
        if (roleManager.getByCode(code) != null) {
            vr.setErrMess("code", "已被使用");
        }
        this.validString(request, vr, "code", 1, 50);
        String name = this.validString(request, vr, "name", 1, 50);
        Role superRole = null;
        String superRoleId = request.getParameter("parentId");
        if (superRoleId != null) {
            superRole = (Role) this.validBaseObjectById(request, vr, roleManager, "parentId", "请重新选择");
        }
        String remark = this.validString(request, vr, "remark", 0, 500);
        if (vr.isAllValidated()) {
            logger.debug("通过校验");
            Role role = new Role();
            role.setCode(code);
            role.setName(name);
            role.setSuperRole(superRole);
            role.setRemark(remark);
            role.setStatus(RoleStatus.VALID);
            request.setAttribute(Role.class.getName(), role);
        }
    }
}
