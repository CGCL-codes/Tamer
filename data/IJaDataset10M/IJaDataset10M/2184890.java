package org.doframework.sample.jdbc_app.factory.test;

import org.doframework.DOF;
import org.doframework.sample.jdbc_app.entity.Customer;
import org.doframework.sample.jdbc_app.factory.CustomerFactory;

/**
 * This TestCase contains sample tests that use the DOF framework.
 * 
 * @author Donald S. Bell
 *
 */
public class DOFSamplesJdbcCustomerFactoryImplTest extends JdbcFactoryImplTest {

    /**
     * This tests to make sure the factory can retreive an existing customer.
     * @Test
     */
    public void testDOFSampleGetCustomer25() {
        Customer dofCustomer = (Customer) DOF.require("customer.25.xml");
        CustomerFactory factory = getCustomerFactory();
        int customerId = dofCustomer.getId();
        Customer factoryCustomer = factory.getById(customerId);
        assertEquals(dofCustomer, factoryCustomer);
    }

    /**
     * This tests that when a customer's name is updated it is saved in the
     * database and is able to be retreived with the updated name.
     * @Test
     */
    public void testDOFSampleUpdateCustomer25() {
        Customer dofCustomer = (Customer) DOF.require("customer.25.xml");
        dofCustomer.setName("Jane Doe");
        CustomerFactory factory = getCustomerFactory();
        factory.update(dofCustomer);
        int customerId = dofCustomer.getId();
        Customer dbCustomer = factory.getById(customerId);
        assertTrue(dofCustomer.equals(dbCustomer));
        DOF.delete("customer.25.xml");
    }

    /**
     * This tests the deleting of a customer.
     * @Test
     */
    public void testDOFSampleDeleteCustomer05() {
        Customer dofCustomer = (Customer) DOF.createScratchObject("customer.05.xml");
        int customerId = dofCustomer.getId();
        CustomerFactory factory = getCustomerFactory();
        factory.delete(dofCustomer);
        Customer dbCustomer = factory.getById(customerId);
        assertNull(dbCustomer);
        DOF.clearFileCache();
    }
}
