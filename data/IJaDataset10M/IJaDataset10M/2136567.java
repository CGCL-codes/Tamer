package com.duroty.application.mail.actions;

import com.duroty.application.mail.interfaces.Mail;
import com.duroty.application.mail.utils.MailDefaultAction;
import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;
import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author durot
 *
 */
public class PropertiesAction extends MailDefaultAction {

    /**
     * DOCUMENT ME!
     */
    private static final String PROPERTIES = "properties";

    /**
     * Creates a new PropertiesAction object.
     */
    public PropertiesAction() {
        super();
    }

    protected ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages errors = new ActionMessages();
        try {
            Mail mailInstance = getMailInstance(request);
            String mid = request.getParameter("mid");
            String properties = mailInstance.displayProperties(mid);
            if (properties != null) {
                properties = properties.replaceAll("\n", "<br/>");
            }
            request.setAttribute(PROPERTIES, properties);
        } catch (Exception ex) {
            String errorMessage = ExceptionUtilities.parseMessage(ex);
            if (errorMessage == null) {
                errorMessage = "NullPointerException";
            }
            errors.add("general", new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX + "general", errorMessage));
            request.setAttribute("exception", errorMessage);
            doTrace(request, DLog.ERROR, getClass(), errorMessage);
        } finally {
        }
        if (errors.isEmpty()) {
            doTrace(request, DLog.INFO, getClass(), "OK");
            return mapping.findForward(Constants.ACTION_SUCCESS_FORWARD);
        } else {
            saveErrors(request, errors);
            return mapping.findForward(Constants.ACTION_FAIL_FORWARD);
        }
    }

    protected void doTrace(HttpServletRequest request, int level, Class classe, String message) throws Exception {
        DLog.log(level, classe, DMessage.toString(request, message));
    }
}
