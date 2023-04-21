package org.tju.ebs.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * OrgEmployee entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "org_employee", catalog = "ebs")
public class OrgEmployee extends org.tju.ebs.bean.BaseEntity implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String id;

    private String code;

    private String personId;

    private String fullName;

    private String email;

    private String jobTitle;

    private String grade;

    private String hireDate;

    private String resignationDate;

    private String orgCompanyId;

    private String orgCompanyName;

    private String status;

    private String createdDate;

    private String createdBy;

    private String lastViewedDate;

    private String lastViewedBy;

    private String lastUpdatedDate;

    private String lastUpdatedBy;

    private String deletedDate;

    private String deletedBy;

    private String currViewedDate;

    private String currViewedBy;

    private Short deletedFlag;

    /** default constructor */
    public OrgEmployee() {
    }

    /** minimal constructor */
    public OrgEmployee(String id) {
        this.id = id;
    }

    /** full constructor */
    public OrgEmployee(String id, String code, String personId, String fullName, String email, String jobTitle, String grade, String hireDate, String resignationDate, String orgCompanyId, String orgCompanyName, String status, String createdDate, String createdBy, String lastViewedDate, String lastViewedBy, String lastUpdatedDate, String lastUpdatedBy, String deletedDate, String deletedBy, String currViewedDate, String currViewedBy, Short deletedFlag) {
        this.id = id;
        this.code = code;
        this.personId = personId;
        this.fullName = fullName;
        this.email = email;
        this.jobTitle = jobTitle;
        this.grade = grade;
        this.hireDate = hireDate;
        this.resignationDate = resignationDate;
        this.orgCompanyId = orgCompanyId;
        this.orgCompanyName = orgCompanyName;
        this.status = status;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastViewedDate = lastViewedDate;
        this.lastViewedBy = lastViewedBy;
        this.lastUpdatedDate = lastUpdatedDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.deletedDate = deletedDate;
        this.deletedBy = deletedBy;
        this.currViewedDate = currViewedDate;
        this.currViewedBy = currViewedBy;
        this.deletedFlag = deletedFlag;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 36)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "code", length = 50)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "person_id", length = 36)
    public String getPersonId() {
        return this.personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @Column(name = "full_name", length = 50)
    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "email", length = 100)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "job_title", length = 100)
    public String getJobTitle() {
        return this.jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Column(name = "grade", length = 10)
    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Column(name = "hire_date", length = 20)
    public String getHireDate() {
        return this.hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    @Column(name = "resignation_date", length = 20)
    public String getResignationDate() {
        return this.resignationDate;
    }

    public void setResignationDate(String resignationDate) {
        this.resignationDate = resignationDate;
    }

    @Column(name = "org_company_id", length = 36)
    public String getOrgCompanyId() {
        return this.orgCompanyId;
    }

    public void setOrgCompanyId(String orgCompanyId) {
        this.orgCompanyId = orgCompanyId;
    }

    @Column(name = "org_company_name", length = 100)
    public String getOrgCompanyName() {
        return this.orgCompanyName;
    }

    public void setOrgCompanyName(String orgCompanyName) {
        this.orgCompanyName = orgCompanyName;
    }

    @Column(name = "status", length = 10)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "created_date", length = 24)
    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "created_by", length = 50)
    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "last_viewed_date", length = 24)
    public String getLastViewedDate() {
        return this.lastViewedDate;
    }

    public void setLastViewedDate(String lastViewedDate) {
        this.lastViewedDate = lastViewedDate;
    }

    @Column(name = "last_viewed_by", length = 50)
    public String getLastViewedBy() {
        return this.lastViewedBy;
    }

    public void setLastViewedBy(String lastViewedBy) {
        this.lastViewedBy = lastViewedBy;
    }

    @Column(name = "last_updated_date", length = 24)
    public String getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @Column(name = "last_updated_by", length = 50)
    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Column(name = "deleted_date", length = 24)
    public String getDeletedDate() {
        return this.deletedDate;
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

    @Column(name = "deleted_by", length = 50)
    public String getDeletedBy() {
        return this.deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    @Column(name = "curr_viewed_date", length = 24)
    public String getCurrViewedDate() {
        return this.currViewedDate;
    }

    public void setCurrViewedDate(String currViewedDate) {
        this.currViewedDate = currViewedDate;
    }

    @Column(name = "curr_viewed_by", length = 50)
    public String getCurrViewedBy() {
        return this.currViewedBy;
    }

    public void setCurrViewedBy(String currViewedBy) {
        this.currViewedBy = currViewedBy;
    }

    @Column(name = "deleted_flag")
    public Short getDeletedFlag() {
        return this.deletedFlag;
    }

    public void setDeletedFlag(Short deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
}
