package com.softplus.carrefour.hd.bltier.entity;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class CfhdCaseHeadLog implements Serializable {

    /** identifier field */
    private com.softplus.carrefour.hd.bltier.entity.CfhdCaseHeadLogPK comp_id;

    /** nullable persistent field */
    private String reqName;

    /** nullable persistent field */
    private String reqTel;

    /** nullable persistent field */
    private String reqFax;

    /** nullable persistent field */
    private Date openCaseDate;

    /** nullable persistent field */
    private Date closeCaseDate;

    /** nullable persistent field */
    private String storeId;

    /** nullable persistent field */
    private String countryId;

    /** nullable persistent field */
    private String cfhdSystem;

    /** nullable persistent field */
    private String cfhdModule;

    /** nullable persistent field */
    private String alDataNo;

    /** nullable persistent field */
    private String goldVersion;

    /** nullable persistent field */
    private String cfhdEnvironment;

    /** nullable persistent field */
    private String screenName;

    /** nullable persistent field */
    private String caseType;

    /** nullable persistent field */
    private Integer severity;

    /** nullable persistent field */
    private String subject;

    /** nullable persistent field */
    private String routeDirection;

    /** nullable persistent field */
    private String openStatus;

    /** nullable persistent field */
    private String caseStatus;

    /** persistent field */
    private Date credLocaltime;

    /** persistent field */
    private Date credServerTime;

    /** nullable persistent field */
    private String originCaseType;

    /** nullable persistent field */
    private Integer originSeverity;

    /** nullable persistent field */
    private String autoEscalateStatus;

    /** full constructor */
    public CfhdCaseHeadLog(com.softplus.carrefour.hd.bltier.entity.CfhdCaseHeadLogPK comp_id, String reqName, String reqTel, String reqFax, Date openCaseDate, Date closeCaseDate, String storeId, String countryId, String cfhdSystem, String cfhdModule, String alDataNo, String goldVersion, String cfhdEnvironment, String screenName, String caseType, Integer severity, String subject, String routeDirection, String openStatus, String caseStatus, Date credLocaltime, Date credServerTime, String originCaseType, Integer originSeverity, String autoEscalateStatus) {
        this.comp_id = comp_id;
        this.reqName = reqName;
        this.reqTel = reqTel;
        this.reqFax = reqFax;
        this.openCaseDate = openCaseDate;
        this.closeCaseDate = closeCaseDate;
        this.storeId = storeId;
        this.countryId = countryId;
        this.cfhdSystem = cfhdSystem;
        this.cfhdModule = cfhdModule;
        this.alDataNo = alDataNo;
        this.goldVersion = goldVersion;
        this.cfhdEnvironment = cfhdEnvironment;
        this.screenName = screenName;
        this.caseType = caseType;
        this.severity = severity;
        this.subject = subject;
        this.routeDirection = routeDirection;
        this.openStatus = openStatus;
        this.caseStatus = caseStatus;
        this.credLocaltime = credLocaltime;
        this.credServerTime = credServerTime;
        this.originCaseType = originCaseType;
        this.originSeverity = originSeverity;
        this.autoEscalateStatus = autoEscalateStatus;
    }

    /** default constructor */
    public CfhdCaseHeadLog() {
    }

    /** minimal constructor */
    public CfhdCaseHeadLog(com.softplus.carrefour.hd.bltier.entity.CfhdCaseHeadLogPK comp_id, Date credLocaltime, Date credServerTime) {
        this.comp_id = comp_id;
        this.credLocaltime = credLocaltime;
        this.credServerTime = credServerTime;
    }

    public com.softplus.carrefour.hd.bltier.entity.CfhdCaseHeadLogPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.softplus.carrefour.hd.bltier.entity.CfhdCaseHeadLogPK comp_id) {
        this.comp_id = comp_id;
    }

    public String getReqName() {
        return this.reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public String getReqTel() {
        return this.reqTel;
    }

    public void setReqTel(String reqTel) {
        this.reqTel = reqTel;
    }

    public String getReqFax() {
        return this.reqFax;
    }

    public void setReqFax(String reqFax) {
        this.reqFax = reqFax;
    }

    public Date getOpenCaseDate() {
        return this.openCaseDate;
    }

    public void setOpenCaseDate(Date openCaseDate) {
        this.openCaseDate = openCaseDate;
    }

    public Date getCloseCaseDate() {
        return this.closeCaseDate;
    }

    public void setCloseCaseDate(Date closeCaseDate) {
        this.closeCaseDate = closeCaseDate;
    }

    public String getStoreId() {
        return this.storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCountryId() {
        return this.countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCfhdSystem() {
        return this.cfhdSystem;
    }

    public void setCfhdSystem(String cfhdSystem) {
        this.cfhdSystem = cfhdSystem;
    }

    public String getCfhdModule() {
        return this.cfhdModule;
    }

    public void setCfhdModule(String cfhdModule) {
        this.cfhdModule = cfhdModule;
    }

    public String getAlDataNo() {
        return this.alDataNo;
    }

    public void setAlDataNo(String alDataNo) {
        this.alDataNo = alDataNo;
    }

    public String getGoldVersion() {
        return this.goldVersion;
    }

    public void setGoldVersion(String goldVersion) {
        this.goldVersion = goldVersion;
    }

    public String getCfhdEnvironment() {
        return this.cfhdEnvironment;
    }

    public void setCfhdEnvironment(String cfhdEnvironment) {
        this.cfhdEnvironment = cfhdEnvironment;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getCaseType() {
        return this.caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public Integer getSeverity() {
        return this.severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRouteDirection() {
        return this.routeDirection;
    }

    public void setRouteDirection(String routeDirection) {
        this.routeDirection = routeDirection;
    }

    public String getOpenStatus() {
        return this.openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    public String getCaseStatus() {
        return this.caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public Date getCredLocaltime() {
        return this.credLocaltime;
    }

    public void setCredLocaltime(Date credLocaltime) {
        this.credLocaltime = credLocaltime;
    }

    public Date getCredServerTime() {
        return this.credServerTime;
    }

    public void setCredServerTime(Date credServerTime) {
        this.credServerTime = credServerTime;
    }

    public String getOriginCaseType() {
        return this.originCaseType;
    }

    public void setOriginCaseType(String originCaseType) {
        this.originCaseType = originCaseType;
    }

    public Integer getOriginSeverity() {
        return this.originSeverity;
    }

    public void setOriginSeverity(Integer originSeverity) {
        this.originSeverity = originSeverity;
    }

    public String getAutoEscalateStatus() {
        return this.autoEscalateStatus;
    }

    public void setAutoEscalateStatus(String autoEscalateStatus) {
        this.autoEscalateStatus = autoEscalateStatus;
    }

    public String toString() {
        return new ToStringBuilder(this).append("comp_id", getComp_id()).toString();
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if (!(other instanceof CfhdCaseHeadLog)) return false;
        CfhdCaseHeadLog castOther = (CfhdCaseHeadLog) other;
        return new EqualsBuilder().append(this.getComp_id(), castOther.getComp_id()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getComp_id()).toHashCode();
    }
}
