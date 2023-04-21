package org.petsoar.order;

import java.util.List;

public interface OrderProcessing {

    void addOrder(Order order);

    void cancelOrder(Order order);

    List getOrders();

    Order getOrder(long id);
}
