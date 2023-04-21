package org.plazmaforge.bsolution.purchase.server.services;

import org.plazmaforge.bsolution.finance.common.beans.FinanceDocument;
import org.plazmaforge.bsolution.finance.common.beans.IPartnerableDocument;
import org.plazmaforge.bsolution.goods.server.services.AbstractGoodsDocumentService;
import org.plazmaforge.bsolution.purchase.common.beans.PurchaseServ;
import org.plazmaforge.bsolution.purchase.common.services.PurchaseServService;

/** 
 * @author Oleh Hapon
 * $Id: PurchaseServServiceImpl.java,v 1.2 2010/04/28 06:24:19 ohapon Exp $
 */
public class PurchaseServServiceImpl extends AbstractGoodsDocumentService<PurchaseServ, Integer> implements PurchaseServService {

    protected Class getEntityClass() {
        return PurchaseServ.class;
    }

    protected void preparePartnerMove(final FinanceDocument document) {
        preparePartnerMoveByPartnerableDocument((IPartnerableDocument) document);
    }
}
