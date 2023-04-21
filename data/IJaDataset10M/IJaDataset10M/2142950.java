package Controller;

import DB.RefMenu;
import KayitInterface.RefMenuKayit;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Can
 */
public class RefMenuCtl implements RefMenuKayit {

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public RefMenuCtl() {
        emf = Persistence.createEntityManagerFactory("OtoYikamaPU");
    }

    public int KayitEkle(RefMenu Menu) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(Menu);
            em.getTransaction().commit();
        } catch (EntityExistsException ex) {
            return -1;
        } catch (Exception ex) {
            return -2;
        } finally {
            if (em != null) {
                em.close();
            }
            return 0;
        }
    }

    public int KayitGuncelle(RefMenu Menu) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(Menu);
            em.getTransaction().commit();
        } catch (Exception ex) {
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
            return 0;
        }
    }

    public int KayitSil(RefMenu Menu) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.remove(Menu);
            em.getTransaction().commit();
        } catch (Exception ex) {
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
            return 0;
        }
    }

    public RefMenu Bul(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RefMenu.class, id);
        } finally {
            em.close();
        }
    }

    public RefMenu mMenuBulMenuID(Integer id) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("RefMenu.findByMenuID");
        q.setParameter("menuID", id);
        return (RefMenu) q.getSingleResult();
    }

    public List<RefMenu> mMenuBulAktifMenu(Boolean aktif) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("RefMenu.findByAktif");
        q.setParameter("aktif", aktif);
        return q.getResultList();
    }

    public List<RefMenu> mMenuBulMenuAdi(String adi) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("RefMenu.findByMenuAdi");
        q.setParameter("menuAdi", adi);
        return q.getResultList();
    }

    public List<RefMenu> tumTabloyuGetir() {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("RefMenu.findAll");
        try {
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int getRefMenuCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from RefMenu as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
