package org.compiere.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import org.adempiere.exceptions.AdempiereException;
import org.adempierelbr.util.AdempiereLBR;
import org.compiere.print.ReportEngine;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *  Shipment Model
 *
 *  @author Jorg Janke
 *  @version $Id: MInOut.java,v 1.4 2006/07/30 00:51:03 jjanke Exp $
 *
 *  Modifications: Added the RMA functionality (Ashley Ramdass)
 *  @author Karsten Thiemann, Schaeffer AG
 * 			<li>Bug [ 1759431 ] Problems with VCreateFrom
 *  @author victor.perez@e-evolution.com, e-Evolution http://www.e-evolution.com
 * 			<li>FR [ 1948157  ]  Is necessary the reference for document reverse
 * 			<li> FR [ 2520591 ] Support multiples calendar for Org
 *			@see http://sourceforge.net/tracker2/?func=detail&atid=879335&aid=2520591&group_id=176962
 *  @author Armen Rizal, Goodwill Consulting
 * 			<li>BF [ 1745154 ] Cost in Reversing Material Related Docs
 *  @see http://sourceforge.net/tracker/?func=detail&atid=879335&aid=1948157&group_id=176962
 *  @author Teo Sarca, teo.sarca@gmail.com
 * 			<li>BF [ 2993853 ] Voiding/Reversing Receipt should void confirmations
 * 				https://sourceforge.net/tracker/?func=detail&atid=879332&aid=2993853&group_id=176962
 */
public class MInOut extends X_M_InOut implements DocAction {

    /**
	 *
	 */
    private static final long serialVersionUID = -239302197968535277L;

    /**
	 * 	Create Shipment From Order
	 *	@param order order
	 *	@param movementDate optional movement date
	 *	@param forceDelivery ignore order delivery rule
	 *	@param allAttributeInstances if true, all attribute set instances
	 *	@param minGuaranteeDate optional minimum guarantee date if all attribute instances
	 *	@param complete complete document (Process if false, Complete if true)
	 *	@param trxName transaction
	 *	@return Shipment or null
	 */
    public static MInOut createFrom(MOrder order, Timestamp movementDate, boolean forceDelivery, boolean allAttributeInstances, Timestamp minGuaranteeDate, boolean complete, String trxName) {
        if (order == null) throw new IllegalArgumentException("No Order");
        if (!forceDelivery && DELIVERYRULE_CompleteLine.equals(order.getDeliveryRule())) {
            return null;
        }
        MInOut retValue = new MInOut(order, 0, movementDate);
        retValue.setDocAction(complete ? DOCACTION_Complete : DOCACTION_Prepare);
        MOrderLine[] oLines = order.getLines(true, "M_Product_ID");
        for (int i = 0; i < oLines.length; i++) {
            BigDecimal qty = oLines[i].getQtyOrdered().subtract(oLines[i].getQtyDelivered());
            if (qty.signum() == 0) continue;
            MStorage[] storages = null;
            MProduct product = oLines[i].getProduct();
            if (product != null && product.get_ID() != 0 && product.isStocked()) {
                String MMPolicy = product.getMMPolicy();
                storages = MStorage.getWarehouse(order.getCtx(), order.getM_Warehouse_ID(), oLines[i].getM_Product_ID(), oLines[i].getM_AttributeSetInstance_ID(), minGuaranteeDate, MClient.MMPOLICY_FiFo.equals(MMPolicy), true, 0, trxName);
            } else {
                continue;
            }
            if (!forceDelivery) {
                BigDecimal maxQty = Env.ZERO;
                for (int ll = 0; ll < storages.length; ll++) maxQty = maxQty.add(storages[ll].getQtyOnHand());
                if (DELIVERYRULE_Availability.equals(order.getDeliveryRule())) {
                    if (maxQty.compareTo(qty) < 0) qty = maxQty;
                } else if (DELIVERYRULE_CompleteLine.equals(order.getDeliveryRule())) {
                    if (maxQty.compareTo(qty) < 0) continue;
                }
            }
            if (retValue.get_ID() == 0) retValue.save(trxName);
            for (int ll = 0; ll < storages.length; ll++) {
                BigDecimal lineQty = storages[ll].getQtyOnHand();
                if (lineQty.compareTo(qty) > 0) lineQty = qty;
                MInOutLine line = new MInOutLine(retValue);
                line.setOrderLine(oLines[i], storages[ll].getM_Locator_ID(), order.isSOTrx() ? lineQty : Env.ZERO);
                line.setQty(lineQty);
                if (oLines[i].getQtyEntered().compareTo(oLines[i].getQtyOrdered()) != 0) line.setQtyEntered(lineQty.multiply(oLines[i].getQtyEntered()).divide(oLines[i].getQtyOrdered(), 12, BigDecimal.ROUND_HALF_UP));
                line.setC_Project_ID(oLines[i].getC_Project_ID());
                line.save(trxName);
                qty = qty.subtract(lineQty);
                if (qty.signum() == 0) break;
            }
        }
        if (retValue.get_ID() == 0) return null;
        return retValue;
    }

    /**
	 * 	Create new Shipment by copying
	 * 	@param from shipment
	 * 	@param dateDoc date of the document date
	 * 	@param C_DocType_ID doc type
	 * 	@param isSOTrx sales order
	 * 	@param counter create counter links
	 * 	@param trxName trx
	 * 	@param setOrder set the order link
	 *	@return Shipment
	 */
    public static MInOut copyFrom(MInOut from, Timestamp dateDoc, Timestamp dateAcct, int C_DocType_ID, boolean isSOTrx, boolean counter, String trxName, boolean setOrder) {
        MInOut to = new MInOut(from.getCtx(), 0, null);
        to.set_TrxName(trxName);
        copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
        to.set_ValueNoCheck("M_InOut_ID", I_ZERO);
        to.set_ValueNoCheck("DocumentNo", null);
        to.setDocStatus(DOCSTATUS_Drafted);
        to.setDocAction(DOCACTION_Complete);
        to.setC_DocType_ID(C_DocType_ID);
        to.setIsSOTrx(isSOTrx);
        if (counter) {
            MDocType docType = MDocType.get(from.getCtx(), C_DocType_ID);
            if (MDocType.DOCBASETYPE_MaterialDelivery.equals(docType.getDocBaseType())) {
                to.setMovementType(isSOTrx ? MOVEMENTTYPE_CustomerShipment : MOVEMENTTYPE_VendorReturns);
            } else if (MDocType.DOCBASETYPE_MaterialReceipt.equals(docType.getDocBaseType())) {
                to.setMovementType(isSOTrx ? MOVEMENTTYPE_CustomerReturns : MOVEMENTTYPE_VendorReceipts);
            }
        }
        to.setDateOrdered(dateDoc);
        to.setDateAcct(dateAcct);
        to.setMovementDate(dateDoc);
        to.setDatePrinted(null);
        to.setIsPrinted(false);
        to.setDateReceived(null);
        to.setNoPackages(0);
        to.setShipDate(null);
        to.setPickDate(null);
        to.setIsInTransit(false);
        to.setIsApproved(false);
        to.setC_Invoice_ID(0);
        to.setTrackingNo(null);
        to.setIsInDispute(false);
        to.setPosted(false);
        to.setProcessed(false);
        to.setProcessing(false);
        to.setC_Order_ID(0);
        to.setM_RMA_ID(0);
        if (counter) {
            to.setC_Order_ID(0);
            to.setRef_InOut_ID(from.getM_InOut_ID());
            if (from.getC_Order_ID() != 0) {
                MOrder peer = new MOrder(from.getCtx(), from.getC_Order_ID(), from.get_TrxName());
                if (peer.getRef_Order_ID() != 0) to.setC_Order_ID(peer.getRef_Order_ID());
            }
            if (from.getC_Invoice_ID() != 0) {
                MInvoice peer = new MInvoice(from.getCtx(), from.getC_Invoice_ID(), from.get_TrxName());
                if (peer.getRef_Invoice_ID() != 0) to.setC_Invoice_ID(peer.getRef_Invoice_ID());
            }
            if (from.getM_RMA_ID() != 0) {
                MRMA peer = new MRMA(from.getCtx(), from.getM_RMA_ID(), from.get_TrxName());
                if (peer.getRef_RMA_ID() > 0) to.setM_RMA_ID(peer.getRef_RMA_ID());
            }
        } else {
            to.setRef_InOut_ID(0);
            if (setOrder) {
                to.setC_Order_ID(from.getC_Order_ID());
                to.setM_RMA_ID(from.getM_RMA_ID());
            }
        }
        if (!to.save(trxName)) throw new IllegalStateException("Could not create Shipment");
        if (counter) from.setRef_InOut_ID(to.getM_InOut_ID());
        if (to.copyLinesFrom(from, counter, setOrder) <= 0) throw new IllegalStateException("Could not create Shipment Lines");
        return to;
    }

    /**
	 *  @deprecated
	 * 	Create new Shipment by copying
	 * 	@param from shipment
	 * 	@param dateDoc date of the document date
	 * 	@param C_DocType_ID doc type
	 * 	@param isSOTrx sales order
	 * 	@param counter create counter links
	 * 	@param trxName trx
	 * 	@param setOrder set the order link
	 *	@return Shipment
	 */
    public static MInOut copyFrom(MInOut from, Timestamp dateDoc, int C_DocType_ID, boolean isSOTrx, boolean counter, String trxName, boolean setOrder) {
        MInOut to = copyFrom(from, dateDoc, dateDoc, C_DocType_ID, isSOTrx, counter, trxName, setOrder);
        return to;
    }

    /**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOut_ID
	 *	@param trxName rx name
	 */
    public MInOut(Properties ctx, int M_InOut_ID, String trxName) {
        super(ctx, M_InOut_ID, trxName);
        if (M_InOut_ID == 0) {
            setIsSOTrx(false);
            setMovementDate(new Timestamp(System.currentTimeMillis()));
            setDateAcct(getMovementDate());
            setDeliveryRule(DELIVERYRULE_Availability);
            setDeliveryViaRule(DELIVERYVIARULE_Pickup);
            setFreightCostRule(FREIGHTCOSTRULE_FreightIncluded);
            setDocStatus(DOCSTATUS_Drafted);
            setDocAction(DOCACTION_Complete);
            setPriorityRule(PRIORITYRULE_Medium);
            setNoPackages(0);
            setIsInTransit(false);
            setIsPrinted(false);
            setSendEMail(false);
            setIsInDispute(false);
            setIsApproved(false);
            super.setProcessed(false);
            setProcessing(false);
            setPosted(false);
        }
    }

    /**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *	@param trxName transaction
	 */
    public MInOut(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
	 * 	Order Constructor - create header only
	 *	@param order order
	 *	@param movementDate optional movement date (default today)
	 *	@param C_DocTypeShipment_ID document type or 0
	 */
    public MInOut(MOrder order, int C_DocTypeShipment_ID, Timestamp movementDate) {
        this(order.getCtx(), 0, order.get_TrxName());
        setClientOrg(order);
        setC_BPartner_ID(order.getC_BPartner_ID());
        setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());
        setAD_User_ID(order.getAD_User_ID());
        setM_Warehouse_ID(order.getM_Warehouse_ID());
        setIsSOTrx(order.isSOTrx());
        if (C_DocTypeShipment_ID == 0) C_DocTypeShipment_ID = DB.getSQLValue(null, "SELECT C_DocTypeShipment_ID FROM C_DocType WHERE C_DocType_ID=?", order.getC_DocType_ID());
        setC_DocType_ID(C_DocTypeShipment_ID);
        String movementTypeShipment = null;
        MDocType dtShipment = new MDocType(order.getCtx(), C_DocTypeShipment_ID, order.get_TrxName());
        if (dtShipment.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialDelivery)) movementTypeShipment = dtShipment.isSOTrx() ? MOVEMENTTYPE_CustomerShipment : MOVEMENTTYPE_VendorReturns; else if (dtShipment.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialReceipt)) movementTypeShipment = dtShipment.isSOTrx() ? MOVEMENTTYPE_CustomerReturns : MOVEMENTTYPE_VendorReceipts;
        setMovementType(movementTypeShipment);
        if (movementDate != null) setMovementDate(movementDate);
        setDateAcct(getMovementDate());
        setC_Order_ID(order.getC_Order_ID());
        setDeliveryRule(order.getDeliveryRule());
        setDeliveryViaRule(order.getDeliveryViaRule());
        setM_Shipper_ID(order.getM_Shipper_ID());
        setFreightCostRule(order.getFreightCostRule());
        setFreightAmt(order.getFreightAmt());
        setSalesRep_ID(order.getSalesRep_ID());
        setC_Activity_ID(order.getC_Activity_ID());
        setC_Campaign_ID(order.getC_Campaign_ID());
        setC_Charge_ID(order.getC_Charge_ID());
        setChargeAmt(order.getChargeAmt());
        setC_Project_ID(order.getC_Project_ID());
        setDateOrdered(order.getDateOrdered());
        setDescription(order.getDescription());
        setPOReference(order.getPOReference());
        setSalesRep_ID(order.getSalesRep_ID());
        setAD_OrgTrx_ID(order.getAD_OrgTrx_ID());
        setUser1_ID(order.getUser1_ID());
        setUser2_ID(order.getUser2_ID());
        setPriorityRule(order.getPriorityRule());
        setIsDropShip(order.isDropShip());
        setDropShip_BPartner_ID(order.getDropShip_BPartner_ID());
        setDropShip_Location_ID(order.getDropShip_Location_ID());
        setDropShip_User_ID(order.getDropShip_User_ID());
    }

    /**
	 * 	Invoice Constructor - create header only
	 *	@param invoice invoice
	 *	@param C_DocTypeShipment_ID document type or 0
	 *	@param movementDate optional movement date (default today)
	 *	@param M_Warehouse_ID warehouse
	 */
    public MInOut(MInvoice invoice, int C_DocTypeShipment_ID, Timestamp movementDate, int M_Warehouse_ID) {
        this(invoice.getCtx(), 0, invoice.get_TrxName());
        setClientOrg(invoice);
        setC_BPartner_ID(invoice.getC_BPartner_ID());
        setC_BPartner_Location_ID(invoice.getC_BPartner_Location_ID());
        setAD_User_ID(invoice.getAD_User_ID());
        setM_Warehouse_ID(M_Warehouse_ID);
        setIsSOTrx(invoice.isSOTrx());
        setMovementType(invoice.isSOTrx() ? MOVEMENTTYPE_CustomerShipment : MOVEMENTTYPE_VendorReceipts);
        MOrder order = null;
        if (invoice.getC_Order_ID() != 0) order = new MOrder(invoice.getCtx(), invoice.getC_Order_ID(), invoice.get_TrxName());
        if (C_DocTypeShipment_ID == 0 && order != null) C_DocTypeShipment_ID = DB.getSQLValue(null, "SELECT C_DocTypeShipment_ID FROM C_DocType WHERE C_DocType_ID=?", order.getC_DocType_ID());
        if (C_DocTypeShipment_ID != 0) setC_DocType_ID(C_DocTypeShipment_ID); else setC_DocType_ID();
        String movementTypeShipment = null;
        MDocType dtShipment = new MDocType(invoice.getCtx(), C_DocTypeShipment_ID, invoice.get_TrxName());
        if (dtShipment.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialDelivery)) movementTypeShipment = dtShipment.isSOTrx() ? MOVEMENTTYPE_CustomerShipment : MOVEMENTTYPE_VendorReturns; else if (dtShipment.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialReceipt)) movementTypeShipment = dtShipment.isSOTrx() ? MOVEMENTTYPE_CustomerReturns : MOVEMENTTYPE_VendorReceipts;
        setMovementType(movementTypeShipment);
        if (movementDate != null) setMovementDate(movementDate);
        setDateAcct(getMovementDate());
        setC_Order_ID(invoice.getC_Order_ID());
        setSalesRep_ID(invoice.getSalesRep_ID());
        setC_Activity_ID(invoice.getC_Activity_ID());
        setC_Campaign_ID(invoice.getC_Campaign_ID());
        setC_Charge_ID(invoice.getC_Charge_ID());
        setChargeAmt(invoice.getChargeAmt());
        setC_Project_ID(invoice.getC_Project_ID());
        setDateOrdered(invoice.getDateOrdered());
        setDescription(invoice.getDescription());
        setPOReference(invoice.getPOReference());
        setAD_OrgTrx_ID(invoice.getAD_OrgTrx_ID());
        setUser1_ID(invoice.getUser1_ID());
        setUser2_ID(invoice.getUser2_ID());
        if (order != null) {
            setDeliveryRule(order.getDeliveryRule());
            setDeliveryViaRule(order.getDeliveryViaRule());
            setM_Shipper_ID(order.getM_Shipper_ID());
            setFreightCostRule(order.getFreightCostRule());
            setFreightAmt(order.getFreightAmt());
            setIsDropShip(order.isDropShip());
            setDropShip_BPartner_ID(order.getDropShip_BPartner_ID());
            setDropShip_Location_ID(order.getDropShip_Location_ID());
            setDropShip_User_ID(order.getDropShip_User_ID());
        }
    }

    /**
	 * 	Copy Constructor - create header only
	 *	@param original original
	 *	@param movementDate optional movement date (default today)
	 *	@param C_DocTypeShipment_ID document type or 0
	 */
    public MInOut(MInOut original, int C_DocTypeShipment_ID, Timestamp movementDate) {
        this(original.getCtx(), 0, original.get_TrxName());
        setClientOrg(original);
        setC_BPartner_ID(original.getC_BPartner_ID());
        setC_BPartner_Location_ID(original.getC_BPartner_Location_ID());
        setAD_User_ID(original.getAD_User_ID());
        setM_Warehouse_ID(original.getM_Warehouse_ID());
        setIsSOTrx(original.isSOTrx());
        setMovementType(original.getMovementType());
        if (C_DocTypeShipment_ID == 0) setC_DocType_ID(original.getC_DocType_ID()); else setC_DocType_ID(C_DocTypeShipment_ID);
        if (movementDate != null) setMovementDate(movementDate);
        setDateAcct(getMovementDate());
        setC_Order_ID(original.getC_Order_ID());
        setDeliveryRule(original.getDeliveryRule());
        setDeliveryViaRule(original.getDeliveryViaRule());
        setM_Shipper_ID(original.getM_Shipper_ID());
        setFreightCostRule(original.getFreightCostRule());
        setFreightAmt(original.getFreightAmt());
        setSalesRep_ID(original.getSalesRep_ID());
        setC_Activity_ID(original.getC_Activity_ID());
        setC_Campaign_ID(original.getC_Campaign_ID());
        setC_Charge_ID(original.getC_Charge_ID());
        setChargeAmt(original.getChargeAmt());
        setC_Project_ID(original.getC_Project_ID());
        setDateOrdered(original.getDateOrdered());
        setDescription(original.getDescription());
        setPOReference(original.getPOReference());
        setSalesRep_ID(original.getSalesRep_ID());
        setAD_OrgTrx_ID(original.getAD_OrgTrx_ID());
        setUser1_ID(original.getUser1_ID());
        setUser2_ID(original.getUser2_ID());
        setIsDropShip(original.isDropShip());
        setDropShip_BPartner_ID(original.getDropShip_BPartner_ID());
        setDropShip_Location_ID(original.getDropShip_Location_ID());
        setDropShip_User_ID(original.getDropShip_User_ID());
    }

    /**	Lines					*/
    private MInOutLine[] m_lines = null;

    /** Confirmations			*/
    private MInOutConfirm[] m_confirms = null;

    /** BPartner				*/
    private MBPartner m_partner = null;

    /**
	 * 	Get Document Status
	 *	@return Document Status Clear Text
	 */
    public String getDocStatusName() {
        return MRefList.getListName(getCtx(), 131, getDocStatus());
    }

    /**
	 * 	Add to Description
	 *	@param description text
	 */
    public void addDescription(String description) {
        String desc = getDescription();
        if (desc == null) setDescription(description); else setDescription(desc + " | " + description);
    }

    /**
	 *	String representation
	 *	@return info
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer("MInOut[").append(get_ID()).append("-").append(getDocumentNo()).append(",DocStatus=").append(getDocStatus()).append("]");
        return sb.toString();
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
        ReportEngine re = ReportEngine.get(getCtx(), ReportEngine.SHIPMENT, getM_InOut_ID(), get_TrxName());
        if (re == null) return null;
        return re.getPDF(file);
    }

    /**
	 * 	Get Lines of Shipment
	 * 	@param requery refresh from db
	 * 	@return lines
	 */
    public MInOutLine[] getLines(boolean requery) {
        if (m_lines != null && !requery) {
            set_TrxName(m_lines, get_TrxName());
            return m_lines;
        }
        List<MInOutLine> list = new Query(getCtx(), I_M_InOutLine.Table_Name, "M_InOut_ID=?", get_TrxName()).setParameters(getM_InOut_ID()).setOrderBy(MInOutLine.COLUMNNAME_Line).list();
        m_lines = new MInOutLine[list.size()];
        list.toArray(m_lines);
        return m_lines;
    }

    /**
	 * 	Get Lines of Shipment
	 * 	@return lines
	 */
    public MInOutLine[] getLines() {
        return getLines(false);
    }

    /**
	 * 	Get Confirmations
	 * 	@param requery requery
	 *	@return array of Confirmations
	 */
    public MInOutConfirm[] getConfirmations(boolean requery) {
        if (m_confirms != null && !requery) {
            set_TrxName(m_confirms, get_TrxName());
            return m_confirms;
        }
        List<MInOutConfirm> list = new Query(getCtx(), I_M_InOutConfirm.Table_Name, "M_InOut_ID=?", get_TrxName()).setParameters(getM_InOut_ID()).list();
        m_confirms = new MInOutConfirm[list.size()];
        list.toArray(m_confirms);
        return m_confirms;
    }

    /**
	 * 	Copy Lines From other Shipment
	 *	@param otherShipment shipment
	 *	@param counter set counter info
	 *	@param setOrder set order link
	 *	@return number of lines copied
	 */
    public int copyLinesFrom(MInOut otherShipment, boolean counter, boolean setOrder) {
        if (isProcessed() || isPosted() || otherShipment == null) return 0;
        MInOutLine[] fromLines = otherShipment.getLines(false);
        int count = 0;
        for (int i = 0; i < fromLines.length; i++) {
            MInOutLine line = new MInOutLine(this);
            MInOutLine fromLine = fromLines[i];
            line.set_TrxName(get_TrxName());
            if (counter) PO.copyValues(fromLine, line, getAD_Client_ID(), getAD_Org_ID()); else PO.copyValues(fromLine, line, fromLine.getAD_Client_ID(), fromLine.getAD_Org_ID());
            line.setM_InOut_ID(getM_InOut_ID());
            line.set_ValueNoCheck("M_InOutLine_ID", I_ZERO);
            if (!setOrder) {
                line.setC_OrderLine_ID(0);
                line.setM_RMALine_ID(0);
            }
            if (!counter) line.setM_AttributeSetInstance_ID(0);
            line.setRef_InOutLine_ID(0);
            line.setIsInvoiced(false);
            line.setConfirmedQty(Env.ZERO);
            line.setPickedQty(Env.ZERO);
            line.setScrappedQty(Env.ZERO);
            line.setTargetQty(Env.ZERO);
            if (getM_Warehouse_ID() != otherShipment.getM_Warehouse_ID()) {
                line.setM_Locator_ID(0);
                line.setM_Locator_ID(Env.ZERO);
            }
            if (counter) {
                line.setRef_InOutLine_ID(fromLine.getM_InOutLine_ID());
                if (fromLine.getC_OrderLine_ID() != 0) {
                    MOrderLine peer = new MOrderLine(getCtx(), fromLine.getC_OrderLine_ID(), get_TrxName());
                    if (peer.getRef_OrderLine_ID() != 0) line.setC_OrderLine_ID(peer.getRef_OrderLine_ID());
                }
                if (fromLine.getM_RMALine_ID() != 0) {
                    MRMALine peer = new MRMALine(getCtx(), fromLine.getM_RMALine_ID(), get_TrxName());
                    if (peer.getRef_RMALine_ID() > 0) line.setM_RMALine_ID(peer.getRef_RMALine_ID());
                }
            }
            line.setProcessed(false);
            if (line.save(get_TrxName())) count++;
            if (counter) {
                fromLine.setRef_InOutLine_ID(line.getM_InOutLine_ID());
                fromLine.save(get_TrxName());
            }
        }
        if (fromLines.length != count) {
            log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
            count = -1;
        }
        return count;
    }

    /** Reversal Flag		*/
    private boolean m_reversal = false;

    /**
	 * 	Set Reversal
	 *	@param reversal reversal
	 */
    private void setReversal(boolean reversal) {
        m_reversal = reversal;
    }

    /**
	 * 	Is Reversal
	 *	@return reversal
	 */
    public boolean isReversal() {
        return m_reversal;
    }

    /**
	 * 	Set Processed.
	 * 	Propagate to Lines/Taxes
	 *	@param processed processed
	 */
    public void setProcessed(boolean processed) {
        super.setProcessed(processed);
        if (get_ID() == 0) return;
        String sql = "UPDATE M_InOutLine SET Processed='" + (processed ? "Y" : "N") + "' WHERE M_InOut_ID=" + getM_InOut_ID();
        int noLine = DB.executeUpdate(sql, get_TrxName());
        m_lines = null;
        log.fine(processed + " - Lines=" + noLine);
    }

    /**
	 * 	Get BPartner
	 *	@return partner
	 */
    public MBPartner getBPartner() {
        if (m_partner == null) m_partner = new MBPartner(getCtx(), getC_BPartner_ID(), get_TrxName());
        return m_partner;
    }

    /**
	 * 	Set Document Type
	 * 	@param DocBaseType doc type MDocType.DOCBASETYPE_
	 */
    public void setC_DocType_ID(String DocBaseType) {
        String sql = "SELECT C_DocType_ID FROM C_DocType " + "WHERE AD_Client_ID=? AND DocBaseType=?" + " AND IsActive='Y'" + " AND IsSOTrx='" + (isSOTrx() ? "Y" : "N") + "' " + "ORDER BY IsDefault DESC";
        int C_DocType_ID = DB.getSQLValue(null, sql, getAD_Client_ID(), DocBaseType);
        if (C_DocType_ID <= 0) log.log(Level.SEVERE, "Not found for AC_Client_ID=" + getAD_Client_ID() + " - " + DocBaseType); else {
            log.fine("DocBaseType=" + DocBaseType + " - C_DocType_ID=" + C_DocType_ID);
            setC_DocType_ID(C_DocType_ID);
            boolean isSOTrx = MDocType.DOCBASETYPE_MaterialDelivery.equals(DocBaseType);
            setIsSOTrx(isSOTrx);
        }
    }

    /**
	 * 	Set Default C_DocType_ID.
	 * 	Based on SO flag
	 */
    public void setC_DocType_ID() {
        if (isSOTrx()) setC_DocType_ID(MDocType.DOCBASETYPE_MaterialDelivery); else setC_DocType_ID(MDocType.DOCBASETYPE_MaterialReceipt);
    }

    /**
	 * 	Set Business Partner Defaults & Details
	 * 	@param bp business partner
	 */
    public void setBPartner(MBPartner bp) {
        if (bp == null) return;
        setC_BPartner_ID(bp.getC_BPartner_ID());
        MBPartnerLocation[] locs = bp.getLocations(false);
        if (locs != null) {
            for (int i = 0; i < locs.length; i++) {
                if (locs[i].isShipTo()) setC_BPartner_Location_ID(locs[i].getC_BPartner_Location_ID());
            }
            if (getC_BPartner_Location_ID() == 0 && locs.length > 0) setC_BPartner_Location_ID(locs[0].getC_BPartner_Location_ID());
        }
        if (getC_BPartner_Location_ID() == 0) log.log(Level.SEVERE, "Has no To Address: " + bp);
        MUser[] contacts = bp.getContacts(false);
        if (contacts != null && contacts.length > 0) setAD_User_ID(contacts[0].getAD_User_ID());
    }

    /**
	 * 	Create the missing next Confirmation
	 */
    public void createConfirmation() {
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        boolean pick = dt.isPickQAConfirm();
        boolean ship = dt.isShipConfirm();
        if (!pick && !ship) {
            log.fine("No need");
            return;
        }
        if (pick && ship) {
            boolean havePick = false;
            boolean haveShip = false;
            MInOutConfirm[] confirmations = getConfirmations(false);
            for (int i = 0; i < confirmations.length; i++) {
                MInOutConfirm confirm = confirmations[i];
                if (MInOutConfirm.CONFIRMTYPE_PickQAConfirm.equals(confirm.getConfirmType())) {
                    if (!confirm.isProcessed()) {
                        log.fine("Unprocessed: " + confirm);
                        return;
                    }
                    havePick = true;
                } else if (MInOutConfirm.CONFIRMTYPE_ShipReceiptConfirm.equals(confirm.getConfirmType())) haveShip = true;
            }
            if (!havePick) {
                MInOutConfirm.create(this, MInOutConfirm.CONFIRMTYPE_PickQAConfirm, false);
                return;
            }
            if (!haveShip) {
                MInOutConfirm.create(this, MInOutConfirm.CONFIRMTYPE_ShipReceiptConfirm, false);
                return;
            }
            return;
        }
        if (pick) MInOutConfirm.create(this, MInOutConfirm.CONFIRMTYPE_PickQAConfirm, true); else if (ship) MInOutConfirm.create(this, MInOutConfirm.CONFIRMTYPE_ShipReceiptConfirm, true);
    }

    private void voidConfirmations() {
        for (MInOutConfirm confirm : getConfirmations(true)) {
            if (!confirm.isProcessed()) {
                if (!confirm.processIt(MInOutConfirm.DOCACTION_Void)) throw new AdempiereException(confirm.getProcessMsg());
                confirm.saveEx();
            }
        }
    }

    /**
	 * 	Set Warehouse and check/set Organization
	 *	@param M_Warehouse_ID id
	 */
    public void setM_Warehouse_ID(int M_Warehouse_ID) {
        if (M_Warehouse_ID == 0) {
            log.severe("Ignored - Cannot set AD_Warehouse_ID to 0");
            return;
        }
        super.setM_Warehouse_ID(M_Warehouse_ID);
        MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
        if (wh.getAD_Org_ID() != getAD_Org_ID()) {
            log.warning("M_Warehouse_ID=" + M_Warehouse_ID + ", Overwritten AD_Org_ID=" + getAD_Org_ID() + "->" + wh.getAD_Org_ID());
            setAD_Org_ID(wh.getAD_Org_ID());
        }
    }

    /**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */
    protected boolean beforeSave(boolean newRecord) {
        if (newRecord) {
            MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
            if (wh.getAD_Org_ID() != getAD_Org_ID()) {
                log.saveError("WarehouseOrgConflict", "");
                return false;
            }
        }
        if (getC_Order_ID() != 0 && getM_RMA_ID() != 0) {
            log.saveError("OrderOrRMA", "");
            return false;
        }
        if (!getMovementType().contentEquals(MInOut.MOVEMENTTYPE_CustomerReturns) && isSOTrx() && getC_Order_ID() == 0 && getM_RMA_ID() == 0) {
            log.saveError("FillMandatory", Msg.translate(getCtx(), "C_Order_ID"));
            return false;
        }
        if (isSOTrx() && getM_RMA_ID() != 0) {
            MRMA rma = new MRMA(getCtx(), getM_RMA_ID(), get_TrxName());
            MDocType docType = MDocType.get(getCtx(), rma.getC_DocType_ID());
            setC_DocType_ID(docType.getC_DocTypeShipment_ID());
        }
        return true;
    }

    /**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
    protected boolean afterSave(boolean newRecord, boolean success) {
        if (!success || newRecord) return success;
        if (is_ValueChanged("AD_Org_ID")) {
            String sql = "UPDATE M_InOutLine ol" + " SET AD_Org_ID =" + "(SELECT AD_Org_ID" + " FROM M_InOut o WHERE ol.M_InOut_ID=o.M_InOut_ID) " + "WHERE M_InOut_ID=" + getC_Order_ID();
            int no = DB.executeUpdate(sql, get_TrxName());
            log.fine("Lines -> #" + no);
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
        log.info(toString());
        setProcessing(false);
        return true;
    }

    /**
	 * 	Invalidate Document
	 * 	@return true if success
	 */
    public boolean invalidateIt() {
        log.info(toString());
        setDocAction(DOCACTION_Prepare);
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
        if (getC_Order_ID() != 0 && getM_RMA_ID() != 0) {
            m_processMsg = "@OrderOrRMA@";
            return DocAction.STATUS_Invalid;
        }
        if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType(), getAD_Org_ID())) {
            m_processMsg = "@PeriodClosed@";
            return DocAction.STATUS_Invalid;
        }
        if (isSOTrx() && !isReversal()) {
            I_C_Order order = getC_Order();
            if (order != null && MDocType.DOCSUBTYPESO_PrepayOrder.equals(order.getC_DocType().getDocSubTypeSO()) && !MSysConfig.getBooleanValue("CHECK_CREDIT_ON_PREPAY_ORDER", true, getAD_Client_ID(), getAD_Org_ID())) {
            } else {
                MBPartner bp = new MBPartner(getCtx(), getC_BPartner_ID(), get_TrxName());
                if (MBPartner.SOCREDITSTATUS_CreditStop.equals(bp.getSOCreditStatus())) {
                    m_processMsg = "@BPartnerCreditStop@ - @TotalOpenBalance@=" + bp.getTotalOpenBalance() + ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
                    return DocAction.STATUS_Invalid;
                }
                if (MBPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus())) {
                    m_processMsg = "@BPartnerCreditHold@ - @TotalOpenBalance@=" + bp.getTotalOpenBalance() + ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
                    return DocAction.STATUS_Invalid;
                }
                BigDecimal notInvoicedAmt = MBPartner.getNotInvoicedAmt(getC_BPartner_ID());
                if (MBPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus(notInvoicedAmt))) {
                    m_processMsg = "@BPartnerOverSCreditHold@ - @TotalOpenBalance@=" + bp.getTotalOpenBalance() + ", @NotInvoicedAmt@=" + notInvoicedAmt + ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
                    return DocAction.STATUS_Invalid;
                }
            }
        }
        MInOutLine[] lines = getLines(true);
        if (lines == null || lines.length == 0) {
            m_processMsg = "@NoLines@";
            return DocAction.STATUS_Invalid;
        }
        BigDecimal Volume = Env.ZERO;
        BigDecimal Weight = Env.ZERO;
        for (int i = 0; i < lines.length; i++) {
            MInOutLine line = lines[i];
            MProduct product = line.getProduct();
            if (product != null) {
                Volume = Volume.add(product.getVolume().multiply(line.getMovementQty()));
                Weight = Weight.add(product.getWeight().multiply(line.getMovementQty()));
            }
            if (line.getM_AttributeSetInstance_ID() != 0) continue;
            if (product != null && product.isASIMandatory(isSOTrx())) {
                m_processMsg = "@M_AttributeSet_ID@ @IsMandatory@ (@Line@ #" + lines[i].getLine() + ", @M_Product_ID@=" + product.getValue() + ")";
                return DocAction.STATUS_Invalid;
            }
        }
        setVolume(Volume);
        setWeight(Weight);
        if (!isReversal()) {
            createConfirmation();
        }
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
        if (m_processMsg != null) return DocAction.STATUS_Invalid;
        m_justPrepared = true;
        if (!DOCACTION_Complete.equals(getDocAction())) setDocAction(DOCACTION_Complete);
        return DocAction.STATUS_InProgress;
    }

    /**
	 * 	Approve Document
	 * 	@return true if success
	 */
    public boolean approveIt() {
        log.info(toString());
        setIsApproved(true);
        return true;
    }

    /**
	 * 	Reject Approval
	 * 	@return true if success
	 */
    public boolean rejectIt() {
        log.info(toString());
        setIsApproved(false);
        return true;
    }

    /**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 * 	@contributor mgrigioni - Melhorias no tratamento de erros do processo
	 *  @contributor rsantana - Criação do processo de Movimentação de Estoque
	 *  @contributor amontenegro - BF[#2388403]
	 */
    public String completeIt() {
        if (!m_justPrepared) {
            String status = prepareIt();
            if (!DocAction.STATUS_InProgress.equals(status)) return status;
        }
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
        if (m_processMsg != null) return DocAction.STATUS_Invalid;
        MDocType docTypeInOut = new MDocType(p_ctx, getC_DocType_ID(), get_TrxName());
        boolean lbr_IsReturn = docTypeInOut.get_ValueAsBoolean("lbr_IsReturn");
        if (docTypeInOut.get_ValueAsBoolean("lbr_GenerateMovement")) {
            MMovement movement = new MMovement(p_ctx, 0, get_TrxName());
            movement.setMovementDate(getDateAcct());
            Integer C_DocType_ID = null;
            C_DocType_ID = (Integer) docTypeInOut.get_Value("LBR_DocTypeMovement_ID");
            if (C_DocType_ID == null || C_DocType_ID.intValue() == 0) {
                log.log(Level.SEVERE, "C_DocType_ID = " + C_DocType_ID);
                return DocAction.STATUS_Invalid;
            }
            movement.setC_DocType_ID(C_DocType_ID);
            movement.setIsApproved(true);
            movement.save(get_TrxName());
            MInOutLine[] lines = getLines(false);
            for (MInOutLine line : lines) {
                Integer M_Warehouse_ID = null;
                M_Warehouse_ID = (Integer) docTypeInOut.get_Value("M_Warehouse_ID");
                if (M_Warehouse_ID == null || M_Warehouse_ID.intValue() == 0) {
                    log.log(Level.SEVERE, "M_Warehouse_ID = " + M_Warehouse_ID);
                    return DocAction.STATUS_Invalid;
                }
                Integer M_OrderLineLocator_ID = null;
                MOrderLine ordLine = new MOrderLine(Env.getCtx(), line.getC_OrderLine_ID(), get_TrxName());
                M_OrderLineLocator_ID = (Integer) ordLine.get_Value("M_Locator_ID");
                MMovementLine mLine = new MMovementLine(movement);
                mLine.setM_Product_ID(line.getM_Product_ID());
                mLine.setLine(line.getLine());
                mLine.setDescription(line.getDescription());
                mLine.setMovementQty(line.getMovementQty());
                if (lbr_IsReturn) mLine.setM_LocatorTo_ID(M_OrderLineLocator_ID); else mLine.setM_Locator_ID(line.getM_Locator_ID());
                int locatorTo = AdempiereLBR.getM_Locator_ID(M_Warehouse_ID, getBPartner(), get_TrxName());
                if (locatorTo <= 0) {
                    log.log(Level.SEVERE, "M_LocatorTo_ID = " + locatorTo);
                    return DocAction.STATUS_Invalid;
                }
                if (lbr_IsReturn) mLine.setM_Locator_ID(locatorTo); else mLine.setM_LocatorTo_ID(locatorTo);
                mLine.save(get_TrxName());
                line.setProcessed(true);
                line.save(get_TrxName());
                if (!MStorage.add(getCtx(), getM_Warehouse_ID(), line.getM_Locator_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), line.getM_AttributeSetInstance_ID(), null, line.getMovementQty().negate(), null, get_TrxName())) {
                    m_processMsg = "Cannot correct Inventory";
                    return DocAction.STATUS_Invalid;
                }
                int M_Product_ID = line.getM_Product_ID();
                int C_OrderLine_ID = line.getC_OrderLine_ID();
                if (M_Product_ID != 0 && C_OrderLine_ID != 0) {
                    MOrderLine oLine = new MOrderLine(getCtx(), C_OrderLine_ID, get_TrxName());
                    if (line.getMovementQty().signum() != 1) oLine.setQtyReserved(Env.ZERO); else oLine.setQtyReserved(oLine.getQtyReserved().subtract(line.getMovementQty()));
                    oLine.save(get_TrxName());
                    log.info("OrderLine QtyReserved = " + oLine.getQtyReserved());
                }
            }
            movement.processIt(DocAction.ACTION_Complete);
            movement.set_Value("M_InOut_ID", getM_InOut_ID());
            movement.save(get_TrxName());
            if (movement.getDocStatus().equalsIgnoreCase(DocAction.STATUS_Completed)) {
                setProcessed(true);
                setDocAction(DocAction.ACTION_Close);
                movement.setDocAction(DocAction.ACTION_None);
                movement.setDocStatus(DocAction.STATUS_Closed);
                movement.save();
                String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
                if (valid != null) {
                    m_processMsg = valid;
                    return DocAction.STATUS_Invalid;
                }
                return DocAction.STATUS_Completed;
            } else return DocAction.STATUS_Invalid;
        }
        MInOutConfirm[] confirmations = getConfirmations(true);
        for (int i = 0; i < confirmations.length; i++) {
            MInOutConfirm confirm = confirmations[i];
            if (!confirm.isProcessed()) {
                if (MInOutConfirm.CONFIRMTYPE_CustomerConfirmation.equals(confirm.getConfirmType())) continue;
                m_processMsg = "Open @M_InOutConfirm_ID@: " + confirm.getConfirmTypeName() + " - " + confirm.getDocumentNo();
                return DocAction.STATUS_InProgress;
            }
        }
        if (!isApproved()) approveIt();
        log.info(toString());
        StringBuffer info = new StringBuffer();
        Set<Integer> inOutOrders = new TreeSet<Integer>();
        MInOutLine[] lines = getLines(false);
        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            MInOutLine sLine = lines[lineIndex];
            MProduct product = sLine.getProduct();
            String MovementType = getMovementType();
            BigDecimal Qty = sLine.getMovementQty();
            if (MovementType.charAt(1) == '-') Qty = Qty.negate();
            BigDecimal QtySO = Env.ZERO;
            BigDecimal QtyPO = Env.ZERO;
            MOrderLine oLine = null;
            if (sLine.getC_OrderLine_ID() != 0) {
                oLine = new MOrderLine(getCtx(), sLine.getC_OrderLine_ID(), get_TrxName());
                inOutOrders.add(oLine.getC_Order_ID());
                log.fine("OrderLine - Reserved=" + oLine.getQtyReserved() + ", Delivered=" + oLine.getQtyDelivered());
                if (isSOTrx()) QtySO = sLine.getMovementQty(); else QtyPO = sLine.getMovementQty();
            }
            MRMALine rmaLine = null;
            if (sLine.getM_RMALine_ID() != 0) {
                rmaLine = new MRMALine(getCtx(), sLine.getM_RMALine_ID(), get_TrxName());
            }
            log.info("Line=" + sLine.getLine() + " - Qty=" + sLine.getMovementQty());
            if (product != null && product.isStocked()) {
                if (!isReversal()) {
                    checkMaterialPolicy(sLine);
                }
                log.fine("Material Transaction");
                MTransaction mtrx = null;
                boolean sameWarehouse = true;
                int reservationAttributeSetInstance_ID = 0;
                if (oLine != null) {
                    reservationAttributeSetInstance_ID = oLine.getM_AttributeSetInstance_ID();
                    sameWarehouse = oLine.getM_Warehouse_ID() == getM_Warehouse_ID();
                }
                if (sLine.getM_AttributeSetInstance_ID() == 0) {
                    MInOutLineMA mas[] = MInOutLineMA.get(getCtx(), sLine.getM_InOutLine_ID(), get_TrxName());
                    for (int j = 0; j < mas.length; j++) {
                        MInOutLineMA ma = mas[j];
                        BigDecimal QtyMA = ma.getMovementQty();
                        if (MovementType.charAt(1) == '-') QtyMA = QtyMA.negate();
                        BigDecimal reservedDiff = Env.ZERO;
                        BigDecimal orderedDiff = Env.ZERO;
                        if (sLine.getC_OrderLine_ID() != 0) {
                            if (isSOTrx()) reservedDiff = ma.getMovementQty().negate(); else orderedDiff = ma.getMovementQty().negate();
                        }
                        if (!MStorage.add(getCtx(), getM_Warehouse_ID(), sLine.getM_Locator_ID(), sLine.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(), reservationAttributeSetInstance_ID, QtyMA, sameWarehouse ? reservedDiff : Env.ZERO, sameWarehouse ? orderedDiff : Env.ZERO, get_TrxName())) {
                            m_processMsg = "Cannot correct Inventory (MA)";
                            return DocAction.STATUS_Invalid;
                        }
                        if (!sameWarehouse) {
                            MWarehouse wh = MWarehouse.get(getCtx(), oLine.getM_Warehouse_ID());
                            if (!MStorage.add(getCtx(), oLine.getM_Warehouse_ID(), wh.getDefaultLocator().getM_Locator_ID(), sLine.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(), reservationAttributeSetInstance_ID, Env.ZERO, reservedDiff, orderedDiff, get_TrxName())) {
                                m_processMsg = "Cannot correct Inventory (MA) in order warehouse";
                                return DocAction.STATUS_Invalid;
                            }
                        }
                        mtrx = new MTransaction(getCtx(), sLine.getAD_Org_ID(), MovementType, sLine.getM_Locator_ID(), sLine.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(), QtyMA, getMovementDate(), get_TrxName());
                        mtrx.setM_InOutLine_ID(sLine.getM_InOutLine_ID());
                        if (!mtrx.save()) {
                            m_processMsg = "Could not create Material Transaction (MA)";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                }
                if (mtrx == null) {
                    BigDecimal reservedDiff = sameWarehouse ? QtySO.negate() : Env.ZERO;
                    BigDecimal orderedDiff = sameWarehouse ? QtyPO.negate() : Env.ZERO;
                    if (!MStorage.add(getCtx(), getM_Warehouse_ID(), sLine.getM_Locator_ID(), sLine.getM_Product_ID(), sLine.getM_AttributeSetInstance_ID(), reservationAttributeSetInstance_ID, Qty, reservedDiff, orderedDiff, get_TrxName())) {
                        m_processMsg = "Cannot correct Inventory";
                        return DocAction.STATUS_Invalid;
                    }
                    if (!sameWarehouse) {
                        MWarehouse wh = MWarehouse.get(getCtx(), oLine.getM_Warehouse_ID());
                        if (!MStorage.add(getCtx(), oLine.getM_Warehouse_ID(), wh.getDefaultLocator().getM_Locator_ID(), sLine.getM_Product_ID(), sLine.getM_AttributeSetInstance_ID(), reservationAttributeSetInstance_ID, Env.ZERO, QtySO.negate(), QtyPO.negate(), get_TrxName())) {
                            m_processMsg = "Cannot correct Inventory";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                    mtrx = new MTransaction(getCtx(), sLine.getAD_Org_ID(), MovementType, sLine.getM_Locator_ID(), sLine.getM_Product_ID(), sLine.getM_AttributeSetInstance_ID(), Qty, getMovementDate(), get_TrxName());
                    mtrx.setM_InOutLine_ID(sLine.getM_InOutLine_ID());
                    if (!mtrx.save()) {
                        m_processMsg = CLogger.retrieveErrorString("Could not create Material Transaction");
                        return DocAction.STATUS_Invalid;
                    }
                }
            }
            if (product != null && oLine != null) oLine.setQtyReserved(oLine.getQtyReserved().subtract(sLine.getMovementQty()));
            if (oLine != null) {
                if (isSOTrx() || sLine.getM_Product_ID() == 0) {
                    if (isSOTrx() && !(getC_DocType().getDocBaseType().equals("MMR"))) oLine.setQtyDelivered(oLine.getQtyDelivered().subtract(Qty)); else oLine.setQtyDelivered(oLine.getQtyDelivered().add(Qty));
                    oLine.setDateDelivered(getMovementDate());
                }
                if (!oLine.save()) {
                    m_processMsg = "Could not update Order Line";
                    return DocAction.STATUS_Invalid;
                } else log.fine("OrderLine -> Reserved=" + oLine.getQtyReserved() + ", Delivered=" + oLine.getQtyReserved());
            } else if (rmaLine != null) {
                if (isSOTrx()) {
                    rmaLine.setQtyDelivered(rmaLine.getQtyDelivered().add(Qty));
                } else {
                    rmaLine.setQtyDelivered(rmaLine.getQtyDelivered().subtract(Qty));
                }
                if (!rmaLine.save()) {
                    m_processMsg = "Could not update RMA Line";
                    return DocAction.STATUS_Invalid;
                }
            }
            if (product != null && isSOTrx() && product.isCreateAsset() && sLine.getMovementQty().signum() > 0 && !isReversal()) {
                log.fine("Asset");
                info.append("@A_Asset_ID@: ");
                int noAssets = sLine.getMovementQty().intValue();
                if (!product.isOneAssetPerUOM()) noAssets = 1;
                for (int i = 0; i < noAssets; i++) {
                    if (i > 0) info.append(" - ");
                    int deliveryCount = i + 1;
                    if (!product.isOneAssetPerUOM()) deliveryCount = 0;
                    MAsset asset = new MAsset(this, sLine, deliveryCount);
                    if (!asset.save(get_TrxName())) {
                        m_processMsg = "Could not create Asset";
                        return DocAction.STATUS_Invalid;
                    }
                    info.append(asset.getValue());
                }
            }
            if (!isSOTrx() && sLine.getM_Product_ID() != 0 && !isReversal()) {
                BigDecimal matchQty = sLine.getMovementQty();
                MInvoiceLine iLine = MInvoiceLine.getOfInOutLine(sLine);
                if (iLine != null && iLine.getM_Product_ID() != 0) {
                    if (matchQty.compareTo(iLine.getQtyInvoiced()) > 0) matchQty = iLine.getQtyInvoiced();
                    MMatchInv[] matches = MMatchInv.get(getCtx(), sLine.getM_InOutLine_ID(), iLine.getC_InvoiceLine_ID(), get_TrxName());
                    if (matches == null || matches.length == 0) {
                        MMatchInv inv = new MMatchInv(iLine, getMovementDate(), matchQty);
                        if (sLine.getM_AttributeSetInstance_ID() != iLine.getM_AttributeSetInstance_ID()) {
                            iLine.setM_AttributeSetInstance_ID(sLine.getM_AttributeSetInstance_ID());
                            iLine.save();
                            inv.setM_AttributeSetInstance_ID(sLine.getM_AttributeSetInstance_ID());
                        }
                        boolean isNewMatchInv = false;
                        if (inv.get_ID() == 0) isNewMatchInv = true;
                        if (!inv.save(get_TrxName())) {
                            m_processMsg = CLogger.retrieveErrorString("Could not create Inv Matching");
                            return DocAction.STATUS_Invalid;
                        }
                        if (isNewMatchInv) addDocsPostProcess(inv);
                    }
                }
                if (sLine.getC_OrderLine_ID() != 0) {
                    log.fine("PO Matching");
                    MMatchPO po = MMatchPO.create(null, sLine, getMovementDate(), matchQty);
                    boolean isNewMatchPO = false;
                    if (po.get_ID() == 0) isNewMatchPO = true;
                    if (!po.save(get_TrxName())) {
                        m_processMsg = "Could not create PO Matching";
                        return DocAction.STATUS_Invalid;
                    }
                    if (isNewMatchPO) addDocsPostProcess(po);
                    if (oLine != null && oLine.getM_AttributeSetInstance_ID() == 0 && sLine.getMovementQty().compareTo(oLine.getQtyOrdered()) == 0) {
                        oLine.setM_AttributeSetInstance_ID(sLine.getM_AttributeSetInstance_ID());
                        oLine.save(get_TrxName());
                    }
                } else {
                    if (iLine != null && iLine.getC_OrderLine_ID() != 0) {
                        log.fine("PO(Inv) Matching");
                        MMatchPO po = MMatchPO.create(iLine, sLine, getMovementDate(), matchQty);
                        boolean isNewMatchPO = false;
                        if (po.get_ID() == 0) isNewMatchPO = true;
                        if (!po.save(get_TrxName())) {
                            m_processMsg = "Could not create PO(Inv) Matching";
                            return DocAction.STATUS_Invalid;
                        }
                        if (isNewMatchPO) addDocsPostProcess(po);
                        oLine = new MOrderLine(getCtx(), po.getC_OrderLine_ID(), get_TrxName());
                        if (oLine != null && oLine.getM_AttributeSetInstance_ID() == 0 && sLine.getMovementQty().compareTo(oLine.getQtyOrdered()) == 0) {
                            oLine.setM_AttributeSetInstance_ID(sLine.getM_AttributeSetInstance_ID());
                            oLine.save(get_TrxName());
                        }
                    }
                }
            }
        }
        MInOut counter = createCounterDoc();
        if (counter != null) info.append(" - @CounterDoc@: @M_InOut_ID@=").append(counter.getDocumentNo());
        MInOut dropShipment = createDropShipment();
        if (dropShipment != null) info.append(" - @DropShipment@: @M_InOut_ID@=").append(dropShipment.getDocumentNo());
        String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
        if (valid != null) {
            m_processMsg = valid;
            return DocAction.STATUS_Invalid;
        }
        setDefiniteDocumentNo();
        if (inOutOrders.size() > 0) {
            MOrder order;
            for (Iterator<Integer> it = inOutOrders.iterator(); it.hasNext(); ) {
                order = new MOrder(getCtx(), it.next().intValue(), get_TrxName());
                try {
                    order.updateIsDelivered();
                } catch (SQLException ee) {
                    log.warning("Could not update isDelivered flag on order " + order.getDocumentNo() + " : " + ee.getMessage());
                }
                order.saveEx(get_TrxName());
            }
        }
        m_processMsg = info.toString();
        setProcessed(true);
        setDocAction(DOCACTION_Close);
        return DocAction.STATUS_Completed;
    }

    ArrayList<PO> docsPostProcess = new ArrayList<PO>();

    private void addDocsPostProcess(PO doc) {
        docsPostProcess.add(doc);
    }

    public ArrayList<PO> getDocsPostProcess() {
        return docsPostProcess;
    }

    /**
	 * Automatically creates a customer shipment for any
	 * drop shipment material receipt
	 * Based on createCounterDoc() by JJ
	 * @return shipment if created else null
	 */
    private MInOut createDropShipment() {
        if (isSOTrx() || !isDropShip() || getC_Order_ID() == 0) return null;
        int C_DocTypeTarget_ID = 0;
        MDocType[] shipmentTypes = MDocType.getOfDocBaseType(getCtx(), MDocType.DOCBASETYPE_MaterialDelivery);
        for (int i = 0; i < shipmentTypes.length; i++) {
            if (shipmentTypes[i].isSOTrx() && (C_DocTypeTarget_ID == 0 || shipmentTypes[i].isDefault())) C_DocTypeTarget_ID = shipmentTypes[i].getC_DocType_ID();
        }
        MInOut dropShipment = copyFrom(this, getMovementDate(), getDateAcct(), C_DocTypeTarget_ID, !isSOTrx(), false, get_TrxName(), true);
        int linkedOrderID = new MOrder(getCtx(), getC_Order_ID(), get_TrxName()).getLink_Order_ID();
        if (linkedOrderID != 0) {
            dropShipment.setC_Order_ID(linkedOrderID);
            int invID = new MOrder(getCtx(), linkedOrderID, get_TrxName()).getC_Invoice_ID();
            if (invID != 0) dropShipment.setC_Invoice_ID(invID);
        } else return null;
        dropShipment.setC_BPartner_ID(getDropShip_BPartner_ID());
        dropShipment.setC_BPartner_Location_ID(getDropShip_Location_ID());
        dropShipment.setAD_User_ID(getDropShip_User_ID());
        dropShipment.setIsDropShip(false);
        dropShipment.setDropShip_BPartner_ID(0);
        dropShipment.setDropShip_Location_ID(0);
        dropShipment.setDropShip_User_ID(0);
        dropShipment.setMovementType(MOVEMENTTYPE_CustomerShipment);
        dropShipment.setSalesRep_ID(getSalesRep_ID());
        dropShipment.save(get_TrxName());
        MInOutLine[] lines = dropShipment.getLines(true);
        for (int i = 0; i < lines.length; i++) {
            MInOutLine dropLine = lines[i];
            MOrderLine ol = new MOrderLine(getCtx(), dropLine.getC_OrderLine_ID(), null);
            if (ol.getC_OrderLine_ID() != 0) {
                dropLine.setC_OrderLine_ID(ol.getLink_OrderLine_ID());
                dropLine.save();
            }
        }
        log.fine(dropShipment.toString());
        dropShipment.setDocAction(DocAction.ACTION_Complete);
        dropShipment.processIt(DocAction.ACTION_Complete);
        dropShipment.save();
        return dropShipment;
    }

    /**
	 * 	Set the definite document number after completed
	 */
    private void setDefiniteDocumentNo() {
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        if (dt.isOverwriteDateOnComplete()) {
            setMovementDate(new Timestamp(System.currentTimeMillis()));
        }
        if (dt.isOverwriteSeqOnComplete()) {
            String value = DB.getDocumentNo(getC_DocType_ID(), get_TrxName(), true, this);
            if (value != null) setDocumentNo(value);
        }
    }

    /**
	 * 	Check Material Policy
	 * 	Sets line ASI
	 */
    private void checkMaterialPolicy(MInOutLine line) {
        int no = MInOutLineMA.deleteInOutLineMA(line.getM_InOutLine_ID(), get_TrxName());
        if (no > 0) log.config("Delete old #" + no);
        String MovementType = getMovementType();
        boolean inTrx = MovementType.charAt(1) == '+';
        boolean needSave = false;
        MProduct product = line.getProduct();
        if (product != null && line.getM_Locator_ID() == 0) {
            line.setM_Warehouse_ID(getM_Warehouse_ID());
            line.setM_Locator_ID(inTrx ? Env.ZERO : line.getMovementQty());
            needSave = true;
        }
        if (product != null && line.getM_AttributeSetInstance_ID() == 0) {
            if (getMovementType().compareTo(MInOut.MOVEMENTTYPE_CustomerReturns) == 0 || getMovementType().compareTo(MInOut.MOVEMENTTYPE_VendorReceipts) == 0) {
                MAttributeSetInstance asi = null;
                MStorage[] storages = MStorage.getWarehouse(getCtx(), getM_Warehouse_ID(), line.getM_Product_ID(), 0, null, MClient.MMPOLICY_FiFo.equals(product.getMMPolicy()), false, line.getM_Locator_ID(), get_TrxName());
                for (MStorage storage : storages) {
                    if (storage.getQtyOnHand().signum() < 0) {
                        asi = new MAttributeSetInstance(getCtx(), storage.getM_AttributeSetInstance_ID(), get_TrxName());
                        break;
                    }
                }
                if (asi == null) {
                    asi = MAttributeSetInstance.create(getCtx(), product, get_TrxName());
                }
                line.setM_AttributeSetInstance_ID(asi.getM_AttributeSetInstance_ID());
                log.config("New ASI=" + line);
                needSave = true;
            } else if (getMovementType().compareTo(MInOut.MOVEMENTTYPE_VendorReturns) == 0 || getMovementType().compareTo(MInOut.MOVEMENTTYPE_CustomerShipment) == 0) {
                String MMPolicy = product.getMMPolicy();
                Timestamp minGuaranteeDate = getMovementDate();
                MStorage[] storages = MStorage.getWarehouse(getCtx(), getM_Warehouse_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), minGuaranteeDate, MClient.MMPOLICY_FiFo.equals(MMPolicy), true, line.getM_Locator_ID(), get_TrxName());
                BigDecimal qtyToDeliver = line.getMovementQty();
                for (MStorage storage : storages) {
                    if (storage.getQtyOnHand().compareTo(qtyToDeliver) >= 0) {
                        MInOutLineMA ma = new MInOutLineMA(line, storage.getM_AttributeSetInstance_ID(), qtyToDeliver);
                        ma.saveEx();
                        qtyToDeliver = Env.ZERO;
                    } else {
                        MInOutLineMA ma = new MInOutLineMA(line, storage.getM_AttributeSetInstance_ID(), storage.getQtyOnHand());
                        ma.saveEx();
                        qtyToDeliver = qtyToDeliver.subtract(storage.getQtyOnHand());
                        log.fine(ma + ", QtyToDeliver=" + qtyToDeliver);
                    }
                    if (qtyToDeliver.signum() == 0) break;
                }
                if (qtyToDeliver.signum() != 0) {
                    MAttributeSetInstance asi = MAttributeSetInstance.create(getCtx(), product, get_TrxName());
                    int M_AttributeSetInstance_ID = asi.getM_AttributeSetInstance_ID();
                    MInOutLineMA ma = new MInOutLineMA(line, M_AttributeSetInstance_ID, qtyToDeliver);
                    ma.saveEx();
                    log.fine("##: " + ma);
                }
            }
        }
        if (needSave) {
            line.saveEx();
        }
    }

    /**************************************************************************
	 * 	Create Counter Document
	 * 	@return InOut
	 */
    private MInOut createCounterDoc() {
        if (getRef_InOut_ID() != 0) return null;
        MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
        int counterC_BPartner_ID = org.getLinkedC_BPartner_ID(get_TrxName());
        if (counterC_BPartner_ID == 0) return null;
        MBPartner bp = new MBPartner(getCtx(), getC_BPartner_ID(), get_TrxName());
        int counterAD_Org_ID = bp.getAD_OrgBP_ID_Int();
        if (counterAD_Org_ID == 0) return null;
        MBPartner counterBP = new MBPartner(getCtx(), counterC_BPartner_ID, get_TrxName());
        MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID, get_TrxName());
        log.info("Counter BP=" + counterBP.getName());
        int C_DocTypeTarget_ID = 0;
        MDocTypeCounter counterDT = MDocTypeCounter.getCounterDocType(getCtx(), getC_DocType_ID());
        if (counterDT != null) {
            log.fine(counterDT.toString());
            if (!counterDT.isCreateCounter() || !counterDT.isValid()) return null;
            C_DocTypeTarget_ID = counterDT.getCounter_C_DocType_ID();
        } else {
            C_DocTypeTarget_ID = MDocTypeCounter.getCounterDocType_ID(getCtx(), getC_DocType_ID());
            log.fine("Indirect C_DocTypeTarget_ID=" + C_DocTypeTarget_ID);
            if (C_DocTypeTarget_ID <= 0) return null;
        }
        MInOut counter = copyFrom(this, getMovementDate(), getDateAcct(), C_DocTypeTarget_ID, !isSOTrx(), true, get_TrxName(), true);
        counter.setAD_Org_ID(counterAD_Org_ID);
        counter.setM_Warehouse_ID(counterOrgInfo.getM_Warehouse_ID());
        counter.setBPartner(counterBP);
        if (isDropShip()) {
            counter.setIsDropShip(true);
            counter.setDropShip_BPartner_ID(getDropShip_BPartner_ID());
            counter.setDropShip_Location_ID(getDropShip_Location_ID());
            counter.setDropShip_User_ID(getDropShip_User_ID());
        }
        counter.setSalesRep_ID(getSalesRep_ID());
        counter.save(get_TrxName());
        String MovementType = counter.getMovementType();
        boolean inTrx = MovementType.charAt(1) == '+';
        MInOutLine[] counterLines = counter.getLines(true);
        for (int i = 0; i < counterLines.length; i++) {
            MInOutLine counterLine = counterLines[i];
            counterLine.setClientOrg(counter);
            counterLine.setM_Warehouse_ID(counter.getM_Warehouse_ID());
            counterLine.setM_Locator_ID(0);
            counterLine.setM_Locator_ID(inTrx ? Env.ZERO : counterLine.getMovementQty());
            counterLine.save(get_TrxName());
        }
        log.fine(counter.toString());
        if (counterDT != null) {
            if (counterDT.getDocAction() != null) {
                counter.setDocAction(counterDT.getDocAction());
                counter.processIt(counterDT.getDocAction());
                counter.save(get_TrxName());
            }
        }
        return counter;
    }

    /**
	 * 	Void Document.
	 * 	@return true if success
	 */
    public boolean voidIt() {
        log.info(toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_VOID);
        if (m_processMsg != null) return false;
        if (DOCSTATUS_Closed.equals(getDocStatus()) || DOCSTATUS_Reversed.equals(getDocStatus()) || DOCSTATUS_Voided.equals(getDocStatus())) {
            m_processMsg = "Document Closed: " + getDocStatus();
            return false;
        }
        if (DOCSTATUS_Drafted.equals(getDocStatus()) || DOCSTATUS_Invalid.equals(getDocStatus()) || DOCSTATUS_InProgress.equals(getDocStatus()) || DOCSTATUS_Approved.equals(getDocStatus()) || DOCSTATUS_NotApproved.equals(getDocStatus())) {
            MInOutLine[] lines = getLines(false);
            for (int i = 0; i < lines.length; i++) {
                MInOutLine line = lines[i];
                BigDecimal old = line.getMovementQty();
                if (old.signum() != 0) {
                    line.setQty(Env.ZERO);
                    line.addDescription("Void (" + old + ")");
                    line.save(get_TrxName());
                }
            }
            setDocStatus(DOCSTATUS_Voided);
            saveEx();
            voidConfirmations();
        } else {
            return reverseCorrectIt();
        }
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_VOID);
        if (m_processMsg != null) return false;
        setProcessed(true);
        setDocAction(DOCACTION_None);
        return true;
    }

    /**
	 * 	Close Document.
	 * 	@return true if success
	 */
    public boolean closeIt() {
        log.info(toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_CLOSE);
        if (m_processMsg != null) return false;
        setProcessed(true);
        setDocAction(DOCACTION_None);
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_CLOSE);
        if (m_processMsg != null) return false;
        return true;
    }

    /**
	 * 	Reverse Correction - same date
	 * 	@return true if success
	 */
    public boolean reverseCorrectIt() {
        log.info(toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REVERSECORRECT);
        if (m_processMsg != null) return false;
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType(), getAD_Org_ID())) {
            m_processMsg = "@PeriodClosed@";
            return false;
        }
        if (!isSOTrx()) {
            MMatchInv[] mInv = MMatchInv.getInOut(getCtx(), getM_InOut_ID(), get_TrxName());
            for (int i = 0; i < mInv.length; i++) mInv[i].deleteEx(true);
            MMatchPO[] mPO = MMatchPO.getInOut(getCtx(), getM_InOut_ID(), get_TrxName());
            for (int i = 0; i < mPO.length; i++) {
                if (mPO[i].getC_InvoiceLine_ID() == 0) mPO[i].deleteEx(true); else {
                    mPO[i].setM_InOutLine_ID(0);
                    mPO[i].saveEx();
                }
            }
        }
        MInOut reversal = copyFrom(this, getMovementDate(), getDateAcct(), getC_DocType_ID(), isSOTrx(), false, get_TrxName(), true);
        if (reversal == null) {
            m_processMsg = "Could not create Ship Reversal";
            return false;
        }
        reversal.setReversal(true);
        MInOutLine[] sLines = getLines(false);
        MInOutLine[] rLines = reversal.getLines(false);
        for (int i = 0; i < rLines.length; i++) {
            MInOutLine rLine = rLines[i];
            rLine.setQtyEntered(rLine.getQtyEntered().negate());
            rLine.setMovementQty(rLine.getMovementQty().negate());
            rLine.setM_AttributeSetInstance_ID(sLines[i].getM_AttributeSetInstance_ID());
            rLine.setReversalLine_ID(sLines[i].getM_InOutLine_ID());
            if (!rLine.save(get_TrxName())) {
                m_processMsg = "Could not correct Ship Reversal Line";
                return false;
            }
            if (rLine.getM_AttributeSetInstance_ID() == 0) {
                MInOutLineMA mas[] = MInOutLineMA.get(getCtx(), sLines[i].getM_InOutLine_ID(), get_TrxName());
                for (int j = 0; j < mas.length; j++) {
                    MInOutLineMA ma = new MInOutLineMA(rLine, mas[j].getM_AttributeSetInstance_ID(), mas[j].getMovementQty().negate());
                    ma.saveEx();
                }
            }
            MAsset asset = MAsset.getFromShipment(getCtx(), sLines[i].getM_InOutLine_ID(), get_TrxName());
            if (asset != null) {
                asset.setIsActive(false);
                asset.addDescription("(" + reversal.getDocumentNo() + " #" + rLine.getLine() + "<-)");
                asset.save();
            }
        }
        reversal.setC_Order_ID(getC_Order_ID());
        reversal.setM_RMA_ID(getM_RMA_ID());
        reversal.addDescription("{->" + getDocumentNo() + ")");
        reversal.setReversal_ID(getM_InOut_ID());
        reversal.saveEx(get_TrxName());
        if (!reversal.processIt(DocAction.ACTION_Complete) || !reversal.getDocStatus().equals(DocAction.STATUS_Completed)) {
            m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
            return false;
        }
        reversal.closeIt();
        reversal.setProcessing(false);
        reversal.setDocStatus(DOCSTATUS_Reversed);
        reversal.setDocAction(DOCACTION_None);
        reversal.saveEx(get_TrxName());
        addDescription("(" + reversal.getDocumentNo() + "<-)");
        setDocStatus(DOCSTATUS_Reversed);
        saveEx();
        voidConfirmations();
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSECORRECT);
        if (m_processMsg != null) return false;
        m_processMsg = reversal.getDocumentNo();
        this.setReversal_ID(reversal.getM_InOut_ID());
        setProcessed(true);
        setDocStatus(DOCSTATUS_Reversed);
        setDocAction(DOCACTION_None);
        return true;
    }

    /**
	 * 	Reverse Accrual - none
	 * 	@return false
	 */
    public boolean reverseAccrualIt() {
        log.info(toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
        if (m_processMsg != null) return false;
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
        if (m_processMsg != null) return false;
        return false;
    }

    /**
	 * 	Re-activate
	 * 	@return false
	 */
    public boolean reActivateIt() {
        log.info(toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REACTIVATE);
        if (m_processMsg != null) return false;
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REACTIVATE);
        if (m_processMsg != null) return false;
        return false;
    }

    /*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
    public String getSummary() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDocumentNo());
        sb.append(":").append(" (#").append(getLines(false).length).append(")");
        if (getDescription() != null && getDescription().length() > 0) sb.append(" - ").append(getDescription());
        return sb.toString();
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
        return Env.ZERO;
    }

    /**
	 * 	Get C_Currency_ID
	 *	@return Accounting Currency
	 */
    public int getC_Currency_ID() {
        return Env.getContextAsInt(getCtx(), "$C_Currency_ID");
    }

    /**
	 * 	Document Status is Complete or Closed
	 *	@return true if CO, CL or RE
	 */
    public boolean isComplete() {
        String ds = getDocStatus();
        return DOCSTATUS_Completed.equals(ds) || DOCSTATUS_Closed.equals(ds) || DOCSTATUS_Reversed.equals(ds);
    }
}
