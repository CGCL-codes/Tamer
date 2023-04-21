package cowsultants.itracker.web.actions;

import java.io.IOException;
import java.util.HashMap;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import cowsultants.itracker.ejb.client.interfaces.IssueHandler;
import cowsultants.itracker.ejb.client.interfaces.IssueHandlerHome;
import cowsultants.itracker.ejb.client.interfaces.ProjectHandler;
import cowsultants.itracker.ejb.client.interfaces.ProjectHandlerHome;
import cowsultants.itracker.ejb.client.models.ProjectModel;
import cowsultants.itracker.ejb.client.models.UserModel;
import cowsultants.itracker.ejb.client.util.NotificationUtilities;
import cowsultants.itracker.ejb.client.util.ProjectUtilities;
import cowsultants.itracker.ejb.client.util.UserUtilities;
import cowsultants.itracker.web.util.Constants;

public class AssignIssueAction extends ITrackerAction {

    public AssignIssueAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        if (!isLoggedIn(request, response)) {
            return mapping.findForward("login");
        }
        try {
            InitialContext ic = new InitialContext();
            Object ihRef = ic.lookup("java:comp/env/" + IssueHandler.JNDI_NAME);
            IssueHandlerHome ihHome = (IssueHandlerHome) PortableRemoteObject.narrow(ihRef, IssueHandlerHome.class);
            IssueHandler ih = ihHome.create();
            Object phRef = ic.lookup("java:comp/env/" + ProjectHandler.JNDI_NAME);
            ProjectHandlerHome phHome = (ProjectHandlerHome) PortableRemoteObject.narrow(phRef, ProjectHandlerHome.class);
            ProjectHandler ph = phHome.create();
            Integer defaultValue = new Integer(-1);
            IntegerConverter converter = new IntegerConverter(defaultValue);
            Integer issueId = (Integer) converter.convert(Integer.class, (String) PropertyUtils.getSimpleProperty(form, "issueId"));
            Integer projectId = (Integer) converter.convert(Integer.class, (String) PropertyUtils.getSimpleProperty(form, "projectId"));
            Integer userId = (Integer) converter.convert(Integer.class, (String) PropertyUtils.getSimpleProperty(form, "userId"));
            HttpSession session = request.getSession(true);
            UserModel currUser = (UserModel) session.getAttribute(Constants.USER_KEY);
            HashMap userPermissions = (HashMap) session.getAttribute(Constants.PERMISSIONS_KEY);
            Integer currUserId = currUser.getId();
            ProjectModel project = ph.getProject(projectId);
            if (project == null) {
                return mapping.findForward("unauthorized");
            }
            if (!userId.equals(currUserId) && !UserUtilities.hasPermission(userPermissions, projectId, UserUtilities.PERMISSION_ASSIGN_OTHERS)) {
                return mapping.findForward("unauthorized");
            } else if (!UserUtilities.hasPermission(userPermissions, projectId, UserUtilities.PERMISSION_ASSIGN_SELF)) {
                return mapping.findForward("unauthorized");
            }
            if (project.getStatus() != ProjectUtilities.STATUS_ACTIVE) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("itracker.web.error.projectlocked"));
            } else {
                ih.assignIssue(issueId, userId, currUserId);
                ih.sendNotification(issueId, NotificationUtilities.TYPE_ASSIGNED, getBaseURL(request));
            }
        } catch (Exception e) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("itracker.web.error.system"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            saveToken(request);
        }
        return mapping.findForward("index");
    }
}
