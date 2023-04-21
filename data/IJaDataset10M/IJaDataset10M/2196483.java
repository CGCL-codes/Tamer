package com.azirar.dna.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import com.azirar.dna.entities.TypeApplication;
import com.azirar.dna.tools.ApplicationException;
import com.azirar.dna.tools.BaseDAO;

/**
 * Class TypeApplicationDAO.
 */
public class TypeApplicationDAO extends BaseDAO {

    /**
	 * Adds the or update type application.
	 *
	 * @param idTypeApplication the id type application
	 * @param nom the nom
	 * @param description the description
	 * @throws ApplicationException the application exception
	 */
    public void addOrUpdateTypeApplication(int idTypeApplication, String nom, String description, String color) throws ApplicationException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            Query q = em.createNamedQuery("selectTypeApplicationById");
            q.setParameter("idTypeApplication", idTypeApplication);
            TypeApplication typeApplication = null;
            try {
                typeApplication = (TypeApplication) q.getSingleResult();
                typeApplication.setDescription(description);
            } catch (NoResultException nre) {
                typeApplication = new TypeApplication();
                typeApplication.setNom(nom);
                typeApplication.setDescription(description);
                typeApplication.setColor(color);
            }
            em.persist(typeApplication);
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
	 * Removes the type application.
	 *
	 * @param idTypeApplication the id type application
	 * @throws ApplicationException the application exception
	 */
    public void removeTypeApplication(int idTypeApplication) throws ApplicationException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            Query q = em.createNamedQuery("selectTypeApplicationById");
            q.setParameter("idTypeApplication", idTypeApplication);
            TypeApplication typeApplication = (TypeApplication) q.getSingleResult();
            em.remove(typeApplication);
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
	 * Retourne : list all type applications.
	 *
	 * @return the list all type applications
	 * @throws ApplicationException the application exception
	 */
    public List<TypeApplication> getListAllTypeApplications() throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectAllTypeApplications");
            List<TypeApplication> listTypesApps = (List<TypeApplication>) q.getResultList();
            return listTypesApps;
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
	 * Retourne : by id.
	 *
	 * @param idTypeApplication the id type application
	 * @return the by id
	 * @throws ApplicationException the application exception
	 */
    public TypeApplication getById(int idTypeApplication) throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectTypeApplicationById");
            q.setParameter("idTypeApplication", idTypeApplication);
            TypeApplication typeApplication = (TypeApplication) q.getSingleResult();
            return typeApplication;
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
