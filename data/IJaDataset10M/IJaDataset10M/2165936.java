package org.primordion.busapp;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.usertype.UserCollectionType;
import org.primordion.xholon.base.XholonList;

/**
 * A custom type for Hibernate so it can use XholonList.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8 (Created on July 3, 2009)
*/
public class XholonListType implements UserCollectionType {

    static int lastInstantiationRequest = -2;

    public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister) throws HibernateException {
        return new PersistentXholonList(session);
    }

    public PersistentCollection wrap(SessionImplementor session, Object collection) {
        if (session.getEntityMode() == EntityMode.DOM4J) {
            throw new IllegalStateException("dom4j not supported");
        } else {
            return new PersistentXholonList(session, (List) collection);
        }
    }

    public Iterator getElementsIterator(Object collection) {
        return ((List) collection).iterator();
    }

    public boolean contains(Object collection, Object entity) {
        return ((List) collection).contains(entity);
    }

    public Object indexOf(Object collection, Object entity) {
        int l = ((List) collection).indexOf(entity);
        if (l < 0) {
            return null;
        } else {
            return new Integer(l);
        }
    }

    public Object replaceElements(Object original, Object target, CollectionPersister persister, Object owner, Map copyCache, SessionImplementor session) throws HibernateException {
        List result = (List) target;
        result.clear();
        result.addAll((XholonList) original);
        return result;
    }

    public Object instantiate(int anticipatedSize) {
        lastInstantiationRequest = anticipatedSize;
        return new XholonList();
    }
}
