package org.tju.ebs.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ProdCategory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "prod_category", catalog = "ebs")
public class ProdCategory extends org.tju.ebs.bean.BaseEntity implements java.io.Serializable {

    private String id;

    private String code;

    private String name;

    private String fullName;

    private Short isUsingFlag;

    private String parentId;

    private String parentName;

    private String companyId;

    private String isLeaf;

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
    public ProdCategory() {
    }

    /** minimal constructor */
    public ProdCategory(String id) {
        this.id = id;
    }

    /** full constructor */
    public ProdCategory(String id, String code, String name, String fullName, Short isUsingFlag, String parentId, String parentName, String companyId, String isLeaf, String createdDate, String createdBy, String lastViewedDate, String lastViewedBy, String lastUpdatedDate, String lastUpdatedBy, String deletedDate, String deletedBy, String currViewedDate, String currViewedBy, Short deletedFlag) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.fullName = fullName;
        this.isUsingFlag = isUsingFlag;
        this.parentId = parentId;
        this.parentName = parentName;
        this.companyId = companyId;
        this.isLeaf = isLeaf;
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

    @Column(name = "code", length = 23)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "name", length = 80)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "full_name", length = 120)
    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "is_using_flag")
    public Short getIsUsingFlag() {
        return this.isUsingFlag;
    }

    public void setIsUsingFlag(Short isUsingFlag) {
        this.isUsingFlag = isUsingFlag;
    }

    @Column(name = "parent_id", length = 36)
    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name = "parent_name", length = 36)
    public String getParentName() {
        return this.parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Column(name = "company_id", length = 36)
    public String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Column(name = "is_leaf", length = 1)
    public String getIsLeaf() {
        return this.isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
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
