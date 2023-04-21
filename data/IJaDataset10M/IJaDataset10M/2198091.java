package com.financialcontrol.jpa;

import com.financialcontrol.db.ContaBanco;
import com.financialcontrol.jpa.exceptions.NonexistentEntityException;
import com.financialcontrol.jpa.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.financialcontrol.db.Banco;
import com.financialcontrol.db.TipoContaBanco;

/**
 *
 * @author Leonardo
 */
public class ContaBancoJpaController {

    public ContaBancoJpaController() {
        emf = Persistence.createEntityManagerFactory("WebFinancialControlPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ContaBanco contaBanco) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Banco nuBanco = contaBanco.getNuBanco();
            if (nuBanco != null) {
                nuBanco = em.getReference(nuBanco.getClass(), nuBanco.getNuBanco());
                contaBanco.setNuBanco(nuBanco);
            }
            TipoContaBanco nuTipoContaBanco = contaBanco.getNuTipoContaBanco();
            if (nuTipoContaBanco != null) {
                nuTipoContaBanco = em.getReference(nuTipoContaBanco.getClass(), nuTipoContaBanco.getNuTipoContaBanco());
                contaBanco.setNuTipoContaBanco(nuTipoContaBanco);
            }
            em.persist(contaBanco);
            if (nuBanco != null) {
                nuBanco.getContaBancoList().add(contaBanco);
                nuBanco = em.merge(nuBanco);
            }
            if (nuTipoContaBanco != null) {
                nuTipoContaBanco.getContaBancoList().add(contaBanco);
                nuTipoContaBanco = em.merge(nuTipoContaBanco);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findContaBanco(contaBanco.getNuContaBanco()) != null) {
                throw new PreexistingEntityException("ContaBanco " + contaBanco + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ContaBanco contaBanco) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ContaBanco persistentContaBanco = em.find(ContaBanco.class, contaBanco.getNuContaBanco());
            Banco nuBancoOld = persistentContaBanco.getNuBanco();
            Banco nuBancoNew = contaBanco.getNuBanco();
            TipoContaBanco nuTipoContaBancoOld = persistentContaBanco.getNuTipoContaBanco();
            TipoContaBanco nuTipoContaBancoNew = contaBanco.getNuTipoContaBanco();
            if (nuBancoNew != null) {
                nuBancoNew = em.getReference(nuBancoNew.getClass(), nuBancoNew.getNuBanco());
                contaBanco.setNuBanco(nuBancoNew);
            }
            if (nuTipoContaBancoNew != null) {
                nuTipoContaBancoNew = em.getReference(nuTipoContaBancoNew.getClass(), nuTipoContaBancoNew.getNuTipoContaBanco());
                contaBanco.setNuTipoContaBanco(nuTipoContaBancoNew);
            }
            contaBanco = em.merge(contaBanco);
            if (nuBancoOld != null && !nuBancoOld.equals(nuBancoNew)) {
                nuBancoOld.getContaBancoList().remove(contaBanco);
                nuBancoOld = em.merge(nuBancoOld);
            }
            if (nuBancoNew != null && !nuBancoNew.equals(nuBancoOld)) {
                nuBancoNew.getContaBancoList().add(contaBanco);
                nuBancoNew = em.merge(nuBancoNew);
            }
            if (nuTipoContaBancoOld != null && !nuTipoContaBancoOld.equals(nuTipoContaBancoNew)) {
                nuTipoContaBancoOld.getContaBancoList().remove(contaBanco);
                nuTipoContaBancoOld = em.merge(nuTipoContaBancoOld);
            }
            if (nuTipoContaBancoNew != null && !nuTipoContaBancoNew.equals(nuTipoContaBancoOld)) {
                nuTipoContaBancoNew.getContaBancoList().add(contaBanco);
                nuTipoContaBancoNew = em.merge(nuTipoContaBancoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contaBanco.getNuContaBanco();
                if (findContaBanco(id) == null) {
                    throw new NonexistentEntityException("The contaBanco with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ContaBanco contaBanco;
            try {
                contaBanco = em.getReference(ContaBanco.class, id);
                contaBanco.getNuContaBanco();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contaBanco with id " + id + " no longer exists.", enfe);
            }
            Banco nuBanco = contaBanco.getNuBanco();
            if (nuBanco != null) {
                nuBanco.getContaBancoList().remove(contaBanco);
                nuBanco = em.merge(nuBanco);
            }
            TipoContaBanco nuTipoContaBanco = contaBanco.getNuTipoContaBanco();
            if (nuTipoContaBanco != null) {
                nuTipoContaBanco.getContaBancoList().remove(contaBanco);
                nuTipoContaBanco = em.merge(nuTipoContaBanco);
            }
            em.remove(contaBanco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ContaBanco> findContaBancoEntities() {
        return findContaBancoEntities(true, -1, -1);
    }

    public List<ContaBanco> findContaBancoEntities(int maxResults, int firstResult) {
        return findContaBancoEntities(false, maxResults, firstResult);
    }

    private List<ContaBanco> findContaBancoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from ContaBanco as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ContaBanco findContaBanco(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ContaBanco.class, id);
        } finally {
            em.close();
        }
    }

    public int getContaBancoCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from ContaBanco as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
