package v201111.orderservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201111.Order;
import com.google.api.ads.dfp.v201111.OrderServiceInterface;

/**
 * This example creates new orders. To determine which orders exist, run
 * GetAllOrdersExample.java. To determine which companies are advertisers, run
 * GetCompaniesByStatementExample.java. To get salespeople and traffickers, run
 * GetAllUsersExample.java.
 *
 * Tags: OrderService.createOrders
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class CreateOrdersExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            OrderServiceInterface orderService = user.getService(DfpService.V201111.ORDER_SERVICE);
            Long advertiserId = Long.parseLong("INSERT_ADVERTISER_COMPANY_ID_HERE");
            Long salespersonId = Long.parseLong("INSERT_SALESPERSON_ID_HERE");
            Long traffickerId = Long.parseLong("INSERT_TRAFFICKER_ID_HERE");
            Order[] orders = new Order[5];
            for (int i = 0; i < 5; i++) {
                Order order = new Order();
                order.setName("Order #" + i);
                order.setAdvertiserId(advertiserId);
                order.setSalespersonId(salespersonId);
                order.setTraffickerId(traffickerId);
                orders[i] = order;
            }
            orders = orderService.createOrders(orders);
            if (orders != null) {
                for (Order order : orders) {
                    System.out.println("An order with ID \"" + order.getId() + "\" and named \"" + order.getName() + "\" was created.");
                }
            } else {
                System.out.println("No orders created.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
