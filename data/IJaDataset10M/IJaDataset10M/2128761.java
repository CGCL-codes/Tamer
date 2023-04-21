package org.openXpertya.grid;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.compiere.plaf.CompierePLAF;
import org.openXpertya.apps.form.VComponentsFactory;
import org.openXpertya.grid.ed.VLocator;
import org.openXpertya.model.MInOut;
import org.openXpertya.model.MInOutLine;
import org.openXpertya.model.MInvoice;
import org.openXpertya.model.MInvoiceLine;
import org.openXpertya.model.MLocator;
import org.openXpertya.model.MLocatorLookup;
import org.openXpertya.model.MOrderLine;
import org.openXpertya.model.MTab;
import org.openXpertya.model.MWarehouse;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import org.openXpertya.util.DisplayType;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class VCreateFromShipment extends VCreateFrom {

    /**
     * Constructor de la clase ...
     *
     *
     * @param mTab
     */
    protected VCreateFromShipment(MTab mTab) {
        super(mTab);
    }

    /** Descripción de Campos */
    private MInvoice m_invoice = null;

    /** Remito que invoca este Crear Desde */
    private MInOut inOut = null;

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws Exception
     */
    protected boolean dynInit() throws Exception {
        log.config("");
        setTitle(Msg.getElement(Env.getCtx(), "M_InOut_ID", false) + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));
        initInvoiceLookup();
        initInvoiceOrderLookup();
        parameterBankPanel.setVisible(false);
        int AD_Column_ID = 3537;
        MLocatorLookup locator = new MLocatorLookup(Env.getCtx(), p_WindowNo);
        locatorField = new VLocator("M_Locator_ID", true, false, true, locator, p_WindowNo);
        setDefaultLocator();
        initBPartner(false);
        bPartnerField.addVetoableChangeListener(new VetoableChangeListener() {

            @Override
            public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
                invoiceField.setValue(null);
                invoiceChanged(0);
            }
        });
        if (isSOTrx()) {
            invoiceLabel.setVisible(false);
            invoiceField.setVisible(false);
            if (getInOut().getC_Order_ID() > 0) {
                orderField.setValue(getInOut().getC_Order_ID());
                orderChanged(getInOut().getC_Order_ID());
            }
            if (getInOut().getLines().length > 0) {
                orderField.setReadWrite(false);
                invoiceOrderField.setReadWrite(false);
                bPartnerField.setReadWrite(false);
            }
        }
        return true;
    }

    private void setDefaultLocator() {
        Integer warehouseId = (Integer) p_mTab.getValue("M_Warehouse_ID");
        if (warehouseId != null) {
            MWarehouse warehouse = new MWarehouse(Env.getCtx(), warehouseId.intValue(), null);
            MLocator locator = warehouse.getDefaultLocator();
            if (locator != null) {
                locatorField.setValue(new Integer(locator.getM_Locator_ID()));
            }
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param C_BPartner_ID
     */
    protected void initBPDetails(int C_BPartner_ID) {
        log.config("C_BPartner_ID=" + C_BPartner_ID);
    }

    /**
     * Descripción de Método
     *
     *
     * @param C_Invoice_ID
     */
    private void loadInvoice(int C_Invoice_ID) {
        log.config("C_Invoice_ID=" + C_Invoice_ID);
        if (C_Invoice_ID > 0) {
            m_invoice = new MInvoice(Env.getCtx(), C_Invoice_ID, null);
            if (bPartnerField != null) {
                bPartnerField.setValue(m_invoice.getC_BPartner_ID());
            }
        }
        p_order = null;
        List<InvoiceLine> data = new ArrayList<InvoiceLine>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append("l.C_InvoiceLine_ID, ").append("l.Line, ").append("l.Description, ").append("l.M_Product_ID, ").append("p.Name AS ProductName, ").append("l.C_UOM_ID, ").append("QtyInvoiced, ").append("l.QtyInvoiced-SUM(NVL(mi.Qty,0)) AS RemainingQty, ").append("l.QtyEntered/l.QtyInvoiced AS Multiplier, ").append("COALESCE(l.C_OrderLine_ID,0) AS C_OrderLine_ID ").append("FROM C_UOM uom, C_InvoiceLine l, M_Product p, M_MatchInv mi ").append("WHERE l.C_UOM_ID=uom.C_UOM_ID ").append("AND l.M_Product_ID=p.M_Product_ID ").append("AND l.C_InvoiceLine_ID=mi.C_InvoiceLine_ID(+) ").append("AND l.C_Invoice_ID=? ").append("GROUP BY l.QtyInvoiced, l.QtyEntered/l.QtyInvoiced, l.C_UOM_ID, l.M_Product_ID, p.Name, l.C_InvoiceLine_ID, l.Line, l.C_OrderLine_ID, l.Description ").append("ORDER BY l.Line ");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql.toString());
            pstmt.setInt(1, C_Invoice_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                InvoiceLine invoiceLine = new InvoiceLine();
                invoiceLine.selected = false;
                invoiceLine.invoiceLineID = rs.getInt("C_InvoiceLine_ID");
                invoiceLine.lineNo = rs.getInt("Line");
                invoiceLine.description = rs.getString("Description");
                BigDecimal multiplier = rs.getBigDecimal("Multiplier");
                BigDecimal qtyInvoiced = rs.getBigDecimal("QtyInvoiced").multiply(multiplier);
                BigDecimal remainingQty = rs.getBigDecimal("RemainingQty").multiply(multiplier);
                invoiceLine.lineQty = qtyInvoiced;
                invoiceLine.remainingQty = remainingQty;
                invoiceLine.productID = rs.getInt("M_Product_ID");
                invoiceLine.productName = rs.getString("ProductName");
                invoiceLine.uomID = rs.getInt("C_UOM_ID");
                invoiceLine.uomName = getUOMName(invoiceLine.uomID);
                invoiceLine.orderLineID = rs.getInt("C_OrderLine_ID");
                if (invoiceLine.remainingQty.compareTo(BigDecimal.ZERO) > 0) {
                    data.add(invoiceLine);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql.toString(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception e) {
            }
        }
        loadTable(data);
    }

    /**
     * Descripción de Método
     *
     */
    protected void info() {
        int count = 0;
        for (SourceEntity sourceEntity : getSourceEntities()) {
            if (sourceEntity.selected) {
                count++;
            }
        }
        statusBar.setStatusLine(String.valueOf(count));
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    protected void save() throws CreateFromSaveException {
        Integer locatorID = (Integer) locatorField.getValue();
        if (locatorID == null || (locatorID == 0)) {
            locatorField.setBackground(CompierePLAF.getFieldBackground_Error());
            throw new CreateFromSaveException("@NoLocator@");
        }
        MInOut inout = getInOut();
        log.config(inout + ", C_Locator_ID=" + locatorID);
        if (p_order != null) {
            inout.setC_Order_ID(p_order.getC_Order_ID());
            inout.setDateOrdered(p_order.getDateOrdered());
            inout.setC_Project_ID(p_order.getC_Project_ID());
        }
        if ((m_invoice != null) && (m_invoice.getC_Invoice_ID() != 0)) {
            inout.setC_Invoice_ID(m_invoice.getC_Invoice_ID());
        }
        if (!inout.save()) {
            throw new CreateFromSaveException(CLogger.retrieveErrorAsString());
        }
        for (SourceEntity sourceEntity : getSelectedSourceEntities()) {
            DocumentLine docLine = (DocumentLine) sourceEntity;
            BigDecimal movementQty = docLine.remainingQty;
            int C_UOM_ID = docLine.uomID;
            int M_Product_ID = docLine.productID;
            MInOutLine iol = new MInOutLine(inout);
            iol.setM_Product_ID(M_Product_ID, C_UOM_ID);
            iol.setQty(movementQty);
            iol.setM_Locator_ID(locatorID);
            iol.setDescription(docLine.description);
            MInvoiceLine il = null;
            MOrderLine ol = null;
            if (docLine.isOrderLine()) {
                OrderLine orderLine = (OrderLine) docLine;
                iol.setC_OrderLine_ID(orderLine.orderLineID);
                ol = new MOrderLine(Env.getCtx(), orderLine.orderLineID, getTrxName());
                iol.setC_Project_ID(ol.getC_Project_ID());
                if (ol.getQtyEntered().compareTo(ol.getQtyOrdered()) != 0) {
                    iol.setMovementQty(movementQty.multiply(ol.getQtyOrdered()).divide(ol.getQtyEntered(), BigDecimal.ROUND_HALF_UP));
                    iol.setC_UOM_ID(ol.getC_UOM_ID());
                }
                if (ol.getM_AttributeSetInstance_ID() != 0) {
                    iol.setM_AttributeSetInstance_ID(ol.getM_AttributeSetInstance_ID());
                }
                if (M_Product_ID == 0 && ol.getC_Charge_ID() != 0) {
                    iol.setC_Charge_ID(ol.getC_Charge_ID());
                }
            } else if (docLine.isInvoiceLine()) {
                InvoiceLine invoiceLine = (InvoiceLine) docLine;
                if (m_invoice != null && m_invoice.isCreditMemo()) {
                    movementQty = movementQty.negate();
                }
                il = new MInvoiceLine(Env.getCtx(), invoiceLine.invoiceLineID, getTrxName());
                iol.setC_Project_ID(il.getC_Project_ID());
                if (il.getQtyEntered().compareTo(il.getQtyInvoiced()) != 0) {
                    iol.setQtyEntered(movementQty.multiply(il.getQtyInvoiced()).divide(il.getQtyEntered(), BigDecimal.ROUND_HALF_UP));
                    iol.setC_UOM_ID(il.getC_UOM_ID());
                }
                if (M_Product_ID == 0 && il.getC_Charge_ID() != 0) {
                    iol.setC_Charge_ID(il.getC_Charge_ID());
                }
                if (invoiceLine.orderLineID > 0) {
                    iol.setC_OrderLine_ID(invoiceLine.orderLineID);
                }
            }
            if (!iol.save()) {
                throw new CreateFromSaveException("@InOutLineSaveError@ (# " + docLine.lineNo + "):<br>" + CLogger.retrieveErrorAsString());
            } else if (il != null) {
                il.setM_InOutLine_ID(iol.getM_InOutLine_ID());
                if (!il.save()) {
                    throw new CreateFromSaveException("@InvoiceLineSaveError@ (# " + il.getLine() + "):<br>" + CLogger.retrieveErrorAsString());
                }
            }
        }
    }

    @Override
    protected String getOrderFilter() {
        StringBuffer filter = new StringBuffer();
        filter.append("C_Order.IsSOTrx='").append(getIsSOTrx()).append("' AND ").append("C_Order.DocStatus IN ('CL','CO') AND ").append("(C_Order.C_Order_ID IN ").append("(SELECT ol.C_Order_ID ").append("FROM C_OrderLine ol ").append("WHERE ol.QtyOrdered > ol.QtyDelivered) ").append("OR ").append("(C_Order.IsSOTrx='Y' AND POSITION('+' IN '").append(getInOut().getMovementType()).append("') > 0)").append("OR ").append("(C_Order.IsSOTrx='N' AND POSITION('-' IN '").append(getInOut().getMovementType()).append("') > 0)").append(")");
        return filter.toString();
    }

    @Override
    protected void orderChanged(int orderID) {
        invoiceField.setValue(null);
        invoiceOrderField.setValue(null);
        loadOrder(orderID, false);
        m_invoice = null;
    }

    /**
     * Este método es invocado cuando el usuario cambia la factura seleccionada
     * en el VLookup. Las subclases puede sobrescribir este comportamiento.
	 * @param invoiceID ID de la nueva factura seleccionada.
     */
    protected void invoiceChanged(int invoiceID) {
        orderField.setValue(null);
        invoiceOrderField.setValue(null);
        loadInvoice(invoiceID);
    }

    /**
     * Este método es invocado cuando el usuario cambia la factura con pedido seleccionada
     * en el VLookup. Las subclases puede sobrescribir este comportamiento.
	 * @param invoiceID ID de la nueva factura seleccionada.
     */
    protected void invoiceOrderChanged(int invoiceID) {
        int relatedOrderID = 0;
        orderField.setValue(null);
        invoiceField.setValue(null);
        if (invoiceID > 0) {
            MInvoice invoice = new MInvoice(getCtx(), invoiceID, getTrxName());
            relatedOrderID = invoice.getC_Order_ID();
        }
        loadOrder(relatedOrderID, false);
        if (relatedOrderID > 0) {
            orderField.setValue(relatedOrderID);
        }
    }

    /**
	 * @return Devuelve el remito origen de este CreateFrom.
	 */
    public MInOut getInOut() {
        if (inOut == null) {
            inOut = new MInOut(getCtx(), (Integer) p_mTab.getValue("M_InOut_ID"), getTrxName());
        }
        inOut.set_TrxName(getTrxName());
        return inOut;
    }

    @Override
    protected boolean beforeAddOrderLine(OrderLine orderLine) {
        String sql = "SELECT COUNT(*) FROM M_InOutLine WHERE M_InOut_ID = ? AND C_OrderLine_ID = ?";
        Long count = (Long) DB.getSQLObject(null, sql, new Object[] { getInOut().getM_InOut_ID(), orderLine.orderLineID });
        if (count != null && count > 0) {
            return false;
        }
        if ((isSOTrx() && getInOut().getMovementType().endsWith("+")) || (!isSOTrx() && getInOut().getMovementType().endsWith("-"))) {
            orderLine.remainingQty = orderLine.qtyDelivered;
        }
        return true;
    }

    /**
	 * Inicializa el lookup de facturas
	 */
    private void initInvoiceLookup() {
        String whereClause = getInvoiceFilter();
        invoiceField = VComponentsFactory.VLookupFactory("C_Invoice_ID", "C_Invoice", p_WindowNo, DisplayType.Search, whereClause, false);
        invoiceField.addVetoableChangeListener(new VetoableChangeListener() {

            @Override
            public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
                Integer invoiceID = (Integer) e.getNewValue();
                invoiceChanged(invoiceID == null ? 0 : invoiceID);
            }
        });
    }

    /**
	 * @return Devuelve el filtro que se aplica al Lookup de Facturas.
	 */
    protected String getInvoiceFilter() {
        StringBuffer filter = new StringBuffer();
        filter.append("C_Invoice.IsSOTrx='").append(getIsSOTrx()).append("' AND ").append("C_Invoice.DocStatus IN ('CL','CO') AND ").append("C_Invoice.C_Invoice_ID IN (").append("SELECT il.C_Invoice_ID ").append("FROM C_InvoiceLine il ").append("LEFT OUTER JOIN M_MatchInv mi ON (il.C_InvoiceLine_ID=mi.C_InvoiceLine_ID) ").append("GROUP BY il.C_Invoice_ID,mi.C_InvoiceLine_ID,il.QtyInvoiced ").append("HAVING (il.QtyInvoiced<>SUM(mi.Qty) AND mi.C_InvoiceLine_ID IS NOT NULL) OR mi.C_InvoiceLine_ID IS NULL) ");
        return filter.toString();
    }

    /**
	 * Inicializa el lookup de facturas con pedidos asociados.
	 */
    private void initInvoiceOrderLookup() {
        String whereClause = getInvoiceOrderFilter();
        invoiceOrderField = VComponentsFactory.VLookupFactory("C_Invoice_ID", "C_Invoice", p_WindowNo, DisplayType.Search, whereClause, false);
        invoiceOrderField.addVetoableChangeListener(new VetoableChangeListener() {

            @Override
            public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
                Integer invoiceID = (Integer) e.getNewValue();
                invoiceOrderChanged(invoiceID == null ? 0 : invoiceID);
            }
        });
    }

    /**
	 * @return Devuelve el filtro que se aplica al Lookup de Facturas asociadas
	 * a pedidos.
	 */
    protected String getInvoiceOrderFilter() {
        StringBuffer filter = new StringBuffer();
        filter.append("C_Invoice.IsSOTrx='").append(getIsSOTrx()).append("' AND ").append("C_Invoice.DocStatus IN ('CL','CO') AND ").append("C_Invoice.C_Order_ID IS NOT NULL AND ").append("C_Invoice.C_Order_ID IN (").append("SELECT C_Order.C_Order_ID ").append("FROM C_Order ").append("WHERE (").append(getOrderFilter()).append(")").append(")");
        return filter.toString();
    }

    /**
	 * Entidad Orígen: Línea de Factura
	 */
    protected class InvoiceLine extends DocumentLine {

        /** ID de la línea de factura */
        protected int invoiceLineID = 0;

        /** La línea de factura puede tener asociada a su vez una línea de pedido */
        protected int orderLineID = 0;

        @Override
        public boolean isInvoiceLine() {
            return true;
        }
    }
}
