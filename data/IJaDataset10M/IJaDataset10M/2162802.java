package my.learning.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class AjaxAction extends DispatchAction {

    public ActionForward check(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");
        if ("admin".equals(name)) {
            response.getOutputStream().println("exist");
        } else {
            response.getWriter().println("用户名未存在");
        }
        return null;
    }
}
