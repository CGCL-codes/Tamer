package org.openjf.usergroup;

import java.sql.Timestamp;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openjf.util.EntityBasic;
import org.openjf.util.ObjectUtil;

public class User implements Cloneable, EntityBasic {

    private int id;

    /**
     * Nick
     */
    private String nick;

    /**
     * e-mail address
     */
    private String email;

    private String login;

    /**
     * Token used to identity the logged user (using cookies)
     */
    private String loginId;

    private String emailToken;

    /**
     * Is the user active? If not, it's blocked
     */
    private boolean active;

    /**
     * Is the user a system admin? If it is, it can do almost everything.
     */
    private boolean admin;

    /**
     * The last time the user viewed all the posts
     */
    private Timestamp viewAllPostsTime;

    /**
     * Password
     */
    private String encryptedPassword;

    /**
     * Is the user information obtained from an external database 
     * such as LDAP?
     */
    private boolean external;

    /**
     * When will the current login expire? 
     */
    private Timestamp loginExpiration;

    public User() {
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public boolean equalsId(Object o) {
        if ((o == null) || (!ObjectUtil.isInstance(getClass(), o))) {
            return false;
        }
        User oo = (User) o;
        return getId() == oo.getId();
    }

    protected boolean equalsProperties(User o) {
        return equalsId(o) && ObjectUtil.equals(getNick(), o.getNick()) && ObjectUtil.equals(getLogin(), o.getLogin()) && ObjectUtil.equals(getLoginId(), o.getLoginId()) && ObjectUtil.equals(getEmailToken(), o.getEmailToken()) && ObjectUtil.equals(getViewAllPostsTime(), o.getViewAllPostsTime()) && ObjectUtil.equals(getEncryptedPassword(), o.getEncryptedPassword()) && ObjectUtil.equals(getLoginExpiration(), o.getLoginExpiration()) && isAdmin() == o.isAdmin() && isActive() == o.isActive() && isExternal() == o.isExternal();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if ((o == null) || (!ObjectUtil.sameClass(getClass(), o))) {
            return false;
        }
        return equalsProperties((User) o);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Timestamp getViewAllPostsTime() {
        return viewAllPostsTime;
    }

    public void setViewAllPostsTime(Timestamp viewAllTime) {
        this.viewAllPostsTime = viewAllTime;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getEmailToken() {
        return emailToken;
    }

    public void setEmailToken(String emailToken) {
        this.emailToken = emailToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Timestamp getLoginExpiration() {
        return loginExpiration;
    }

    public void setLoginExpiration(Timestamp loginExpiration) {
        this.loginExpiration = loginExpiration;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }
}
