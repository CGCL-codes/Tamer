package com.duroty.controller.actions;

import com.duroty.application.admin.utils.UserObj;
import com.duroty.application.open.interfaces.Open;
import com.duroty.application.open.interfaces.OpenHome;
import com.duroty.application.open.interfaces.OpenUtil;
import com.duroty.config.Configuration;
import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;
import com.duroty.controller.singleton.CaptchaServiceSingleton;
import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;
import com.octo.captcha.service.CaptchaServiceException;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import java.rmi.RemoteException;
import java.util.Hashtable;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author durot
 *
 */
public class RegisterAction extends DefaultAction {

    /**
     *
     */
    public RegisterAction() {
        super();
    }

    protected void doInit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    }

    protected ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages errors = new ActionMessages();
        try {
            DynaActionForm _form = (DynaActionForm) form;
            Boolean isResponseCorrect = Boolean.FALSE;
            String captchaId = request.getSession().getId();
            String captchResponse = (String) _form.get("j_captcha_response");
            try {
                isResponseCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(captchaId, captchResponse);
            } catch (CaptchaServiceException e) {
            }
            if (!isResponseCorrect.booleanValue()) {
                errors.add("general", new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX + "general", "general.captcha.error"));
                request.setAttribute("exception", "general.captcha.error");
                doTrace(request, DLog.ERROR, getClass(), "general.captcha.error");
            } else {
                Open openInstance = getOpenInstance(request);
                String username = (String) _form.get("username");
                if (StringUtils.isBlank(username)) {
                    throw new Exception("ErrorMessages.username.required");
                }
                if (openInstance.existUser(username)) {
                    throw new Exception("ErrorMessages.username.exist");
                }
                String email = (String) _form.get("email");
                if (StringUtils.isBlank(email)) {
                    throw new Exception("ErrorMessages.email.required");
                }
                String password = (String) _form.get("password");
                String confirmPassword = (String) _form.get("confirmPassword");
                confirmPassword(password, confirmPassword);
                UserObj userObj = new UserObj();
                userObj.setName((String) _form.get("name"));
                userObj.setEmail(email);
                String language = (String) _form.get("language");
                if (StringUtils.isBlank(language)) {
                    language = "en";
                }
                userObj.setLanguage(language);
                userObj.setUsername(username);
                userObj.setPassword(password);
                openInstance.register(userObj);
            }
        } catch (Exception ex) {
            String errorMessage = ExceptionUtilities.parseMessage(ex);
            if (errorMessage == null) {
                errorMessage = "NullPointerException";
            }
            errors.add("general", new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX + "general", errorMessage));
            request.setAttribute("exception", errorMessage);
            doTrace(request, DLog.ERROR, getClass(), errorMessage);
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

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NamingException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     * @throws CreateException DOCUMENT ME!
     */
    protected Open getOpenInstance(HttpServletRequest request) throws NamingException, RemoteException, CreateException {
        OpenHome home = null;
        Boolean localServer = new Boolean(Configuration.properties.getProperty(Configuration.LOCAL_WEB_SERVER));
        if (localServer.booleanValue()) {
            home = OpenUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = OpenUtil.getHome(environment);
        }
        return home.create();
    }

    /**
     * DOCUMENT ME!
     *
     * @param password DOCUMENT ME!
     * @param confirmPassword DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected boolean confirmPassword(String password, String confirmPassword) throws Exception {
        try {
            if (StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
                throw new Exception("ErrorMessages.password.required");
            }
            password = password.trim();
            confirmPassword = confirmPassword.trim();
            if (!password.equals(confirmPassword)) {
                throw new Exception("ErrorMessages.password.confirm");
            }
            return true;
        } finally {
        }
    }
}
