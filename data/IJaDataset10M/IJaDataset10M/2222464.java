package org.wdcode.back.action.role;

import org.wdcode.back.action.BaseBackQueryHelperAction;
import org.wdcode.back.po.Role;
import org.wdcode.back.service.RoleService;

/**
 * 查询角色管理Action
 * @author WD
 * @since JDK6
 * @version 1.0 2009-09-23
 */
public final class RoleQueryAction extends BaseBackQueryHelperAction<RoleService, Role> {

    private static final long serialVersionUID = -4402894896286314540L;

    /**
	 * 修改权限
	 * @return
	 * @throws Exception
	 */
    public String limit() throws Exception {
        return SUCCESS;
    }
}
