package net.sf.brightside.bonko.metamodel.beans;

import java.util.Date;
import java.util.LinkedList;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.easymock.EasyMock.createStrictMock;
import net.sf.brightside.bonko.metamodel.BuyProduct;
import net.sf.brightside.bonko.metamodel.Customer;
import net.sf.brightside.bonko.metamodel.Product;
import static org.testng.AssertJUnit.*;

public class BuyProductBeanTest {

    BuyProduct buyProductUnderTest;

    @BeforeMethod
    public void setUp() {
        buyProductUnderTest = new BuyProductBean();
        buyProductUnderTest.setProducts(new LinkedList<Product>());
    }

    @Test
    public void testDate() {
        Date date = new Date();
        assertNull(buyProductUnderTest.getDate());
        buyProductUnderTest.setDate(date);
        assertEquals(date, buyProductUnderTest.getDate());
    }

    @Test
    public void testCustomer() {
        Customer customer = createStrictMock(Customer.class);
        assertNull(buyProductUnderTest.getCustomer());
        buyProductUnderTest.setCustomer(customer);
        assertEquals(customer, buyProductUnderTest.getCustomer());
    }

    @Test
    public void testProductAssociation() {
        Product product = createStrictMock(Product.class);
        assertNotNull(buyProductUnderTest.getProducts());
        assertFalse(buyProductUnderTest.getProducts().contains(product));
        buyProductUnderTest.getProducts().add(product);
        assertTrue(buyProductUnderTest.getProducts().contains(product));
    }
}
