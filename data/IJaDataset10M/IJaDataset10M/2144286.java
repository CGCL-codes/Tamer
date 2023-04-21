package com.example.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 07-11-2008
 * 
 * XDoclet definition:
 * 
 * @struts.form name="forgotPasswordForm"
 */
public class ForgotPasswordForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * Method validate
	 * 
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    /**
	 * Method reset
	 * 
	 * @param mapping
	 * @param request
	 */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }
}
