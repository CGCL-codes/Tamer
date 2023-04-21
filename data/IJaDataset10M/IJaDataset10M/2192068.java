package de.bwb.ekp.entities;

import static org.junit.Assert.assertFalse;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Dorian Gloski
 * @copyright akquinet AG, 2007
 */
public class AusschreibungHomeTest {

    private EntityManagerFactory factory;

    private EntityManager entityManager;

    private final AusschreibungHome ausschreibungHome = new AusschreibungHome();

    @Before
    public void setUp() {
        this.factory = Persistence.createEntityManagerFactory("EinkaufsplattformTest");
        this.entityManager = this.factory.createEntityManager();
        this.ausschreibungHome.setEntityManager(this.entityManager);
    }

    @After
    public void tearDown() {
        if (this.entityManager != null) {
            this.entityManager.close();
        }
        if (this.factory != null) {
            this.factory.close();
        }
    }

    @Test
    public void testCheckDokumentUsed() {
        final Query query = this.entityManager.createQuery("from Dokument where id = 1");
        assertFalse(this.ausschreibungHome.checkDokumentUsed((Dokument) query.getSingleResult()));
    }
}
