package com.avatal.business.validation;

import javax.ejb.SessionBean;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * @ejb.bean name="UserValidator"
 *	jndi-name="UserValidatorBean"
 *	type="Stateless" 
**/
public abstract class UserValidatorBean implements SessionBean, ValidationErrors {

    /**
	 * @ejb.interface-method
	 *	tview-type="remote" 
	**/
    public ActionErrors validatePassword(String password, String passwordRpt) {
        ActionErrors errors = new ActionErrors();
        if (!password.equals(passwordRpt)) {
            errors.add("Password", new ActionError(ERROR_PASSWORD_RPT));
        }
        return errors;
    }
}
