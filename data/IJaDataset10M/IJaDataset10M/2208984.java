package net.martinimix.service.payment;

import net.martinimix.dao.payment.PaymentDao;
import net.martinimix.domain.order.Order;
import net.martinimix.domain.order.OrderHeader;
import net.martinimix.domain.payment.CreditCard;
import net.martinimix.domain.payment.CreditCardAuthorization;
import net.martinimix.domain.payment.Payment;
import net.martinimix.domain.payment.PaymentMethod;
import net.martinimix.service.payment.provider.CreditCardProcessor;

/**
 * Provides a credit card payment service.  This payment service
 * relies on a {@link CreditCardProcessor} to process payments.
 * 
 * @author Scott Rossillo
 *
 */
public class CreditCardPaymentService implements PaymentService {

    private CreditCardProcessor creditCardProcessor;

    private PaymentDao paymentDao;

    /**
	 * Creates a new credit card payment service.
	 */
    public CreditCardPaymentService() {
    }

    /**
	 * Authorizes the given credit card as a payment method for the given order.
	 * 
	 * @param creditCard the <code>CreditCard</code> to authorize
	 * @param order the <code>Order</code> for which the given <code>creditCard</code>
	 * will be authorized
	 * 
	 * @return an order <code>Payment</code> resulting from this authorization request
	 */
    protected Payment authorizeCeditCard(CreditCard creditCard, Order order) {
        CreditCardAuthorization auth = creditCardProcessor.authorizePayment(creditCard, order);
        return handlePostAuthorization(auth, creditCard, order);
    }

    protected Payment handlePostAuthorization(CreditCardAuthorization auth, CreditCard creditCard, Order order) {
        Payment payment = order.findPayment(creditCard);
        if (payment == null) {
            payment = this.createPayment(creditCard, order.getOrderHeader());
        }
        auth.setPaymentId(payment.getId());
        if (auth.isPositiveResponse()) {
            payment.setStatusCode(Payment.STATUS_AUTHORIZED);
        } else {
            payment.setStatusCode(Payment.STATUS_INVALID);
        }
        paymentDao.savePayment(payment);
        paymentDao.saveCreditCardTransaction(auth);
        payment.setCreditCardTransaction(auth);
        return payment;
    }

    public Payment authorizePayment(PaymentMethod paymentMethod, Order order) {
        return authorizeCeditCard((CreditCard) paymentMethod, order);
    }

    public Payment createPayment(PaymentMethod paymentMethod, OrderHeader orderHeader) {
        final Payment payment = new Payment();
        final CreditCard creditCard = (CreditCard) paymentMethod;
        payment.setPaymentTypeId(creditCard.getId());
        payment.setPaymentType(Payment.TYPE_CREDIT_CARD);
        payment.setStatusCode(Payment.STATUS_PENDING_AUTHORIZATION);
        payment.setOrderHeaderId(orderHeader.getId());
        paymentDao.savePayment(payment);
        return payment;
    }

    /**
	 * Sets the credit card processor this service will use for payment transactions.
	 * 
	 * @param creditCardProcessor the <code>CreditCardProcessor</code> this payment
	 * service will use for payment transactions
	 */
    public void setCreditCardProcessor(CreditCardProcessor creditCardProcessor) {
        this.creditCardProcessor = creditCardProcessor;
    }

    /**
	 * Returns the payment data access object associated with this payment service.
	 * 
	 * @return the <code>PaymentDao</code> used by this service
	 */
    public PaymentDao getPaymentDao() {
        return paymentDao;
    }

    /**
	 * Sets the payment data access object for this payment service.
	 * 
	 * @param paymentDao the <code>PaymentDao</code> for this service
	 */
    public void setPaymentDao(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }
}
