package com.company.erp.metadata.model;

/**
 * TBMaterielType entity. @author MyEclipse Persistence Tools
 */
public class TBMaterielType implements java.io.Serializable {

    private String id;

    private String name;

    private String remark;

    private String createDate;

    private String creator;

    private String modifyDate;

    private String modifior;

    private String deleted;

    /** default constructor */
    public TBMaterielType() {
    }

    /** minimal constructor */
    public TBMaterielType(String id) {
        this.id = id;
    }

    /** full constructor */
    public TBMaterielType(String id, String name, String remark, String createDate, String creator, String modifyDate, String modifior, String deleted) {
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.createDate = createDate;
        this.creator = creator;
        this.modifyDate = modifyDate;
        this.modifior = modifior;
        this.deleted = deleted;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifior() {
        return modifior;
    }

    public void setModifior(String modifior) {
        this.modifior = modifior;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
