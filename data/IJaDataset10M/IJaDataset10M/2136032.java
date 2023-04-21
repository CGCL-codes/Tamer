package com.malethan.seemorej.hibernate.dao;

import com.malethan.seemorej.dao.GenericDao;
import com.malethan.seemorej.dao.ValidationMessage;
import com.malethan.seemorej.hibernate.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import java.io.Serializable;
import java.util.List;

public class GenericDaoHibernate<T, PK extends Serializable> implements GenericDao<T, PK> {

    protected final Log log = LogFactory.getLog(getClass());

    private Class<T> persistentClass;

    protected Session getSession() {
        return HibernateUtil.currentSession();
    }

    public GenericDaoHibernate(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    @SuppressWarnings("unchecked")
    public List<T> find() {
        return find(-1, -1);
    }

    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table. If offset and limit
     * are > -1 then a subset of the results are returned.
     *
     * @param offset the 0 based index of the first result
     * @param limit  the max number of records to return
     * @return a subset of all results
     */
    @SuppressWarnings({ "unchecked" })
    public List<T> find(int offset, int limit) {
        Query query = getSession().createQuery("FROM " + this.persistentClass.getName());
        if (offset > -1 && limit > -1) {
            query.setFirstResult(offset);
            query.setMaxResults(limit);
        }
        return query.list();
    }

    /**
     * Counts the total number of records for the given entity.
     *
     * @return total number of records
     */
    public Long count() {
        return (Long) getSession().createQuery("select count(obj) FROM " + this.persistentClass.getName() + " obj").uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public T load(PK id) {
        return (T) getSession().load(this.persistentClass, id);
    }

    @SuppressWarnings("unchecked")
    public boolean exists(PK id) {
        T entity = (T) getSession().get(this.persistentClass, id);
        return entity != null;
    }

    @SuppressWarnings("unchecked")
    public T save(T object) {
        getSession().beginTransaction();
        Object obj = getSession().merge(object);
        getSession().getTransaction().commit();
        getSession().flush();
        return (T) obj;
    }

    public T saveOrUpdate(T object) {
        getSession().beginTransaction();
        getSession().saveOrUpdate(object);
        getSession().getTransaction().commit();
        getSession().flush();
        return object;
    }

    public void remove(PK id) {
        getSession().beginTransaction();
        getSession().delete(this.load(id));
        getSession().getTransaction().commit();
    }

    public void remove(T object) {
        getSession().beginTransaction();
        getSession().delete(object);
        getSession().getTransaction().commit();
    }

    public ValidationMessage[] validate(T object) {
        ClassValidator validator = new ClassValidator<T>(this.persistentClass);
        InvalidValue[] invalidValues = validator.getInvalidValues(object);
        ValidationMessage[] messages = new ValidationMessage[invalidValues.length];
        for (int i = 0; i < invalidValues.length; i++) {
            InvalidValue value = invalidValues[i];
            messages[i] = new ValidationMessageHibernate(value);
        }
        return messages;
    }
}
