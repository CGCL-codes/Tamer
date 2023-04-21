package org.isurf.spmiddleware.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.isurf.spmiddleware.SPClientProfile;
import org.isurf.spmiddleware.dao.SPClientProfileDAO;

/**
 * JPA implementation of the SPClientProfileDAO.
 */
public class SPClientProfileDAOImpl implements SPClientProfileDAO {

    private static Logger logger = Logger.getLogger(SPClientProfileDAOImpl.class);

    @PersistenceUnit
    private EntityManagerFactory emf;

    /**
	 * Sets the EntityManagerFactory.
	 *
	 * @param emf The EntityManagerFactory to be set.
	 */
    public void setEntityManager(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /** {@inheritDoc} */
    public SPClientProfile find() {
        logger.debug("find: ");
        SPClientProfile profile = null;
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("findSPClientProfile");
            profile = (SPClientProfile) query.getSingleResult();
        } catch (NoResultException nre) {
            logger.debug("find: no SPClientProfile found, returning new SPClientProfile");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return profile;
    }

    /** {@inheritDoc} */
    public void saveOrUpdate(SPClientProfile newVersion) {
        SPClientProfile currentVersion = find();
        EntityManager em = emf.createEntityManager();
        try {
            if (currentVersion != null) {
                newVersion.setId(currentVersion.getId());
            }
            em.getTransaction().begin();
            em.merge(newVersion);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
