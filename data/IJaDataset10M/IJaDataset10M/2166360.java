package lg.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lg.dao.api.MenuDao;
import lg.domain.bean.Menu;
import lg.domain.bean.Usuario;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Home object for domain model class Menu.
 * @see model.Menu
 * @author Hibernate Tools
 */
@Repository
public class MenuDaoImpl extends AbstractDaoImpl implements MenuDao {

    private static final Log log = LogFactory.getLog(MenuDaoImpl.class);

    public void initialize(Object obj) {
        getHibernateTemplate().initialize(obj);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void persist(Menu transientInstance) {
        log.debug("persisting Menu instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    @Transactional
    public void remove(Menu persistentInstance) {
        log.debug("removing Menu instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    @Transactional
    public Menu merge(Menu detachedInstance) {
        log.debug("merging Menu instance");
        try {
            Menu result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    @Transactional(readOnly = true)
    public Menu findById(Integer id) {
        log.debug("getting Menu instance with id: " + id);
        try {
            Menu instance = entityManager.find(Menu.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    @Transactional(readOnly = true)
    public Menu findByAccion(String tipo) {
        log.debug("getting Usuario instance with name: " + tipo);
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Usuario.class);
            criteria.add(Restrictions.eq("tipo", tipo));
            Menu instance = (Menu) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
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
}
