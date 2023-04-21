package login;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 12-01-2008
 * 
 * XDoclet definition:
 * @struts.form name="loginForm"
 */
public class LoginForm extends ActionForm {

    /** password property */
    private String password;

    /** flag property */
    private String type;

    /** username property */
    private String username;

    /** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    /** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    /** 
	 * Returns the password.
	 * @return String
	 */
    public String getPassword() {
        return password;
    }

    /** 
	 * Set the password.
	 * @param password The password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /** 
	 * Returns the flag.
	 * @return String
	 */
    public String getType() {
        return type;
    }

    /** 
	 * Set the flag.
	 * @param flag The flag to set
	 */
    public void setType(String type) {
        this.type = type;
    }

    /** 
	 * Returns the username.
	 * @return String
	 */
    public String getUsername() {
        return username;
    }

    /** 
	 * Set the username.
	 * @param username The username to set
	 */
    public void setUsername(String username) {
        this.username = username;
    }
}
