package org.springframework.integration.samples.process;

import org.junit.Assert;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.core.Message;

public class CoffeOrderTest {

    public static void main(String[] arg) {
        CoffeOrderTest test = new CoffeOrderTest();
        test.testGateway();
    }

    public void testGateway() {
        AbstractApplicationContext ac = new ClassPathXmlApplicationContext("main-config.xml", CoffeOrderTest.class);
        CoffeOrderService coffeService = (CoffeOrderService) ac.getBean("coffeOrderService");
        Assert.assertNotNull(coffeService);
        CoffeOrder order = new CoffeOrderImpl("MOCHIATO", "GRANDE", "Caramel", "Soy");
        coffeService.placeOrder(order, "25", 2);
        PaymentOrderService paymentService = (PaymentOrderService) ac.getBean("paymentOrderService");
        Assert.assertNotNull(paymentService);
        Payment payment = new PaymentImpl();
        paymentService.pay(payment, "25", 2);
        OrderFulfillmentService fulfillment = (OrderFulfillmentService) ac.getBean("orderFulfillmentService");
        Object coffe = (fulfillment.recieveOrder());
        System.out.println("Order's up - " + coffe);
        ac.close();
    }
}
