package de.g18.gruppe3.common.model;

/**
 * Model für einen Administrator im LDAP -> Administrator darf User-Accounts anlegen
 *
 * @author <a href="mailto:madmakro@gmx.net">Jan Schwart</a>
 */
public class AccountAdmin extends AbstractModel {

    public static final String PROPERTYNAME_LOGIN = "login";

    public static final String PROPERTYNAME_PASSWORT = "passwort";

    private String login;

    private String passwort;

    public AccountAdmin(String stLoginName, String stPasswort) {
        setLogin(stLoginName);
        setPasswort(stPasswort);
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String aNewValue) {
        String oldValue = getPasswort();
        passwort = aNewValue;
        firePropertyChange(PROPERTYNAME_PASSWORT, oldValue, getPasswort());
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String aNewValue) {
        String oldValue = getLogin();
        login = aNewValue;
        firePropertyChange(PROPERTYNAME_LOGIN, oldValue, getLogin());
    }
}
