package com.kescom.matrix.core.db.hibernate;

import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.kescom.matrix.core.db.IIndex;
import com.kescom.matrix.core.db.IMultiIndexedCollection;

public class HibernateMultiIndexedCollection<T extends IIndex> extends HibernateIndexedCollection<T> implements IMultiIndexedCollection<T> {

    @SuppressWarnings("unchecked")
    public Collection<T> select(String indexName, IIndex indexValue) {
        return select(indexName, (Object) indexValue);
    }

    @SuppressWarnings("unchecked")
    public Collection<T> select(String indexName, Object indexValue) {
        Session session = sessionManager.peekThreadSession().getSession();
        Criteria criteria = session.createCriteria(clazz).add(Restrictions.eq(indexName, indexValue));
        if (orderField != null) criteria = criteria.addOrder(Order.asc(orderField));
        List<T> list = criteria.list();
        return list;
    }

    @SuppressWarnings("unchecked")
    public Collection<T> select(String indexName, String indexValue) {
        return select(indexName, (Object) indexValue);
    }

    @SuppressWarnings("unchecked")
    public Collection<T> select2(String indexName, String indexValue, String indexName2, String indexValue2) {
        Session session = sessionManager.peekThreadSession().getSession();
        String hql = "from " + clazz.getName() + " as x where " + indexName + "=? and " + indexName2 + "=?";
        if (orderField != null) hql = hql + " order by " + orderField;
        List<T> list = session.createQuery(hql).setString(0, indexValue).setString(1, indexValue2).list();
        return list;
    }

    @SuppressWarnings("unchecked")
    public Collection<T> select2(String indexName, IIndex indexValue, String indexName2, IIndex indexValue2) {
        Session session = sessionManager.peekThreadSession().getSession();
        String hql = "from " + clazz.getName() + " as x where " + indexName + "=? and " + indexName2 + "=?";
        if (orderField != null) hql = hql + " order by " + orderField;
        List<T> list = session.createQuery(hql).setEntity(0, indexValue).setEntity(1, indexValue2).list();
        return list;
    }

    public T selectSingle(String indexName, IIndex indexValue) {
        Collection<T> result = select(indexName, indexValue);
        if (result != null && result.size() > 0) return result.iterator().next(); else return null;
    }

    public T selectSingle(String indexName, String indexValue) {
        Collection<T> result = select(indexName, indexValue);
        if (result != null && result.size() > 0) return result.iterator().next(); else return null;
    }
}
