package entity;

import entity.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ntonk
 */
public class DaftarGaji implements Serializable {

    public DaftarGaji() {
        emf = Persistence.createEntityManagerFactory("persistence");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public boolean checkId(String idgaji) {
        boolean result = false;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT count(o) FROM Gaji AS o WHERE o.id=:id");
            q.setParameter("idgaji", idgaji);
            int jumlahGaji = ((Long) q.getSingleResult()).intValue();
            if (jumlahGaji > 0) {
                result = true;
            }
        } finally {
            em.close();
        }
        return result;
    }

    public Gaji findGaji(String idgaji) {
        Gaji gaji = null;
        EntityManager em = getEntityManager();
        try {
            boolean hasilCheck = this.checkId(idgaji);
            if (hasilCheck) {
                Query q = em.createQuery("SELECT object(o) FROM Gaji AS o WHERE o.id=:id");
                q.setParameter("idgaji", idgaji);
                gaji = (Gaji) q.getSingleResult();
            }
        } finally {
            em.close();
        }
        return gaji;
    }

    public Gaji getGaji(String idgaji) {
        Gaji gaji = null;
        EntityManager em = getEntityManager();
        try {
            boolean hasilCheck = this.checkId(idgaji);
            if (hasilCheck) {
                Query q = em.createQuery("SELECT object(o) FROM Gaji AS o WHERE o.id=:id");
                q.setParameter("idgaji", idgaji);
                gaji = (Gaji) q.getSingleResult();
            }
        } finally {
            em.close();
        }
        return gaji;
    }

    public List<Gaji> getGji() {
        List<Gaji> proposals = new ArrayList<Gaji>();
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT object(o) FROM Gaji AS o");
            proposals = q.getResultList();
        } finally {
            em.close();
        }
        return proposals;
    }

    public List<Gaji> getGaj(String idgaji, String namapenerima) {
        List<Gaji> gajis = new ArrayList<Gaji>();
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT object(o) FROM GAji AS o WHERE o.idgaji=:idgaji and o.namapenerima LIKE :namapenerima ORDER BY o.namapenerima desc");
            q.setParameter("idgaji", idgaji);
            q.setParameter("namapenerima", namapenerima + '%');
            gajis = q.getResultList();
        } finally {
            em.close();
        }
        return gajis;
    }

    public void editGaji(Gaji gaji) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(gaji);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public void addGaji(Gaji gaji) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(gaji);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public void deleteGaji(String idgaji) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Gaji gaji;
            try {
                gaji = em.getReference(Gaji.class, idgaji);
                String idgaji1 = gaji.getIdgaji();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaksi with id " + idgaji + " no longer exists.", enfe);
            }
            em.remove(gaji);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
