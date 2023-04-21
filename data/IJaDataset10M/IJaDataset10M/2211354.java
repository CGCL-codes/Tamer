package org.equanda.login;

import java.util.List;

/**
 * Information from the EquandaUser table:
 * - application server userName
 * - application server password
 * - is administrator? if so, the border will differ, other menus would be available
 *
 * @author NetRom team
 */
public class LoginInfo {

    boolean administrator;

    String userName;

    String password;

    String language;

    List<String> roles;

    long deadline;

    public LoginInfo() {
    }

    public LoginInfo(boolean administrator, String userName, String password) {
        this.administrator = administrator;
        this.userName = userName;
        this.password = password;
    }

    public LoginInfo(boolean administrator, String userName, String password, String language) {
        this(administrator, userName, password);
        this.language = language;
    }

    public LoginInfo(boolean administrator, String userName, String password, String language, List<String> roles) {
        this(administrator, userName, password, language);
        setRoles(roles);
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * Get validity deadline for this record.
     * Can be used to aid in caching LoginInfo objects.
     *
     * @return time in ms for deadline
     */
    public long getDeadline() {
        return deadline;
    }

    /**
     * Set validity deadline for this record.
     * Can be used to aid in caching LoginInfo objects.
     *
     * @param deadline time in ms for deadline
     */
    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }
}
