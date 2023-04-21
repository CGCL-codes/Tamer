package net.sf.bootstrap.framework.dao.impl;

import java.io.Serializable;
import java.util.List;
import net.sf.bootstrap.framework.dao.DAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of the BaseDAO to provide generic data access
 * methods.
 *
 * @author Mark Moloney
 */
public class BaseDAOHibernate extends HibernateDaoSupport implements DAO {

    protected final Log log = LogFactory.getLog(getClass());

    public void refresh(Object obj) {
        getHibernateTemplate().refresh(obj);
    }

    public void store(Object obj) {
        getHibernateTemplate().saveOrUpdate(obj);
    }

    public Object load(Class target, Serializable id) {
        Object obj = getHibernateTemplate().get(target, id);
        if (obj == null) {
            throw new ObjectRetrievalFailureException(target, id);
        }
        return obj;
    }

    public List findAll(Class target) {
        return getHibernateTemplate().loadAll(target);
    }

    public void remove(Class target, Serializable id) {
        getHibernateTemplate().delete(load(target, id));
    }
}
