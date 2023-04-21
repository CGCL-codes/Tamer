package de.cue4net.eventservice.controller.user;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.acegisecurity.providers.encoding.ShaPasswordEncoder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import de.cue4net.eventservice.Globals;
import de.cue4net.eventservice.controller.command.PasswordResetFormCommand;
import de.cue4net.eventservice.email.PasswordResetEmail;
import de.cue4net.eventservice.model.user.User;
import de.cue4net.eventservice.model.user.dao.LoginManager;
import de.cue4net.eventservice.model.user.dao.UserAccountManager;
import de.cue4net.eventservice.util.RandomTools;

/**
 * Password Reset Form Controller
 *
 * @author Thorsten Vogel
 * @version $Id: PasswordResetFormController.java,v 1.2 2007/02/15 00:37:33 tv
 *          Exp $
 */
public class PasswordResetFormController extends SimpleFormController implements InitializingBean {

    private UserAccountManager accountManager;

    private LoginManager loginManager;

    private PasswordResetEmail emailSender;

    private ShaPasswordEncoder passwordEncoder;

    public PasswordResetFormController() {
        setValidateOnBinding(true);
    }

    public void afterPropertiesSet() throws Exception {
        if (accountManager == null) {
            throw new IllegalStateException("Must set accountManager bean property.");
        }
        if (loginManager == null) {
            throw new IllegalStateException("Must set loginManager bean property.");
        }
        if (emailSender == null) {
            throw new IllegalStateException("Must set emailSender bean property.");
        }
        if (passwordEncoder == null) {
            throw new IllegalStateException("Must set passwordEncoder bean property.");
        }
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        PasswordResetFormCommand profileForm = (PasswordResetFormCommand) command;
        Boolean success = Boolean.FALSE;
        if (isFormSubmission(request)) {
            User target = accountManager.getUserByEmailAddress(profileForm.getEmail());
            String newPassword = RandomTools.getRandomString(6).toLowerCase();
            String cryptedPassword = passwordEncoder.encodePassword(newPassword, null);
            target.getLogin().setPassword(cryptedPassword);
            emailSender.setMessageSourceAccessor(getMessageSourceAccessor());
            if (getEmailSender().sendEmail(target, newPassword)) {
                logger.debug("Email with new password sent to user with id " + target.getId());
                accountManager.saveOrUpdate(target);
                logger.debug("Updated password for user with id " + target.getId());
                success = Boolean.TRUE;
            }
            request.getSession().setAttribute(Globals.SESSION_TOKEN_KEY, null);
        }
        return new ModelAndView(getSuccessView(), "passwordresetsuccess", success);
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        PasswordResetFormCommand prForm = new PasswordResetFormCommand();
        String tokenString = RandomTools.getRandomString(6).toUpperCase();
        request.getSession().setAttribute(Globals.SESSION_TOKEN_KEY, tokenString);
        prForm.setTokenString(tokenString);
        return prForm;
    }

    public PasswordResetEmail getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(PasswordResetEmail emailSender) {
        this.emailSender = emailSender;
    }

    public UserAccountManager getAccountManager() {
        return this.accountManager;
    }

    public void setAccountManager(UserAccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public LoginManager getLoginManager() {
        return this.loginManager;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public void setPasswordEncoder(ShaPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
