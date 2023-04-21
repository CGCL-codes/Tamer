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
import org.apache.struts.action.DynaActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author durot
 *
 */
public class DeleteAllAction extends MailDefaultAction {

    /**
     *
     */
    public DeleteAllAction() {
        super();
    }

    protected ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages errors = new ActionMessages();
        try {
            Mail mailInstance = getMailInstance(request);
            DynaActionForm _form = (DynaActionForm) form;
            String folder = (String) _form.get("folder");
            int pos = folder.indexOf("folder:");
            if (pos > -1) {
                folder = folder.substring(pos + 7, folder.length());
                mailInstance.deleteMessagesInFolder(folder);
            } else {
                pos = folder.indexOf("label:");
                if (pos > -1) {
                    folder = folder.substring(pos + 11, folder.length());
                    mailInstance.deleteMessagesInLabel(new Integer(folder));
                } else {
                }
            }
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
