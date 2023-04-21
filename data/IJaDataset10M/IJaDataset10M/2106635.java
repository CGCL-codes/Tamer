package portlets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.supcon.tinyportal.util.Constants;

/**
 * @author michael
 * 
 */
public class LoginHandler extends ActionSupport {

    private static Log logger = LogFactory.getLog(LoginHandler.class);

    private static final long serialVersionUID = 1L;

    public String login() {
        logger.info("login invoking...");
        if (username != null && password != null && username.equals("michael") && password.equals("asdf")) {
            logger.info("login success");
            ServletActionContext.getContext().getSession().put("USER", getUsername());
        } else {
            logger.info("login error");
            ServletActionContext.getContext().getSession().put("ERROR", "Invalid username or password!");
        }
        ServletActionContext.getRequest().setAttribute(Constants.PORTLET_EVENT, Boolean.TRUE);
        return NONE;
    }

    public String logout() {
        logger.info("logout invoking...");
        ServletActionContext.getContext().getSession().remove("USER");
        ServletActionContext.getRequest().setAttribute(Constants.PORTLET_EVENT, Boolean.TRUE);
        return NONE;
    }

    private String username;

    private String password;

    private boolean rememberme;

    /**
	 * @return the rememberme
	 */
    public boolean isRememberme() {
        return rememberme;
    }

    /**
	 * @param rememberme the rememberme to set
	 */
    public void setRememberme(boolean rememberme) {
        this.rememberme = rememberme;
    }

    /**
	 * @return the username
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @param username
	 *            the username to set
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @param password
	 *            the password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }
}
