package org.nomicron.suber.model.hibernate;

import org.nomicron.suber.model.dao.TurnDao;
import org.nomicron.suber.model.object.Turn;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.util.List;

/**
 * Hibernate implementation of the TurnDao.
 */
public class TurnDaoImpl extends SuberHibernateDaoImpl implements TurnDao {

    protected static Logger log = LogManager.getLogger(TurnDaoImpl.class);

    /**
     * Get a new instance of a Turn.
     *
     * @return new Turn object
     */
    public Turn create() {
        return (Turn) createPersistent();
    }

    /**
     * Store the specified Turn to the persistence layer.
     *
     * @param object Turn to store
     */
    public void store(Turn object) {
        storePersisent(object);
    }

    /**
     * Find the Turn with the specified id.
     *
     * @param id id to look for
     * @return matching Turn
     */
    public Turn findById(Integer id) {
        return (Turn) findPersistentById(id);
    }

    /**
     * Find all Turn objects
     *
     * @return List of Turns
     */
    public List<Turn> findAll() {
        Criteria criteria = getSession().createCriteria(getObjectClass());
        return criteria.list();
    }

    /**
     * Find the maximum ordinal.
     *
     * @return ordinal
     */
    public Integer maxOrdinal() {
        Criteria criteria = getSession().createCriteria(getObjectClass());
        criteria.setProjection(Projections.max("ordinal"));
        return (Integer) criteria.uniqueResult();
    }

    /**
     * Get the Turn with the specified ordinal.
     *
     * @param ordinal ordinal
     * @return Turn
     */
    public Turn findByOrdinal(Integer ordinal) {
        Criteria criteria = getSession().createCriteria(getObjectClass());
        criteria.add(Restrictions.eq("ordinal", ordinal));
        return (Turn) criteria.uniqueResult();
    }
}
