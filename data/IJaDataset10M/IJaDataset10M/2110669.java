package net.sourceforge.eci.webapp.controller;

import org.springframework.security.AccessDeniedException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import net.sourceforge.eci.Constants;
import net.sourceforge.eci.model.User;
import net.sourceforge.eci.service.RoleManager;
import net.sourceforge.eci.service.UserExistsException;
import net.sourceforge.eci.webapp.util.RequestUtil;
import org.springframework.mail.MailException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class SignupController extends BaseFormController {

    private RoleManager roleManager;

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public SignupController() {
        setCommandName("user");
        setCommandClass(User.class);
    }

    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }
        User user = (User) command;
        Locale locale = request.getLocale();
        user.setEnabled(true);
        user.addRole(roleManager.getRole(Constants.USER_ROLE));
        try {
            this.getUserManager().saveUser(user);
        } catch (AccessDeniedException ade) {
            log.warn(ade.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } catch (UserExistsException e) {
            errors.rejectValue("username", "errors.existing.user", new Object[] { user.getUsername(), user.getEmail() }, "duplicate user");
            user.setPassword(user.getConfirmPassword());
            return showForm(request, response, errors);
        }
        saveMessage(request, getText("user.registered", user.getUsername(), locale));
        request.getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getConfirmPassword(), user.getAuthorities());
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + user.getUsername() + "' an account information e-mail");
        }
        message.setSubject(getText("signup.email.subject", locale));
        try {
            sendUserMessage(user, getText("signup.email.message", locale), RequestUtil.getAppURL(request));
        } catch (MailException me) {
            saveError(request, me.getMostSpecificCause().getMessage());
        }
        return new ModelAndView(getSuccessView());
    }
}
