package org.opentides.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opentides.controller.ChangePasswordController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opentides.service.UserService;
import org.opentides.util.StringUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.opentides.bean.PasswordReset;

/**
 * @author allanctan
 *
 */
public class ChangePasswordController extends SimpleFormController {

    private static Log _log = LogFactory.getLog(ChangePasswordController.class);

    private UserService userService;

    @SuppressWarnings("unchecked")
    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map model = new HashMap<String, String>();
        model.put("action", "change");
        return model;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        PasswordReset passwd = (PasswordReset) command;
        boolean success = false;
        String secureCode = (String) request.getSession().getAttribute(ConfirmPasswordResetController.SECURE_SESSION_KEY);
        if (!StringUtil.isEmpty(secureCode) && secureCode.startsWith(ConfirmPasswordResetController.SECURE_SESSION_CODE) && (passwd.getEmailAddress().equals(secureCode.substring(ConfirmPasswordResetController.SECURE_SESSION_CODE.length())))) {
            success = userService.resetPassword(passwd);
        }
        if (success) {
            _log.info("Password changed. " + passwd.getEmailAddress() + " has successfully changed password.");
            Map<String, String> model = new HashMap<String, String>();
            model.put("message", "msg.password-change-successful");
            model.put("title", "label.forgot-password");
            return new ModelAndView(getSuccessView(), model);
        } else {
            Map<String, String> model = new HashMap<String, String>();
            _log.info("Unauthorized access to reset password using email [" + passwd.getEmailAddress() + "] and secureCode [" + secureCode + "] from IP " + request.getRemoteAddr());
            model.put("message", "error.unauthorized-access-to-change-password");
            model.put("title", "label.forgot-password");
            return new ModelAndView(getSuccessView(), model);
        }
    }

    /**
	 * @param userService the userService to set
	 */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
