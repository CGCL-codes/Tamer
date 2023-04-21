package com.koossery.adempiere.fe.beans.payroll.movement;

import java.sql.Timestamp;

/**
 * @version 1.0
 * @created 24-d�c.-2008 14:35:02
 */
public class IHR_MovementBean {

    private int ad_PrintFormat_ID;

    private int ad_Workflow_ID;

    private int c_BPartner_ID;

    private int c_Charge_ID;

    private int c_DocType_ID;

    private int c_DocTypeTarget_ID;

    private int c_PaymentSelection_ID;

    private String columnSQL;

    private Timestamp dateACCT;

    private String docAction;

    private String docStatus;

    private String documentNO;

    private int hr_Department_ID;

    private int hr_Employee_ID;

    private int hr_Job_ID;

    private int hr_Payroll_ID;

    private int hr_Period_ID;

    private int hr_Process_ID;

    private String isActive;

    private String name;

    private String posted;

    private String processed;

    private String processing;

    private static final long serialVersionUID = 1L;

    public IHR_MovementBean() {
    }

    public int getAd_PrintFormat_ID() {
        return ad_PrintFormat_ID;
    }

    public void setAd_PrintFormat_ID(int ad_PrintFormat_ID) {
        this.ad_PrintFormat_ID = ad_PrintFormat_ID;
    }

    public int getAd_Workflow_ID() {
        return ad_Workflow_ID;
    }

    public void setAd_Workflow_ID(int ad_Workflow_ID) {
        this.ad_Workflow_ID = ad_Workflow_ID;
    }

    public int getC_BPartner_ID() {
        return c_BPartner_ID;
    }

    public void setC_BPartner_ID(int partner_ID) {
        c_BPartner_ID = partner_ID;
    }

    public int getC_Charge_ID() {
        return c_Charge_ID;
    }

    public void setC_Charge_ID(int charge_ID) {
        c_Charge_ID = charge_ID;
    }

    public int getC_DocType_ID() {
        return c_DocType_ID;
    }

    public void setC_DocType_ID(int docType_ID) {
        c_DocType_ID = docType_ID;
    }

    public int getC_DocTypeTarget_ID() {
        return c_DocTypeTarget_ID;
    }

    public void setC_DocTypeTarget_ID(int docTypeTarget_ID) {
        c_DocTypeTarget_ID = docTypeTarget_ID;
    }

    public int getC_PaymentSelection_ID() {
        return c_PaymentSelection_ID;
    }

    public void setC_PaymentSelection_ID(int paymentSelection_ID) {
        c_PaymentSelection_ID = paymentSelection_ID;
    }

    public String getColumnSQL() {
        return columnSQL;
    }

    public void setColumnSQL(String columnSQL) {
        this.columnSQL = columnSQL;
    }

    public Timestamp getDateACCT() {
        return dateACCT;
    }

    public void setDateACCT(Timestamp dateACCT) {
        this.dateACCT = dateACCT;
    }

    public String getDocAction() {
        return docAction;
    }

    public void setDocAction(String docAction) {
        this.docAction = docAction;
    }

    public String getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

    public String getDocumentNO() {
        return documentNO;
    }

    public void setDocumentNO(String documentNO) {
        this.documentNO = documentNO;
    }

    public int getHr_Department_ID() {
        return hr_Department_ID;
    }

    public void setHr_Department_ID(int hr_Department_ID) {
        this.hr_Department_ID = hr_Department_ID;
    }

    public int getHr_Employee_ID() {
        return hr_Employee_ID;
    }

    public void setHr_Employee_ID(int hr_Employee_ID) {
        this.hr_Employee_ID = hr_Employee_ID;
    }

    public int getHr_Job_ID() {
        return hr_Job_ID;
    }

    public void setHr_Job_ID(int hr_Job_ID) {
        this.hr_Job_ID = hr_Job_ID;
    }

    public int getHr_Payroll_ID() {
        return hr_Payroll_ID;
    }

    public void setHr_Payroll_ID(int hr_Payroll_ID) {
        this.hr_Payroll_ID = hr_Payroll_ID;
    }

    public int getHr_Period_ID() {
        return hr_Period_ID;
    }

    public void setHr_Period_ID(int hr_Period_ID) {
        this.hr_Period_ID = hr_Period_ID;
    }

    public int getHr_Process_ID() {
        return hr_Process_ID;
    }

    public void setHr_Process_ID(int hr_Process_ID) {
        this.hr_Process_ID = hr_Process_ID;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
