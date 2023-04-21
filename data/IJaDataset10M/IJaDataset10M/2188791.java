package gemini.basic.dao;

import gemini.basic.model.OrderStatus;
import java.util.List;

/**
 *
 */
public interface OrderStatusDao {

    public List<OrderStatus> getAll();

    boolean deleteOrderStatus(OrderStatus cartStatus);

    public OrderStatus getById(Integer id);

    public OrderStatus saveOrUpdate(OrderStatus ostatus, boolean b);
}
