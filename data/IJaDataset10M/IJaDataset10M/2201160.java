package org.itracker.web.actions.admin.configuration;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.itracker.services.util.UserUtilities;
import org.itracker.web.actions.base.ItrackerBaseAction;
import org.itracker.web.forms.ImportForm;

public class ImportDataFormAction extends ItrackerBaseAction {

    private static final Logger log = Logger.getLogger(ImportDataFormAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionMessages errors = new ActionMessages();
        if (!hasPermission(UserUtilities.PERMISSION_USER_ADMIN, request, response)) {
            return mapping.findForward("unauthorized");
        }
        try {
            ImportForm importForm = (ImportForm) form;
            if (importForm == null) {
                importForm = new ImportForm();
            }
            importForm.setOptionreuseusers(Boolean.TRUE);
            importForm.setOptionreuseprojects(Boolean.TRUE);
            importForm.setOptionreuseconfig(Boolean.TRUE);
            importForm.setOptionreusefields(Boolean.TRUE);
            importForm.setOptioncreatepasswords(Boolean.TRUE);
            request.setAttribute("importForm", importForm);
            saveToken(request);
            return mapping.getInputForward();
        } catch (Exception e) {
            log.error("Exception while creating import data form.", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.system"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return mapping.findForward("error");
    }
}
