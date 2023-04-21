package com.ehs.pm.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author E15567
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "userId", "password", "userName", "drCode", "privilages", "active" })
@XmlRootElement(name = "User")
public class User extends AuditBean {

    private String userId;

    private String password;

    private String userName;

    private String drCode;

    private String privilages;

    private String active;

    public void setActive(String active) {
        this.active = active;
    }

    public void setDrCode(String drCode) {
        this.drCode = drCode;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPrivilages(String privilages) {
        this.privilages = privilages;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getActive() {
        return active;
    }

    public String getDrCode() {
        return drCode;
    }

    public String getPassword() {
        return password;
    }

    public String getPrivilages() {
        return privilages;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
