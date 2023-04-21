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
import cowsultants.itracker.ejb.client.interfaces.SystemConfiguration;
import cowsultants.itracker.ejb.client.interfaces.SystemConfigurationHome;
import cowsultants.itracker.ejb.client.util.Logger;
import cowsultants.itracker.ejb.client.util.SystemConfigurationUtilities;
import cowsultants.itracker.ejb.client.util.UserUtilities;

public class InitializeLanguagesAction extends ITrackerAction {

    public InitializeLanguagesAction() {
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
            Object scRef = ic.lookup("java:comp/env/" + SystemConfiguration.JNDI_NAME);
            SystemConfigurationHome scHome = (SystemConfigurationHome) PortableRemoteObject.narrow(scRef, SystemConfigurationHome.class);
            SystemConfiguration sc = scHome.create();
            SystemConfigurationUtilities.initializeAllLanguages(sc, true);
            return mapping.getInputForward();
        } catch (Exception e) {
            Logger.logError("Exception while reinitializing languages.", e);
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("itracker.web.error.system"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return mapping.findForward("error");
    }
}
