package com.example.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 06-30-2008
 * 
 * XDoclet definition:
 * 
 * @struts.form name="logoutForm"
 */
public class LogoutForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

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
