package net.sf.javamapper.jpetstore.dao.ibatis;

import net.sf.javamapper.jpetstore.domain.LineItem;
import net.sf.javamapper.jpetstore.domain.Order;
import org.springframework.dao.DataAccessException;

public class MsSqlOrderDao extends SqlMapOrderDao {

    /**
   * Special MS SQL Server version to allow the Item ID
	 * to be retrieved from an identity column.
   */
    public void insertOrder(Order order) throws DataAccessException {
        Integer orderId = (Integer) getSqlMapClientTemplate().queryForObject("msSqlServerInsertOrder", order);
        order.setOrderId(orderId.intValue());
        getSqlMapClientTemplate().insert("insertOrderStatus", order);
        for (int i = 0; i < order.getLineItems().size(); i++) {
            LineItem lineItem = (LineItem) order.getLineItems().get(i);
            lineItem.setOrderId(order.getOrderId());
            getSqlMapClientTemplate().insert("insertLineItem", lineItem);
        }
    }
}
