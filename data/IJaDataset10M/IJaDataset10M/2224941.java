package entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Aditya-Z
 */
public class KelolaTJ {

    public KelolaTJ() {
        emf = Persistence.createEntityManagerFactory("Dokter_mandiriPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void createTanyaJawab(TanyaJawab tanyaJawab) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(tanyaJawab);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public TanyaJawab getTanyaJawab(String username) {
        TanyaJawab tanyaJawab = null;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT object(a) FROM TanyaJawab AS a WHERE a.nama=:usr");
            q.setParameter("usr", username);
            tanyaJawab = (TanyaJawab) q.getSingleResult();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return tanyaJawab;
    }

    public boolean checkTanyaJawab(String username) {
        boolean result = false;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT count(o) FROM TanyaJawab AS o WHERE o.nama=:usr");
            q.setParameter("usr", username);
            int jumlahUser = ((Long) q.getSingleResult()).intValue();
            if (jumlahUser > 0) {
                result = true;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return result;
    }

    public void createPromotor(TanyaJawab tanyaJawab) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(tanyaJawab);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
