package com.quesofttech.business.domain.security;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import com.quesofttech.business.common.exception.AuthenticationException;
import com.quesofttech.business.common.exception.BusinessException;
import com.quesofttech.business.common.exception.GenericBusinessException;
import com.quesofttech.business.common.exception.ValueRequiredException;
import com.quesofttech.business.domain.base.BaseEntity;
import com.quesofttech.business.domain.embeddable.RowInfo;
import com.quesofttech.util.StringUtil;

/**
 * The User entity.
 */
@Entity
@Table(name = "User", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_Login" }) })
@SuppressWarnings("serial")
public class User extends BaseEntity {

    private static final long serialVersionUID = 4365329688768691261L;

    public static final String ADMIN_LOGINID = "admin";

    public static final String[] SALUTATIONS = { "Ms", "Mrs", "Mr", "Dr", "Prof" };

    @TableGenerator(name = "user_id", table = "PrimaryKeys", pkColumnName = "tableName", pkColumnValue = "user", valueColumnName = "keyField")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_id")
    @Column(name = "id_User", nullable = false)
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "user_Login", length = 20, nullable = false)
    private String login;

    @Column(name = "user_FirstName", length = 30, nullable = false)
    private String firstName;

    @Column(name = "user_LastName", length = 30, nullable = false)
    private String lastName;

    @Column(name = "user_Salutation", length = 10)
    private String salutation;

    @Column(name = "user_Telephone", length = 15)
    private String telephone;

    @Column(name = "user_EmailAddress", length = 50)
    private String emailAddress;

    @Column(name = "user_ExpiryDate")
    @Temporal(TemporalType.TIMESTAMP)
    private java.sql.Timestamp expiryDate;

    @OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_UserPassword")
    private UserPassword userPassword = new UserPassword();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user", targetEntity = UserRole.class)
    private List<UserRole> userRoles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user", targetEntity = UserProgram.class)
    private List<UserProgram> userPrograms;

    @Embedded
    RowInfo rowInfo;

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("User: [");
        buf.append("id=" + id + ", ");
        buf.append("login=" + login + ", ");
        buf.append("salutation=" + salutation + ", ");
        buf.append("firstName=" + firstName + ", ");
        buf.append("lastName=" + lastName + ", ");
        buf.append("emailAddress=" + emailAddress + ", ");
        buf.append("expiryDate=" + expiryDate + ", ");
        buf.append("telephone=" + telephone + ", ");
        buf.append("version=" + version);
        buf.append("]");
        return buf.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof User) && getId() != null && ((User) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? super.hashCode() : getId().hashCode();
    }

    @Override
    public Serializable getIdForMessages() {
        return getId();
    }

    @PrePersist
    void prePersist() throws BusinessException {
        validate();
    }

    @PostLoad
    void postLoad() {
    }

    @PreUpdate
    void preUpdate() throws BusinessException {
        if (rowInfo.getRecordStatus() != "D") {
            validate();
        }
        java.util.Date today = new java.util.Date();
        rowInfo.setModifyTimestamp(new java.sql.Timestamp(today.getTime()));
    }

    @PreRemove
    void preRemove() throws BusinessException {
    }

    public void validate() throws BusinessException {
        if (StringUtil.isEmpty(login)) {
            throw new ValueRequiredException(this, "User_loginId");
        }
        if (StringUtil.isEmpty(firstName)) {
            throw new ValueRequiredException(this, "User_firstName");
        }
        if (StringUtil.isEmpty(lastName)) {
            throw new ValueRequiredException(this, "User_lastName");
        }
        if (expiryDate != null && login.equals(ADMIN_LOGINID)) {
            throw new GenericBusinessException("User_expirydate_not_permitted_for_user", new Object[] { ADMIN_LOGINID });
        }
    }

    public void authenticate(String password) throws AuthenticationException {
        System.out.println("[User] Just In");
        if (this.userPassword != null) this.userPassword.authenticate(password); else System.out.println("[User] UserPassword = null");
    }

    /**
	 * This method provides a way for users to change their own passwords.
	 */
    void changePassword(String currentPassword, String newPassword) throws BusinessException {
        this.userPassword.changePassword(currentPassword, newPassword);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    /**
	 * This method provides a way for security officers to "reset" the userPassword.
	 */
    void setPassword(String newPassword) throws BusinessException {
        this.userPassword.setPassword(newPassword);
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public java.sql.Timestamp getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(java.sql.Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getRecordStatus() {
        return rowInfo.getRecordStatus();
    }

    public void setRecordStatus(String recordStatus) {
        this.rowInfo.setRecordStatus(recordStatus);
    }

    public String getSessionId() {
        return rowInfo.getSessionId();
    }

    public void setSessionId(String sessionId) {
        this.rowInfo.setSessionId(sessionId);
    }

    public String getCreateLogin() {
        return rowInfo.getCreateLogin();
    }

    public void setCreateLogin(String createLogin) {
        this.rowInfo.setCreateLogin(createLogin);
    }

    public String getCreateApp() {
        return rowInfo.getCreateApp();
    }

    public void setCreateApp(String createApp) {
        this.rowInfo.setCreateApp(createApp);
    }

    public java.sql.Timestamp getCreateTimestamp() {
        return rowInfo.getCreateTimestamp();
    }

    public void setCreateTimestamp(java.sql.Timestamp createTimestamp) {
        this.rowInfo.setCreateTimestamp(createTimestamp);
    }

    public String getModifyLogin() {
        return rowInfo.getModifyLogin();
    }

    public void setModifyLogin(String modifyLogin) {
        this.rowInfo.setModifyLogin(modifyLogin);
    }

    public String getModifyApp() {
        return rowInfo.getModifyApp();
    }

    public void setModifyApp(String modifyApp) {
        this.rowInfo.setModifyApp(modifyApp);
    }

    public java.sql.Timestamp getModifyTimestamp() {
        return rowInfo.getModifyTimestamp();
    }

    public void setModifyTimestamp(java.sql.Timestamp modifyTimestamp) {
        this.rowInfo.setModifyTimestamp(modifyTimestamp);
    }

    /**
	 * @return the userRoles
	 */
    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    /**
	 * @param userRoles the userRoles to set
	 */
    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    /**
	 * @return the userPrograms
	 */
    public List<UserProgram> getUserPrograms() {
        return userPrograms;
    }

    /**
	 * @param userPrograms the userPrograms to set
	 */
    public void setUserPrograms(List<UserProgram> userPrograms) {
        this.userPrograms = userPrograms;
    }
}
