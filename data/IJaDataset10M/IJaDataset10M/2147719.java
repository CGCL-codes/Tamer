package br.gov.demoiselle.pedidos.persistence.dao;

import java.util.Collection;
import br.gov.demoiselle.pedidos.bean.Order;
import br.gov.framework.demoiselle.core.layer.IDAO;

public interface IOrderDAO extends IDAO<Order> {

    /**
	 * Returns those orders that have a set arrival date indicating that they have shipped
	 * 
	 * @param itemId
	 * @return
	 */
    public Collection<Order> getShippedOrdersForItem(long itemId);

    /**
	 * Returns those orders that have a set arrival date indicating that they have shipped
	 * 
	 * @param itemId
	 * @return
	 */
    public Collection<Order> getPendingOrdersForItem(long itemId);

    /**
	 * request that an order be canceled.  Assume success if no exception is thrown
	 * 
	 * @param orderId
	 */
    public void requestCancelOrder(long orderId);

    /**
	 * @param orderId
	 * @param newQuantity
	 */
    public void alterOrderQuantity(long orderId, int newQuantity);

    /**
	 * Create a new order request for a particular item
	 * 
	 * @param order
	 */
    public void createNewOrder(Order order);

    /**
	 * finds a particular order by the specified order id
	 * 
	 * @param orderId
	 * @return
	 */
    public Order getOrderById(long orderId);
}
