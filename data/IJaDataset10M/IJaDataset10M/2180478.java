package spidr.webapp;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.*;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.commons.beanutils.PropertyUtils;
import spidr.datamodel.User;

public final class UserAction extends Action {

    private Logger log = Logger.getLogger("spidr.webapp.UserAction");

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Locale locale = getLocale(request);
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            log.debug("Session is missing or has expired for client from " + request.getRemoteAddr());
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.session.nouser"));
            saveErrors(request, errors);
            return (mapping.findForward("logon"));
        }
        if (log.isDebugEnabled()) {
            log.debug("User '" + user.getLogin() + "' from " + request.getRemoteAddr() + " in session " + session.getId());
        }
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) {
                request.removeAttribute(mapping.getAttribute());
            } else {
                session.removeAttribute(mapping.getAttribute());
            }
        }
        if (user.isAdministrator()) {
            return (mapping.findForward("sme"));
        } else {
            return (mapping.findForward("success"));
        }
    }
}
