package org.xmdl.taslak.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jmock.Mock;
import org.xmdl.ida.lib.test.BaseManagerMockTestCase;
import org.xmdl.taslak.dao.*;
import org.xmdl.taslak.model.*;
import org.xmdl.taslak.model.search.*;

/**
 *
 * OrderElement Service Implementation Test
 *  
 * $Id$
 *
 * @generated
 */
public class OrderElementManagerImplTest extends BaseManagerMockTestCase {

    /**
     * @generated
     */
    private OrderElementManagerImpl manager = null;

    /**
     * @generated
     */
    private Mock dao = null;

    /**
     * @generated
     */
    private OrderElement orderElement = null;

    /**
     * @generated
     */
    protected void setUp() throws Exception {
        dao = new Mock(OrderElementDAO.class);
        manager = new OrderElementManagerImpl((OrderElementDAO) dao.proxy());
    }

    /**
     * @generated
     */
    protected void tearDown() throws Exception {
        manager = null;
    }

    /**
     * @generated
     */
    public void testGet() {
        log.debug("testing get");
        Long id = 7L;
        orderElement = new OrderElement();
        dao.expects(once()).method("get").with(eq(id)).will(returnValue(orderElement));
        OrderElement result = manager.get(id);
        assertSame(orderElement, result);
    }

    /**
     * @generated
     */
    public void testGetAll() {
        log.debug("testing getAll");
        List<OrderElement> list = new ArrayList<OrderElement>();
        dao.expects(once()).method("getAll").will(returnValue(list));
        List<OrderElement> result = manager.getAll();
        assertSame(list, result);
    }

    /**
     * @generated
     */
    public void testSearch() {
        log.debug("testing search");
        List<OrderElement> list = new ArrayList<OrderElement>();
        OrderElementSearch searchBean = new OrderElementSearch();
        dao.expects(once()).method("search").with(eq(searchBean)).will(returnValue(list));
        Collection<OrderElement> result = manager.search(searchBean);
        assertSame(list, result);
    }

    /**
     * @generated
     */
    public void testSave() {
        log.debug("testing save");
        orderElement = new OrderElement();
        dao.expects(once()).method("save").with(same(orderElement)).will(returnValue(orderElement));
        manager.save(orderElement);
    }

    /**
     * @generated
     */
    public void testRemove() {
        log.debug("testing remove");
        Long id = 11L;
        orderElement = new OrderElement();
        dao.expects(once()).method("remove").with(eq(id)).isVoid();
        manager.remove(id);
    }
}
