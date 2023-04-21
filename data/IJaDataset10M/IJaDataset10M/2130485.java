package bisnes.dao;

import hibernates.Place;
import hibernates.Premie;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class Premie.
 * @see bisnes.dao.Premie
 * @author Hibernate Tools
 */
public class PremieDao extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(PremieDao.class);

    public void persist(Premie transientInstance) {
        log.debug("persisting Premie instance");
        try {
            getHibernateTemplate().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(Premie instance) {
        log.debug("attaching dirty Premie instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Premie instance) {
        log.debug("attaching clean Premie instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Premie persistentInstance) {
        log.debug("deleting Premie instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Premie merge(Premie detachedInstance) {
        log.debug("merging Premie instance");
        try {
            Premie result = (Premie) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Premie findById(long id) {
        log.debug("getting Premie instance with id: " + id);
        try {
            Premie instance = (Premie) getHibernateTemplate().get("bisnes.dao.Premie", id);
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Premie> findByExample(Premie instance) {
        log.debug("finding Premie instance by example");
        try {
            List<Premie> results = (List<Premie>) getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Premie> findAll() {
        List<Premie> premie = getHibernateTemplate().find("from Premie order by dataPrzyznania desc");
        return premie;
    }

    @SuppressWarnings("unchecked")
    public List<Premie> findCriteria(long id_pracownik) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Premie.class).add(Restrictions.eq("pracownicy.idPracownik", id_pracownik));
        List<Premie> premie = getHibernateTemplate().findByCriteria(criteria);
        return premie;
    }
}
