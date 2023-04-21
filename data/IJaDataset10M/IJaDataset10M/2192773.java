package it.hotel.model.customer.manager;

import it.hotel.model.abstrakt.manager.IhDAO;
import it.hotel.model.customer.Customer;
import java.util.Collection;

public interface ICustomerDAO extends IhDAO {

    public Collection searchCustomer(Customer exampleEntity);
}
