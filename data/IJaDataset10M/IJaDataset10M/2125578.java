package edu.webteach.form;

import org.apache.struts.action.*;

/**
 * Form bean class for storing
 * information from login form.
 * Used in login.jsp.
 *
 * @see LoginAction, org.apache.struts.action.ActionForm
 * @author Igor Shubovych
 */
public class LoginForm extends ActionForm {

    protected String name;

    protected String password;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
