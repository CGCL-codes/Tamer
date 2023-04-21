package org.evertree.breakfast.test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import org.evertree.breakfast.Breakfast;
import org.evertree.breakfast.component.InsertOrUpdateAll;
import org.hibernate.NonUniqueObjectException;
import org.junit.Test;

public class InsertOrUpdateAllTest extends DatabaseTestAbstract {

    public InsertOrUpdateAllTest() throws Exception {
        super();
    }

    @Test
    public void testSaveCustomers() throws Exception {
        db.reset();
        assertEquals(0, db.findCustomers().size());
        Collection<Customer> customers = new ArrayList<Customer>();
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Arthur");
        customers.add(customer1);
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Eliah");
        customers.add(customer2);
        Customer customer3 = new Customer();
        customer3.setId(3L);
        customer3.setName("Malcolm");
        customers.add(customer3);
        InsertOrUpdateAll insertOrUpdateAll = new InsertOrUpdateAll();
        insertOrUpdateAll.setProperties(props);
        insertOrUpdateAll.receive(customers);
        insertOrUpdateAll.execute();
        assertEquals(3, db.findCustomers().size());
        assertEquals(customer1, db.findCustomer(1L));
        assertEquals(customer2, db.findCustomer(2L));
        assertEquals(customer3, db.findCustomer(3L));
    }

    @Test
    public void testSaveInvoices() throws Exception {
        db.reset();
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Arthur");
        db.createCustomer(customer1);
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Eliah");
        assertEquals(0, db.findInvoices().size());
        Collection<Invoice> invoices = new ArrayList<Invoice>();
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        invoice1.setDate(new Date());
        invoice1.setCustomer(customer1);
        invoices.add(invoice1);
        Invoice invoice2 = new Invoice();
        invoice2.setId(2L);
        invoice2.setDate(new Date(new Date().getTime() + DAY_IN_MILISECS));
        invoice2.setCustomer(customer2);
        invoices.add(invoice2);
        Invoice invoice3 = new Invoice();
        invoice3.setId(3L);
        invoice3.setDate(new Date(new Date().getTime() + 2 * DAY_IN_MILISECS));
        invoice3.setCustomer(customer2);
        invoices.add(invoice3);
        InsertOrUpdateAll insertOrUpdateAll = new InsertOrUpdateAll();
        insertOrUpdateAll.setProperties(props);
        insertOrUpdateAll.receive(invoices);
        insertOrUpdateAll.execute();
        assertEquals(3, db.findInvoices().size());
        assertEquals(invoice1, db.findInvoice(1L));
        assertEquals(customer1, db.findInvoice(1L).getCustomer());
        assertEquals(invoice2, db.findInvoice(2L));
        assertEquals(customer2, db.findInvoice(2L).getCustomer());
        assertEquals(invoice3, db.findInvoice(3L));
        assertEquals(customer2, db.findInvoice(3L).getCustomer());
    }

    @Test
    public void testSaveClientInvoiceItems() throws Exception {
        db.reset();
        Collection<Object> entities = new ArrayList<Object>();
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Arthur");
        entities.add(customer1);
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        invoice1.setDate(new Date());
        invoice1.setCustomer(customer1);
        entities.add(invoice1);
        Item item1 = new Item();
        item1.setId(1L);
        item1.setDescription("MacBook");
        item1.setValue(2000.50);
        item1.setInvoice(invoice1);
        entities.add(item1);
        assertEquals(0, db.findCustomers().size());
        assertEquals(0, db.findInvoices().size());
        assertEquals(0, db.findItems().size());
        InsertOrUpdateAll insertOrUpdateAll = new InsertOrUpdateAll();
        insertOrUpdateAll.setProperties(props);
        insertOrUpdateAll.receive(entities);
        insertOrUpdateAll.execute();
        assertEquals(1, db.findCustomers().size());
        assertEquals(1, db.findInvoices().size());
        assertEquals(1, db.findItems().size());
        Item item = db.findItem(1L);
        assertEquals(item1, item);
        assertEquals(invoice1, item.getInvoice());
        assertEquals(customer1, item.getInvoice().getCustomer());
    }

    @Test
    public void testSaveSameObject() throws Exception {
        db.reset();
        Collection<Object> entities = new ArrayList<Object>();
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Arthur");
        entities.add(customer1);
        entities.add(customer1);
        assertEquals(0, db.findCustomers().size());
        InsertOrUpdateAll insertOrUpdateAll = new InsertOrUpdateAll();
        insertOrUpdateAll.setProperties(props);
        insertOrUpdateAll.receive(entities);
        insertOrUpdateAll.execute();
        assertEquals(1, db.findCustomers().size());
        assertEquals(customer1, db.findCustomer(1L));
    }

    @Test
    public void testSameCustomer() throws Exception {
        db.reset();
        Collection<Object> entities = new ArrayList<Object>();
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Arthur");
        Customer customer2 = new Customer();
        customer2.setId(1L);
        customer2.setName("Artur");
        entities.add(customer1);
        entities.add(customer2);
        assertEquals(0, db.findCustomers().size());
        try {
            InsertOrUpdateAll insertOrUpdateAll = new InsertOrUpdateAll();
            insertOrUpdateAll.setProperties(props);
            insertOrUpdateAll.receive(entities);
            insertOrUpdateAll.execute();
            fail("An exception should be thrown!");
        } catch (NonUniqueObjectException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testExistentCustomer() throws Exception {
        db.reset();
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Arthur");
        db.createCustomer(customer1);
        assertEquals(1, db.findCustomers().size());
        Collection<Object> entities = new ArrayList<Object>();
        Customer customer2 = new Customer();
        customer2.setId(1L);
        customer2.setName("Artur");
        entities.add(customer2);
        InsertOrUpdateAll insertOrUpdateAll = new InsertOrUpdateAll();
        insertOrUpdateAll.setProperties(props);
        insertOrUpdateAll.receive(entities);
        insertOrUpdateAll.execute();
        assertEquals(1, db.findCustomers().size());
        assertEquals(customer2, db.findCustomer(1L));
    }

    @Test
    public void createOrphanEntity() throws Exception {
        db.reset();
        assertEquals(0, db.findItems().size());
        Item item1 = new Item();
        item1.setId(1L);
        item1.setDescription("Orphan");
        item1.setValue(2000.50);
        item1.setInvoice(null);
        InsertOrUpdateAll insertOrUpdateAll = new InsertOrUpdateAll();
        insertOrUpdateAll.setProperties(props);
        insertOrUpdateAll.receive(Collections.singleton(item1));
        insertOrUpdateAll.execute();
        assertEquals(1, db.findItems().size());
        assertEquals(item1, db.findItem(1L));
        assertNull(db.findItem(1L).getInvoice());
    }

    @Test
    public void testMyComponentInScript() throws Exception {
        db.reset();
        String[] args = { "src/test/resources/insert-or-update-all-test.xml" };
        Breakfast.main(args);
        assertEquals(3, db.findCustomers().size());
    }
}
