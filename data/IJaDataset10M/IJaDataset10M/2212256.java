package se.umu.cs.pvtht10.p4.g5.persistence.entities;

import static org.junit.Assert.*;
import java.util.GregorianCalendar;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.umu.cs.pvtht10.p4.g5.persistence.CategoryExistsException;
import se.umu.cs.pvtht10.p4.g5.persistence.CategoryService;
import se.umu.cs.pvtht10.p4.g5.persistence.ConditionService;
import se.umu.cs.pvtht10.p4.g5.persistence.CustomerExistsException;
import se.umu.cs.pvtht10.p4.g5.persistence.CustomerService;
import se.umu.cs.pvtht10.p4.g5.persistence.EMF;
import se.umu.cs.pvtht10.p4.g5.persistence.ProductService;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TestConditionXinY {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    private EntityManager em;

    private ConditionService cServe;

    private CustomerService uServe;

    private CategoryService tServe;

    private ProductService pServe;

    private CustomerEntity customer;

    private CategoryEntity category;

    @Before
    public void setUp() throws CustomerExistsException, CategoryExistsException {
        helper.setUp();
        em = EMF.get().createEntityManager();
        cServe = new ConditionService(em);
        uServe = new CustomerService(em);
        tServe = new CategoryService(em);
        pServe = new ProductService(em);
        customer = uServe.createCustomerAtomic("card001", "Jonas", "Söderberg");
        category = tServe.createCategoryAtomic("Frukt");
    }

    @After
    public void tearDown() {
        em.close();
        helper.tearDown();
    }

    @Test
    public void testValidate() {
        ProductEntity product = pServe.createProductAtomic("Banan", category);
        uServe.buy(customer, product, new GregorianCalendar().getTime());
        ConditionEntity c = cServe.createConditionAtomic(new ConditionXinY(1, category));
        assertTrue("Should validate", c.validate(customer));
    }

    @Test
    public void testNoValidate() {
        ConditionEntity c = cServe.createConditionAtomic(new ConditionXinY(1, category));
        assertFalse("Should not validate", c.validate(customer));
    }

    @Test
    public void testConditionXinY() {
        ConditionEntity c = cServe.createConditionAtomic(new ConditionXinY(1, category));
        assertNotNull("Condition should exist", c);
    }

    @Test
    public void testGetSetAmount() {
        ConditionXinY c = new ConditionXinY(1, category);
        cServe.createConditionAtomic(c);
        assertEquals("Should have amount 1", 1, c.getAmount());
        c.setAmount(3);
        assertEquals("Should have amount 3", 3, c.getAmount());
        c.setAmount(2);
        assertEquals("Should have amount 2", 2, c.getAmount());
    }

    @Test
    public void testGetSetCategory() throws CategoryExistsException {
        ConditionXinY c = new ConditionXinY(1, category);
        cServe.createConditionAtomic(c);
        CategoryEntity cat = tServe.createCategoryAtomic("Godis");
        assertEquals("Should have category Frukt", category, c.getCategory());
        c.setCategory(cat);
        assertEquals("Should have category Godis", cat, c.getCategory());
        c.setCategoryKey(category.getKey());
        assertEquals("Should have category Frukt", category.getKey(), c.getCategoryKey());
    }
}
