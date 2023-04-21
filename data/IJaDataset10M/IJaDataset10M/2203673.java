package com.ddh.app.webapp.action;

import org.springframework.security.AccessDeniedException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.apache.struts2.ServletActionContext;
import com.ddh.app.Constants;
import com.ddh.app.model.User;
import com.ddh.app.service.UserExistsException;
import com.ddh.app.webapp.util.RequestUtil;
import org.springframework.mail.MailException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Action to allow new users to sign up.
 */
public class SignupAction extends BaseAction {

    private static final long serialVersionUID = 6558317334878272308L;

    private User user;

    private String cancel;

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Return an instance of the user - to display when validation errors occur
     * @return a populated user
     */
    public User getUser() {
        return user;
    }

    /**
     * When method=GET, "input" is returned. Otherwise, "success" is returned.
     * @return cancel, input or success
     */
    public String execute() {
        if (cancel != null) {
            return CANCEL;
        }
        if (ServletActionContext.getRequest().getMethod().equals("GET")) {
            return INPUT;
        }
        return SUCCESS;
    }

    /**
     * Returns "input"
     * @return "input" by default
     */
    public String doDefault() {
        return INPUT;
    }

    /**
     * Save the user, encrypting their passwords if necessary
     * @return success when good things happen
     * @throws Exception when bad things happen
     */
    public String save() throws Exception {
        user.setEnabled(true);
        user.addRole(roleManager.getRole(Constants.USER_ROLE));
        try {
            userManager.saveUser(user);
        } catch (AccessDeniedException ade) {
            log.warn(ade.getMessage());
            getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } catch (UserExistsException e) {
            log.warn(e.getMessage());
            List<String> args = new ArrayList<String>();
            args.add(user.getUsername());
            args.add(user.getEmail());
            addActionError(getText("errors.existing.user", args));
            user.setPassword(user.getConfirmPassword());
            return INPUT;
        }
        saveMessage(getText("user.registered"));
        getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getConfirmPassword(), user.getAuthorities());
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
        mailMessage.setSubject(getText("signup.email.subject"));
        try {
            sendUserMessage(user, getText("signup.email.message"), RequestUtil.getAppURL(getRequest()));
        } catch (MailException me) {
            addActionError(me.getMostSpecificCause().getMessage());
        }
        return SUCCESS;
    }
}
