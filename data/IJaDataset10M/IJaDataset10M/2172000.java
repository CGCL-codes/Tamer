package org.sqlanyware.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author seb
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class LogonForm extends ActionForm {

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);
    }

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
        return super.validate(arg0, arg1);
    }

    /**
    * Returns the action.
    * @return String
    */
    public String getAction() {
        return action;
    }

    /**
    * Sets the action.
    * @param action The action to set
    */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return Returns the user.
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user The user to set.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
	 * Returns the remember.
	 * @return boolean
	 */
    public boolean isRemember() {
        return remember;
    }

    /**
	 * Sets the remember.
	 * @param remember The remember to set
	 */
    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    private String user = null;

    private String password = null;

    private String action = null;

    private boolean remember = false;
}
