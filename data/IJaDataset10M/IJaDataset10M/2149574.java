package cn.sharezoo.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 02-15-2008
 * 
 * XDoclet definition:
 * @struts.form name="loginForm"
 */
public class LoginForm extends ActionForm {

    /** username property */
    private String username;

    /** password property */
    private String password;

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
}
