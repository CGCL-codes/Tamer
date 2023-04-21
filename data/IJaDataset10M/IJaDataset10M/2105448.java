package com.tll.dao;

import com.tll.model.Customer;

/**
 * CustomerDaoTestHandler
 * @author jpk
 */
public class CustomerDaoTestHandler extends AbstractAccountDaoTestHandler<Customer> {

    @Override
    public Class<Customer> entityClass() {
        return Customer.class;
    }
}
