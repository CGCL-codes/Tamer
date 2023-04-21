package jp.co.msr.osaka.umgr.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.msr.osaka.umgr.entity.Role;
import jp.co.msr.osaka.umgr.service.RoleService;
import jp.co.msr.osaka.umgr.service.ServiceFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * ロール一覧を表示するためのアクション
 * 
 * @author fu-yuka
 * 
 */
public class RoleListAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        RoleService roleService = serviceFactory.createRoleService();
        List<Role> roleList = roleService.findAllRole();
        request.setAttribute("roleList", roleList);
        return mapping.findForward("success");
    }
}
