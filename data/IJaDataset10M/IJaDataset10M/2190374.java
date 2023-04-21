package edu.osu.cse.be.struts.form;

import org.apache.struts.validator.ValidatorActionForm;

/**
 * @author Mauktik
 */
public class ResetPasswordForm extends ValidatorActionForm {

    String userName;

    String emailAddress;

    public void clear() {
        userName = null;
        emailAddress = null;
    }

    /**
	 * 
	 * @uml.property name="emailAddress"
	 */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
	 * 
	 * @uml.property name="emailAddress"
	 */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
	 * 
	 * @uml.property name="userName"
	 */
    public String getUserName() {
        return userName;
    }

    /**
	 * 
	 * @uml.property name="userName"
	 */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
