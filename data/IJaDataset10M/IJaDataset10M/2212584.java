#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId}.interfaces;

import java.util.List;

import ${package}.common.wrappers.OrderWrapper;
import ${package}.${artifactId}.exception.BOException;

/**
 * Interface defining the order business ${artifactId} 
 * All objects returned by the defined methods return an client-serializable objects 
 * Interface has 2 implementations: OrderBOImpl, OrderBOTestImpl
 * @see OrderBOTestImpl  
 * @see OrderBOImpl
 * @author pstepaniak
 *
 */
public interface OrderBO {
	/**
	 * Returns all orders.
	 * @return
	 * @throws BOException
	 */
	List<OrderWrapper> getAllOrders() throws BOException;

	/**
	 * Returns an order with the given orderId.
	 * @param orderId
	 * @return
	 * @throws BOException
	 */
	OrderWrapper getOrder(Integer orderId) throws BOException;
	
	/**
	 * Creates a new order
	 * @param orderWrapper
	 * @return
	 * @throws BOException
	 */
	boolean createOrder(OrderWrapper orderWrapper) throws BOException;
	
	/**
	 * Updates an existing order
	 * @param orderWrapper
	 * @return
	 * @throws BOException
	 */
	boolean updateOrder(OrderWrapper orderWrapper) throws BOException;
	
	/**
	 * Deletes an order with the given orderId
	 * @param orderId
	 * @return
	 * @throws BOException
	 */
	boolean deleteOrder(Integer orderId) throws BOException;
}
