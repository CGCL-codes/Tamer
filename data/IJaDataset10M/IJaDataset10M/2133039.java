package com.company.erp.metadata.model;

import com.company.erp.customer.model.TBCustomer;

/**
 * TBMateriel entity. @author MyEclipse Persistence Tools
 */
public class TBMateriel implements java.io.Serializable {

    private String id;

    private String code;

    private String name;

    private String type;

    private Double price;

    private String isAgreement;

    private String unit;

    private String status;

    private String auditor;

    private String auditInfo;

    private String auditDate;

    private String creator;

    private String createDate;

    private String modifior;

    private String modifyDate;

    private String remark;

    private String unitName;

    private TBUnit tbUnit;

    private TBMaterielType tbType;

    /** default constructor */
    public TBMateriel() {
    }

    /** minimal constructor */
    public TBMateriel(String name) {
        this.name = name;
    }

    /** full constructor */
    public TBMateriel(String code, String name, String type, Double price, String isAgreement, String unit, String status, String auditor, String auditInfo, String auditDate, String creator, String createDate, String modifior, String modifyDate, String remark) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.price = price;
        this.isAgreement = isAgreement;
        this.unit = unit;
        this.status = status;
        this.auditor = auditor;
        this.auditInfo = auditInfo;
        this.auditDate = auditDate;
        this.creator = creator;
        this.createDate = createDate;
        this.modifior = modifior;
        this.modifyDate = modifyDate;
        this.remark = remark;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public TBMaterielType getTbType() {
        return tbType;
    }

    public void setTbType(TBMaterielType tbType) {
        this.tbType = tbType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitName() {
        if ((unitName == null || unitName.equals("")) && getTbUnit() != null) {
            unitName = getTbUnit().getUnit();
        }
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public TBUnit getTbUnit() {
        return tbUnit;
    }

    public void setTbUnit(TBUnit tbUnit) {
        this.tbUnit = tbUnit;
    }

    public String getIsAgreement() {
        return isAgreement;
    }

    public void setIsAgreement(String isAgreement) {
        this.isAgreement = isAgreement;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(String auditInfo) {
        this.auditInfo = auditInfo;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifior() {
        return modifior;
    }

    public void setModifior(String modifior) {
        this.modifior = modifior;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }
}
