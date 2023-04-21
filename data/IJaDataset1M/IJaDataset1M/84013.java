package org.plazmaforge.bsolution.sale.server.services;

import org.plazmaforge.bsolution.finance.common.beans.FinanceDocument;
import org.plazmaforge.bsolution.finance.common.beans.IPartnerableDocument;
import org.plazmaforge.bsolution.goods.server.services.AbstractGoodsDocumentService;
import org.plazmaforge.bsolution.sale.common.beans.SaleInvoice;
import org.plazmaforge.bsolution.sale.common.services.SaleInvoiceService;

/**
 * @author hapon
 *
 */
public class SaleInvoiceServiceImpl extends AbstractGoodsDocumentService<SaleInvoice, Integer> implements SaleInvoiceService {

    protected Class getEntityClass() {
        return SaleInvoice.class;
    }

    protected void preparePartnerMove(final FinanceDocument document) {
        preparePartnerMoveByPartnerableDocument((IPartnerableDocument) document);
    }
}
