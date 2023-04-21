package v201108.orderservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201108.ApproveAndOverbookOrders;
import com.google.api.ads.dfp.v201108.Order;
import com.google.api.ads.dfp.v201108.OrderPage;
import com.google.api.ads.dfp.v201108.OrderServiceInterface;
import com.google.api.ads.dfp.v201108.OrderStatus;
import com.google.api.ads.dfp.v201108.Statement;
import com.google.api.ads.dfp.v201108.UpdateResult;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * This example approves and overbooks all draft orders. To determine which
 * orders exist, run GetAllOrdersExample.java.
 *
 * Tags: OrderService.getOrdersByStatement, OrderService.performOrderAction
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class ApproveOrdersExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            OrderServiceInterface orderService = user.getService(DfpService.V201108.ORDER_SERVICE);
            String statementText = "WHERE status IN ('" + OrderStatus.DRAFT + "','" + OrderStatus.PENDING_APPROVAL + "') LIMIT 500";
            Statement filterStatement = new Statement();
            OrderPage page = new OrderPage();
            int offset = 0;
            int i = 0;
            List<Long> orderIds = new ArrayList<Long>();
            do {
                filterStatement.setQuery(statementText + " OFFSET " + offset);
                page = orderService.getOrdersByStatement(filterStatement);
                if (page.getResults() != null) {
                    for (Order order : page.getResults()) {
                        if (!order.getIsArchived()) {
                            System.out.println(i + ") Order with ID \"" + order.getId() + "\", name \"" + order.getName() + "\", and status \"" + order.getStatus() + "\" will be approved.");
                            orderIds.add(order.getId());
                            i++;
                        }
                    }
                }
                offset += 500;
            } while (offset < page.getTotalResultSetSize());
            System.out.println("Number of orders to be approved: " + orderIds.size());
            if (orderIds.size() > 0) {
                filterStatement.setQuery("WHERE id IN (" + StringUtils.join(orderIds, ",") + ")");
                ApproveAndOverbookOrders action = new ApproveAndOverbookOrders();
                UpdateResult result = orderService.performOrderAction(action, filterStatement);
                if (result != null && result.getNumChanges() > 0) {
                    System.out.println("Number of orders approved: " + result.getNumChanges());
                } else {
                    System.out.println("No orders were approved.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
