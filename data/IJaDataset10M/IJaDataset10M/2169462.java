package ch.exm.storm.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import ch.exm.storm.store.Cache;
import ch.exm.storm.test.domain.Customer;
import ch.exm.storm.test.domain.Item;

public class LoadTest {

    private static Cache cache;

    @BeforeClass
    public static void setUp() {
        cache = new Cache();
    }

    @Test
    public void testLoad() {
        Iterable<Customer> customers = cache.getAll(Customer.class);
        int customerNr = 0;
        for (Customer customer : customers) customerNr++;
        assertEquals(4, customerNr);
    }

    @Test
    public void testSameObject() {
        Customer customer = cache.getById(Customer.class, 1L);
        assertNotNull(customer);
        Item item = customer.getOrders().iterator().next().getOrderedItems().iterator().next().getItem();
        assertNotNull(item);
        Item cachedItem = cache.getById(Item.class, item.getId());
        assertNotNull(cachedItem);
        assertEquals(item, cachedItem);
        assertTrue(item == cachedItem);
    }
}
