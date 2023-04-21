package org.koossery.adempiere.core.contract.dto.org.recurring;

import java.sql.Timestamp;
import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;
import org.koossery.adempiere.core.contract.itf.org.recurring.IC_Recurring_RunDTO;

public class C_Recurring_RunDTO extends KTADempiereBaseDTO implements IC_Recurring_RunDTO {

    private static final long serialVersionUID = 1L;

    private int c_Invoice_ID;

    private int c_Order_ID;

    private int c_Payment_ID;

    private int c_Project_ID;

    private int c_Recurring_ID;

    private int c_Recurring_Run_ID;

    private Timestamp dateDoc;

    private int gl_JournalBatch_ID;

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

    public int getC_Recurring_Run_ID() {
        return c_Recurring_Run_ID;
    }

    public void setC_Recurring_Run_ID(int c_Recurring_Run_ID) {
        this.c_Recurring_Run_ID = c_Recurring_Run_ID;
    }

    public Timestamp getDateDoc() {
        return dateDoc;
    }

    public void setDateDoc(Timestamp dateDoc) {
        this.dateDoc = dateDoc;
    }

    public int getGl_JournalBatch_ID() {
        return gl_JournalBatch_ID;
    }

    public void setGl_JournalBatch_ID(int gl_JournalBatch_ID) {
        this.gl_JournalBatch_ID = gl_JournalBatch_ID;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
