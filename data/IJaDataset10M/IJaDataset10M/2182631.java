package entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author fredy
 */
public class daftarBaju {

    public daftarBaju() {
        emf = Persistence.createEntityManagerFactory("RBPL_HOPEPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public boolean check(String username, String password) {
        boolean result = false;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT count(o) FROM baju AS o WHERE o.username=:username AND o.password=:password");
            q.setParameter("username", username);
            q.setParameter("password", password);
            int jumlahBaju = ((Long) q.getSingleResult()).intValue();
            if (jumlahBaju > 0) {
                result = true;
            }
        } finally {
            em.close();
        }
        return result;
    }

    public boolean checkBaju(String username) {
        boolean result = false;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT count(o) FROM baju AS o WHERE o.username=:username");
            q.setParameter("username", username);
            int jumlahBaju = ((Long) q.getSingleResult()).intValue();
            if (jumlahBaju > 0) {
                result = true;
            }
        } finally {
            em.close();
        }
        return result;
    }

    public baju getBaju(String username, String password) {
        baju baju = null;
        EntityManager em = getEntityManager();
        try {
            boolean hasilCheck = this.check(username, password);
            if (hasilCheck) {
                Query q = em.createQuery("SELECT object(o) FROM baju AS o WHERE o.username=:username AND o.password=:password");
                q.setParameter("username", username);
                q.setParameter("password", password);
                baju = (baju) q.getSingleResult();
            }
        } finally {
            em.close();
        }
        return baju;
    }

    public void addBaju(baju baju) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(baju);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public List<baju> getBaju() {
        List<baju> bajus = new ArrayList<baju>();
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT object(o) FROM baju AS o");
            bajus = q.getResultList();
        } finally {
            em.close();
        }
        return bajus;
    }

    public List<baju> getUsername(String username) {
        List<baju> bajus = new ArrayList<baju>();
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT object(o) FROM baju AS o WHERE o.username=:username");
            q.setParameter("username", username);
            bajus = q.getResultList();
        } finally {
            em.close();
        }
        return bajus;
    }

    public void getBajus(baju baju) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void addBaju(servlet.baju baju) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
