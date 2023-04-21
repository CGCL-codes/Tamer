package org.yes.cart.payment.impl;

import org.junit.Before;
import org.junit.Test;
import org.yes.cart.domain.entity.CustomerOrder;
import org.yes.cart.domain.entity.CustomerOrderDelivery;
import org.yes.cart.payment.PaymentGateway;
import org.yes.cart.payment.dto.Payment;
import org.yes.cart.payment.persistence.entity.PaymentGatewayParameter;
import org.yes.cart.payment.service.CustomerOrderPaymentService;
import java.util.Iterator;
import java.util.UUID;
import static org.junit.Assert.assertEquals;

/**
 * User: Igor Azarny iazarny@yahoo.com
 * Date: 09-May-2011
 * Time: 14:12:54
 */
public class PayPalNvpPaymentGatewayImplTest extends CappPaymentModuleDBTestCase {

    private PaymentProcessorSurrogate paymentProcessor;

    private PayPalNvpPaymentGatewayImpl payPalNvpPaymentGateway;

    private CustomerOrderPaymentService customerOrderPaymentService;

    private boolean isTestAllowed() {
        return "true".equals(System.getProperty("testPgPayPal"));
    }

    @Before
    public void setUp() throws Exception {
        if (isTestAllowed()) {
            customerOrderPaymentService = (CustomerOrderPaymentService) ctx().getBean("customerOrderPaymentService");
            payPalNvpPaymentGateway = (PayPalNvpPaymentGatewayImpl) ctx().getBean("payPalNvpPaymentGateway");
            paymentProcessor = new PaymentProcessorSurrogate(customerOrderPaymentService, payPalNvpPaymentGateway);
        }
    }

    @Test
    public void testGetPaymentGatewayParameters() {
        if (isTestAllowed()) {
            for (PaymentGatewayParameter parameter : payPalNvpPaymentGateway.getPaymentGatewayParameters()) {
                assertEquals("payPalNvpPaymentGateway", parameter.getPgLabel());
            }
        }
    }

    @Test
    public void testAuthPlusReverseAuthorization() {
        if (isTestAllowed()) {
            String orderNum = UUID.randomUUID().toString();
            CustomerOrder customerOrder = createCustomerOrder(orderNum);
            assertEquals(Payment.PAYMENT_STATUS_OK, paymentProcessor.authorize(customerOrder, createCardParameters()));
            assertEquals(2, customerOrderPaymentService.findBy(orderNum, null, Payment.PAYMENT_STATUS_OK, PaymentGateway.AUTH).size());
            paymentProcessor.reverseAuthorizatios(orderNum);
            assertEquals(2, customerOrderPaymentService.findBy(orderNum, null, Payment.PAYMENT_STATUS_OK, PaymentGateway.REVERSE_AUTH).size());
            assertEquals(4, customerOrderPaymentService.findBy(orderNum, null, Payment.PAYMENT_STATUS_OK, null).size());
        }
    }

    @Test
    public void testAuthPlusCapture() {
        if (isTestAllowed()) {
            String orderNum = UUID.randomUUID().toString();
            CustomerOrder customerOrder = createCustomerOrder(orderNum);
            assertEquals(Payment.PAYMENT_STATUS_OK, paymentProcessor.authorize(customerOrder, createCardParameters()));
            assertEquals(2, customerOrderPaymentService.findBy(orderNum, null, Payment.PAYMENT_STATUS_OK, PaymentGateway.AUTH).size());
            Iterator<CustomerOrderDelivery> iter = customerOrder.getDelivery().iterator();
            assertEquals(Payment.PAYMENT_STATUS_OK, paymentProcessor.shipmentComplete(customerOrder, iter.next().getDevileryNum()));
            assertEquals(1, customerOrderPaymentService.findBy(orderNum, null, Payment.PAYMENT_STATUS_OK, PaymentGateway.CAPTURE).size());
            assertEquals(Payment.PAYMENT_STATUS_OK, paymentProcessor.shipmentComplete(customerOrder, iter.next().getDevileryNum()));
            assertEquals(2, customerOrderPaymentService.findBy(orderNum, null, Payment.PAYMENT_STATUS_OK, PaymentGateway.CAPTURE).size());
        }
    }

    @Test
    public void testAuthPlusCapturePlusVoidCapture() {
        if (isTestAllowed()) {
            orderCancelationFlow(false);
        }
    }

    @Test
    public void testAuthPlusCapturePlusRefund() {
        if (isTestAllowed()) {
            orderCancelationFlow(true);
        }
    }

    private void orderCancelationFlow(boolean useRefund) {
        String orderNum = UUID.randomUUID().toString();
        CustomerOrder customerOrder = createCustomerOrder(orderNum);
        assertEquals(Payment.PAYMENT_STATUS_OK, paymentProcessor.authorize(customerOrder, createCardParameters()));
        assertEquals(2, customerOrderPaymentService.findBy(orderNum, null, Payment.PAYMENT_STATUS_OK, PaymentGateway.AUTH).size());
        Iterator<CustomerOrderDelivery> iter = customerOrder.getDelivery().iterator();
        paymentProcessor.shipmentComplete(customerOrder, iter.next().getDevileryNum());
        assertEquals(1, customerOrderPaymentService.findBy(orderNum, null, Payment.PAYMENT_STATUS_OK, PaymentGateway.CAPTURE).size());
        paymentProcessor.shipmentComplete(customerOrder, iter.next().getDevileryNum());
        assertEquals(2, customerOrderPaymentService.findBy(orderNum, null, Payment.PAYMENT_STATUS_OK, PaymentGateway.CAPTURE).size());
        assertEquals(Payment.PAYMENT_STATUS_OK, paymentProcessor.cancelOrder(customerOrder, useRefund));
        assertEquals(2, customerOrderPaymentService.findBy(orderNum, null, Payment.PAYMENT_STATUS_OK, useRefund ? PaymentGateway.REFUND : PaymentGateway.VOID_CAPTURE).size());
        assertEquals(6, customerOrderPaymentService.findBy(orderNum, null, Payment.PAYMENT_STATUS_OK, null).size());
    }

    @Test
    public void testAuthCapture() {
        if (isTestAllowed()) {
            String orderNum = UUID.randomUUID().toString();
            CustomerOrder customerOrder = createCustomerOrder(orderNum);
            assertEquals(Payment.PAYMENT_STATUS_OK, paymentProcessor.authorizeCapture(customerOrder, createCardParameters()));
            assertEquals(2, customerOrderPaymentService.findBy(orderNum, null, Payment.PAYMENT_STATUS_OK, PaymentGateway.AUTH_CAPTURE).size());
        }
    }

    public String getVisaCardNumber() {
        return "4200341341826822";
    }
}
