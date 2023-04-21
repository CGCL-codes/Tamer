package org.openXpertya.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import org.openXpertya.process.DocAction;
import org.openXpertya.process.DocumentEngine;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class MRepairOrder extends X_C_Repair_Order implements DocAction {

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param C_Order_ID
     * @param trxName
     */
    public MRepairOrder(Properties ctx, int C_Repair_Order_ID, String trxName) {
        super(ctx, C_Repair_Order_ID, trxName);
        if (C_Repair_Order_ID == 0) {
            setDocStatus(DOCSTATUS_Drafted);
            setDocAction(DOCACTION_Prepare);
            setInvoiceRule(INVOICERULE_Immediate);
            setPaymentRule(PAYMENTRULE_OnCredit);
            setPriorityRule(PRIORITYRULE_Medium);
            setIsDiscountPrinted(false);
            setIsTaxIncluded(false);
            setSendEMail(false);
            setIsApproved(false);
            setIsPrinted(false);
            setIsCreditApproved(false);
            setIsInvoiced(false);
            setIsSelfService(false);
            super.setProcessed(false);
            setProcessing(false);
            setDatePromised(new Timestamp(System.currentTimeMillis()));
            setDateOrdered(new Timestamp(System.currentTimeMillis()));
            setTotalLines(Env.ZERO);
            setGrandTotal(Env.ZERO);
        }
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param project
     * @param IsSOTrx
     * @param DocSubTypeSO
     */
    public MRepairOrder(MProject project, boolean IsSOTrx, String DocSubTypeSO) {
        this(project.getCtx(), 0, project.get_TrxName());
        setAD_Client_ID(project.getAD_Client_ID());
        setAD_Org_ID(project.getAD_Org_ID());
        setC_Campaign_ID(project.getC_Campaign_ID());
        setSalesRep_ID(project.getSalesRep_ID());
        setC_Project_ID(project.getC_Project_ID());
        setDescription(project.getName());
        Timestamp ts = project.getDateContract();
        if (ts != null) {
            setDateOrdered(ts);
        }
        ts = project.getDateFinish();
        if (ts != null) {
            setDatePromised(ts);
        }
        setC_BPartner_ID(project.getC_BPartner_ID());
        setC_BPartner_Location_ID(project.getC_BPartner_Location_ID());
        setAD_User_ID(project.getAD_User_ID());
        setM_Warehouse_ID(project.getM_Warehouse_ID());
        setM_PriceList_ID(project.getM_PriceList_ID());
        setC_PaymentTerm_ID(project.getC_PaymentTerm_ID());
        if (IsSOTrx) {
            if ((DocSubTypeSO == null) || (DocSubTypeSO.length() == 0)) {
                setC_DocTypeTarget_ID(DocSubTypeSO_OnCredit);
            } else {
                setC_DocTypeTarget_ID(DocSubTypeSO);
            }
        } else {
            setC_DocTypeTarget_ID();
        }
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param rs
     * @param trxName
     */
    public MRepairOrder(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** Descripción de Campos */
    private MRepairOrderLine[] m_lines = null;

    /** Descripción de Campos */
    private boolean m_forceCreation = false;

    /**
     * Descripción de Método
     *
     *
     * @param AD_Client_ID
     * @param AD_Org_ID
     */
    public void setClientOrg(int AD_Client_ID, int AD_Org_ID) {
        super.setClientOrg(AD_Client_ID, AD_Org_ID);
    }

    /**
     * Descripción de Método
     *
     *
     * @param AD_Org_ID
     */
    public void setAD_Org_ID(int AD_Org_ID) {
        super.setAD_Org_ID(AD_Org_ID);
    }

    /**
     * Descripción de Método
     *
     *
     * @param description
     */
    public void addDescription(String description) {
        String desc = getDescription();
        if (desc == null) {
            setDescription(description);
        } else {
            setDescription(desc + " | " + description);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param C_BPartner_ID
     */
    public void setC_BPartner_ID(int C_BPartner_ID) {
        super.setC_BPartner_ID(C_BPartner_ID);
        super.setBill_BPartner_ID(C_BPartner_ID);
    }

    /**
     * Descripción de Método
     *
     *
     * @param C_BPartner_Location_ID
     */
    public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
        super.setC_BPartner_Location_ID(C_BPartner_Location_ID);
        super.setBill_Location_ID(C_BPartner_Location_ID);
    }

    /**
     * Descripción de Método
     *
     *
     * @param AD_User_ID
     */
    public void setAD_User_ID(int AD_User_ID) {
        super.setAD_User_ID(AD_User_ID);
        super.setBill_User_ID(AD_User_ID);
    }

    /**
     * Descripción de Método
     *
     *
     * @param C_BPartner_ID
     */
    public void setShip_BPartner_ID(int C_BPartner_ID) {
        super.setC_BPartner_ID(C_BPartner_ID);
    }

    /**
     * Descripción de Método
     *
     *
     * @param C_BPartner_Location_ID
     */
    public void setShip_Location_ID(int C_BPartner_Location_ID) {
        super.setC_BPartner_Location_ID(C_BPartner_Location_ID);
    }

    /**
     * Descripción de Método
     *
     *
     * @param AD_User_ID
     */
    public void setShip_User_ID(int AD_User_ID) {
        super.setAD_User_ID(AD_User_ID);
    }

    /**
     * Descripción de Método
     *
     *
     * @param M_Warehouse_ID
     */
    public void setM_Warehouse_ID(int M_Warehouse_ID) {
        super.setM_Warehouse_ID(M_Warehouse_ID);
    }

    /** Descripción de Campos */
    public static final String DocSubTypeSO_Standard = "SO";

    /** Descripción de Campos */
    public static final String DocSubTypeSO_Quotation = "OB";

    /** Descripción de Campos */
    public static final String DocSubTypeSO_Proposal = "ON";

    /** Descripción de Campos */
    public static final String DocSubTypeSO_Prepay = "PR";

    /** Descripción de Campos */
    public static final String DocSubTypeSO_POS = "WR";

    /** Descripción de Campos */
    public static final String DocSubTypeSO_Warehouse = "WP";

    /** Descripción de Campos */
    public static final String DocSubTypeSO_OnCredit = "WI";

    /** Descripción de Campos */
    public static final String DocSubTypeSO_RMA = "RM";

    /**
     * Descripción de Método
     *
     *
     * @param DocSubTypeSO_x
     */
    public void setC_DocTypeTarget_ID(String DocSubTypeSO_x) {
        String sql = "SELECT C_DocType_ID FROM C_DocType " + "WHERE AD_Client_ID=? AND AD_Org_ID IN (0," + getAD_Org_ID() + ") AND DocSubTypeSO=? " + "ORDER BY AD_Org_ID DESC, IsDefault DESC";
        int C_DocType_ID = DB.getSQLValue(null, sql, getAD_Client_ID(), DocSubTypeSO_x);
        if (C_DocType_ID <= 0) {
            log.severe("Not found for AD_Client_ID=" + getAD_Client_ID() + ", SubType=" + DocSubTypeSO_x);
        } else {
            log.fine("(SO) - " + DocSubTypeSO_x);
            setC_DocTypeTarget_ID(C_DocType_ID);
        }
    }

    /**
     * Descripción de Método
     *
     */
    public void setC_DocTypeTarget_ID() {
        String sql = "SELECT C_DocType_ID FROM C_DocType " + "WHERE AD_Client_ID=? AND AD_Org_ID IN (0," + getAD_Org_ID() + ") AND DocBaseType='POO' " + "ORDER BY AD_Org_ID DESC, IsDefault DESC";
        int C_DocType_ID = DB.getSQLValue(null, sql, getAD_Client_ID());
        if (C_DocType_ID <= 0) {
            log.severe("No POO found for AD_Client_ID=" + getAD_Client_ID());
        } else {
            log.fine("(PO) - " + C_DocType_ID);
            setC_DocTypeTarget_ID(C_DocType_ID);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param bp
     */
    public void setBPartner(MBPartner bp) {
        if (bp == null) {
            return;
        }
        setC_BPartner_ID(bp.getC_BPartner_ID());
        int ii = 0;
        ii = bp.getPO_PaymentTerm_ID();
        if (ii != 0) {
            setC_PaymentTerm_ID(ii);
        }
        ii = bp.getPO_PriceList_ID();
        if (ii != 0) {
            setM_PriceList_ID(ii);
        }
        String ss = bp.getInvoiceRule();
        if (ss != null) {
            setInvoiceRule(ss);
        }
        ss = bp.getPaymentRule();
        if (ss != null) {
            setPaymentRule(ss);
        }
        ii = bp.getSalesRep_ID();
        if (ii != 0) {
            setSalesRep_ID(ii);
        }
        MBPartnerLocation[] locs = bp.getLocations(false);
        if (locs != null) {
            for (int i = 0; i < locs.length; i++) {
                if (locs[i].isShipTo()) {
                    super.setC_BPartner_Location_ID(locs[i].getC_BPartner_Location_ID());
                }
                if (locs[i].isBillTo()) {
                    setBill_Location_ID(locs[i].getC_BPartner_Location_ID());
                }
            }
            if ((getC_BPartner_Location_ID() == 0) && (locs.length > 0)) {
                super.setC_BPartner_Location_ID(locs[0].getC_BPartner_Location_ID());
            }
            if ((getBill_Location_ID() == 0) && (locs.length > 0)) {
                setBill_Location_ID(locs[0].getC_BPartner_Location_ID());
            }
        }
        if (getC_BPartner_Location_ID() == 0) {
            log.log(Level.SEVERE, "MOrder.setBPartner - Has no Ship To Address: " + bp);
        }
        if (getBill_Location_ID() == 0) {
            log.log(Level.SEVERE, "MOrder.setBPartner - Has no Bill To Address: " + bp);
        }
        MUser[] contacts = bp.getContacts(false);
        if ((contacts != null) && (contacts.length == 1)) {
            setAD_User_ID(contacts[0].getAD_User_ID());
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("MRepairOrder[").append(getID()).append("-").append(getDocumentNo()).append(",C_DocType_ID=").append(getC_DocType_ID()).append("]");
        return sb.toString();
    }

    /**
     * Descripción de Método
     *
     *
     * @param M_PriceList_ID
     */
    public void setM_PriceList_ID(int M_PriceList_ID) {
        MPriceList pl = MPriceList.get(getCtx(), M_PriceList_ID, null);
        if (pl.getID() == M_PriceList_ID) {
            super.setM_PriceList_ID(M_PriceList_ID);
            setC_Currency_ID(pl.getC_Currency_ID());
            setIsTaxIncluded(pl.isTaxIncluded());
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getCurrencyISO() {
        return MCurrency.getISO_Code(getCtx(), getC_Currency_ID());
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public int getPrecision() {
        return MCurrency.getStdPrecision(getCtx(), getC_Currency_ID());
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getDocStatusName() {
        return MRefList.getListName(getCtx(), 131, getDocStatus());
    }

    /**
     * Descripción de Método
     *
     *
     * @param DocAction
     */
    public void setDocAction(String DocAction) {
        setDocAction(DocAction, false);
    }

    /**
     * Descripción de Método
     *
     *
     * @param DocAction
     * @param forceCreation
     */
    public void setDocAction(String DocAction, boolean forceCreation) {
        super.setDocAction(DocAction);
        m_forceCreation = forceCreation;
    }

    /**
     * Descripción de Método
     *
     *
     * @param processed
     */
    public void setProcessed(boolean processed) {
        super.setProcessed(processed);
    }

    /**
     * Descripción de Método
     *
     *
     * @param newRecord
     *
     * @return
     */
    protected boolean beforeSave(boolean newRecord) {
        return true;
    }

    /**
     * Descripción de Método
     * 
     *
     * @param newRecord
     * @param success
     *
     * @return
     */
    protected boolean afterSave(boolean newRecord, boolean success) {
        if (!success || newRecord) {
            return success;
        }
        afterSaveSync("AD_Org_ID");
        afterSaveSync("C_BPartner_ID");
        afterSaveSync("C_BPartner_Location_ID");
        afterSaveSync("DateOrdered");
        afterSaveSync("DatePromised");
        afterSaveSync("M_Warehouse_ID");
        afterSaveSync("C_Currency_ID");
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @param columnName
     */
    private void afterSaveSync(String columnName) {
        log.fine(" En afterSaveSync con columName = " + columnName);
        if (is_ValueChanged(columnName)) {
            String sql;
            if (DB.isPostgreSQL()) {
                sql = "UPDATE C_Repair_Order_Line " + "SET " + columnName + "=o." + columnName + " " + "FROM C_Repair_Order o " + "WHERE C_Repair_Order_Line.C_Repair_Order_ID=o.C_Repair_Order_ID " + "AND C_Repair_Order_Line.C_Repair_Order_ID=" + getC_Repair_Order_ID();
            } else {
                sql = "UPDATE C_Repair_Order_Line ol" + " SET " + columnName + " =" + "(SELECT " + columnName + " FROM C_Repair_Order o WHERE ol.C_Repair_Order_ID=o.C_Repair_Order_ID) " + "WHERE C_Repair_Order_ID=" + getC_Repair_Order_ID();
            }
            int no = DB.executeUpdate(sql, get_TrxName());
            log.fine(columnName + " Lines -> #" + no);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    protected boolean beforeDelete() {
        if (isProcessed()) {
            return false;
        }
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @param processAction
     *
     * @return
     */
    public boolean processIt(String processAction) {
        m_processMsg = null;
        DocumentEngine engine = new DocumentEngine(this, getDocStatus());
        return engine.processIt(processAction, getDocAction(), log);
    }

    /** Descripción de Campos */
    private boolean m_justPrepared = false;

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean unlockIt() {
        log.info("unlockIt - " + toString());
        setProcessing(false);
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean invalidateIt() {
        log.info(toString());
        setDocAction(DOCACTION_Prepare);
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String prepareIt() {
        m_justPrepared = true;
        return DocAction.STATUS_InProgress;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean approveIt() {
        log.info("approveIt - " + toString());
        setIsApproved(true);
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean rejectIt() {
        log.info("rejectIt - " + toString());
        setIsApproved(false);
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String completeIt() {
        if (canCompleteOrder() == false) {
            log.fine("completar orden de reparación: no se puede completar la orden de reparación");
            return getDocStatus();
        }
        if (haveToGenerateOrder(false) == true) {
            MRepairOrderTypeDialog dlg = new MRepairOrderTypeDialog(null, 0);
            if (dlg.initDialog() == false) {
                log.severe("completando orden de reparación: no se ha podido inicializar el diálogo");
                return getDocStatus();
            }
            dlg.setVisible(true);
            int C_DocTypeTarget_ID = dlg.getC_DocType_ID();
            if (C_DocTypeTarget_ID == -1) {
                log.info("completando orden de reparación: se ha cancelado el diálogo de selección de tipo de pedido");
                return getDocStatus();
            }
            boolean order_ok = generateOrder(C_DocTypeTarget_ID, false, true, 0, get_TrxName());
            if (order_ok == false) {
                log.severe("completar orden de reparación: no se ha podido crear el pedido de reparacion asociado");
                return getDocStatus();
            }
        }
        setDocAction(DOCACTION_Close);
        return DocAction.STATUS_Completed;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean postIt() {
        log.info(toString());
        return false;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean voidIt() {
        setProcessed(true);
        setDocAction(DOCACTION_None);
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean closeIt() {
        setProcessed(true);
        setDocAction(DOCACTION_None);
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean reverseCorrectIt() {
        log.info(toString());
        return voidIt();
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean reverseAccrualIt() {
        log.info(toString());
        return false;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean reActivateIt() {
        setDocAction(DOCACTION_Complete);
        setProcessed(false);
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getSummary() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDocumentNo());
        if ((getDescription() != null) && (getDescription().length() > 0)) {
            sb.append(" - ").append(getDescription());
        }
        return sb.toString();
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public int getDoc_User_ID() {
        return getSalesRep_ID();
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public BigDecimal getApprovalAmt() {
        return getGrandTotal();
    }

    /**
     * Descripción de Método
     *
     *
     * @param otherOrder
     * @param counter
     * @param copyASI
     *
     * @return
     */
    public int copyLinesFrom(MRepairOrder otherRepairOrder, boolean counter, boolean copyASI) {
        if (isProcessed() || (otherRepairOrder == null)) {
            return 0;
        }
        MRepairOrderLine[] fromLines = otherRepairOrder.getLines(false, null);
        int count = 0;
        for (int i = 0; i < fromLines.length; i++) {
            MRepairOrderLine line = new MRepairOrderLine(this);
            PO.copyValues(fromLines[i], line, getAD_Client_ID(), getAD_Org_ID());
            line.setC_Repair_Order_ID(getC_Repair_Order_ID());
            line.setRepairOrder(this);
            line.setC_Repair_Order_Line_ID(0);
            line.setC_Repair_Order_Product_ID(0);
            if (!copyASI) {
                line.setM_AttributeSetInstance_ID(0);
                line.setS_ResourceAssignment_ID(0);
            }
            if (getC_BPartner_ID() != otherRepairOrder.getC_BPartner_ID()) {
                line.setTax();
            }
            line.setProcessed(false);
            if (line.save(get_TrxName())) {
                count++;
            }
        }
        if (fromLines.length != count) {
            log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
        }
        return count;
    }

    /**
     * Descripción de Método
     *
     *
     * @param whereClause
     * @param orderClause
     *
     * @return
     */
    public MRepairOrderLine[] getLines(String whereClause, String orderClause) {
        log.fine("getLines 1");
        ArrayList list = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT * FROM C_Repair_Order_Line WHERE C_Repair_Order_ID=? ");
        if (whereClause != null) {
            sql.append(whereClause);
        }
        if (orderClause != null) {
            sql.append(" ").append(orderClause);
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
            pstmt.setInt(1, getC_Repair_Order_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MRepairOrderLine ol = new MRepairOrderLine(getCtx(), rs, get_TrxName());
                ol.setHeaderInfo(this);
                list.add(ol);
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getLines - " + sql, e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            pstmt = null;
        }
        MRepairOrderLine[] lines = new MRepairOrderLine[list.size()];
        list.toArray(lines);
        return lines;
    }

    /**
     * Descripción de Método
     *
     *
     * @param requery
     * @param orderBy
     *
     * @return
     */
    public MRepairOrderLine[] getLines(boolean requery, String orderBy) {
        log.fine("getLines 2");
        if ((m_lines != null) && !requery) {
            return m_lines;
        }
        String orderClause = "ORDER BY ";
        if ((orderBy != null) && (orderBy.length() > 0)) {
            orderClause += orderBy;
        } else {
            orderClause += "Line";
        }
        m_lines = getLines(null, orderClause);
        return m_lines;
    }

    /**
     * 	Genera el pedido o presupuesto de reparación que se le va a pasar al cliente
     * 
     *	@param C_DocTypeTarget_ID	Tipo de pedido que se va a generar
     *	@param generarPresupuesto	Indica si estamos generando un presupuesto que se pasará al cliente
     *								o un pedido que se va a cobrar. Si se genera un presupuesto, se
     *								guardan todas las líneas de la orden en el presupuesto. Si se genera
     *								un pedido, dependerá de los valores de guardarTodas y AD_PInstance_ID
     *	@param guardarTodas	Indica si en el pedido que generemos se deben incluir todas
     *						las líneas de la orden de reparación. En caso de que no se cojan todas las
     *						líneas, AD_PInstance_ID deberá tener un valor válido distinto de 0. Aunque
     *						se cojan todas las líneas, será includeLineInOrder() quien finalmente decida
     *						si se debe incluir en el pedido final
     *	@param AD_PInstance_ID	si no se guardan todas las líneas, AD_PInstance_ID debe ser el
     *							identificador del proceso en el cual se han elegido las líneas que
     *							se incluirán en el pedido
     *  @param trx_name	Identificador de la transaccion
     *  
     *  @return	devuelve true en caso de que se haya podido generar el pedido de reparación,
     *  		false en caso contrario
     */
    public boolean generateOrder(int C_DocTypeTarget_ID, boolean generarPresupuesto, boolean guardarTodas, int AD_PInstance_ID, String trx_name) {
        boolean dev = true;
        if (generarPresupuesto == false && guardarTodas == false && AD_PInstance_ID == 0) {
            log.severe("Al generar un pedido o presupuesto de reparación: generando pedido, eligiendo líneas, sin elección");
            return false;
        }
        ArrayList listado_lineas = getLineList(generarPresupuesto, guardarTodas, AD_PInstance_ID);
        if (listado_lineas == null) {
            log.severe("Al generar un pedido o presupuesto de reparación: error obteniendo líneas a incluir");
            return false;
        }
        if (listado_lineas.size() == 0) {
            log.info("Al generar un pedido o presupuesto de reparación: no hay líneas para incluir en el pedido");
            return true;
        }
        MOrder pedido_reparacion = new MOrder(getCtx(), 0, trx_name);
        boolean fill_ok = fillRepairOrder(pedido_reparacion, C_DocTypeTarget_ID, listado_lineas, generarPresupuesto, trx_name);
        if (fill_ok == false) {
            log.severe("Al generar un pedido o presupuesto de reparación: no se ha podido guardar la cabecera o las líneas");
            return false;
        }
        return dev;
    }

    /**
     * Obtiene una lista con las líneas que se deben incluir en el pedido o presupuesto de reparacion
     * segun los parametros pasados
     * 
     * @param generarPresupuesto	Si estamos generando un presupuesto, se guardan todas las líneas
     * @param guardarTodas	Si generamos un pedido, podemos guardar todas las líneas (dependiendo en
     * 						ultima instancia de includeLineInOrder()) o solo unas seleccionadas
     * @param AD_PInstance_ID	Si generamos un pedido, solo con algunas lineas, las obtendremos de la
     * 							tabla de procesado con esta instancia de proceso
     * @return
     */
    protected ArrayList getLineList(boolean generarPresupuesto, boolean guardarTodas, int AD_PInstance_ID) {
        ArrayList lista_lineas = new ArrayList();
        MRepairOrderLine lineas[] = getLines(false, null);
        int ilen = lineas.length;
        if (generarPresupuesto == true) {
            for (int i = 0; i < ilen; i++) {
                MRepairOrderLine linea = lineas[i];
                if (linea.includeLineInOrder(generarPresupuesto) == true) lista_lineas.add(linea);
            }
        } else if (guardarTodas == true) {
            for (int i = 0; i < ilen; i++) {
                MRepairOrderLine linea = lineas[i];
                if (linea.includeLineInOrder(generarPresupuesto) == true) lista_lineas.add(linea);
            }
        } else {
            lista_lineas = getLinesSelected(AD_PInstance_ID);
        }
        return lista_lineas;
    }

    /**
     * Obtiene las lineas seleccionadas en el proceso indicado por parametro
     * 
     * @param AD_PInstance_ID	identificador del proceso en el que se han marcado las líneas
     * 
     * @return	una lista de MRepairOrderLine si todo va bien, o null si hay algún problema
     */
    protected ArrayList getLinesSelected(int AD_PInstance_ID) {
        ArrayList lista = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT Key_ID ").append("FROM C_Process_Selection ").append("WHERE AD_PInstance_ID = ?");
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
            pstmt.setInt(1, AD_PInstance_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int C_Repair_Order_Line_ID = rs.getInt("Key_ID");
                MRepairOrderLine linea = new MRepairOrderLine(getCtx(), C_Repair_Order_Line_ID, get_TrxName());
                if (linea == null || linea.getC_Repair_Order_Line_ID() == 0) return null;
                lista.add(linea);
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;
        } catch (java.sql.SQLException e) {
            log.severe("Obteniendo líneas marcadas: " + e.toString());
            lista.clear();
            lista = null;
        }
        return lista;
    }

    /**
     * Guarda los datos de la cabecera del pedido y graba las líneas en el pedido o presupuesto
     * 
     * @param pedido_reparacion	pedido en el que vamos a guardar los datos
     * @param C_DocTypeTarget_ID	Tipo de documento destino
     * @param listado_lineas	lista con las lineas que se deben incluir en el pedido o presupuesto
     * @param esPresupuesto	si es presupuesto no hay que reflejar la linea de pedido
     * @param trx_name	nombre de la transaccion
     * 
     * @return	true si se han podido grabar todos los datos, false en caso contrario 
     */
    protected boolean fillRepairOrder(MOrder pedido_reparacion, int C_DocTypeTarget_ID, ArrayList listado_lineas, boolean esPresupuesto, String trx_name) {
        boolean dev = true;
        pedido_reparacion.setC_DocTypeTarget_ID(C_DocTypeTarget_ID);
        pedido_reparacion.setC_Repair_Order_ID(getC_Repair_Order_ID());
        pedido_reparacion.setDateOrdered(getDateOrdered());
        pedido_reparacion.setDatePromised(getDatePromised());
        pedido_reparacion.setC_BPartner_ID(getC_BPartner_ID());
        pedido_reparacion.setC_BPartner_Location_ID(getC_BPartner_Location_ID());
        pedido_reparacion.setBill_BPartner_ID(getBill_BPartner_ID());
        pedido_reparacion.setBill_Location_ID(getBill_Location_ID());
        pedido_reparacion.setAD_User_ID(getAD_User_ID());
        pedido_reparacion.setBill_User_ID(getBill_User_ID());
        pedido_reparacion.setDescription(getDescription());
        pedido_reparacion.setM_Warehouse_ID(getM_Warehouse_ID());
        pedido_reparacion.setInvoiceRule(getInvoiceRule());
        pedido_reparacion.setM_PriceList_ID(getM_PriceList_ID());
        pedido_reparacion.setC_Currency_ID(getC_Currency_ID());
        pedido_reparacion.setSalesRep_ID(getSalesRep_ID());
        if (pedido_reparacion.save(get_TrxName()) != true) {
            log.severe("guardando presupuesto o pedido de reparación: no se ha podido guardar la cabecera del documento");
            return false;
        }
        int llen = listado_lineas.size();
        for (int i = 0; i < llen; i++) {
            MRepairOrderLine linea = (MRepairOrderLine) listado_lineas.get(i);
            MOrderLine linea_reparacion = new MOrderLine(pedido_reparacion);
            linea_reparacion.setM_Product_ID(linea.getM_Product_ID());
            linea_reparacion.setM_AttributeSetInstance_ID(linea.getM_AttributeSetInstance_ID());
            linea_reparacion.setS_ResourceAssignment_ID(linea.getS_ResourceAssignment_ID());
            linea_reparacion.setDescription(linea.getDescription());
            linea_reparacion.setQtyOrdered(linea.getQtyOrdered());
            linea_reparacion.setQtyEntered(linea.getQtyEntered());
            linea_reparacion.setC_UOM_ID(linea.getC_UOM_ID());
            linea_reparacion.setPriceEntered(linea.getPriceEntered());
            linea_reparacion.setPriceActual(linea.getPriceActual());
            linea_reparacion.setPriceList(linea.getPriceList());
            linea_reparacion.setPriceLimit(linea.getPriceLimit());
            linea_reparacion.setC_Tax_ID(linea.getC_Tax_ID());
            linea_reparacion.setDiscount(linea.getDiscount());
            if (linea_reparacion.save(trx_name) != true) {
                log.severe("guardando presupuesto o pedido de reparación: no se ha podido grabar la nueva linea");
                return false;
            }
            if (esPresupuesto == false) {
                linea.setC_OrderLine_ID(linea_reparacion.getC_OrderLine_ID());
                if (linea.save(trx_name) != true) {
                    log.severe("guardando presupuesto o pedido de reparación: no se ha podido actualizar la línea de reparación");
                    return false;
                }
            }
        }
        return dev;
    }

    /**
     * Realizará comprobaciones especificas para saber si se puede completar una orden de reparacion
     * 
     * @return	true si se puede completar la orden de reparacion, false en caso contrario
     */
    protected boolean canCompleteOrder() {
        boolean dev = true;
        return dev;
    }

    /**
     * Comprueba si hay que generar un pedido o presupuesto
     * 
     * @param	esPresupuesto	indica si queremos generar un presupuesto o un pedido
     *
     * @return
     */
    protected boolean haveToGenerateOrder(boolean esPresupuesto) {
        boolean dev = true;
        if (isWarranty() == true) dev = false;
        return dev;
    }
}
