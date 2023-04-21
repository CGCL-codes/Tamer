package com.azirar.dna.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import com.azirar.dna.entities.Application;
import com.azirar.dna.entities.Indisponibilite;
import com.azirar.dna.entities.Interaction;
import com.azirar.dna.entities.TypeIndisponibilite;
import com.azirar.dna.tools.ApplicationException;
import com.azirar.dna.tools.BaseDAO;

/**
 * Class IndisponibiliteDAO.
 */
public class IndisponibiliteDAO extends BaseDAO {

    /**
	 * Adds the or update.
	 *
	 * @param idIndisponibilite the id indisponibilite
	 * @param idApplication the id application
	 * @param idTypeIndisponibilite the id type indisponibilite
	 * @param idService the id interaction
	 * @param description the description
	 * @param dateDebut the date debut
	 * @param dateFin the date fin
	 * @param dateIndisponibilite the date livraison
	 * @param dateDisponibilite the date disponibilite
	 * @param planifiee the planifiee
	 * @throws ApplicationException the application exception
	 */
    public void addOrUpdate(int idIndisponibilite, int idApplication, int idTypeIndisponibilite, int idService, String description, Date dateDebut, Date dateFin, Date dateIndisponibilite, Date dateDisponibilite, boolean planifiee) throws ApplicationException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            Query q = em.createNamedQuery("selectIndisponibiliteById");
            q.setParameter("idIndisponibilite", idIndisponibilite);
            Indisponibilite indisponibilite = null;
            try {
                indisponibilite = (Indisponibilite) q.getSingleResult();
            } catch (NoResultException nre) {
                indisponibilite = new Indisponibilite();
            }
            q = em.createNamedQuery("selectServiceById");
            q.setParameter("idService", idService);
            Interaction interaction = null;
            try {
                interaction = (Interaction) q.getSingleResult();
            } catch (NoResultException nre) {
            }
            q = em.createNamedQuery("selectApplicationById");
            q.setParameter("idApplication", idApplication);
            Application application = (Application) q.getSingleResult();
            q = em.createNamedQuery("selectTypeIndisponibiliteById");
            q.setParameter("idTypeIndisponibilite", idTypeIndisponibilite);
            TypeIndisponibilite typeIndisponibilite = (TypeIndisponibilite) q.getSingleResult();
            indisponibilite.setInteraction(interaction);
            indisponibilite.setApplication(application);
            indisponibilite.setTypeIndisponibilite(typeIndisponibilite);
            indisponibilite.setDateDebut(dateDebut);
            indisponibilite.setDateFin(dateFin);
            indisponibilite.setDateIndisponibilite(dateIndisponibilite);
            indisponibilite.setDateDisponibilite(dateDisponibilite);
            indisponibilite.setDescription(description);
            indisponibilite.setPlanifiee(planifiee);
            em.persist(indisponibilite);
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
	 * Removes the.
	 *
	 * @param idIndisponibilite the id indisponibilite
	 * @throws ApplicationException the application exception
	 */
    public void remove(int idIndisponibilite) throws ApplicationException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            Query q = em.createNamedQuery("selectIndisponibiliteById");
            q.setParameter("idIndisponibilite", idIndisponibilite);
            Indisponibilite indisponibilite = (Indisponibilite) q.getSingleResult();
            em.remove(indisponibilite);
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
	 * Retourne : by id.
	 *
	 * @param idIndisponibilite the id indisponibilite
	 * @return the by id
	 * @throws ApplicationException the application exception
	 */
    public Indisponibilite getById(int idIndisponibilite) throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectIndisponibiliteById");
            q.setParameter("idIndisponibilite", idIndisponibilite);
            Indisponibilite indisponibilite = (Indisponibilite) q.getSingleResult();
            return indisponibilite;
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
    public List<Indisponibilite> getAllByIdApplication(int idApplication) throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectAllIndisponibilitesByIdApplication");
            q.setParameter("idApplication", idApplication);
            List<Indisponibilite> indisponibilites = (List<Indisponibilite>) q.getResultList();
            return indisponibilites;
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
	 * Retourne : all by id interaction.
	 *
	 * @param idService the id interaction
	 * @return the all by id interaction
	 * @throws ApplicationException the application exception
	 */
    public List<Indisponibilite> getAllByIdService(int idService) throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectIndisponibiliteByIdService");
            q.setParameter("idService", idService);
            List<Indisponibilite> indisponibilites = (List<Indisponibilite>) q.getResultList();
            return indisponibilites;
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
    public List<Indisponibilite> getAll() throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectAllIndisponibilites");
            List<Indisponibilite> indisponibilites = (List<Indisponibilite>) q.getResultList();
            return indisponibilites;
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
	 * Retourne : all by id type indisponibilite.
	 *
	 * @param idTypeIndisponibilite the id type indisponibilite
	 * @return the all by id type indisponibilite
	 * @throws ApplicationException the application exception
	 */
    public List<Indisponibilite> getAllByIdTypeIndisponibilite(int idTypeIndisponibilite) throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectAllIndisponibilitesByIdTypeIndisponibilite");
            q.setParameter("idTypeIndisponibilite", idTypeIndisponibilite);
            List<Indisponibilite> indisponibilites = (List<Indisponibilite>) q.getResultList();
            return indisponibilites;
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
	 * Select all indisponibilites by id application between date.
	 *
	 * @param idApplication the id application
	 * @param currentDate the current date
	 * @return the list
	 * @throws ApplicationException the application exception
	 */
    public List<Indisponibilite> selectAllIndisponibilitesByIdApplicationBetweenDate(int idApplication, Date currentDate) throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("selectAllIndisponibilitesByIdApplicationBetweenDate");
            q.setParameter("idApplication", idApplication);
            q.setParameter("date", currentDate);
            List<Indisponibilite> indisponibilites = (List<Indisponibilite>) q.getResultList();
            return indisponibilites;
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Indisponibilite> getAllByIdApplicationAndDateDebutAndDateFinPlage(int idApplication, Date dateDebutPlage, Date dateFinPlage) throws ApplicationException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("getAllByIdApplicationBetweenDateDebutAndDateFinPlage");
            q.setParameter("idApplication", idApplication);
            q.setParameter("dateDebutPlage", dateDebutPlage);
            ;
            q.setParameter("dateFinPlage", dateFinPlage);
            List<Indisponibilite> indisponibilites = (List<Indisponibilite>) q.getResultList();
            return indisponibilites;
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
