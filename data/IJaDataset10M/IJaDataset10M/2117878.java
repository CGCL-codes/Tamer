package entity;

import entity.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author cYubi
 */
public class DaftarBarangnewarrival implements Serializable {

    public DaftarBarangnewarrival() {
        emf = Persistence.createEntityManagerFactory("persistence");
    }

    @Id
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public boolean checkBarangnewarrival(String nama_barangnew, Long id_barangnew) {
        boolean result = false;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT count(o) FROM Barangnewarrival AS o WHERE o.nama_barangnew=:nama_barangnew AND o.id_barangnew=:id_barangnew");
            q.setParameter("nama_barangnew", nama_barangnew);
            q.setParameter("id_barangnew", id_barangnew);
            int jumlahBarangnewarrival = ((Long) q.getSingleResult()).intValue();
            if (jumlahBarangnewarrival > 0) {
                result = true;
            }
        } finally {
            em.close();
        }
        return result;
    }

    public boolean cekBarangnewarrival() {
        boolean result = false;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT count(o) FROM Barangnewarrival AS o");
            int jumlahBarangnewarrival = ((Long) q.getSingleResult()).intValue();
            if (jumlahBarangnewarrival > 0) {
                result = true;
            }
        } finally {
            em.close();
        }
        return result;
    }

    public void addBarangnewarrival(Barangnewarrival newarrival) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(newarrival);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Barangnewarrival> getDaftarBarangnewarrival() {
        List<Barangnewarrival> daftarNewarrival = new ArrayList<Barangnewarrival>();
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT object(o) FROM Barangnewarrival AS o");
            daftarNewarrival = q.getResultList();
        } finally {
            em.close();
        }
        return daftarNewarrival;
    }

    public List<Barangnewarrival> getDaftarBarangnewarrival(User user) {
        List<Barangnewarrival> daftarBarangnewarrival = new ArrayList<Barangnewarrival>();
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT object(o) FROM Barangnewarrival AS o");
            daftarBarangnewarrival = q.getResultList();
        } finally {
            em.close();
        }
        return daftarBarangnewarrival;
    }

    public List<Barangnewarrival> getDaftarBarangnewarrivalPengguna(long id) {
        List<Barangnewarrival> daftarBarangnewarrival = new ArrayList<Barangnewarrival>();
        int stat = 0;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT object(o) FROM Barangnewarrival AS o WHERE o.id=:id");
            q.setParameter("id", id);
            daftarBarangnewarrival = q.getResultList();
        } finally {
            em.close();
        }
        return daftarBarangnewarrival;
    }

    public Barangnewarrival getBarangnewarrival(long id_barangnew) {
        Barangnewarrival newarrival = null;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT object(o) FROM Barangnewarrival AS o WHERE o.id_barangnew=:id_barangnew");
            q.setParameter("id_barangnew", id_barangnew);
            newarrival = (Barangnewarrival) q.getSingleResult();
        } finally {
            em.close();
        }
        return newarrival;
    }

    public void editBarangnewarrival(Barangnewarrival newarrival) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(newarrival);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public void deleteBarangnewarrival(Long id) throws NonexistentEntityException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            Barangnewarrival newarrival;
            try {
                newarrival = em.find(Barangnewarrival.class, id);
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The Barangnewarrival with id " + id + " no longer exists.", enfe);
            }
            em.remove(newarrival);
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
