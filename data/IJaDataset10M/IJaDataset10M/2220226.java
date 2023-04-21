package bisnes.dao;

import hibernates.Hurtownie;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class Hurtownie.
 * 
 * @see bisnes.dao.Hurtownie
 * @author Hibernate Tools
 */
public class HurtownieDao extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(HurtownieDao.class);

    public void persist(Hurtownie transientInstance) {
        log.debug("persisting Hurtownie instance");
        try {
            getHibernateTemplate().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(Hurtownie instance) {
        log.debug("attaching dirty Hurtownie instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Hurtownie instance) {
        log.debug("attaching clean Hurtownie instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Hurtownie persistentInstance) {
        log.debug("deleting Hurtownie instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Hurtownie merge(Hurtownie detachedInstance) {
        log.debug("merging Hurtownie instance");
        try {
            Hurtownie result = (Hurtownie) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Hurtownie findById(Long id) {
        log.debug("getting Hurtownie instance with id: " + id);
        try {
            Hurtownie instance = (Hurtownie) getHibernateTemplate().get("bisnes.dao.Hurtownie", id);
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
    public List<Hurtownie> findByExample(Hurtownie instance) {
        log.debug("finding Hurtownie instance by example");
        try {
            List<Hurtownie> results = (List<Hurtownie>) getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Hurtownie> findByProduktID(Long id_produkt) {
        List<Hurtownie> result = getHibernateTemplate().findByNamedParam("" + "select distinct h " + "from Hurtownie h " + "join h.lhurtownieProdukties as lhp " + "join lhp.id.produkty as prod " + "where prod.idProdukt = :id_pr", "id_pr", id_produkt);
        System.out.println("in query");
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Hurtownie> find(Hurtownie hurtownia) {
        List<Hurtownie> hurtownie = getHibernateTemplate().findByNamedParam("From Hurtownie where nazwa like :nazwa", "nazwa", hurtownia.getNazwa() + "%");
        return hurtownie;
    }

    @SuppressWarnings("unchecked")
    public List<Hurtownie> findAll() {
        List<Hurtownie> hurtownie = getHibernateTemplate().find("from Hurtownie");
        return hurtownie;
    }
}
