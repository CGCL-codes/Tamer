package com.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.dao.CustomerDao;
import com.dao.HibernateDaoSupport;
import com.dao.Pagination;
import com.daoImpl.CustomerDaoImpl;
import com.domain.Customers;

public class CustomersDaotest {

    private CustomerDao customerDao;

    @Before
    public void init() {
        this.customerDao = new CustomerDaoImpl();
        HibernateDaoSupport.initSession();
    }

    @After
    public void closeTest() {
        HibernateDaoSupport.close();
        HibernateDaoSupport.closeSessionFactory();
    }

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            Customers customers = new Customers();
            customers.setFirst_name("n" + 1);
            this.customerDao.saveOrUpdate(customers);
        }
        HibernateDaoSupport.commit();
    }
}
