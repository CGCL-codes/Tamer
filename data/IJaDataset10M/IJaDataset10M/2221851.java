package de.cue4net.eventservice.model.command;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.cue4net.eventservice.model.user.User;

/**
 * A rock solid command POJO which is passed to validator and controller
 * @author Keino Uelze - cue4net
 * @version $Id: UserRegistrationCommand.java,v 1.8 2008-06-05 11:00:07 keino Exp $
 */
public class UserRegistrationCommand implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4717852207559000417L;

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    public static final String PASSWORD_DISPLAY_VALUE = "******";

    private Long id;

    private String username, usernameCopy, password, passwordRepitition;

    private String firstName, lastName, street, postCode, city, country;

    private String emailAddress, emailAddressRepitition, emailAddressCopy;

    private String phone, mobile, fax;

    public UserRegistrationCommand() {
    }

    public UserRegistrationCommand(User user) {
        this.id = user.getId();
        this.username = user.getLogin().getUserName();
        this.usernameCopy = user.getLogin().getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.street = user.getPrivateAddress().getStreet();
        this.postCode = user.getPrivateAddress().getZipCode();
        this.city = user.getPrivateAddress().getCity();
        this.country = user.getPrivateAddress().getCountry();
        this.emailAddress = user.getEmailAddress().getEmailAddress();
        this.emailAddressRepitition = user.getEmailAddress().getEmailAddress();
        this.emailAddressCopy = user.getEmailAddress().getEmailAddress();
        this.phone = user.getPhone().getPhone();
        this.mobile = user.getPhone().getMobile();
        this.fax = user.getPhone().getFax();
        this.password = PASSWORD_DISPLAY_VALUE;
        this.passwordRepitition = PASSWORD_DISPLAY_VALUE;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMobile() {
        return mobile;
    }

    public boolean hasUserNameChanged() {
        return !this.username.equals(this.usernameCopy);
    }

    public boolean hasEmailAddressChanged() {
        return !this.emailAddress.equals(this.emailAddressCopy);
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddressRepitition() {
        return emailAddressRepitition;
    }

    public void setEmailAddressRepitition(String emailAddressRepitition) {
        this.emailAddressRepitition = emailAddressRepitition;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPasswordRepitition() {
        return passwordRepitition;
    }

    public void setPasswordRepitition(String passwordrepitition) {
        this.passwordRepitition = passwordrepitition;
    }

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

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
        result = PRIME * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = PRIME * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = PRIME * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final UserRegistrationCommand other = (UserRegistrationCommand) obj;
        if (emailAddress == null) {
            if (other.emailAddress != null) return false;
        } else if (!emailAddress.equals(other.emailAddress)) return false;
        if (firstName == null) {
            if (other.firstName != null) return false;
        } else if (!firstName.equals(other.firstName)) return false;
        if (lastName == null) {
            if (other.lastName != null) return false;
        } else if (!lastName.equals(other.lastName)) return false;
        if (username == null) {
            if (other.username != null) return false;
        } else if (!username.equals(other.username)) return false;
        return true;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
