package cowsultants.itracker.web.actions;

import java.io.IOException;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import cowsultants.itracker.ejb.client.interfaces.UserHandler;
import cowsultants.itracker.ejb.client.interfaces.UserHandlerHome;
import cowsultants.itracker.ejb.client.util.UserUtilities;

public class UnlockUserAction extends ITrackerAction {

    public UnlockUserAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        if (!isLoggedIn(request, response)) {
            return mapping.findForward("login");
        }
        if (!hasPermission(UserUtilities.PERMISSION_USER_ADMIN, request, response)) {
            return mapping.findForward("unauthorized");
        }
        try {
            InitialContext ic = new InitialContext();
            Object uhRef = ic.lookup("java:comp/env/" + UserHandler.JNDI_NAME);
            UserHandlerHome uhHome = (UserHandlerHome) PortableRemoteObject.narrow(uhRef, UserHandlerHome.class);
            UserHandler uh = uhHome.create();
            Integer userId = new Integer((request.getParameter("id") == null ? "-1" : (request.getParameter("id"))));
            uh.setUserStatus(userId, UserUtilities.STATUS_ACTIVE);
        } catch (Exception e) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("itracker.web.error.system"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return mapping.findForward("listusers");
    }
}
