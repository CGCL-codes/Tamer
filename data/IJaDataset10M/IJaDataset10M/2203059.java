package net.sourceforge.seqware.common.model;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public class RegistrationDTO extends Registration {

    private static final long serialVersionUID = 8465486434105955512L;

    private String confirmEmailAddress;

    private String confirmPassword;

    private Registration domainObject;

    public RegistrationDTO() {
        super();
    }

    public String getConfirmEmailAddress() {
        return confirmEmailAddress;
    }

    public void setConfirmEmailAddress(String confirmEmailAddress) {
        this.confirmEmailAddress = confirmEmailAddress;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Registration getDomainObject() {
        return domainObject;
    }

    public void setDomainObject(Registration domainObject) {
        this.domainObject = domainObject;
    }
}
