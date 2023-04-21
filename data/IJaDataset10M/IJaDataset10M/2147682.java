package org.esprit.ocm.server.dao.hibernate;

import java.util.List;
import org.apache.log4j.Logger;
import org.esprit.ocm.server.dao.common.IDao;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

/**
 * This class serves as the Base class for all other Daos - namely to hold
 * common methods that they might all use. Can be used for standard CRUD
 * operations.</p>
 * 
 */
public class BaseDaoHibernate<T> extends HibernateDAO implements IDao<T> {

    /**
	 * logger
	 */
    protected static final Logger logger = Logger.getLogger(BaseDaoHibernate.class);

    private Class<T> type;

    /**
	 * Constructeur avec le type d'objet interrog� par le DAO
	 * 
	 * @param type
	 */
    public BaseDaoHibernate(Class<T> _type) {
        this.type = _type;
    }

    public T add(T _o) {
        if (logger.isDebugEnabled()) {
            logger.debug("add object of class " + type.getName() + " : " + _o);
        }
        getCurrentSession().save(_o);
        return _o;
    }

    public T save(T _o) {
        if (logger.isDebugEnabled()) {
            logger.debug("update object of class " + type.getName() + " : " + _o);
        }
        try {
            getHibernateTemplate().saveOrUpdate(_o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _o;
    }

    @SuppressWarnings("unchecked")
    public List<T> findRestrictedList(int startPosition, int nbElements, String orderBy, String orderSens) {
        Criteria criteria = getCurrentSession().createCriteria(type);
        criteria.setFirstResult(startPosition);
        criteria.setMaxResults(nbElements);
        if (orderSens.equalsIgnoreCase("ASC")) {
            criteria.addOrder(Order.asc(orderBy));
        } else {
            criteria.addOrder(Order.desc(orderBy));
        }
        List<T> results = criteria.list();
        return results;
    }

    @SuppressWarnings("unchecked")
    public T findById(int _id) {
        if (logger.isDebugEnabled()) {
            logger.debug("find object of class " + type.getName() + " with id : " + _id);
        }
        Session session = getCurrentSession();
        T o = (T) session.get(type, _id);
        if (logger.isDebugEnabled()) {
            logger.debug("found " + o);
        }
        return o;
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        try {
            String[] sortOrders = getNaturalSortOrders();
            Criteria crit = getCurrentSession().createCriteria(type);
            if (sortOrders != null) {
                for (int i = 0; i < sortOrders.length; i++) {
                    crit.addOrder(Order.asc(sortOrders[i]));
                }
            }
            return crit.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<T> findByTypeCode(T object) {
        return findByTypeCode(object);
    }

    public int count() {
        Criteria crit = getCurrentSession().createCriteria(type);
        crit.setProjection(Projections.rowCount());
        return ((Integer) crit.list().get(0)).intValue();
    }

    public void delete(int _id) {
        if (logger.isDebugEnabled()) {
            logger.debug("delete object of class : " + type.getName() + " with id : " + _id);
        }
        getCurrentSession().delete(findById(_id));
    }

    public void delete(T object) {
        if (logger.isDebugEnabled()) {
            logger.debug("delete object of class : " + type.getName() + " : " + object);
        }
        getCurrentSession().delete(object);
    }

    public Class<T> getType() {
        return type;
    }

    /**
	 * This method could be override to give the column names used to sort the
	 * list
	 * 
	 * @return the column used to sort the list of type T
	 */
    public String[] getNaturalSortOrders() {
        return null;
    }

    @SuppressWarnings("unchecked")
    public T afficher(int id) {
        T o = (T) getCurrentSession().get(type, id);
        System.out.println(o);
        return o;
    }
}
