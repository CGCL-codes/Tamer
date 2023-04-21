package fr.devcoop.jee.tp8.impl;

import fr.devcoop.jee.tp8.DAOException;
import fr.devcoop.jee.tp8.DVD;
import fr.devcoop.jee.tp8.DVD_;
import fr.devcoop.jee.tp8.Media_;
import fr.devcoop.jee.tp8.OpticalDiscDAO;
import fr.devcoop.jee.tp8.Person;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author lforet
 */
public class DVDDAOJPAImpl extends MediaDAOJPAAbstract<DVD> implements OpticalDiscDAO {

    public DVDDAOJPAImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<DVD> getAll() throws DAOException {
        Query query = entityManager.createNamedQuery("allDVDs");
        List<DVD> toReturn = query.getResultList();
        return toReturn;
    }

    @Override
    public List<DVD> findByTitle(String titlePrefix) {
        CriteriaBuilder bldr = entityManager.getCriteriaBuilder();
        CriteriaQuery<DVD> query = bldr.createQuery(DVD.class);
        Root<DVD> p = query.from(DVD.class);
        titlePrefix = titlePrefix + "%";
        query.select(p).where(bldr.like(p.get(Media_.title), titlePrefix));
        TypedQuery<DVD> q = entityManager.createQuery(query);
        return q.getResultList();
    }

    @Override
    public List<DVD> findAllBetween(Long lowDuration, Long highDuration) {
        CriteriaBuilder bldr = entityManager.getCriteriaBuilder();
        CriteriaQuery<DVD> query = bldr.createQuery(DVD.class);
        Root<DVD> p = query.from(DVD.class);
        query.select(p).where(bldr.between(p.get(DVD_.duration), lowDuration, highDuration));
        TypedQuery<DVD> q = entityManager.createQuery(query);
        return q.getResultList();
    }
}
