package cn.sharezoo.struts.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.struts.ActionSupport;
import cn.sharezoo.service.UserService;

public class UserDeleteAction extends ActionSupport {

    static final Logger log = Logger.getLogger(UserDeleteAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = new Long(0);
        if (StringUtils.isNumeric(request.getParameter("id"))) {
            id = new Long(request.getParameter("id"));
        }
        WebApplicationContext ctx = getWebApplicationContext();
        UserService userService = (UserService) ctx.getBean("userService");
        String type = request.getParameter("type");
        ActionMessages errors = new ActionMessages();
        if ("coach".equalsIgnoreCase(type)) {
            try {
                userService.deleteCoach(id);
            } catch (RuntimeException e) {
                ActionMessage msg = new ActionMessage("error.group.cannot.delete");
                log.debug("error.group.cannot.delete");
                errors.add("cannot.delete", msg);
            }
        } else if ("student".equalsIgnoreCase(type)) {
            userService.deleteUser(id);
        } else if ("admin".equalsIgnoreCase(type)) {
            userService.deleteAdmin(id);
        }
        if (errors.size() > 0) {
            log.debug("redirect to input");
            addErrors(request, errors);
            RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/userManager/fetchUsers.do?type=" + type);
            dispatch.forward(request, response);
        }
        return mapping.findForward(type);
    }
}
