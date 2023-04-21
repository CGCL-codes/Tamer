package struts.optional.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import struts.optional.ActionHelper;

public class TokenInterceptor implements WebInterceptorInterface {

    public ActionForward beforeMethod(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter("TOKEN");
        if (token == null) return null;
        HttpSession session = request.getSession();
        ActionForward fw = null;
        if (session.getAttribute(token) != null) {
            ActionHelper helper = ActionHelper.getInstance();
            helper.addError("strutter.invalidtoken");
            fw = mapping.findForward("invalidtoken");
        }
        session.setAttribute(token, new Object());
        return fw;
    }

    public ActionForward afterMethod(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    public ActionForward afterView(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    public ActionForward beforeView(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
