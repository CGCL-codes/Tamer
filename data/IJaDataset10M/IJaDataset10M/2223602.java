package org.shopformat.domain.customer;

import org.shopformat.domain.BaseGenericRepository;

public interface CustomerRepository extends BaseGenericRepository<Customer, Long> {

    Customer findByEmailAddress(String email);
}
