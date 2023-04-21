package org.blueoxygen.cimande;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class BackendUser implements Serializable {

    /** identifier field */
    private java.lang.String id;

    /** persistent field */
    private java.lang.String username;

    /** persistent field */
    private java.lang.String password;

    /** nullable persistent field */
    private java.lang.String firstName;

    /** nullable persistent field */
    private java.lang.String lastName;

    /** nullable persistent field */
    private java.lang.String companyId;

    /** nullable persistent field */
    private java.lang.String jobPositionId;

    /** nullable persistent field */
    private java.lang.String email;

    /** nullable persistent field */
    private java.lang.String address1;

    /** nullable persistent field */
    private java.lang.String address2;

    /** nullable persistent field */
    private java.lang.String city;

    /** nullable persistent field */
    private java.lang.String description;

    /** nullable persistent field */
    private java.lang.String roleId;

    /** persistent field */
    private byte calendarFlag;

    /** persistent field */
    private byte projectFlag;

    /** nullable persistent field */
    private java.lang.String createBy;

    /** nullable persistent field */
    private java.util.Date createDate;

    /** nullable persistent field */
    private java.lang.String updateBy;

    /** nullable persistent field */
    private java.util.Date updateDate;

    /** persistent field */
    private byte activeFlag;

    /** persistent field */
    private java.lang.String statusId;

    /** nullable persistent field */
    private java.lang.String siteId;

    /** nullable persistent field */
    private int rewardPoint;

    /** nullable persistent field */
    private byte nameVisible;

    /** nullable persistent field */
    private byte emailVisible;

    /** persistent field */
    private long jiveId;

    /** full constructor */
    public BackendUser(java.lang.String id, java.lang.String username, java.lang.String password, java.lang.String firstName, java.lang.String lastName, java.lang.String companyId, java.lang.String jobPositionId, java.lang.String email, java.lang.String address1, java.lang.String address2, java.lang.String city, java.lang.String description, java.lang.String roleId, byte calendarFlag, byte projectFlag, java.lang.String createBy, java.util.Date createDate, java.lang.String updateBy, java.util.Date updateDate, byte activeFlag, java.lang.String statusId, java.lang.String siteId, int rewardPoint, byte nameVisible, byte emailVisible, long jiveId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyId = companyId;
        this.jobPositionId = jobPositionId;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.description = description;
        this.roleId = roleId;
        this.calendarFlag = calendarFlag;
        this.projectFlag = projectFlag;
        this.createBy = createBy;
        this.createDate = createDate;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
        this.activeFlag = activeFlag;
        this.statusId = statusId;
        this.siteId = siteId;
        this.rewardPoint = rewardPoint;
        this.nameVisible = nameVisible;
        this.emailVisible = emailVisible;
        this.jiveId = jiveId;
    }

    /** default constructor */
    public BackendUser() {
    }

    /** minimal constructor */
    public BackendUser(java.lang.String id, java.lang.String username, java.lang.String password, byte calendarFlag, byte projectFlag, byte activeFlag, java.lang.String statusId, long jiveId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.calendarFlag = calendarFlag;
        this.projectFlag = projectFlag;
        this.activeFlag = activeFlag;
        this.statusId = statusId;
        this.jiveId = jiveId;
    }

    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    public java.lang.String getUsername() {
        return this.username;
    }

    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    public java.lang.String getPassword() {
        return this.password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    public java.lang.String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }

    public java.lang.String getLastName() {
        return this.lastName;
    }

    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }

    public java.lang.String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(java.lang.String companyId) {
        this.companyId = companyId;
    }

    public java.lang.String getJobPositionId() {
        return this.jobPositionId;
    }

    public void setJobPositionId(java.lang.String jobPositionId) {
        this.jobPositionId = jobPositionId;
    }

    public java.lang.String getEmail() {
        return this.email;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public java.lang.String getAddress1() {
        return this.address1;
    }

    public void setAddress1(java.lang.String address1) {
        this.address1 = address1;
    }

    public java.lang.String getAddress2() {
        return this.address2;
    }

    public void setAddress2(java.lang.String address2) {
        this.address2 = address2;
    }

    public java.lang.String getCity() {
        return this.city;
    }

    public void setCity(java.lang.String city) {
        this.city = city;
    }

    public java.lang.String getDescription() {
        return this.description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(java.lang.String roleId) {
        this.roleId = roleId;
    }

    public byte getCalendarFlag() {
        return this.calendarFlag;
    }

    public void setCalendarFlag(byte calendarFlag) {
        this.calendarFlag = calendarFlag;
    }

    public byte getProjectFlag() {
        return this.projectFlag;
    }

    public void setProjectFlag(byte projectFlag) {
        this.projectFlag = projectFlag;
    }

    public java.lang.String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(java.lang.String createBy) {
        this.createBy = createBy;
    }

    public java.util.Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    public java.lang.String getUpdateBy() {
        return this.updateBy;
    }

    public void setUpdateBy(java.lang.String updateBy) {
        this.updateBy = updateBy;
    }

    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }

    public byte getActiveFlag() {
        return this.activeFlag;
    }

    public void setActiveFlag(byte activeFlag) {
        this.activeFlag = activeFlag;
    }

    public java.lang.String getStatusId() {
        return this.statusId;
    }

    public void setStatusId(java.lang.String statusId) {
        this.statusId = statusId;
    }

    public java.lang.String getSiteId() {
        return this.siteId;
    }

    public void setSiteId(java.lang.String siteId) {
        this.siteId = siteId;
    }

    public int getRewardPoint() {
        return this.rewardPoint;
    }

    public void setRewardPoint(int rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    public byte getNameVisible() {
        return this.nameVisible;
    }

    public void setNameVisible(byte nameVisible) {
        this.nameVisible = nameVisible;
    }

    public byte getEmailVisible() {
        return this.emailVisible;
    }

    public void setEmailVisible(byte emailVisible) {
        this.emailVisible = emailVisible;
    }

    public long getJiveId() {
        return this.jiveId;
    }

    public void setJiveId(long jiveId) {
        this.jiveId = jiveId;
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof BackendUser)) return false;
        BackendUser castOther = (BackendUser) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }
}
