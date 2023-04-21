package com.ballroomregistrar.compinabox.online.data.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.springframework.transaction.annotation.Transactional;
import com.ballroomregistrar.compinabox.online.data.Competition;
import com.ballroomregistrar.compinabox.online.data.CompetitionRepository;
import com.ballroomregistrar.compinabox.online.data.CompetitionType;

@Transactional
public class HibernateCompetitionRepository implements CompetitionRepository {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(final EntityManager em) {
        entityManager = em;
    }

    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            throw new IllegalStateException("EntityManager has not been set on DAO before usage");
        }
        return entityManager;
    }

    public Competition getByName(final String competitionName) {
        Query query = getEntityManager().createQuery("SELECT c FROM Competition c WHERE c.name = :compName").setParameter("compName", competitionName);
        return (Competition) query.getSingleResult();
    }

    public Competition find(final Long id) {
        return getEntityManager().find(Competition.class, id);
    }

    protected Session getSession() {
        final HibernateEntityManager hem = (HibernateEntityManager) getEntityManager();
        return hem.getSession();
    }

    public void save(Competition competition) {
        getSession().saveOrUpdate(competition);
    }

    public void delete(Competition competition) {
        competition.getEvents().clear();
        save(competition);
        getSession().delete(competition);
    }

    @SuppressWarnings("unchecked")
    public List<Competition> getOpenCompetitions() {
        Query query = getEntityManager().createNamedQuery("openCompetitions");
        return (List<Competition>) query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Competition> getCompetitionsWithResults() {
        Query query = getEntityManager().createNamedQuery("competitionsWithResults");
        return (List<Competition>) query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<CompetitionType> getCompetitionTypes() {
        Query query = getEntityManager().createNamedQuery("competitionTypeList");
        return (List<CompetitionType>) query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Competition> search(String search) {
        Query query = getEntityManager().createNamedQuery("searchCompetitions");
        query.setParameter("searchTerm", search);
        return (List<Competition>) query.getResultList();
    }
}
