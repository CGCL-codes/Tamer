package org.apache.camel.example.cafe.test;

import org.apache.camel.example.cafe.OrderItem;
import org.apache.camel.example.cafe.stuff.DrinkRouter;

public class TestDrinkRouter extends DrinkRouter {

    private boolean testModel = true;

    public void setTestModel(boolean testModel) {
        this.testModel = testModel;
    }

    public String resolveOrderItemChannel(OrderItem orderItem) {
        if (testModel) {
            return (orderItem.isIced()) ? "mock:coldDrinks" : "mock:hotDrinks";
        } else {
            return super.resolveOrderItemChannel(orderItem);
        }
    }
}
