package org.softflex.persistencia.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import junit.framework.TestCase;

public class MetricaTest extends TestCase {

    private EntityManagerFactory emf;

    private EntityManager em;

    public void setUp() throws Exception {
        try {
            emf = Persistence.createEntityManagerFactory("softflex-persistencia-jpa");
            em = emf.createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testFindAll() {
        Query q = em.createNamedQuery("ALLMETRIC");
        List<Metrica> metrics = q.getResultList();
        for (Metrica u : metrics) {
            System.out.println(u.getDescripcion());
        }
    }

    public void testFindAllById() {
        Query q = em.createNamedQuery("ALLMETRIC_BY_ID");
        q.setParameter("theid", 1L);
        List<Metrica> metrics = q.getResultList();
        for (Metrica u : metrics) {
            System.out.println(u.getDescripcion());
        }
        Query q2 = em.createNamedQuery("ALLMETRIC_BY_ID_NAMED");
        q2.setParameter(1, 1L);
        List<Metrica> metrics2 = q2.getResultList();
        for (Metrica u : metrics2) {
            System.out.println(u.getDescripcion());
        }
    }
}
