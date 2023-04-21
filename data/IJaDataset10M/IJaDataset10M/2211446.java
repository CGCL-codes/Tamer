package org.commsuite.model.ws;

import java.util.Date;
import org.commsuite.enums.Country;
import org.commsuite.enums.Language;
import org.commsuite.enums.TimeZone;

/**
 * @since 1.0
 * TODO: PRZYZNAC SIE ! KTO POPELNIL TA KLASE ? ;)
 */
public interface WSUser {

    public Long getId();

    public String getLogin();

    public void setLogin(String login);

    public String getPassword();

    public void setPassword(String password);

    public String getCity();

    public void setCity(String city);

    public Country getCountry();

    public void setCountry(Country country);

    public String getDepartment();

    public void setDepartment(String department);

    public String getEmail();

    public void setEmail(String email);

    public boolean isEnabled();

    public void setEnabled(boolean enabled);

    public String getFax();

    public void setFax(String fax);

    public String getFirstName();

    public void setFirstName(String firstName);

    public Language getLanguage();

    public void setLanguage(Language language);

    public String getLastName();

    public void setLastName(String lastName);

    public String getMobile();

    public void setMobile(String mobile);

    public String getPosition();

    public void setPosition(String position);

    public String getPostalCode();

    public void setPostalCode(String postalCode);

    public String getState();

    public void setState(String state);

    public String getStreet();

    public void setStreet(String street);

    public String getTelephone();

    public void setTelephone(String telephone);

    public TimeZone getTimeZone();

    public void setTimeZone(TimeZone timeZone);

    public Date getValidFrom();

    public void setValidFrom(Date validFrom);

    public Date getValidTo();

    public void setValidTo(Date validTo);
}
