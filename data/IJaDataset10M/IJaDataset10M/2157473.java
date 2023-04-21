package com.scholardesk.abstracts.mgt;

import java.util.List;
import com.scholardesk.annotation.WebParam;
import com.scholardesk.validator.Validatable;
import com.scholardesk.validator.ValidatorFailure;
import com.scholardesk.validator.ValidatorUtil;

public class ContactUsersFormObject implements Validatable {

    private String subject;

    private String message;

    private String[] addresses;

    private List<ValidatorFailure> failures = null;

    public void setSubject(String _subject) {
        subject = _subject;
    }

    @WebParam
    public String getSubject() {
        return subject;
    }

    public void setMessage(String _message) {
        message = _message;
    }

    @WebParam
    public String getMessage() {
        return message;
    }

    public void setAddresses(String[] _addresses) {
        addresses = _addresses;
    }

    @WebParam
    public String[] getAddresses() {
        return addresses;
    }

    public boolean validate(String form, String[] config_files) {
        failures = ValidatorUtil.validateForm(form, config_files, this);
        if (failures.isEmpty()) return true;
        return false;
    }

    public List<ValidatorFailure> getFailures() {
        return failures;
    }
}
