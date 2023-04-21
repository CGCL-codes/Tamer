package org.objectwiz.entityDAO;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import org.objectwiz.entity.Company;
import org.objectwiz.entityDAO.exceptions.NonexistentEntityException;
import org.objectwiz.entity.Technician;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author xym
 */
public class CompanyJpaController {

    public CompanyJpaController() {
        emf = Persistence.createEntityManagerFactory("projet-s4");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Company company) {
        if (company.getTechnicians() == null) {
            company.setTechnicians(new HashSet<Technician>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Technician> attachedTechnicians = new HashSet<Technician>();
            for (Technician techniciansTechnicianToAttach : company.getTechnicians()) {
                techniciansTechnicianToAttach = em.getReference(techniciansTechnicianToAttach.getClass(), techniciansTechnicianToAttach.getId());
                attachedTechnicians.add(techniciansTechnicianToAttach);
            }
            company.setTechnicians(attachedTechnicians);
            em.persist(company);
            for (Technician techniciansTechnician : company.getTechnicians()) {
                Company oldCompanyOfTechniciansTechnician = techniciansTechnician.getCompany();
                techniciansTechnician.setCompany(company);
                techniciansTechnician = em.merge(techniciansTechnician);
                if (oldCompanyOfTechniciansTechnician != null) {
                    oldCompanyOfTechniciansTechnician.getTechnicians().remove(techniciansTechnician);
                    oldCompanyOfTechniciansTechnician = em.merge(oldCompanyOfTechniciansTechnician);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Company company) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Company persistentCompany = em.find(Company.class, company.getId());
            Set<Technician> techniciansOld = persistentCompany.getTechnicians();
            Set<Technician> techniciansNew = company.getTechnicians();
            Set<Technician> attachedTechniciansNew = new HashSet<Technician>();
            for (Technician techniciansNewTechnicianToAttach : techniciansNew) {
                techniciansNewTechnicianToAttach = em.getReference(techniciansNewTechnicianToAttach.getClass(), techniciansNewTechnicianToAttach.getId());
                attachedTechniciansNew.add(techniciansNewTechnicianToAttach);
            }
            techniciansNew = attachedTechniciansNew;
            company.setTechnicians(techniciansNew);
            company = em.merge(company);
            for (Technician techniciansOldTechnician : techniciansOld) {
                if (!techniciansNew.contains(techniciansOldTechnician)) {
                    techniciansOldTechnician.setCompany(null);
                    techniciansOldTechnician = em.merge(techniciansOldTechnician);
                }
            }
            for (Technician techniciansNewTechnician : techniciansNew) {
                if (!techniciansOld.contains(techniciansNewTechnician)) {
                    Company oldCompanyOfTechniciansNewTechnician = techniciansNewTechnician.getCompany();
                    techniciansNewTechnician.setCompany(company);
                    techniciansNewTechnician = em.merge(techniciansNewTechnician);
                    if (oldCompanyOfTechniciansNewTechnician != null && !oldCompanyOfTechniciansNewTechnician.equals(company)) {
                        oldCompanyOfTechniciansNewTechnician.getTechnicians().remove(techniciansNewTechnician);
                        oldCompanyOfTechniciansNewTechnician = em.merge(oldCompanyOfTechniciansNewTechnician);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = company.getId();
                if (findCompany(id) == null) {
                    throw new NonexistentEntityException("The company with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Company company;
            try {
                company = em.getReference(Company.class, id);
                company.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The company with id " + id + " no longer exists.", enfe);
            }
            Set<Technician> technicians = company.getTechnicians();
            for (Technician techniciansTechnician : technicians) {
                techniciansTechnician.setCompany(null);
                techniciansTechnician = em.merge(techniciansTechnician);
            }
            em.remove(company);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Company> findCompanyEntities() {
        return findCompanyEntities(true, -1, -1);
    }

    public List<Company> findCompanyEntities(int maxResults, int firstResult) {
        return findCompanyEntities(false, maxResults, firstResult);
    }

    private List<Company> findCompanyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Company as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Company findCompany(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Company.class, id);
        } finally {
            em.close();
        }
    }

    public List<Company> findCompanies(String name, String city) {
        EntityManager em = null;
        em = getEntityManager();
        em.getTransaction().begin();
        Query q = em.createQuery("select company from Company as company where company.name = :name and company.city= :city");
        q.setParameter("name", name);
        q.setParameter("city", city);
        List<Company> companies = q.getResultList();
        em.getTransaction().commit();
        em.close();
        return companies;
    }

    public int getCompanyCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Company as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
