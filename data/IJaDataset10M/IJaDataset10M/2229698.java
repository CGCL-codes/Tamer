package com.daoImpl;

import com.dao.CustomerDao;
import com.dao.HibernateDaoSupport;
import com.domain.Customers;

public class CustomerDaoImpl extends HibernateDaoSupport<Customers, Long> implements CustomerDao {

    public CustomerDaoImpl() {
        this.entityClass = Customers.class;
    }
}
