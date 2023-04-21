package org.compiere.process;

import java.util.logging.*;
import org.compiere.model.*;

/**
 *	Create (Generate) Invoice from Shipment
 *	
 *  @author Jorg Janke
 *  @version $Id: InOutCreateInvoice.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class InOutCreateInvoice extends SvrProcess {

    /**	Shipment					*/
    private int p_M_InOut_ID = 0;

    /**	Price List Version			*/
    private int p_M_PriceList_ID = 0;

    private String p_InvoiceDocumentNo = null;

    /**
	 *  Prepare - e.g., get Parameters.
	 */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null) ; else if (name.equals("M_PriceList_ID")) p_M_PriceList_ID = para[i].getParameterAsInt(); else if (name.equals("InvoiceDocumentNo")) p_InvoiceDocumentNo = (String) para[i].getParameter(); else log.log(Level.SEVERE, "Unknown Parameter: " + name);
        }
        p_M_InOut_ID = getRecord_ID();
    }

    /**
	 * 	Create Invoice.
	 *	@return document no
	 *	@throws Exception
	 */
    protected String doIt() throws Exception {
        log.info("M_InOut_ID=" + p_M_InOut_ID + ", M_PriceList_ID=" + p_M_PriceList_ID + ", InvoiceDocumentNo=" + p_InvoiceDocumentNo);
        if (p_M_InOut_ID == 0) throw new IllegalArgumentException("No Shipment");
        MInOut ship = new MInOut(getCtx(), p_M_InOut_ID, null);
        if (ship.get_ID() == 0) throw new IllegalArgumentException("Shipment not found");
        if (!MInOut.DOCSTATUS_Completed.equals(ship.getDocStatus())) throw new IllegalArgumentException("Shipment not completed");
        MInvoice invoice = new MInvoice(ship, null);
        if (p_M_PriceList_ID != 0 && ship.getM_RMA_ID() != 0) invoice.setM_PriceList_ID(p_M_PriceList_ID);
        if (p_InvoiceDocumentNo != null && p_InvoiceDocumentNo.length() > 0) invoice.setDocumentNo(p_InvoiceDocumentNo);
        if (!invoice.save()) throw new IllegalArgumentException("Cannot save Invoice");
        MInOutLine[] shipLines = ship.getLines(false);
        for (int i = 0; i < shipLines.length; i++) {
            MInOutLine sLine = shipLines[i];
            MInvoiceLine line = new MInvoiceLine(invoice);
            line.setShipLine(sLine);
            line.setQtyEntered(sLine.getQtyEntered());
            line.setQtyInvoiced(sLine.getMovementQty());
            if (!line.save()) throw new IllegalArgumentException("Cannot save Invoice Line");
        }
        return invoice.getDocumentNo();
    }
}
