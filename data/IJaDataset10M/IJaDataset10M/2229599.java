package org.jboss.tutorial.jboss_deployment_descriptor.bean;

import java.util.HashMap;
import javax.ejb.Remove;

public interface ShoppingCart {

    void buy(String product, int quantity);

    void priceCheck(String product);

    HashMap<String, Integer> getCartContents();

    void checkout();
}
