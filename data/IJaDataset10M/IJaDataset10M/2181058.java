package net.sf.sail.webapp.dao.impl;

import java.io.Serializable;
import java.util.List;
import net.sf.sail.webapp.dao.ObjectNotFoundException;
import net.sf.sail.webapp.dao.SimpleDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Cynick Young
 * 
 * @version $Id: AbstractHibernateDao.java 1956 2008-06-17 23:16:02Z hiroki $
 * 
 */
public abstract class AbstractHibernateDao<T> extends HibernateDaoSupport implements SimpleDao<T> {

    /**
	 * @see net.sf.sail.webapp.dao.SimpleDao#delete(java.lang.Object)
	 */
    public void delete(T object) {
        this.getHibernateTemplate().delete(object);
    }

    /**
	 * @see net.sf.sail.webapp.dao.SimpleDao#save(java.lang.Object)
	 */
    public void save(T object) {
        this.getHibernateTemplate().saveOrUpdate(object);
    }

    /**
	 * @see net.sf.sail.webapp.dao.SimpleDao#getList()
	 */
    @SuppressWarnings("unchecked")
    public List<T> getList() {
        return this.getHibernateTemplate().find(this.getFindAllQuery());
    }

    /**
	 * Gets a string that will perform a query to retrieve all available objects
	 * from the persistent data store.
	 * 
	 * @return <code>String</code> query
	 */
    protected abstract String getFindAllQuery();

    /**
	 * @see net.sf.sail.webapp.dao.SimpleDao#getById(java.lang.Integer)
	 */
    @SuppressWarnings("unchecked")
    public T getById(Serializable id) throws ObjectNotFoundException {
        T object = null;
        try {
            object = (T) this.getHibernateTemplate().get(this.getDataObjectClass(), Long.valueOf(id.toString()));
        } catch (NumberFormatException e) {
            return null;
        }
        if (object == null) throw new ObjectNotFoundException((Long) id, this.getDataObjectClass());
        return object;
    }

    /**
	 * Gets the class of the persistent entity.
	 * 
	 * @return the Class
	 */
    protected abstract Class<? extends T> getDataObjectClass();
}
