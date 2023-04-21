package edu.hawaii.myisern.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.util.Log;
import edu.hawaii.myisern.model.IsernManager;
import edu.hawaii.myisern.model.User;

/**
 * Handles user login to MyIsern system.
 * Modeled after the Bugzooky example.
 *
 * @author Tim Fennell
 * @author Lisa Chen
 */
public class LoginActionBean extends IsernActionBean {

    /** Logging debug output. */
    protected static Log log = Log.getInstance(IsernActionBean.class);

    /** User's login name. */
    @Validate(field = "username", required = true)
    private String username;

    /** User's login password. */
    @Validate(field = "password", required = true)
    private String password;

    /** Url user is attempting to reach. */
    private String targetUrl;

    /**
   * Set user's login name.
   * 
   * @param username User's login name.
   */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
   * Get the user's login name.
   * @return User's login name.
   */
    public String getUsername() {
        return this.username;
    }

    /**
   * Set user's login password.
   * 
   * @param password User's login password.
   */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
   * Get user's login password.
   * 
   * @return User's password.
   */
    public String getPassword() {
        return this.password;
    }

    /** The URL the user was trying to access 
   * (null if the login page was accessed directly).
   * 
   * @return Url user is attempting to access.
   */
    public String getTargetUrl() {
        return targetUrl;
    }

    /** The URL the user was trying to access 
   * (null if the login page was accessed directly).
   * 
   * @param targetUrl Url user is attempting to access.
   */
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    /**
   * Allows user login with correct username and password,
   * otherwise redirect to the login page with appropriate Stripes error.
   * 
   * @throws Exception if something goes wrong.
   * @return The next appropriate jsp page.
   */
    @DefaultHandler
    public Resolution login() throws Exception {
        log.debug("they called login");
        User user = new User(IsernManager.getAppConfig().getUsername(), IsernManager.getAppConfig().getPassword());
        log.debug("username = " + user.getUsername());
        log.debug("password = " + user.getPassword());
        if (user.getUsername().equals(this.username)) {
            if (user.getPassword().equals(this.password)) {
                log.debug("here's the context at successful login validation: " + getContext().toString());
                getContext().getRequest().getSession().setAttribute("user", user);
                return new RedirectResolution("/organizations.jsp");
            } else {
                ValidationError error = new SimpleError("Invalid Password");
                log.debug("here's the context at invalid password: " + getContext().toString());
                getContext().getValidationErrors().add("password", error);
                return getContext().getSourcePageResolution();
            }
        } else {
            if (user.getPassword().equals(this.password)) {
                ValidationError error = new SimpleError("Invalid User Name");
                log.debug("here's the context at invalid user: " + getContext().toString());
                getContext().getValidationErrors().add("username", error);
                return getContext().getSourcePageResolution();
            } else {
                ValidationError error = new SimpleError("Invalid User Name");
                getContext().getValidationErrors().add("username", error);
                log.debug("here's the context at invalid user and password: " + getContext().toString());
                error = new SimpleError("Invalid Password");
                getContext().getValidationErrors().add("password", error);
                log.debug("here's the context at invalid user and password: " + getContext().toString());
                return getContext().getSourcePageResolution();
            }
        }
    }
}
