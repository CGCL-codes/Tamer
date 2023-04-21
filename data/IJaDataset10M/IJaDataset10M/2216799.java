package hu.de.rft.bookstore.model.dao;

import hu.de.rft.bookstore.model.businessobject.Customer;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author X3cut0r
 */
public interface CustomerDAO {

    public Customer getCustomer(String username) throws DataAccessException;

    public void removeCustomer(String username) throws DataAccessException;

    public void removeCustomer(Customer customer) throws DataAccessException;

    public void addCustomer(Customer customer) throws DataAccessException;

    public void updateCustomer(Customer customer) throws DataAccessException;
}
