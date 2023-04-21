package entity;

import controller.exceptions.NonexistentEntityException;
import entity.Kejadiansejarah;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class KejadiansejarahLogic {

    private int jumlahKejadian = -1;

    public KejadiansejarahLogic() {
        emf = Persistence.createEntityManagerFactory("aci1PU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<Kejadiansejarah> getKejadian() {
        List<Kejadiansejarah> kejadian = new ArrayList<Kejadiansejarah>();
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT k FROM Kejadiansejarah k");
            kejadian = q.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return kejadian;
    }

    public boolean checkKejadian(Integer idKejadian) {
        boolean result = false;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT count(k) FROM Kejadiansejarah AS k WHERE k.idKejadian=:idKejadian");
            q.setParameter("idKejadian", idKejadian);
            int jumlahKej = ((Long) q.getSingleResult()).intValue();
            if (jumlahKej > 0) {
                result = true;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return result;
    }

    public boolean checkKejadian(String namaKejadian) {
        boolean result = false;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT count(k) FROM Kejadiansejarah AS k WHERE k.namaKejadian=:namaKejadian");
            q.setParameter("namaKejadian", namaKejadian);
            int jumlahKejadian = ((Long) q.getSingleResult()).intValue();
            if (jumlahKejadian > 0) {
                result = true;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return result;
    }

    public Kejadiansejarah getIdKejadian(Integer idKejadian) {
        Kejadiansejarah Kejadian = null;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT k FROM Kejadiansejarah k WHERE k.idKejadian = :idKejadian");
            q.setParameter("idKejadian", idKejadian);
            Kejadian = (Kejadiansejarah) q.getSingleResult();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return Kejadian;
    }

    public Kejadiansejarah getNamaKejadian(String namaKejadian) {
        Kejadiansejarah Kejadian = null;
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT k FROM Kejadiansejarah k WHERE k.namaKejadian = :namaKejadian");
            q.setParameter("namaKejadian", namaKejadian);
            Kejadian = (Kejadiansejarah) q.getSingleResult();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return Kejadian;
    }

    public List<Kejadiansejarah> index(String keyword) {
        List<Kejadiansejarah> kejadian = new ArrayList<Kejadiansejarah>();
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT object(k) FROM Kejadiansejarah AS k WHERE k.namaKejadian LIKE :keyword");
            q.setParameter("keyword", keyword + "%");
            kejadian = q.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return kejadian;
    }

    public List<Kejadiansejarah> searchByKeyword(String keyword) {
        List<Kejadiansejarah> kejadian = new ArrayList<Kejadiansejarah>();
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT object(k) FROM Kejadiansejarah AS k WHERE k.namaKejadian LIKE :keyword");
            q.setParameter("keyword", "%" + keyword + "%");
            kejadian = q.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return kejadian;
    }

    public void createArtikelKejadian(Kejadiansejarah kejadian) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(kejadian);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editArtikelKejadian(Kejadiansejarah kejadian) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(kejadian);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void deleteKejadian(int id) throws NonexistentEntityException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            Kejadiansejarah kejadian;
            try {
                kejadian = em.getReference(Kejadiansejarah.class, id);
                kejadian.getIdKejadian();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The articel with id " + id + " no longer exists.", enfe);
            }
            em.remove(kejadian);
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
