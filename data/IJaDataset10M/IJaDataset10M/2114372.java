package com.azirar.dna.dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import com.azirar.dna.entities.Application;
import com.azirar.dna.entities.ApplicationComposant;
import com.azirar.dna.entities.Composant;
import com.azirar.dna.tools.ApplicationException;
import com.azirar.dna.tools.BaseDAO;

/**
 * Class ApplicationComposantDAO.
 */
public class ApplicationComposantDAO extends BaseDAO {

    /**
	 * Adds the.
	 *
	 * @param idApplication the id application
	 * @param idComposant the id composant
	 * @throws ApplicationException the application exception
	 */
    public void add(int idApplication, int idComposant) throws ApplicationException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            if (!contributeurAlreadyAttached(idComposant, idApplication)) {
                ApplicationComposant object = new ApplicationComposant();
                Query q = em.createNamedQuery("selectComposantById");
                q.setParameter("idComposant", idComposant);
                Composant composant = (Composant) q.getSingleResult();
                q = em.createNamedQuery("selectApplicationById");
                q.setParameter("idApplication", idApplication);
                Application application = (Application) q.getSingleResult();
                object.setApplication(application);
                object.setComposant(composant);
                em.persist(object);
                em.getTransaction().commit();
            } else {
                throw new ApplicationException("Ce composant a �t� d�j� rattach� � cette application");
            }
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
	 * Removes the.
	 *
	 * @param idApplication the id application
	 * @param idComposant the id composant
	 * @throws ApplicationException the application exception
	 */
    public void remove(int idApplication, int idComposant) throws ApplicationException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            Query q = em.createNamedQuery("selectApplicationComposantById");
            q.setParameter("idComposant", idComposant);
            q.setParameter("idApplication", idApplication);
            ApplicationComposant appComp = (ApplicationComposant) q.getSingleResult();
            em.remove(appComp);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
	 * Retourne : all.
	 *
	 * @return the all
	 * @throws ApplicationException the application exception
	 */
    public List<ApplicationComposant> getAll() throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectAllApplicationComposant");
            List<ApplicationComposant> lst = (List<ApplicationComposant>) q.getResultList();
            return lst;
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
	 * Contributeur already attached.
	 *
	 * @param idComposant the id composant
	 * @param idApplication the id application
	 * @return true, if successful
	 * @throws ApplicationException the application exception
	 */
    public boolean contributeurAlreadyAttached(int idComposant, int idApplication) throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectApplicationComposantById");
            q.setParameter("idComposant", idComposant);
            q.setParameter("idApplication", idApplication);
            ApplicationComposant appContr = null;
            try {
                appContr = (ApplicationComposant) q.getSingleResult();
            } catch (NoResultException nre) {
            }
            return appContr != null;
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
	 * Retourne : all by id application.
	 *
	 * @param idApplication the id application
	 * @return the all by id application
	 * @throws ApplicationException the application exception
	 */
    public List<ApplicationComposant> getAllByIdApplication(int idApplication) throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectAllApplicationComposantByIdApplication");
            q.setParameter("idApplication", idApplication);
            List<ApplicationComposant> lst = (List<ApplicationComposant>) q.getResultList();
            return lst;
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
	 * Retourne : all composants by id application.
	 *
	 * @param idApplication the id application
	 * @return the all composants by id application
	 * @throws ApplicationException the application exception
	 */
    public List<Composant> getAllComposantsByIdApplication(int idApplication) throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectAllApplicationComposantByIdApplication");
            q.setParameter("idApplication", idApplication);
            List<ApplicationComposant> lstComp = (List<ApplicationComposant>) q.getResultList();
            List<Composant> lst = new ArrayList<Composant>();
            for (ApplicationComposant appComp : lstComp) {
                lst.add(appComp.getComposant());
            }
            return lst;
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
	 * Retourne : all by id composant.
	 *
	 * @param idComposant the id composant
	 * @return the all by id composant
	 * @throws ApplicationException the application exception
	 */
    public List<ApplicationComposant> getAllByIdComposant(int idComposant) throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectAllApplicationComposantByIdComposant");
            q.setParameter("idComposant", idComposant);
            List<ApplicationComposant> lst = (List<ApplicationComposant>) q.getResultList();
            return lst;
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
