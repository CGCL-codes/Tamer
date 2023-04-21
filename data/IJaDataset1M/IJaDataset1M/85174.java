package com.duroty.application.admin.actions;

import com.duroty.application.admin.interfaces.Admin;
import com.duroty.application.admin.utils.AdminDefaultAction;
import com.duroty.application.admin.utils.UserObj;
import com.duroty.application.chat.exceptions.ChatException;
import com.duroty.application.chat.exceptions.NotAcceptChatException;
import com.duroty.application.chat.exceptions.NotLoggedInException;
import com.duroty.application.chat.exceptions.NotOnlineException;
import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;
import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author durot
 *
 */
public class UpdateUserAction extends AdminDefaultAction {

    /**
     *
     */
    public UpdateUserAction() {
        super();
    }

    protected ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages errors = new ActionMessages();
        try {
            DynaActionForm _form = (DynaActionForm) form;
            Admin adminInstance = getAdminInstance(request);
            UserObj userObj = new UserObj();
            String password = (String) _form.get("password");
            String confirmPassword = (String) _form.get("confirmPassword");
            if (!StringUtils.isBlank(password)) {
                adminInstance.confirmPassword(password, confirmPassword);
                userObj.setPassword(password);
            }
            userObj.setIdint(((Integer) _form.get("idint")).intValue());
            Boolean active = new Boolean(false);
            if (_form.get("active") != null) {
                active = (Boolean) _form.get("active");
            }
            userObj.setActive(active.booleanValue());
            userObj.setEmail((String) _form.get("email"));
            userObj.setEmailIdentity((String) _form.get("emailIdentity"));
            Boolean htmlMessages = new Boolean(false);
            if (_form.get("htmlMessages") != null) {
                htmlMessages = (Boolean) _form.get("htmlMessages");
            }
            userObj.setHtmlMessages(htmlMessages.booleanValue());
            userObj.setLanguage((String) _form.get("language"));
            userObj.setMessagesByPage(((Integer) _form.get("byPage")).intValue());
            userObj.setName((String) _form.get("name"));
            userObj.setQuotaSize(((Integer) _form.get("quotaSize")).intValue());
            userObj.setRegisterDate(new Date());
            userObj.setRoles((Integer[]) _form.get("roles"));
            userObj.setSignature((String) _form.get("signature"));
            Boolean spamTolerance = new Boolean(false);
            if (_form.get("spamTolerance") != null) {
                spamTolerance = (Boolean) _form.get("spamTolerance");
            }
            userObj.setSpamTolerance(spamTolerance.booleanValue());
            userObj.setVacationBody((String) _form.get("vacationBody"));
            userObj.setVacationSubject((String) _form.get("vacationSubject"));
            Boolean vacationActive = new Boolean(false);
            if (_form.get("vacationActive") != null) {
                vacationActive = (Boolean) _form.get("vacationActive");
            }
            userObj.setVactionActive(vacationActive.booleanValue());
            adminInstance.updateUser(userObj);
        } catch (Exception ex) {
            if (ex instanceof ChatException) {
                if (ex.getCause() instanceof NotOnlineException) {
                    request.setAttribute("result", "not_online");
                } else if (ex.getCause() instanceof NotLoggedInException) {
                    request.setAttribute("result", "not_logged_in");
                } else if (ex.getCause() instanceof NotAcceptChatException) {
                    request.setAttribute("result", "not_accept_chat");
                } else {
                    request.setAttribute("result", ex.getMessage());
                }
            } else {
                String errorMessage = ExceptionUtilities.parseMessage(ex);
                if (errorMessage == null) {
                    errorMessage = "NullPointerException";
                }
                request.setAttribute("result", errorMessage);
                errors.add("general", new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX + "general", errorMessage));
                request.setAttribute("exception", errorMessage);
                doTrace(request, DLog.ERROR, getClass(), errorMessage);
            }
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
