package transactionscript;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import etc.Month;

public class Service {

    private final Finder finder;

    public Service(Finder finder) {
        this.finder = finder;
    }

    public Set<Month> cuillenMonths(String name) {
        try {
            HashSet<Month> ret = new HashSet<Month>();
            ResultSet customerResult = finder.findCustomerIDNamed(name);
            if (customerResult.next()) {
                ResultSet ordersResult = finder.findOrders(customerResult.getInt("customer_id"));
                while (ordersResult.next()) if (isCuillen(ordersResult.getInt("order_id"))) ret.add(new Month(ordersResult.getDate("order_date")));
            }
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isCuillen(Integer orderID) {
        try {
            BigDecimal taliskerTotal = BigDecimal.ZERO;
            ResultSet lineItemResult = finder.findLineItemsForOrderID(orderID);
            while (lineItemResult.next()) if (lineItemResult.getString("product").equals("Talisker")) taliskerTotal = taliskerTotal.add(lineItemResult.getBigDecimal("cost"));
            return (taliskerTotal.compareTo(new BigDecimal(5000)) > 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
