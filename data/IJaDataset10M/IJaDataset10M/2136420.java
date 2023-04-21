package org.sevaapp.dao;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.sevaapp.domain.Lab;

@Repository
@Transactional
public class LabDaoJpaImpl implements LabDao {

    private static final Log log = LogFactory.getLog(LabDaoJpaImpl.class);

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public Lab load(Integer id) throws DaoException {
        log.debug("load() method called.");
        return em.find(Lab.class, id);
    }

    public Collection<Lab> findAll() throws DaoException {
        log.debug("findAll() method called.");
        String query = "SELECT lab from Lab lab";
        List<Lab> labList = (List<Lab>) em.createQuery(query).getResultList();
        return labList;
    }

    public void add(Lab domainObject) throws DaoException {
        log.debug("add(domainObject) method called.");
        em.persist(domainObject);
    }

    public void update(Lab domainObject) throws DaoException {
        log.debug("update() method called.");
        em.merge(domainObject);
    }

    public void delete(Integer id) throws DaoException {
        log.debug("delete() method called.");
        Lab lab = em.find(Lab.class, id);
        em.merge(lab);
        em.remove(lab);
    }
}
