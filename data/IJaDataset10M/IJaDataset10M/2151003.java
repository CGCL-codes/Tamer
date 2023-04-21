package org.torweg.pulse.component.shop.checkout;

import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.torweg.pulse.accesscontrol.User.Everybody;
import org.torweg.pulse.annotations.Action;
import org.torweg.pulse.annotations.Permission;
import org.torweg.pulse.annotations.RequestBean;
import org.torweg.pulse.bundle.Controller;
import org.torweg.pulse.component.shop.ShoppingCartController;
import org.torweg.pulse.component.shop.checkout.model.Order;
import org.torweg.pulse.component.shop.checkout.model.OrderPosition;
import org.torweg.pulse.component.shop.model.ShopItem;
import org.torweg.pulse.component.shop.model.ShoppingCart;
import org.torweg.pulse.component.shop.model.ShoppingCartPosition;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.service.event.ForbiddenEvent;
import org.torweg.pulse.service.event.RedirectEvent;
import org.torweg.pulse.service.request.ServiceRequest;
import org.torweg.pulse.util.entity.OrderBy;
import org.torweg.pulse.util.entity.SortDirection;
import org.torweg.pulse.util.requestbean.RequestAwareRequestBean;
import org.torweg.pulse.util.xml.XMLConverter;

/**
 * For displaying {@code Order}s via the {@code OrderHistory}.
 * 
 * @author Daniel Dietz
 * @version $Revision$
 */
public class OrderHistoryController extends Controller {

    /**
	 * The logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderHistoryController.class);

    /**
	 * Creates and loads the {@code OrderHistory}.
	 * 
	 * @param requestBean
	 *            the current {@code OrderHistoryRequestBean}
	 * 
	 * @return JAXB-annotated {@code OrderHistory}
	 */
    @Action(value = "getOrderHistory", generate = true)
    @Permission("getOrderHistory")
    public final OrderHistory getOrderHistory(@RequestBean final OrderHistoryRequestBean requestBean) {
        if (requestBean.getServiceRequest().getUser() instanceof Everybody) {
            requestBean.addEvent(new ForbiddenEvent());
            return null;
        }
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setUser(requestBean.getServiceRequest().getUser());
        orderHistory.setOffset(requestBean.getOffset());
        orderHistory.setPageSize(requestBean.getPageSize());
        orderHistory.setOrderBy(requestBean.getOrderBy());
        orderHistory.loadOrders();
        debugResult(orderHistory);
        return orderHistory;
    }

    /**
	 * Loads an {@code code} as specified by the given
	 * {@code AddOrderPositionsToCartRequestBean} and if and only if the order
	 * belongs to the current {@code User} tries to add the
	 * {@code OrderPosition}s to the current {@code ShoppingCart}.
	 * 
	 * @param requestBean
	 *            the current {@code AddOrderPositionsToCartRequestBean}
	 */
    @Action(value = "addOrderToCart", generate = true)
    @Permission("addOrderToCart")
    public final void addOrderToCart(@RequestBean final AddOrderPositionsToCartRequestBean requestBean) {
        LOGGER.error("orderId: {}", requestBean.getOrderId());
        if (requestBean.getOrderId() == null) {
            redirectToCart(requestBean);
            return;
        }
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        try {
            Order order = (Order) s.get(Order.class, requestBean.getOrderId());
            if (order != null && order.getCustomer().getUserId().equals(requestBean.getServiceRequest().getUser().getId())) {
                addToCart(order, requestBean, s);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException("Error: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
        }
        redirectToCart(requestBean);
    }

    /**
	 * Adds the {@code OrderPosition}s of the given {@code Order} to the current
	 * {@code ShoppingCart}.
	 * 
	 * @param order
	 *            the {@code Order}
	 * @param requestBean
	 *            the current {@code AddOrderPositionsToCartRequestBean}
	 * @param s
	 *            the current Hibernate<sup>TM</sup>-{@code Session}
	 */
    private void addToCart(final Order order, final AddOrderPositionsToCartRequestBean requestBean, final Session s) {
        Set<ShoppingCartPosition> positions = buildShoppingCartPositions(order, s);
        if (positions.isEmpty()) {
            return;
        }
        ShoppingCart cart = ShoppingCart.getCart(order.getOrderMetaData().getBundle(), requestBean.getServiceRequest(), null);
        for (ShoppingCartPosition position : positions) {
            cart.addShoppingCartPosition(position, requestBean.getServiceRequest());
        }
    }

    /**
	 * Builds {@code ShoppingCartPosition}s from the given {@code Order}.
	 * 
	 * @param order
	 *            the {@code Order}
	 * @param s
	 *            the current Hibernate<sup>TM</sup>-{@code Session}
	 * @return {@code ShoppingCartPosition}s
	 */
    private Set<ShoppingCartPosition> buildShoppingCartPositions(final Order order, final Session s) {
        Set<ShoppingCartPosition> positions = new HashSet<ShoppingCartPosition>();
        for (OrderPosition orderPosition : order.getOrderContents().getOrderPositions()) {
            ShopItem item = (ShopItem) s.get(orderPosition.getItemType(), orderPosition.getItemId());
            if (item != null) {
                positions.add(new ShoppingCartPosition(item, orderPosition.getQuantity()));
            }
        }
        return positions;
    }

    /**
	 * Redirects to display {@code ShoppingCart}.
	 * 
	 * @param requestBean
	 *            the current {@code AddOrderPositionsToCartRequestBean}
	 */
    private void redirectToCart(final AddOrderPositionsToCartRequestBean requestBean) {
        ServiceRequest request = requestBean.getServiceRequest();
        request.getEventManager().addEvent(new RedirectEvent(request.getCommand().createCopy(false).setAction(ShoppingCartController.DISPLAY_CART_ACTION).toCommandURL(request)));
    }

    /**
	 * Tries to marshal the given {@code Object} using the {@code XMLConverter}
	 * to the <tt>LOGGER></tt>.
	 * 
	 * @param o
	 *            the {@code Object}
	 */
    private void debugResult(final Object o) {
        try {
            LOGGER.trace(XMLConverter.marshal(o));
        } catch (JAXBException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
    }

    /**
	 * The request bean for the {@code OrderHistoryController} for displaying
	 * the {@code OrderHistory}.
	 */
    public static class OrderHistoryRequestBean extends RequestAwareRequestBean {

        /**
		 * The <tt>offset</tt>.
		 */
        private Integer offset;

        /**
		 * The default <tt>offset</tt>.
		 */
        public static final int DEFAULT_OFFSET = 0;

        /**
		 * The <tt>pageSize</tt>.
		 */
        private Integer pageSize;

        /**
		 * The default <tt>pageSize</tt>.
		 */
        public static final int DEFAULT_PAGE_SIZE = 10;

        /**
		 * The name of the property to order by.
		 */
        private String orderBy;

        /**
		 * The default <tt>orderBy</tt> on property <tt>metaData.orderDate</tt>
		 * and with {@code SortDirection.DESCENDING}.
		 */
        public static final OrderBy DEFAULT_ORDER_BY = new OrderBy("metaData.orderDate", SortDirection.DESCENDING);

        /**
		 * The direction of the order.
		 */
        private String orderDirection;

        /**
		 * Returns the <tt>offset</tt>.
		 * <p>
		 * Default: <tt>0</tt>.
		 * </p>
		 * 
		 * @return the <tt>offset</tt>, or
		 *         {@code OrderHistoryRequestBean.DEFAULT_OFFSET} if the
		 *         <tt>offset</tt> is either {@code null} or <tt>&lt; 0</tt>
		 *         (less than zero)
		 */
        public final int getOffset() {
            if (this.offset == null || this.offset < 0) {
                return OrderHistoryRequestBean.DEFAULT_OFFSET;
            }
            return this.offset;
        }

        /**
		 * Sets the <tt>offset</tt>.
		 * 
		 * @param value
		 *            the value to set the <tt>offset</tt> from
		 */
        @RequestBean.Parameter("offset")
        public final void setOffset(final String value) {
            this.offset = integerFromString(value);
        }

        /**
		 * Returns the <tt>pageSize</tt>.
		 * <p>
		 * Default: <tt>20</tt>.
		 * </p>
		 * 
		 * @return the <tt>pageSize</tt>, or
		 *         {@code OrderHistoryRequestBean.DEFAULT_PAGE_SIZE} if the
		 *         <tt>pagesSize</tt> is either {@code null} or <tt>&lt; 1</tt>
		 *         (less than one)
		 */
        public final int getPageSize() {
            if (this.pageSize == null || this.pageSize < 1) {
                return OrderHistoryRequestBean.DEFAULT_PAGE_SIZE;
            }
            return this.pageSize;
        }

        /**
		 * Sets the <tt>pageSize</tt>.
		 * 
		 * @param value
		 *            the value to set the <tt>pageSize</tt> from
		 */
        @RequestBean.Parameter("pageSize")
        public final void setPageSize(final String value) {
            this.pageSize = integerFromString(value);
        }

        /**
		 * Returns the {@code OrderBy} as build from <tt>orderBy</tt> and
		 * <tt>orderDirection</tt>.
		 * <p>
		 * Default: An {@code OrderBy} with the property to sort by being
		 * <tt>metaData.orderDate</tt> and the {@code SortDirection.DESCENDING}.
		 * </p>
		 * 
		 * @return the {@code OrderBy} or the default
		 *         {@code OrderHistoryRequestBean.DEFAULT_ORDER_BY} if
		 *         <tt>orderBy</tt> is {@code null}
		 */
        public final OrderBy getOrderBy() {
            if (this.orderBy == null || this.orderBy.equals("")) {
                return OrderHistoryRequestBean.DEFAULT_ORDER_BY;
            }
            if (this.orderDirection == null || this.orderDirection.equals("")) {
                return new OrderBy(this.orderBy);
            }
            return new OrderBy(this.orderBy, SortDirection.valueof(this.orderDirection));
        }

        /**
		 * Sets the <tt>orderBy</tt>.
		 * 
		 * @param value
		 *            the value to set the <tt>orderBy</tt> from
		 */
        @RequestBean.Parameter("orderBy")
        public final void setOrderBy(final String value) {
            this.orderBy = value;
        }

        /**
		 * Sets the <tt>orderDirection</tt>.
		 * 
		 * @param value
		 *            the value to set the <tt>orderDirection</tt> from
		 */
        @RequestBean.Parameter("orderDirection")
        public final void setOrderDirection(final String value) {
            this.orderDirection = value;
        }

        /**
		 * Tries to convert the given <tt>value</tt> to an {@code Integer}.
		 * 
		 * @param value
		 *            the value to convert
		 * 
		 * @return the converted <tt>value</tt> as {@code Integer}, or
		 *         {@code null} if the given <tt>value</tt> cannot be converted
		 */
        private Integer integerFromString(final String value) {
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    /**
	 * The request bean for the {@code OrderHistoryController} for add the
	 * positions of an order to the current {@code ShoppingCart}.
	 */
    public static class AddOrderPositionsToCartRequestBean extends RequestAwareRequestBean {

        /**
		 * The <tt>ID</tt> of the {@code Order}.
		 */
        private Long orderId;

        /**
		 * Returns <tt>ID</tt> of the {@code Order}.
		 * 
		 * @return the <tt>orderId</tt>
		 */
        public final Long getOrderId() {
            return this.orderId;
        }

        /**
		 * Sets <tt>ID</tt> of the {@code Order}.
		 * 
		 * @param value
		 *            the value to set <tt>orderId</tt> from
		 */
        @RequestBean.Parameter("orderId")
        public final void setOrderId(final String value) {
            this.orderId = longFromString(value);
        }

        /**
		 * Tries to convert the given <tt>value</tt> to an {@code Integer}.
		 * 
		 * @param value
		 *            the value to convert
		 * 
		 * @return the converted <tt>value</tt> as {@code Integer}, or
		 *         {@code null} if the given <tt>value</tt> cannot be converted
		 */
        private Long longFromString(final String value) {
            try {
                return Long.valueOf(value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}
