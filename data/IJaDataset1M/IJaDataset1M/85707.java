package org.tju.ebs.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysView entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_view", catalog = "ebs")
public class SysView extends org.tju.ebs.bean.BaseEntity implements java.io.Serializable {

    private String id;

    private String viewCode;

    private String viewName;

    private String viewComment;

    private String viewScript;

    private String sysObjectId;

    private String sysObjectCode;

    private String sysObjectShortName;

    private Short isDefault;

    private String ownerId;

    private String ownerCode;

    private String ownerFullName;

    private String combineType;

    private Short limited;

    private Short shared;

    private String hql;

    private Short position;

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
    public SysView() {
    }

    /** minimal constructor */
    public SysView(String id) {
        this.id = id;
    }

    /** full constructor */
    public SysView(String id, String viewCode, String viewName, String viewComment, String viewScript, String sysObjectId, String sysObjectCode, String sysObjectShortName, Short isDefault, String ownerId, String ownerCode, String ownerFullName, String combineType, Short limited, Short shared, String hql, Short position, String createdDate, String createdBy, String lastViewedDate, String lastViewedBy, String lastUpdatedDate, String lastUpdatedBy, String deletedDate, String deletedBy, String currViewedDate, String currViewedBy, Short deletedFlag) {
        this.id = id;
        this.viewCode = viewCode;
        this.viewName = viewName;
        this.viewComment = viewComment;
        this.viewScript = viewScript;
        this.sysObjectId = sysObjectId;
        this.sysObjectCode = sysObjectCode;
        this.sysObjectShortName = sysObjectShortName;
        this.isDefault = isDefault;
        this.ownerId = ownerId;
        this.ownerCode = ownerCode;
        this.ownerFullName = ownerFullName;
        this.combineType = combineType;
        this.limited = limited;
        this.shared = shared;
        this.hql = hql;
        this.position = position;
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

    @Column(name = "view_code", length = 50)
    public String getViewCode() {
        return this.viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode;
    }

    @Column(name = "view_name", length = 100)
    public String getViewName() {
        return this.viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    @Column(name = "view_comment", length = 65535)
    public String getViewComment() {
        return this.viewComment;
    }

    public void setViewComment(String viewComment) {
        this.viewComment = viewComment;
    }

    @Column(name = "view_script", length = 65535)
    public String getViewScript() {
        return this.viewScript;
    }

    public void setViewScript(String viewScript) {
        this.viewScript = viewScript;
    }

    @Column(name = "sys_object_id", length = 36)
    public String getSysObjectId() {
        return this.sysObjectId;
    }

    public void setSysObjectId(String sysObjectId) {
        this.sysObjectId = sysObjectId;
    }

    @Column(name = "sys_object_code", length = 50)
    public String getSysObjectCode() {
        return this.sysObjectCode;
    }

    public void setSysObjectCode(String sysObjectCode) {
        this.sysObjectCode = sysObjectCode;
    }

    @Column(name = "sys_object_short_name", length = 50)
    public String getSysObjectShortName() {
        return this.sysObjectShortName;
    }

    public void setSysObjectShortName(String sysObjectShortName) {
        this.sysObjectShortName = sysObjectShortName;
    }

    @Column(name = "is_default")
    public Short getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Short isDefault) {
        this.isDefault = isDefault;
    }

    @Column(name = "owner_id", length = 50)
    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Column(name = "owner_code", length = 50)
    public String getOwnerCode() {
        return this.ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    @Column(name = "owner_full_name", length = 100)
    public String getOwnerFullName() {
        return this.ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    @Column(name = "combine_type", length = 100)
    public String getCombineType() {
        return this.combineType;
    }

    public void setCombineType(String combineType) {
        this.combineType = combineType;
    }

    @Column(name = "limited")
    public Short getLimited() {
        return this.limited;
    }

    public void setLimited(Short limited) {
        this.limited = limited;
    }

    @Column(name = "shared")
    public Short getShared() {
        return this.shared;
    }

    public void setShared(Short shared) {
        this.shared = shared;
    }

    @Column(name = "hql", length = 65535)
    public String getHql() {
        return this.hql;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }

    @Column(name = "position")
    public Short getPosition() {
        return this.position;
    }

    public void setPosition(Short position) {
        this.position = position;
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
