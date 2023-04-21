package com.knwebapp.gwt.services.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MailTransferBo implements Serializable {

    private String userNameField;

    private String emailField;

    private String phoneNumberField;

    private String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUserNameField() {
        return userNameField;
    }

    public void setUserNameField(String userNameField) {
        this.userNameField = userNameField;
    }

    public String getEmailField() {
        return emailField;
    }

    public void setEmailField(String emailField) {
        this.emailField = emailField;
    }

    public String getPhoneNumberField() {
        return phoneNumberField;
    }

    public void setPhoneNumberField(String phoneNumberField) {
        this.phoneNumberField = phoneNumberField;
    }
}
