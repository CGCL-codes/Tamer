package com.liferay.samplestruts.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * <a href="UnsubscribeForm.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class UnsubscribeForm extends ValidatorForm {

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getEmailAddress() {
        return _emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        _emailAddress = emailAddress;
    }

    public void reset(ActionMapping mapping, HttpServletRequest req) {
        _firstName = null;
        _lastName = null;
        _emailAddress = null;
    }

    public String toString() {
        return _firstName + " " + _lastName + " " + _emailAddress;
    }

    private String _firstName;

    private String _lastName;

    private String _emailAddress;
}
