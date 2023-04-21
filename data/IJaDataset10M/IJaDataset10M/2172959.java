package whf.framework.security.entity;

import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;
import whf.framework.entity.AbstractEntity;

/**
 * @author wanghaifeng
 * 
 */
public class User extends AbstractEntity {

    private String username;

    private String password;

    /**
	 * @property Date:passwordModifyDate 最近的密码修改时间
	 */
    private Date passwordModifyDate;

    /**
	 * @property Date:lastLogin 最精登陆事件
	 */
    private Timestamp lastLogin;

    /**
	 * 账户是否国企
	 * @property boolean:accountNonExpired
	 */
    private boolean accountNonExpired;

    /**
	 * 帐号是否被锁
	 * @property boolean:accountNonLocked
	 */
    private boolean accountNonLocked = true;

    /**
	 * 凭证是否过期
	 * @property boolean:credentialsNonExpired
	 */
    private boolean credentialsNonExpired = true;

    /**
	 * @property boolean:enabled 是否有效
	 */
    private boolean enabled = true;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private String tel1;

    private String tel2;

    private String address1;

    private String address2;

    private String zip;

    /**
	 * @property String:lastLoginIP 最近登陆的IP地址
	 */
    private String lastLoginIP;

    /**
	 * @property UserType:userType
	 */
    private UserType userType;

    /**
	 * @property Blob:photo 相片
	 */
    private Blob photo;

    private String branch;

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public final String getLastLoginIP() {
        return lastLoginIP;
    }

    public final void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public final String getAddress1() {
        return address1;
    }

    public final void setAddress1(String address1) {
        this.address1 = address1;
    }

    public final String getAddress2() {
        return address2;
    }

    public final void setAddress2(String address2) {
        this.address2 = address2;
    }

    public final Blob getPhoto() {
        return photo;
    }

    public final void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public final String getZip() {
        return zip;
    }

    public final void setZip(String zip) {
        this.zip = zip;
    }

    public final Date getPasswordModifyDate() {
        return passwordModifyDate;
    }

    public final void setPasswordModifyDate(Date passwordModifyDate) {
        this.passwordModifyDate = passwordModifyDate;
    }

    public final UserType getUserType() {
        return userType;
    }

    public final void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getDisplayName() {
        if (this.getDept() != null) {
            return this.getDept().getDisplayName() + " &gt;&gt; " + this.getName();
        } else {
            return this.getName();
        }
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
