package org.xmdl.taslak.dao;

import java.util.*;
import org.springframework.dao.DataAccessException;
import org.xmdl.ida.lib.test.BaseDaoTestCase;
import org.xmdl.taslak.model.*;
import org.xmdl.taslak.model.search.*;

/**
 *
 * OrderElement DAO Test Case
 *  
 * $Id$
 *
 * @generated
 */
public class OrderElementDAOTest extends BaseDaoTestCase {

    /**
     * @generated
     */
    private OrderElementDAO orderElementDAO = null;

    /**
     * @generated
     */
    public void setOrderElementDAO(OrderElementDAO orderElementDAO) {
        this.orderElementDAO = orderElementDAO;
    }

    /**
     * @generated
     */
    public void testAddAndRemoveOrderElement() throws Exception {
        OrderElement orderElement = new OrderElement();
        orderElement.setQuantity(new Long(5178411341324860787L));
        Order order = new Order();
        order.setId(1L);
        orderElement.setOrder(order);
        orderElement = orderElementDAO.save(orderElement);
        flush();
        orderElement = orderElementDAO.get(orderElement.getId());
        assertNotNull(orderElement.getId());
        assertEquals(new Long(5178411341324860787L), orderElement.getQuantity());
        log.debug("removing orderElement...");
        orderElementDAO.remove(orderElement.getId());
        flush();
        try {
            orderElementDAO.get(orderElement.getId());
            fail("OrderElement found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }

    /**
     * @generated
     */
    public void testSearch() throws Exception {
        OrderElementSearch search = new OrderElementSearch();
        search.setQuantityMin(new Long(5178411341324860787L));
        search.setQuantityMax(new Long(5178411341324860787L));
        Collection<OrderElement> orderElements = orderElementDAO.search(search);
        assertTrue(orderElements.size() > 0);
    }

    /**
     * @generated
     */
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        OrderElement orderElement = new OrderElement();
        orderElement.setQuantity(new Long(5178411341324860787L));
        Order order = new Order();
        order.setId(1L);
        orderElement.setOrder(order);
        orderElementDAO.save(orderElement);
    }
}
