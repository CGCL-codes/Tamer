package com.busdepot.webapp.domain;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Autowired()
    CustomerRepository customerRepository;

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    public List<Customer> getCustomerByUsername(String username) {
        return customerRepository.getCustomerByUsername(username);
    }
}
