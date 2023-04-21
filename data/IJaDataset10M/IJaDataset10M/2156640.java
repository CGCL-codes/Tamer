package v201111.orderservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201111.Statement;
import com.google.api.ads.dfp.v201111.Order;
import com.google.api.ads.dfp.v201111.OrderPage;
import com.google.api.ads.dfp.v201111.OrderServiceInterface;

/**
 * This example updates the notes of each order up to the first 500.
 * To determine which orders exist, run GetAllOrdersExample.java.
 *
 * Tags: OrderService.getOrdersByStatement, OrderService.updateOrders
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class UpdateOrdersExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            OrderServiceInterface orderService = user.getService(DfpService.V201111.ORDER_SERVICE);
            Statement filterStatement = new Statement("LIMIT 500", null);
            OrderPage page = orderService.getOrdersByStatement(filterStatement);
            if (page.getResults() != null) {
                Order[] orders = page.getResults();
                int i = 0;
                for (Order order : orders) {
                    if (order.getIsArchived()) {
                        orders[i] = null;
                    } else {
                        order.setNotes("Spoke to advertiser. All is well.");
                    }
                    i++;
                }
                orders = orderService.updateOrders(orders);
                if (orders != null) {
                    for (Order order : orders) {
                        System.out.println("Order with ID \"" + order.getId() + "\", name \"" + order.getName() + "\", advertiser \"" + order.getAdvertiserId() + "\", and notes \"" + order.getNotes() + "\" was updated.");
                    }
                } else {
                    System.out.println("No orders updated.");
                }
            } else {
                System.out.println("No orders found to update.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
