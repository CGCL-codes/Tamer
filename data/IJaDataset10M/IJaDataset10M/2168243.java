package gemini.castor.ui.client.page.content.home.register.inputaccount;

import com.google.gwt.validation.client.NotEmpty;
import com.google.gwt.validation.client.interfaces.IValidatable;

public class InputAccountFormObject implements IValidatable {

    @NotEmpty
    private String username;

    @NotEmpty
    private String email;

    private String language;

    private String distributorCode;

    private String distributorPin;

    private String distributorEmail;

    private String fullName;

    private String spouseFullName;

    @NotEmpty
    private String pin;

    @NotEmpty
    private String retypePin;

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setRetypePin(String retypePin) {
        this.retypePin = retypePin;
    }

    public String getRetypePin() {
        return retypePin;
    }

    public void setDistributorCode(String distributorCode) {
        this.distributorCode = distributorCode;
    }

    public String getDistributorCode() {
        return distributorCode;
    }

    public void setSpouseFullName(String spouseFullName) {
        this.spouseFullName = spouseFullName;
    }

    public String getSpouseFullName() {
        return spouseFullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDistributorPin(String distributorPin) {
        this.distributorPin = distributorPin;
    }

    public String getDistributorPin() {
        return distributorPin;
    }

    public void setDistributorEmail(String distributorEmail) {
        this.distributorEmail = distributorEmail;
    }

    public String getDistributorEmail() {
        return distributorEmail;
    }
}
