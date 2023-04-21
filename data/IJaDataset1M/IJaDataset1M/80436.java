package net.sf.brightside.bonko.metamodel.spring;

import net.sf.brightside.bonko.core.spring.AbstractSpringTest;
import net.sf.brightside.bonko.metamodel.BuyProduct;
import net.sf.brightside.bonko.metamodel.Product;
import org.testng.annotations.Test;

public class ProductTest extends AbstractSpringTest {

    private Product productUnderTest;

    protected Product createUnderTest() {
        return (Product) getApplicationContext().getBean(Product.class.getName());
    }

    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        productUnderTest = createUnderTest();
    }

    @Test
    public void testCustomers() {
        BuyProduct buyProduct = (BuyProduct) applicationContext.getBean(BuyProduct.class.getName());
        getPersistenceManager().save(productUnderTest);
        productUnderTest.getCustomers().add(buyProduct);
        assertTrue(productUnderTest.getCustomers().contains(buyProduct));
        productUnderTest.getCustomers().remove(buyProduct);
        assertFalse(productUnderTest.getCustomers().contains(buyProduct));
    }

    @Test
    public void testCustomersPersistence() {
        BuyProduct buyProduct = (BuyProduct) applicationContext.getBean(BuyProduct.class.getName());
        getPersistenceManager().save(productUnderTest);
        int populationBeforeSave = getPersistenceManager().get(buyProduct).size();
        productUnderTest.getCustomers().add(buyProduct);
        assertEquals(populationBeforeSave + 1, getPersistenceManager().get(buyProduct).size());
        productUnderTest.getCustomers().remove(buyProduct);
        assertEquals(populationBeforeSave + 1, getPersistenceManager().get(buyProduct).size());
    }
}
