package jwis.bl.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author javier
 */
public class ItemTest {

    public ItemTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Initialize database with Items.
     */
    @Test
    public void initItems() {
        System.out.println("initItems");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JWIS_PU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        Item item1 = new Item();
        item1.setCode("P001");
        item1.setName("Cafe");
        item1.setDescription("");
        Item item2 = new Item();
        item2.setCode("P002");
        item2.setName("Te");
        item2.setDescription("");
        Item item3 = new Item();
        item3.setCode("P003");
        item3.setName("Cocoa");
        item3.setDescription("");
        tx.begin();
        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        tx.commit();
        em.close();
        emf.close();
    }
}
