package com.cosmos.acacia.crm.gui.purchase;

import com.cosmos.acacia.crm.data.purchase.PurchaseInvoice;
import com.cosmos.acacia.gui.entity.BusinessDocumentListPanel;
import com.cosmos.acacia.gui.entity.EntityPanel;

/**
 *
 * @author Miro
 */
public class PurchaseInvoiceListPanel extends BusinessDocumentListPanel<PurchaseInvoice> {

    public PurchaseInvoiceListPanel() {
        super(PurchaseInvoice.class, null);
    }

    @Override
    protected EntityPanel getEntityPanel(PurchaseInvoice entity) {
        return new PurchaseInvoicePanel(this, entity);
    }
}
