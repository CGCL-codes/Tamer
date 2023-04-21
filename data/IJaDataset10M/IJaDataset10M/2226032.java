package com.avaje.tests.model.basic;

import javax.persistence.Entity;
import com.avaje.ebean.annotation.Sql;
import com.avaje.ebean.annotation.SqlSelect;

/**
 * This is an example of a "report" type entity that is based on raw SQL.
 * <p>
 * Typically the SQL includes a GROUP BY clause.
 * </p>
 * <p>
 * Note that often this type of entity doesn't have an @Id property.
 * It generally can not be saved or deleted. Also note that by default lazy 
 * loading is also disabled for this type of bean.
 * </p>
 */
@Entity
@Sql(select = { @SqlSelect(name = "default", query = "select order_id, count(*) as total_items, sum(order_qty*unit_price) as total_amount from o_order_detail group by order_id"), @SqlSelect(name = "total.only", query = "select order_id, sum(order_qty*unit_price) as total_amount from o_order_detail group by order_id") })
public class OrderReport {

    Integer orderId;

    Double totalAmount;

    Double totalItems;

    public String toString() {
        return "orderId:" + orderId + " totalAmount:" + totalAmount + " totalItems:" + totalItems;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Double totalItems) {
        this.totalItems = totalItems;
    }
}
