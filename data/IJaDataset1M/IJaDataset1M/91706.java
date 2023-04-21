package net.sf.simplecq.loadgen;

import java.util.Date;
import org.apache.commons.lang.RandomStringUtils;

/**
 * Load generator for the "customer" and "address" table of Sakila.
 * 
 * @author Sherif Behna
 */
public class CustomerAddressLoadGen extends AbstractLoadGenerator {

    public CustomerAddressLoadGen() {
        super();
    }

    @Override
    protected void prepareDistribution(LoadGeneratorDistribution distribution) {
        distribution.add(20, new CreateCustomerOperation());
        distribution.add(48, new UpdateAddressOperation());
        distribution.add(20, new ChangeStoreOperation());
        distribution.add(7, new InactivateCustomerOperation());
        distribution.add(5, new ReactivateCustomerOperation());
    }

    private class CreateCustomerOperation implements LoadGenerator {

        public void run() {
            getSimpleJdbcTemplate().update("insert into address(address, district, " + "city_id, postal_code, phone, " + "last_update) values (?, ?, ?, ?, ?, ?)", RandomStringUtils.randomAlphanumeric(50), RandomStringUtils.randomAlphabetic(10), selectRandomId("city"), RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomNumeric(10), new Date());
            Long addressId = getSimpleJdbcTemplate().queryForLong("SELECT @@IDENTITY");
            getSimpleJdbcTemplate().update("insert into customer(store_id, first_name, " + "last_name, email, address_id, active, " + "create_date, last_update) " + "values (?, ?, ?, ?, ?, ?, ?, ?)", selectRandomId("store"), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphanumeric(16), addressId, true, new Date(), new Date());
        }
    }

    private class UpdateAddressOperation implements LoadGenerator {

        public void run() {
            getSimpleJdbcTemplate().update("update address " + "set address = ?, district = ?, city_id = ?, " + "postal_code = ?, phone = ?, last_update = ? " + "where address_id = ?", RandomStringUtils.randomAlphanumeric(50), RandomStringUtils.randomAlphabetic(10), selectRandomId("city"), RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomNumeric(10), new Date(), selectRandomId("address"));
        }
    }

    private class ChangeStoreOperation implements LoadGenerator {

        public void run() {
            getSimpleJdbcTemplate().update("update customer " + "set store_id = ?, last_update = ? " + "where customer_id = ?", selectRandomId("store"), new Date(), selectRandomId("customer"));
        }
    }

    private class InactivateCustomerOperation implements LoadGenerator {

        public void run() {
            getSimpleJdbcTemplate().update("update customer " + "set active = ?, last_update = ? " + "where customer_id = ?", false, new Date(), selectRandomId("customer", "active = true"));
        }
    }

    private class ReactivateCustomerOperation implements LoadGenerator {

        public void run() {
            getSimpleJdbcTemplate().update("update customer " + "set active = ?, last_update = ? " + "where customer_id = ?", true, new Date(), selectRandomId("customer", "active = false"));
        }
    }
}
