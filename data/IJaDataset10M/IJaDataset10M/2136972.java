package pl.net.bluesoft.archetype.gwt.logic.impl;

import java.util.List;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.net.bluesoft.archetype.gwt.common.utils.ValUtils;
import pl.net.bluesoft.archetype.gwt.common.wrappers.OrderWrapper;
import pl.net.bluesoft.archetype.gwt.dao.OrderDAO;
import pl.net.bluesoft.archetype.gwt.dao.pojo.Order;
import pl.net.bluesoft.archetype.gwt.logic.exception.BOException;
import pl.net.bluesoft.archetype.gwt.logic.interfaces.OrderBO;
import pl.net.bluesoft.archetype.gwt.logic.utils.CopyUtils;

/**
 * DAO based implementation of the order business logic
 * This class handles the DTO transformation, for generating serialzable classes
 * In this class all privilege check should be included.
 * @author pstepaniak
 *
 */
public class OrderBOImpl implements OrderBO {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBOImpl.class);

    @Autowired
    private transient OrderDAO orderDAO;

    public List<OrderWrapper> getAllOrders() throws BOException {
        LOGGER.info("getAllOrders ...");
        final Session session = orderDAO.openNewSession(FlushMode.AUTO);
        final List<Order> orders = orderDAO.selectAll(session);
        final List<OrderWrapper> orderWrappers = CopyUtils.getOrderWrappers(orders);
        orderDAO.closeSession(session);
        return orderWrappers;
    }

    public OrderWrapper getOrder(final Integer orderId) throws BOException {
        LOGGER.info("getOrder ... [" + orderId + "]");
        final Session session = orderDAO.openNewSession(FlushMode.AUTO);
        final Order order = orderDAO.selectById(orderId, session);
        final OrderWrapper orderWrapper = CopyUtils.getOrderWrapper(order);
        orderDAO.closeSession(session);
        return orderWrapper;
    }

    public boolean createOrder(final OrderWrapper orderWrapper) throws BOException {
        LOGGER.info("createOrder ... [ orderWrapper!=null: " + (orderWrapper != null) + "]");
        boolean result = false;
        try {
            final Order order = CopyUtils.getOrder(orderWrapper);
            result = orderDAO.insert(order);
        } catch (Exception ex) {
            LOGGER.error(ValUtils.toString(ex.getMessage()));
            if (ex.getCause() != null) {
                LOGGER.error(ValUtils.toString(ex.getCause().getMessage()));
            }
        }
        return result;
    }

    public boolean updateOrder(final OrderWrapper orderWrapper) throws BOException {
        LOGGER.info("updateOrder ... [ orderWrapper!=null: " + (orderWrapper != null) + "]");
        final Order order = CopyUtils.getOrder(orderWrapper);
        return orderDAO.merge(order);
    }

    public boolean deleteOrder(final Integer orderId) throws BOException {
        LOGGER.info("deleteOrder ... [" + orderId + "]");
        final Order order = orderDAO.selectById(orderId);
        return orderDAO.delete(order);
    }
}
