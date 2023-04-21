package exchange.orderbook.register;

import java.util.SortedSet;
import joxtr.common.domain.TransactionType;
import exchange.domain.orderbook.BookMatchingSessionResult;
import exchange.domain.orderbook.OrderBookEntry;
import exchange.event.IllegalFunctionalStateEvent;
import exchange.orderbook.OrderBook;

/**
 * Used as a helper component to the {@link OrderBook}. Encapsulates the actual
 * storage and sorting and manipulation of the orders with respect to one
 * another. The interface deals with {@link OrderHolder} so that implementations
 * of this interface can remain ignorant about the structure of an order except
 * for certain aspects that are relevant to its operation.
 * 
 * @author Ray Pendergraph
 * 
 */
public interface OrderRegister {

    public void insertOrder(OrderBookEntry entry);

    /**
     * Returns the market orders for the side of the register requested.
     * 
     * @param side
     *                Which side of the order book.
     * 
     * @return The orders sorted according to the rules of the holder.
     */
    public SortedSet<OrderHolder> getMarketOrders(TransactionType side);

    /**
     * Returns the limit orders for the side of the register requested.
     * 
     * @param side
     *                Which side of the order book.
     * 
     * @return The orders sorted according to the rules of the holder.
     */
    public SortedSet<OrderHolder> getLimitOrders(TransactionType side);

    /**
     * Runs a matching session against a new entry to determine what orders
     * already in the register could be eligible for mutual execution.
     * 
     * Preconditions: No contents of the register are changed at this time
     * 
     * @param incomingOrder
     *                The new order.
     * 
     * @return A result from what the match register found.
     */
    public BookMatchingSessionResult runMatchingSession(OrderBookEntry incomingOrder);

    /**
     * Removes an order from the register.<br>
     * 
     * <b>Post Conditions</b><br>
     * <li>If the order does not exist or the type of order is null the an
     * {@link IllegalFunctionalStateEvent} will be fired.</li>
     * <li></li>
     * 
     * @param order
     *                The order to be removed.
     */
    public void removeOrder(OrderHolder order);
}
