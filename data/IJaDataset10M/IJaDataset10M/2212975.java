package com.javaeye.order.service;

import java.util.List;
import com.javaeye.order.dto.Customer;
import com.javaeye.common.web.PageInfo;
import com.javaeye.common.web.ReportData;

public interface ICustomerService {

    public List<Customer> getCustomerList(Customer condition, PageInfo pageInfo);

    public Customer saveCustomer(Customer customer);

    public boolean removeCustomer(String id);

    public Customer getCustomer(String id);

    public List<Customer> getAutoCompleteCustomers(String name, int size);

    public List<ReportData> getNewCustomerAtMonth(int month);
}
