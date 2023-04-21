package findgoshow.entity;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author roger
 */
@Entity
@Table(name = "User", catalog = "findgoshow")
@NamedQueries({ @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"), @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"), @NamedQuery(name = "User.findByLogin", query = "SELECT u FROM User u WHERE u.login = :login"), @NamedQuery(name = "User.findByLoginAndPasswords", query = "SELECT u FROM User u WHERE u.login = :login AND u.passwordSHA1 = :passwordSHA1 AND u.passwordMD5 = :passwordMD5"), @NamedQuery(name = "User.findByPasswordSHA1", query = "SELECT u FROM User u WHERE u.passwordSHA1 = :passwordSHA1"), @NamedQuery(name = "User.findByPasswordMD5", query = "SELECT u FROM User u WHERE u.passwordMD5 = :passwordMD5"), @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"), @NamedQuery(name = "User.findByAuthorizationToken", query = "SELECT u FROM User u WHERE u.authorizationToken = :authorizationToken"), @NamedQuery(name = "User.findByIp", query = "SELECT u FROM User u WHERE u.ip = :ip"), @NamedQuery(name = "User.findByLastActivityDate", query = "SELECT u FROM User u WHERE u.lastActivityDate = :lastActivityDate"), @NamedQuery(name = "User.findByIsOnline", query = "SELECT u FROM User u WHERE u.isOnline = :isOnline"), @NamedQuery(name = "User.findByLanguageId", query = "SELECT u FROM User u WHERE u.languageId = :languageId"), @NamedQuery(name = "User.findByStatus", query = "SELECT u FROM User u WHERE u.status = :status"), @NamedQuery(name = "User.findByMessagesMax", query = "SELECT u FROM User u WHERE u.messagesMax = :messagesMax"), @NamedQuery(name = "User.findByVisible", query = "SELECT u FROM User u WHERE u.visible = :visible"), @NamedQuery(name = "User.findByShowOnline", query = "SELECT u FROM User u WHERE u.showOnline = :showOnline"), @NamedQuery(name = "User.findByWantsMessages", query = "SELECT u FROM User u WHERE u.wantsMessages = :wantsMessages"), @NamedQuery(name = "User.findByUserClass", query = "SELECT u FROM User u WHERE u.userClass = :userClass") })
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic(optional = false)
    @Column(name = "login", nullable = false, length = 64)
    private String login;

    @Basic(optional = false)
    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName;

    @Basic(optional = false)
    @Column(name = "last_name", nullable = false, length = 64)
    private String lastName;

    @Basic(optional = false)
    @Column(name = "password_sha1", nullable = false, length = 128)
    private String passwordSHA1;

    @Basic(optional = false)
    @Column(name = "password_md5", nullable = false, length = 128)
    private String passwordMD5;

    @Basic(optional = false)
    @Column(name = "email", nullable = false, length = 64)
    private String email;

    @Basic(optional = false)
    @Column(name = "authorization_token", nullable = false, length = 64)
    private String authorizationToken;

    @Basic(optional = false)
    @Column(name = "ip", nullable = false, length = 35)
    private String ip;

    @Basic(optional = false)
    @Column(name = "last_activity_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar lastActivityDate;

    @Basic(optional = false)
    @Column(name = "is_online", nullable = false)
    private boolean isOnline;

    @Basic(optional = false)
    @Column(name = "language_id", nullable = false)
    private long languageId;

    @Basic(optional = false)
    @Column(name = "status", nullable = false, length = 13)
    private String status;

    @Basic(optional = false)
    @Column(name = "messages_max", nullable = false)
    private int messagesMax;

    @Basic(optional = false)
    @Column(name = "visible", nullable = false)
    private boolean visible;

    @Basic(optional = false)
    @Column(name = "show_online", nullable = false)
    private boolean showOnline;

    @Basic(optional = false)
    @Column(name = "wants_messages", nullable = false)
    private boolean wantsMessages;

    @Basic(optional = false)
    @Column(name = "user_class", nullable = false, length = 6)
    private String userClass;

    public User() {
    }

    public void prepareToRegistration() {
        Calendar calendar = Calendar.getInstance();
        login = null;
        firstName = null;
        lastName = null;
        passwordSHA1 = null;
        passwordMD5 = null;
        email = null;
        authorizationToken = null;
        ip = null;
        lastActivityDate = calendar;
        isOnline = false;
        languageId = 0;
        status = null;
        messagesMax = 100;
        visible = true;
        showOnline = true;
        wantsMessages = true;
        userClass = "user";
    }

    public String generateAuthorizationToken() {
        return Helpers.Strings.encode(login + passwordMD5 + passwordSHA1 + email + Math.random(), "MD5");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getPasswordSHA1() {
        return passwordSHA1;
    }

    public void setPasswordSHA1(String passwordSHA1) {
        this.passwordSHA1 = passwordSHA1;
    }

    public String getPasswordMD5() {
        return passwordMD5;
    }

    public void setPasswordMD5(String passwordMD5) {
        this.passwordMD5 = passwordMD5;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Calendar getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Calendar lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public long getLanguage() {
        return languageId;
    }

    public void setLanguage(long language) {
        this.languageId = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMessagesMax() {
        return messagesMax;
    }

    public void setMessagesMax(int messagesMax) {
        this.messagesMax = messagesMax;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getShowOnline() {
        return showOnline;
    }

    public void setShowOnline(boolean showOnline) {
        this.showOnline = showOnline;
    }

    public boolean getWantsMessages() {
        return wantsMessages;
    }

    public void setWantsMessages(boolean wantsMessages) {
        this.wantsMessages = wantsMessages;
    }

    public String getClass1() {
        return userClass;
    }

    public void setClass1(String class1) {
        this.userClass = class1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "findgoshow.entity.User[" + "id = " + id + ", " + "login = " + login + ", " + "firstName = " + firstName + ", " + "lastName = " + lastName + ", " + "passwordSHA1 = " + passwordSHA1 + ", " + "passwordMD5 = " + passwordMD5 + ", " + "email = " + email + ", " + "authorizationToken = " + authorizationToken + ", " + "ip = " + ip + ", " + "lastActivityDate = " + lastActivityDate + ", " + "isOnline = " + isOnline + ", " + "language = " + languageId + ", " + "status = " + status + ", " + "messagesMax = " + messagesMax + ", " + "visible = " + visible + ", " + "showOnline = " + showOnline + ", " + "wantsMessages = " + wantsMessages + ", " + "userClass = " + userClass + "]";
    }
}
