package de.aoj.example2;

public class Main {

    public static void main(String[] args) {
        Order.Entity order = Order.entity();
        order.addPosition(createOrderPosition(order, 2));
        testOrder(order);
        order.addPosition(createOrderPosition(order, 0));
        testOrder(order);
    }

    private static OrderPosition.Entity createOrderPosition(Order.Entity order, int quantity) {
        OrderPosition.Entity result = OrderPosition.entity(order);
        result.setQuantity(quantity);
        return result;
    }

    private static void testOrder(Order.Entity order) {
        if (order.isValid()) System.out.println("Order is valid"); else System.out.println("Order is not valid");
    }
}
