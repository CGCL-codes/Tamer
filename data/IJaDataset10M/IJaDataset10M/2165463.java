package org.koossery.adempiere.core.contract.criteria.user;

import java.sql.Timestamp;
import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;
import org.koossery.adempiere.core.contract.itf.user.IAD_UserDTO;

public class AD_UserCriteria extends KTADempiereBaseCriteria implements IAD_UserDTO {

    private static final long serialVersionUID = 1L;

    private int ad_OrgTrx_ID;

    private int ad_User_ID;

    private Timestamp birthday;

    private int c_BPartner_ID;

    private int c_BPartner_Location_ID;

    private int c_Greeting_ID;

    private int c_Job_ID;

    private String comments;

    private String connectionProfile;

    private String description;

    private String email;

    private String emailUser;

    private String emailUserPW;

    private String emailVerify;

    private Timestamp emailVerifyDate;

    private String fax;

    private Timestamp lastContact;

    private String lastResult;

    private String ldAPUser;

    private String name;

    private String notificationType;

    private String password;

    private String phone;

    private String phone2;

    private int supervisor_ID;

    private String title;

    private String userPIN;

    private String value;

    private String isFullBPAccess;

    private String isProcessing;

    private String isActive;

    public int getAd_OrgTrx_ID() {
        return ad_OrgTrx_ID;
    }

    public void setAd_OrgTrx_ID(int ad_OrgTrx_ID) {
        this.ad_OrgTrx_ID = ad_OrgTrx_ID;
    }

    public int getAd_User_ID() {
        return ad_User_ID;
    }

    public void setAd_User_ID(int ad_User_ID) {
        this.ad_User_ID = ad_User_ID;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public int getC_BPartner_ID() {
        return c_BPartner_ID;
    }

    public void setC_BPartner_ID(int c_BPartner_ID) {
        this.c_BPartner_ID = c_BPartner_ID;
    }

    public int getC_BPartner_Location_ID() {
        return c_BPartner_Location_ID;
    }

    public void setC_BPartner_Location_ID(int c_BPartner_Location_ID) {
        this.c_BPartner_Location_ID = c_BPartner_Location_ID;
    }

    public int getC_Greeting_ID() {
        return c_Greeting_ID;
    }

    public void setC_Greeting_ID(int c_Greeting_ID) {
        this.c_Greeting_ID = c_Greeting_ID;
    }

    public int getC_Job_ID() {
        return c_Job_ID;
    }

    public void setC_Job_ID(int c_Job_ID) {
        this.c_Job_ID = c_Job_ID;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getConnectionProfile() {
        return connectionProfile;
    }

    public void setConnectionProfile(String connectionProfile) {
        this.connectionProfile = connectionProfile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getEmailUserPW() {
        return emailUserPW;
    }

    public void setEmailUserPW(String emailUserPW) {
        this.emailUserPW = emailUserPW;
    }

    public String getEmailVerify() {
        return emailVerify;
    }

    public void setEmailVerify(String emailVerify) {
        this.emailVerify = emailVerify;
    }

    public Timestamp getEmailVerifyDate() {
        return emailVerifyDate;
    }

    public void setEmailVerifyDate(Timestamp emailVerifyDate) {
        this.emailVerifyDate = emailVerifyDate;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Timestamp getLastContact() {
        return lastContact;
    }

    public void setLastContact(Timestamp lastContact) {
        this.lastContact = lastContact;
    }

    public String getLastResult() {
        return lastResult;
    }

    public void setLastResult(String lastResult) {
        this.lastResult = lastResult;
    }

    public String getLdAPUser() {
        return ldAPUser;
    }

    public void setLdAPUser(String ldAPUser) {
        this.ldAPUser = ldAPUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public int getSupervisor_ID() {
        return supervisor_ID;
    }

    public void setSupervisor_ID(int supervisor_ID) {
        this.supervisor_ID = supervisor_ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserPIN() {
        return userPIN;
    }

    public void setUserPIN(String userPIN) {
        this.userPIN = userPIN;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIsFullBPAccess() {
        return isFullBPAccess;
    }

    public void setIsFullBPAccess(String isFullBPAccess) {
        this.isFullBPAccess = isFullBPAccess;
    }

    public String getIsProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(String isProcessing) {
        this.isProcessing = isProcessing;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
