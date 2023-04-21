package com.docum.service;

import com.docum.domain.po.common.Customer;

public interface CustomerService extends BaseService {

    public static final String SERVICE_NAME = "customerService";

    public Customer getDefaultCustomer();
}
