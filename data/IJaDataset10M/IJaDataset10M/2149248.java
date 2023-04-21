package com.quikj.application.communicator.admin.controller;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form bean for the user profile page. This form has the following fields, with
 * default values in square brackets:
 * <ul>
 * <li><b>password</b> - Entered password value
 * <li><b>username</b> - Entered username value
 * </ul>
 * 
 * @author Vinod Batra
 * @version $Revision: 1.2 $ $Date: 2003/05/04 19:57:30 $
 */
public final class LogonForm extends ActionForm {

    /**
	 * The password.
	 */
    private String password = null;

    /**
	 * The username.
	 */
    private String username = null;

    /**
	 * Return the password.
	 */
    public String getPassword() {
        return (this.password);
    }

    /**
	 * Return the username.
	 */
    public String getUsername() {
        return (this.username);
    }

    /**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.password = null;
        this.username = null;
    }

    /**
	 * Set the password.
	 * 
	 * @param password
	 *            The new password
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * Set the username.
	 * 
	 * @param username
	 *            The new username
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found. If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if ((username == null) || (username.length() < 1)) errors.add("username", new ActionError("error.username.required"));
        if ((password == null) || (password.length() < 1)) errors.add("password", new ActionError("error.password.required"));
        return errors;
    }
}
