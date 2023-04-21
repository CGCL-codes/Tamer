package com.kongur.network.erp.warehouse;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.kongur.network.erp.dao.warehouse.PickOrderDAO;
import com.kongur.network.erp.query.warehouse.PickOrderQuery;
import com.kongur.network.metal.demo.test.TestBase;

/***
 *
 */
public class PickOrderDAOTestImpl extends TestBase {

    @Autowired
    private PickOrderDAO pickOrderDAO;

    public void setPickOrderDAO(PickOrderDAO pickOrderDAO) {
        this.pickOrderDAO = pickOrderDAO;
    }

    @Test
    public void testQuery() {
        PickOrderQuery query = new PickOrderQuery();
        query.setSellerNick("zhangsan");
        pickOrderDAO.queryPickOrders(query);
    }

    public void testGetById() {
        pickOrderDAO.selectPickOrderById(1L);
    }
}
