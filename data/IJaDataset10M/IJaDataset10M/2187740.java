package org.compiere.model;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 *	RMA Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRMA.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 *
 *  Modifications: Completed RMA functionality (Ashley Ramdass)
 */
public class MRMA extends X_M_RMA implements DocAction {

    /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_RMA_ID id
	 *	@param trxName transaction
	 */
    public MRMA(Properties ctx, int M_RMA_ID, String trxName) {
        super(ctx, M_RMA_ID, trxName);
        if (M_RMA_ID == 0) {
            setDocAction(DOCACTION_Complete);
            setDocStatus(DOCSTATUS_Drafted);
            setIsApproved(false);
            setProcessed(false);
        }
    }

    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
    public MRMA(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** Lines					*/
    private MRMALine[] m_lines = null;

    /** The Shipment			*/
    private MInOut m_inout = null;

    /**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return lines
	 */
    public MRMALine[] getLines(boolean requery) {
        if (m_lines != null && !requery) {
            set_TrxName(m_lines, get_TrxName());
            return m_lines;
        }
        ArrayList<MRMALine> list = new ArrayList<MRMALine>();
        String sql = "SELECT * FROM M_RMALine WHERE M_RMA_ID=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, getM_RMA_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(new MRMALine(getCtx(), rs, get_TrxName()));
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        m_lines = new MRMALine[list.size()];
        list.toArray(m_lines);
        return m_lines;
    }

    /**
	 * 	Get Shipment
	 *	@return shipment
	 */
    public MInOut getShipment() {
        if (m_inout == null && getInOut_ID() != 0) m_inout = new MInOut(getCtx(), getInOut_ID(), get_TrxName());
        return m_inout;
    }

    /**
     * Get the original order on which the shipment/receipt defined is based upon.
     * @return order
     */
    public MOrder getOriginalOrder() {
        MInOut shipment = getShipment();
        if (shipment == null || shipment.getC_Order_ID() == 0) {
            return null;
        }
        return new MOrder(getCtx(), shipment.getC_Order_ID(), get_TrxName());
    }

    /**
     * Get the original invoice on which the shipment/receipt defined is based upon.
     * @return invoice
     */
    public MInvoice getOriginalInvoice() {
        MInOut shipment = getShipment();
        if (shipment == null) {
            return null;
        }
        int invId = 0;
        if (shipment.getC_Invoice_ID() != 0) {
            invId = shipment.getC_Invoice_ID();
        } else {
            String sqlStmt = "SELECT C_Invoice_ID FROM C_Invoice WHERE C_Order_ID=?";
            invId = DB.getSQLValue(null, sqlStmt, shipment.getC_Order_ID());
        }
        if (invId <= 0) {
            return null;
        }
        return new MInvoice(getCtx(), invId, get_TrxName());
    }

    /**
	 * 	Set M_InOut_ID
	 *	@param M_InOut_ID id
	 */
    public void setM_InOut_ID(int M_InOut_ID) {
        setInOut_ID(M_InOut_ID);
        setC_Currency_ID(0);
        setAmt(Env.ZERO);
        setC_BPartner_ID(0);
        m_inout = null;
    }

    /**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
    public String getDocumentInfo() {
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        return dt.getName() + " " + getDocumentNo();
    }

    /**
	 * 	Create PDF
	 *	@return File or null
	 */
    public File createPDF() {
        try {
            File temp = File.createTempFile(get_TableName() + get_ID() + "_", ".pdf");
            return createPDF(temp);
        } catch (Exception e) {
            log.severe("Could not create PDF - " + e.getMessage());
        }
        return null;
    }

    /**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
    public File createPDF(File file) {
        return null;
    }

    /**
	 * 	Before Save
	 *	Set BPartner, Currency
	 *	@param newRecord new
	 *	@return true
	 */
    protected boolean beforeSave(boolean newRecord) {
        getShipment();
        if (getC_BPartner_ID() == 0) {
            if (m_inout != null) setC_BPartner_ID(m_inout.getC_BPartner_ID());
        }
        if (getC_Currency_ID() == 0) {
            if (m_inout != null) {
                if (m_inout.getC_Order_ID() != 0) {
                    MOrder order = new MOrder(getCtx(), m_inout.getC_Order_ID(), get_TrxName());
                    setC_Currency_ID(order.getC_Currency_ID());
                } else if (m_inout.getC_Invoice_ID() != 0) {
                    MInvoice invoice = new MInvoice(getCtx(), m_inout.getC_Invoice_ID(), get_TrxName());
                    setC_Currency_ID(invoice.getC_Currency_ID());
                }
            }
        }
        if (m_inout != null && m_inout.isSOTrx() != isSOTrx()) {
            log.saveError("RMA.IsSOTrx <> InOut.IsSOTrx", "");
            return false;
        }
        return true;
    }

    /**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
    public boolean processIt(String processAction) {
        m_processMsg = null;
        DocumentEngine engine = new DocumentEngine(this, getDocStatus());
        return engine.processIt(processAction, getDocAction());
    }

    /**	Process Message 			*/
    private String m_processMsg = null;

    /**	Just Prepared Flag			*/
    private boolean m_justPrepared = false;

    /**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
    public boolean unlockIt() {
        log.info("unlockIt - " + toString());
        setProcessing(false);
        return true;
    }

    /**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
    public boolean invalidateIt() {
        log.info("invalidateIt - " + toString());
        return true;
    }

    /**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
    public String prepareIt() {
        log.info(toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
        if (m_processMsg != null) return DocAction.STATUS_Invalid;
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        MRMALine[] lines = getLines(false);
        if (lines.length == 0) {
            m_processMsg = "@NoLines@";
            return DocAction.STATUS_Invalid;
        }
        setAmt(getTotalAmount());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
        if (m_processMsg != null) return DocAction.STATUS_Invalid;
        m_justPrepared = true;
        return DocAction.STATUS_InProgress;
    }

    /**
	 * 	Approve Document
	 * 	@return true if success 
	 */
    public boolean approveIt() {
        log.info("approveIt - " + toString());
        setIsApproved(true);
        return true;
    }

    /**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
    public boolean rejectIt() {
        log.info("rejectIt - " + toString());
        setIsApproved(false);
        return true;
    }

    /**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
    public String completeIt() {
        if (!m_justPrepared) {
            String status = prepareIt();
            if (!DocAction.STATUS_InProgress.equals(status)) return status;
        }
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
        if (m_processMsg != null) return DocAction.STATUS_Invalid;
        if (!isApproved()) approveIt();
        log.info("completeIt - " + toString());
        String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
        if (valid != null) {
            m_processMsg = valid;
            return DocAction.STATUS_Invalid;
        }
        setDefiniteDocumentNo();
        setProcessed(true);
        setDocAction(DOCACTION_Close);
        return DocAction.STATUS_Completed;
    }

    /**
	 * 	Set the definite document number after completed
	 */
    private void setDefiniteDocumentNo() {
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        if (dt.isOverwriteSeqOnComplete()) {
            String value = DB.getDocumentNo(getC_DocType_ID(), get_TrxName(), true, this);
            if (value != null) setDocumentNo(value);
        }
    }

    /**
	 * 	Void Document.
	 * 	@return true if success 
	 */
    public boolean voidIt() {
        log.info("voidIt - " + toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_VOID);
        if (m_processMsg != null) return false;
        MRMALine lines[] = getLines(true);
        for (MRMALine rmaLine : lines) {
            rmaLine.addDescription(Msg.getMsg(getCtx(), "Voided") + " (" + rmaLine.getQty() + ")");
            rmaLine.setQty(Env.ZERO);
            rmaLine.setAmt(Env.ZERO);
            if (!rmaLine.save()) {
                m_processMsg = "Could not update line";
            }
        }
        addDescription(Msg.getMsg(getCtx(), "Voided"));
        setAmt(Env.ZERO);
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_VOID);
        if (m_processMsg != null) return false;
        setProcessed(true);
        setDocAction(DOCACTION_None);
        return true;
    }

    /**
	 * 	Close Document.
	 * 	Cancel not delivered Qunatities
	 * 	@return true if success 
	 */
    public boolean closeIt() {
        log.info("closeIt - " + toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_CLOSE);
        if (m_processMsg != null) return false;
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_CLOSE);
        if (m_processMsg != null) return false;
        return true;
    }

    /**
	 * 	Reverse Correction
	 * 	@return true if success 
	 */
    public boolean reverseCorrectIt() {
        log.info("reverseCorrectIt - " + toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REVERSECORRECT);
        if (m_processMsg != null) return false;
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSECORRECT);
        if (m_processMsg != null) return false;
        return false;
    }

    /**
	 * 	Reverse Accrual - none
	 * 	@return true if success 
	 */
    public boolean reverseAccrualIt() {
        log.info("reverseAccrualIt - " + toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
        if (m_processMsg != null) return false;
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
        if (m_processMsg != null) return false;
        return false;
    }

    /** 
	 * 	Re-activate
	 * 	@return true if success 
	 */
    public boolean reActivateIt() {
        log.info("reActivateIt - " + toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REACTIVATE);
        if (m_processMsg != null) return false;
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REACTIVATE);
        if (m_processMsg != null) return false;
        return false;
    }

    /**
     *  Set Processed.
     *  Propagate to Lines
     *  @param processed processed
     */
    public void setProcessed(boolean processed) {
        super.setProcessed(processed);
        if (get_ID() == 0) return;
        String set = "SET Processed='" + (processed ? "Y" : "N") + "' WHERE M_RMA_ID=" + getM_RMA_ID();
        int noLine = DB.executeUpdate("UPDATE M_RMALine " + set, get_TrxName());
        m_lines = null;
        log.fine("setProcessed - " + processed + " - Lines=" + noLine);
    }

    /**
     *  Add to Description
     *  @param description text
     */
    public void addDescription(String description) {
        String desc = getDescription();
        if (desc == null) setDescription(description); else setDescription(desc + " | " + description);
    }

    /**
     * Get the total amount based on the lines
     * @return Total Amount
     */
    public BigDecimal getTotalAmount() {
        MRMALine lines[] = this.getLines(true);
        BigDecimal amt = Env.ZERO;
        for (MRMALine line : lines) {
            amt = amt.add(line.getLineNetAmt());
        }
        return amt;
    }

    /**
     * Updates the amount on the document
     */
    public void updateAmount() {
        setAmt(getTotalAmount());
    }

    /*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
    public String getSummary() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDocumentNo());
        sb.append(": ").append(Msg.translate(getCtx(), "Amt")).append("=").append(getAmt()).append(" (#").append(getLines(false).length).append(")");
        if (getDescription() != null && getDescription().length() > 0) sb.append(" - ").append(getDescription());
        return sb.toString();
    }

    /**
     * Retrieves all the charge lines that is present on the document
     * @return Charge Lines
     */
    public MRMALine[] getChargeLines() {
        StringBuffer whereClause = new StringBuffer();
        whereClause.append("IsActive='Y' AND M_RMA_ID=");
        whereClause.append(get_ID());
        whereClause.append(" AND C_Charge_ID IS NOT null");
        int rmaLineIds[] = MRMALine.getAllIDs(MRMALine.Table_Name, whereClause.toString(), get_TrxName());
        ArrayList<MRMALine> chargeLineList = new ArrayList<MRMALine>();
        for (int i = 0; i < rmaLineIds.length; i++) {
            MRMALine rmaLine = new MRMALine(getCtx(), rmaLineIds[i], get_TrxName());
            chargeLineList.add(rmaLine);
        }
        MRMALine lines[] = new MRMALine[chargeLineList.size()];
        chargeLineList.toArray(lines);
        return lines;
    }

    /**
     * Get whether Tax is included (based on the original order) 
     * @return True if tax is included
     */
    public boolean isTaxIncluded() {
        MOrder order = getOriginalOrder();
        if (order != null && order.get_ID() != 0) {
            return order.isTaxIncluded();
        }
        return true;
    }

    /**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
    public String getProcessMsg() {
        return m_processMsg;
    }

    /**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
    public int getDoc_User_ID() {
        return getSalesRep_ID();
    }

    /**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
    public BigDecimal getApprovalAmt() {
        return getAmt();
    }
}
