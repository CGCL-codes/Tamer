package org.plazmaforge.bsolution.bank.common;

import org.plazmaforge.bsolution.bank.common.beans.PaymentDocument;
import org.plazmaforge.bsolution.base.EnterpriseEnvironment;
import org.plazmaforge.bsolution.document.common.beans.Document;
import org.plazmaforge.bsolution.finance.common.DefaultFinanceDocumentCreator;
import org.plazmaforge.framework.core.exception.ApplicationException;

/** 
 * @author Oleh Hapon
 * $Id: TemplatePaymentDocumentCreator.java,v 1.3 2010/12/05 07:55:58 ohapon Exp $
 */
public class TemplatePaymentDocumentCreator extends DefaultFinanceDocumentCreator {

    protected void populate(Document parentDocument, Document document) throws ApplicationException {
        if (parentDocument == null || document == null) {
            return;
        }
        super.populate(parentDocument, document);
        PaymentDocument template = (PaymentDocument) parentDocument;
        PaymentDocument paymentDocument = (PaymentDocument) document;
        paymentDocument.setOrganizationBankAccount(template.getOrganizationBankAccount());
        paymentDocument.setPartnerBankAccount(template.getPartnerBankAccount());
        paymentDocument.setPaymentDate(template.getPaymentDate());
        paymentDocument.setPaymentType(template.getPaymentType());
        paymentDocument.setTaxPrintType(template.getTaxPrintType());
        paymentDocument.setPaymentMissing(template.getPaymentMissing());
        paymentDocument.setPeriodId(EnterpriseEnvironment.getPeriodId());
    }
}
