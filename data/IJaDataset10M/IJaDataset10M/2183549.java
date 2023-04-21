package org.tju.ebs.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysParam entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_param", catalog = "ebs")
public class SysParam extends org.tju.ebs.bean.BaseEntity implements java.io.Serializable {

    private Integer id;

    private String listName;

    private String listKey;

    private String listLabelChn;

    private String listLabelEn;

    private Short sequence;

    private Short status;

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
    public SysParam() {
    }

    /** full constructor */
    public SysParam(String listName, String listKey, String listLabelChn, String listLabelEn, Short sequence, Short status, String createdDate, String createdBy, String lastViewedDate, String lastViewedBy, String lastUpdatedDate, String lastUpdatedBy, String deletedDate, String deletedBy, String currViewedDate, String currViewedBy, Short deletedFlag) {
        this.listName = listName;
        this.listKey = listKey;
        this.listLabelChn = listLabelChn;
        this.listLabelEn = listLabelEn;
        this.sequence = sequence;
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
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "list_name", length = 100)
    public String getListName() {
        return this.listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    @Column(name = "list_key", length = 100)
    public String getListKey() {
        return this.listKey;
    }

    public void setListKey(String listKey) {
        this.listKey = listKey;
    }

    @Column(name = "list_label_chn", length = 100)
    public String getListLabelChn() {
        return this.listLabelChn;
    }

    public void setListLabelChn(String listLabelChn) {
        this.listLabelChn = listLabelChn;
    }

    @Column(name = "list_label_en", length = 100)
    public String getListLabelEn() {
        return this.listLabelEn;
    }

    public void setListLabelEn(String listLabelEn) {
        this.listLabelEn = listLabelEn;
    }

    @Column(name = "sequence")
    public Short getSequence() {
        return this.sequence;
    }

    public void setSequence(Short sequence) {
        this.sequence = sequence;
    }

    @Column(name = "status")
    public Short getStatus() {
        return this.status;
    }

    public void setStatus(Short status) {
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
