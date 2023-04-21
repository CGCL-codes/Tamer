package net.sf.brightside.moljac.service;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import net.sf.brightside.moljac.core.spring.AbstractSpringTest;
import net.sf.brightside.moljac.metamodel.Bookstore;
import net.sf.brightside.moljac.metamodel.Customer;

public class RegisterYourBookstoreTest extends AbstractSpringTest {

    private RegisterYourBookstore objectUnderTest;

    private Bookstore bookstore;

    private Customer customer;

    protected RegisterYourBookstore createRegisterYourBookstoreTest() {
        return (RegisterYourBookstore) applicationContext.getBean("RegisterYourBookstore");
    }

    protected Bookstore createBookstore() {
        return (Bookstore) applicationContext.getBean(Bookstore.class.getName());
    }

    protected Customer createCustomer() {
        return (Customer) applicationContext.getBean(Customer.class.getName());
    }

    @Override
    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        objectUnderTest = createRegisterYourBookstoreTest();
        bookstore = createBookstore();
        customer = createCustomer();
    }

    @BeforeMethod
    public void saveBookstoreFounder() {
        CustomerRegistration registerCustomer = (CustomerRegistration) applicationContext.getBean("CustomerRegistration");
        registerCustomer.setCustomer(customer);
        customer.setBookstore(null);
        registerCustomer.execute();
    }

    @Test
    public void testExist() {
        assertNotNull(objectUnderTest);
    }

    @Test
    public void executeTest() {
        bookstore.setName("aaaaaa");
        objectUnderTest.setBookstore(bookstore);
        objectUnderTest.setCustomerId((Long) customer.get_id());
        assertTrue(objectUnderTest.execute());
    }
}
