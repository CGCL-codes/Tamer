package org.broadleafcommerce.checkout.service;

import org.broadleafcommerce.core.payment.domain.PaymentResponseItem;
import org.broadleafcommerce.core.payment.domain.PaymentResponseItemImpl;
import org.broadleafcommerce.core.payment.service.PaymentContext;
import org.broadleafcommerce.core.payment.service.exception.PaymentException;
import org.broadleafcommerce.core.payment.service.module.AbstractModule;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;
import org.broadleafcommerce.openadmin.time.SystemTime;

/**
 * @author jfischer
 *
 */
public class DummyCreditCardModule extends AbstractModule {

    public PaymentResponseItem authorize(PaymentContext paymentContext) throws PaymentException {
        return createResponse(paymentContext);
    }

    public PaymentResponseItem authorizeAndDebit(PaymentContext paymentContext) throws PaymentException {
        return createResponse(paymentContext);
    }

    public PaymentResponseItem debit(PaymentContext paymentContext) throws PaymentException {
        return createResponse(paymentContext);
    }

    public PaymentResponseItem credit(PaymentContext paymentContext) throws PaymentException {
        return createResponse(paymentContext);
    }

    public PaymentResponseItem voidPayment(PaymentContext paymentContext) throws PaymentException {
        return createResponse(paymentContext);
    }

    public PaymentResponseItem balance(PaymentContext paymentContext) throws PaymentException {
        return createResponse(paymentContext);
    }

    public PaymentResponseItem reverseAuthorize(PaymentContext paymentContext) throws PaymentException {
        return createResponse(paymentContext);
    }

    private PaymentResponseItem createResponse(PaymentContext paymentContext) {
        paymentContext.getPaymentInfo().setReferenceNumber("abc123");
        PaymentResponseItem responseItem = new PaymentResponseItemImpl();
        responseItem.setTransactionTimestamp(SystemTime.asDate());
        responseItem.setReferenceNumber(paymentContext.getPaymentInfo().getReferenceNumber());
        responseItem.setTransactionId(paymentContext.getPaymentInfo().getReferenceNumber());
        responseItem.setTransactionSuccess(true);
        responseItem.setAmountPaid(paymentContext.getPaymentInfo().getAmount());
        return responseItem;
    }

    public Boolean isValidCandidate(PaymentInfoType paymentType) {
        return PaymentInfoType.CREDIT_CARD.equals(paymentType);
    }
}
