package org.vardb.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;
import org.vardb.CVardbException;

@Repository
public abstract class CAbstractTempSessionDaoImpl extends CAbstractDaoImpl {

    public static final int BATCH_SIZE = 50;

    protected SessionFactory sessionFactory;

    @Required
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected StatelessSession getStatelessSession() {
        return this.sessionFactory.openStatelessSession();
    }

    @Override
    public Object load(Class<?> cls, Serializable id) {
        throw new CVardbException("load is not implemented in StatelessSession");
    }

    @Override
    public Object get(Class<?> cls, Serializable id) {
        StatelessSession session = getStatelessSession();
        try {
            return session.get(cls, id);
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Object obj) {
        StatelessSession session = getStatelessSession();
        try {
            Transaction tx = session.beginTransaction();
            session.insert(obj);
            tx.commit();
        } finally {
            session.close();
        }
    }

    @Override
    public void saveOrUpdate(Object obj) {
        throw new CVardbException("saveOrUpdate is not implemented in StatelessSession");
    }

    @Override
    public void update(Object obj) {
        StatelessSession session = getStatelessSession();
        try {
            Transaction tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Object obj) {
        StatelessSession session = getStatelessSession();
        try {
            session.delete(obj);
        } finally {
            session.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List find(String sql) {
        StatelessSession session = getStatelessSession();
        try {
            Query query = session.createQuery(sql);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List findByNamedParam(String sql, Map<String, Object> map) {
        StatelessSession session = getStatelessSession();
        try {
            Query query = session.createQuery(sql);
            CDatabaseHelper.setParams(query, map);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public void saveOrUpdateAll(Collection<? extends Object> list) {
        throw new CVardbException("saveOrUpdateAll is not implemented in StatelessSession");
    }

    @Override
    public void deleteAll(Collection<? extends Object> list) {
        StatelessSession session = getStatelessSession();
        try {
            for (Object obj : list) {
                session.delete(obj);
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void clear() {
    }
}
