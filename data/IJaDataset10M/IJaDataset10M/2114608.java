package net.sf.gateway.mef.databases.tables.vt;

import java.io.Serializable;
import java.util.Date;

public class RsFormData2004 implements Serializable {

    static final long serialVersionUID = 1;

    private String submissionKey;

    public String getSubmissionKey() {
        return this.submissionKey;
    }

    public void setSubmissionKey(String submissionKey) {
        this.submissionKey = submissionKey;
    }

    private String formImageKey;

    public String getFormImageKey() {
        return this.formImageKey;
    }

    public void setFormImageKey(String formImageKey) {
        this.formImageKey = formImageKey;
    }

    private String externalId;

    public String getExternalId() {
        return this.externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    private String externalIdType;

    public String getExternalIdType() {
        return this.externalIdType;
    }

    public void setExternalIdType(String externalIdType) {
        this.externalIdType = externalIdType;
    }

    private String formNumber;

    public String getFormNumber() {
        return this.formNumber;
    }

    public void setFormNumber(String formNumber) {
        this.formNumber = formNumber;
    }

    private String barcode;

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    private String vendorId;

    public String getVendorId() {
        return this.vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    private String preparerSsn;

    public String getPreparerSsn() {
        return this.preparerSsn;
    }

    public void setPreparerSsn(String preparerSsn) {
        this.preparerSsn = preparerSsn;
    }

    private String preparerEin;

    public String getPreparerEin() {
        return this.preparerEin;
    }

    public void setPreparerEin(String preparerEin) {
        this.preparerEin = preparerEin;
    }

    private String taxTypeCode;

    public String getTaxTypeCode() {
        return this.taxTypeCode;
    }

    public void setTaxTypeCode(String taxTypeCode) {
        this.taxTypeCode = taxTypeCode;
    }

    private String onlineIndicator;

    public String getOnlineIndicator() {
        return this.onlineIndicator;
    }

    public void setOnlineIndicator(String onlineIndicator) {
        this.onlineIndicator = onlineIndicator;
    }

    private String stateOnlyIndicator;

    public String getStateOnlyIndicator() {
        return this.stateOnlyIndicator;
    }

    public void setStateOnlyIndicator(String stateOnlyIndicator) {
        this.stateOnlyIndicator = stateOnlyIndicator;
    }

    private String inputSourceFile;

    public String getInputSourceFile() {
        return this.inputSourceFile;
    }

    public void setInputSourceFile(String inputSourceFile) {
        this.inputSourceFile = inputSourceFile;
    }

    private String createUserId;

    public String getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    private Date createTmstamp;

    public Date getCreateTmstamp() {
        return this.createTmstamp;
    }

    public void setCreateTmstamp(Date createTmstamp) {
        this.createTmstamp = createTmstamp;
    }
}
