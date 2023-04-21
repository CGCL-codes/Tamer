package com.mycompany.custmgmt.services;

import java.util.List;
import org.apache.log4j.Logger;
import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.Filter;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.annotation.NotPersisted;
import com.mycompany.custmgmt.domain.Country;
import com.mycompany.custmgmt.domain.Customer;
import com.mycompany.custmgmt.domain.Product;

@Named("Customers")
public class CustomerRepository extends AbstractFactoryAndRepository {

    private static final Logger LOGGER = Logger.getLogger(CustomerRepository.class);

    @Hidden
    @NotPersisted
    public Logger getLOGGER() {
        return LOGGER;
    }

    /**
     * Lists all customers in the repository.
     */
    public List<Customer> showAll() {
        return allInstances(Customer.class, false);
    }

    /**
     * Returns a list of Customers with given last name.
     */
    public List<Customer> findAllByName(@Named("Last name") final String lastName) {
        return allMatches(Customer.class, new FilterLastName(lastName), false);
    }

    /**
     * Returns the first Customer with given last name.
     */
    public Customer findByName(@Named("Last name") final String lastName) {
        return firstMatch(Customer.class, new FilterLastName(lastName), false);
    }

    private final class FilterLastName implements Filter {

        private final String name;

        private FilterLastName(String name) {
            this.name = name;
        }

        public boolean accept(Object obj) {
            Customer pojo = (Customer) obj;
            return pojo.getLastName().toLowerCase().contains(name.toLowerCase());
        }
    }

    /**
     * Creates a new (still-transient) customer.
     * 
     * @return
     */
    public Customer newCustomer() {
        Customer customer = (Customer) newTransientInstance(Customer.class);
        return customer;
    }

    /**
     * Creates a new (already persisted) customer.
     * 
     * <p>
     * For use by fixtures only.
     * 
     * @return
     */
    @Hidden
    public Customer newCustomer(String firstName, String lastName, int customerNumber, Country countryOfBirth) {
        Customer customer = newCustomer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCustomerNumber(customerNumber);
        customer.modifyCountryOfBirth(countryOfBirth);
        customer.makePersistent();
        return customer;
    }
}
