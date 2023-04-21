package net.sourceforge.tile3d.service;

import java.util.Collection;
import net.sourceforge.tile3d.model.Customer;

public interface ICustomerService {

    public Collection findAllCustomers();

    public Boolean create(Customer p_customer);

    public Boolean delete(Long p_customerId);

    public Boolean update(Customer p_customer);

    public Customer search(Long p_CustomerId);
}
