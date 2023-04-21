package org.gbif.portal.action;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.gbif.ecat.cfg.DataDirConfig;
import org.gbif.portal.model.Agent;

public class LoginAction extends BaseAction implements ServletRequestAware {

    private String redirectUrl;

    private String email = "Hans";

    private String password;

    private HttpServletRequest request;

    public String execute() {
        currentMenu = "login";
        Agent user = new Agent();
        user.setEmail(email);
        user.setPassword(password);
        user.setAdmin(true);
        if (user != null) {
            log.debug("User " + email + " logged in successfully");
            user.setLastLogin(new Date());
            session.put(DataDirConfig.SESSION_USER, user);
            setRedirectUrl();
            return SUCCESS;
        } else {
            addFieldError("email", "The email - password combination does not exists");
            log.info("User " + email + " failed to log in with password " + password);
        }
        return INPUT;
    }

    private void setRedirectUrl() {
        redirectUrl = getDomain() + "/index/";
        if (request != null) {
            String referer = request.getHeader("Referer");
            if (referer != null && referer.startsWith(getDomain()) && !(referer.endsWith("login") || referer.endsWith("register/save"))) {
                redirectUrl = referer;
            }
        }
        log.info("Redirecting to " + redirectUrl);
    }

    public String logout() {
        setRedirectUrl();
        session.clear();
        return SUCCESS;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.equalsIgnoreCase("email")) {
            this.email = email;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
