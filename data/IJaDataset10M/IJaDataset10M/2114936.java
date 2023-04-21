package no.ugland.utransprod;

import java.util.List;
import java.util.Set;
import no.ugland.utransprod.model.Order;
import no.ugland.utransprod.model.OrderLine;
import no.ugland.utransprod.service.OrderManager;
import no.ugland.utransprod.util.ModelUtil;

/**
 * @author atle.brekka
 *
 */
public class SetOrderLineAttributeInfo {

    /**
	 * 
	 */
    public static void setOrderLineAttributeInfo() {
        OrderManager orderManager = (OrderManager) ModelUtil.getBean("orderManager");
        List<Order> orders = orderManager.findAllNotSent();
        Set<OrderLine> orderLines;
        for (Order order : orders) {
            orderManager.lazyLoadTree(order);
            orderLines = order.getOrderLines();
            for (OrderLine orderLine : orderLines) {
                orderLine.setAttributeInfo(orderLine.getAttributesAsString());
            }
            try {
                orderManager.saveOrder(order);
            } catch (ProTransException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        SetOrderLineAttributeInfo.setOrderLineAttributeInfo();
        System.exit(0);
    }
}
