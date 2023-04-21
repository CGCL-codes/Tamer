package com.anasoft.os.daofusion.sample.hellodao.server.db;

import java.util.Date;
import java.util.Random;
import com.anasoft.os.daofusion.sample.hellodao.server.dao.CountryDao;
import com.anasoft.os.daofusion.sample.hellodao.server.dao.CustomerDao;
import com.anasoft.os.daofusion.sample.hellodao.server.dao.entity.Address;
import com.anasoft.os.daofusion.sample.hellodao.server.dao.entity.ContactDetails;
import com.anasoft.os.daofusion.sample.hellodao.server.dao.entity.Country;
import com.anasoft.os.daofusion.sample.hellodao.server.dao.entity.Customer;
import com.anasoft.os.daofusion.sample.hellodao.server.dao.entity.Order;
import com.anasoft.os.daofusion.sample.hellodao.server.dao.impl.DaoManager;

/**
 * Generates some initial test data.
 */
public class DatabaseInitializer {

    private static final String[] FIRST_NAMES = { "William", "Jack", "Thomas", "Joshua", "James", "Peter", "Oliver", "David", "Max", "Sam", "John", "Anthony" };

    private static final String[] LAST_NAMES = { "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Wilson", "Taylor", "Anderson", "Robinson" };

    private static final String[] COUNTRIES = { "AF", "AX", "AL", "DZ", "AS", "AD", "AO", "AI", "AQ", "AG", "AR", "AM", "AW", "AC", "AU", "AT", "AZ", "BS", "BH", "BB", "BD", "BY", "BE", "BZ", "BJ", "BM", "BT", "BW", "BO", "BA", "BV", "BR", "BN", "BG", "BF" };

    private static final Random FN_RND = new Random();

    private static final Random LN_RND = new Random();

    private static final Random C_RND = new Random();

    private static final Random P_RND = new Random();

    private static final Random O_RND = new Random();

    private int numberOfCustomers;

    private CustomerDao customerDao = DaoManager.getCustomerDao();

    private CountryDao countryDao = DaoManager.getCountryDao();

    public DatabaseInitializer(int numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }

    public void clean() {
        customerDao.deleteAll();
    }

    public void init() {
        for (int i = 0; i < COUNTRIES.length; i++) {
            Country country = new Country();
            country.setName(COUNTRIES[i]);
            countryDao.saveOrUpdate(country);
        }
        for (int i = 0; i < numberOfCustomers; i++) {
            Customer customer = getCustomer(i);
            int numberOfOrders = O_RND.nextInt(10);
            for (int j = 0; j < numberOfOrders; j++) {
                Order order = getOrder();
                customer.addOrder(order);
            }
            customerDao.saveOrUpdate(customer);
        }
    }

    protected Customer getCustomer(int index) {
        String firstName = getFirstName();
        String lastName = getLastName(index);
        String email = getEmail(firstName, lastName, index);
        ContactDetails details = new ContactDetails();
        details.setEmail(email);
        details.setPhone(getPhone());
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setContactDetails(details);
        return customer;
    }

    protected String getPhone() {
        StringBuilder sb = new StringBuilder();
        sb.append("+4219");
        for (int i = 0; i < 8; i++) sb.append(String.valueOf(P_RND.nextInt(10)));
        return sb.toString();
    }

    protected Order getOrder() {
        Order order = new Order();
        order.setDescription("Some description");
        order.setBillingAddress(getAddress());
        order.setShippingAddress(getAddress());
        order.setCreationDate(new Date());
        order.setComplete(false);
        return order;
    }

    protected Address getAddress() {
        Address address = new Address();
        address.setCity("Metropolis");
        address.setCountry(getCountry());
        return address;
    }

    protected Country getCountry() {
        String name = COUNTRIES[C_RND.nextInt(COUNTRIES.length)];
        return countryDao.getByName(name);
    }

    protected String getFirstName() {
        return FIRST_NAMES[FN_RND.nextInt(FIRST_NAMES.length)];
    }

    protected String getLastName(int index) {
        return LAST_NAMES[LN_RND.nextInt(LAST_NAMES.length)] + index;
    }

    protected String getEmail(String firstName, String lastName, int index) {
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + index + "@" + "domain.com";
    }
}
