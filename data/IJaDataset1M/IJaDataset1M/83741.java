package net.jautomock.test.mocking.example.orderservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.jautomock.testutil.XPathUtil;
import net.jautomock.testutil.XmlParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Example application, implementing a service used to order items.
 * 
 * @author Philipp Kumar
 */
public class OrderService {

    private int nextId = 1;

    private final Map<Integer, Order> orders = new HashMap<Integer, Order>();

    public int placeOrder(String orderXml) {
        Document orderDoc = XmlParser.parseString(orderXml);
        int customerId = getCustomerId(orderDoc);
        List<Node> items = getItems(orderDoc);
        Order order = new Order(customerId);
        for (Node item : items) {
            int productId = Integer.parseInt(item.getAttributes().getNamedItem("productId").getNodeValue());
            int quantity = Integer.parseInt(item.getAttributes().getNamedItem("quantity").getNodeValue());
            order.add(productId, quantity);
        }
        this.orders.put(this.nextId, order);
        return this.nextId++;
    }

    public double calculateTotalCharge(int orderId) {
        return this.orders.get(orderId).sumUp();
    }

    private int getCustomerId(Document order) {
        return Integer.parseInt(order.getElementsByTagName("order").item(0).getAttributes().getNamedItem("customerId").getNodeValue());
    }

    private List<Node> getItems(Document order) {
        return XPathUtil.evaluateXPath(order, "/order/item");
    }
}
