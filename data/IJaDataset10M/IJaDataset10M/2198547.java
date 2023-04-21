package org.wdcode.back.action.ajax;

import java.util.List;
import org.wdcode.back.helper.DepartmentHelper;
import org.wdcode.back.helper.OperateHelper;
import org.wdcode.back.po.Operate;
import org.wdcode.base.struts.action.BaseAjaxAction;

/**
 * Ajax提交的Action
 * @author WD
 * @since JDK6
 * @version 1.0 2009-10-13
 */
@SuppressWarnings("unchecked")
public final class BackAjaxAction extends BaseAjaxAction {

    private static final long serialVersionUID = -956938676938951523L;

    /**
	 * 获得公司下的所有部门
	 * @return
	 * @throws Exception
	 */
    public String depaByComp() throws Exception {
        return ajax(DepartmentHelper.getHelper().getByComp(getId()));
    }

    /**
	 * 根据角色获得操作
	 * @return
	 * @throws Exception
	 */
    public String operByRole() throws Exception {
        List<Operate> list = OperateHelper.getHelper().getByRole(getId());
        return ajax(list, OperateHelper.getHelper().getNotList(list));
    }
}
