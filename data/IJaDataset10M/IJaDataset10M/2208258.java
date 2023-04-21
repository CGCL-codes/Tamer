package com.koossery.adempiere.fe.beans.organizationRules;

import java.sql.Timestamp;
import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;
import org.koossery.adempiere.core.contract.itf.org.recurring.IC_RecurringDTO;

public class C_RecurringBean extends KTADempiereBaseDTO implements IC_RecurringDTO {

    private static final long serialVersionUID = 1L;

    private int c_Invoice_ID;

    private int c_Order_ID;

    private int c_Payment_ID;

    private int c_Project_ID;

    private int c_Recurring_ID;

    private Timestamp dateLastRun;

    private Timestamp dateNextRun;

    private String description;

    private int frequency;

    private String frequencyType;

    private int gl_JournalBatch_ID;

    private String help;

    private String name;

    private String recurringType;

    private int runsMax;

    private int runsRemaining;

    private String isProcessing;

    private String isActive;

    public int getC_Invoice_ID() {
        return c_Invoice_ID;
    }

    public void setC_Invoice_ID(int c_Invoice_ID) {
        this.c_Invoice_ID = c_Invoice_ID;
    }

    public int getC_Order_ID() {
        return c_Order_ID;
    }

    public void setC_Order_ID(int c_Order_ID) {
        this.c_Order_ID = c_Order_ID;
    }

    public int getC_Payment_ID() {
        return c_Payment_ID;
    }

    public void setC_Payment_ID(int c_Payment_ID) {
        this.c_Payment_ID = c_Payment_ID;
    }

    public int getC_Project_ID() {
        return c_Project_ID;
    }

    public void setC_Project_ID(int c_Project_ID) {
        this.c_Project_ID = c_Project_ID;
    }

    public int getC_Recurring_ID() {
        return c_Recurring_ID;
    }

    public void setC_Recurring_ID(int c_Recurring_ID) {
        this.c_Recurring_ID = c_Recurring_ID;
    }

    public Timestamp getDateLastRun() {
        return dateLastRun;
    }

    public void setDateLastRun(Timestamp dateLastRun) {
        this.dateLastRun = dateLastRun;
    }

    public Timestamp getDateNextRun() {
        return dateNextRun;
    }

    public void setDateNextRun(Timestamp dateNextRun) {
        this.dateNextRun = dateNextRun;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public int getGl_JournalBatch_ID() {
        return gl_JournalBatch_ID;
    }

    public void setGl_JournalBatch_ID(int gl_JournalBatch_ID) {
        this.gl_JournalBatch_ID = gl_JournalBatch_ID;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecurringType() {
        return recurringType;
    }

    public void setRecurringType(String recurringType) {
        this.recurringType = recurringType;
    }

    public int getRunsMax() {
        return runsMax;
    }

    public void setRunsMax(int runsMax) {
        this.runsMax = runsMax;
    }

    public int getRunsRemaining() {
        return runsRemaining;
    }

    public void setRunsRemaining(int runsRemaining) {
        this.runsRemaining = runsRemaining;
    }

    public String getIsProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(String isProcessing) {
        this.isProcessing = isProcessing;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
