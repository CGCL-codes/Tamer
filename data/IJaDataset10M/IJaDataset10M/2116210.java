package com.google.code.openperfmon.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import com.google.code.openperfmon.db.DBManager;
import com.google.code.openperfmon.domain.AbstractEntity;

public class UniversalDAO<T extends AbstractEntity> {

    private Class<T> persistentClass;

    public UniversalDAO(Class<T> cls) {
        persistentClass = cls;
    }

    @SuppressWarnings("unchecked")
    public T getById(Long id) {
        return (T) DBManager.get().get(persistentClass, id);
    }

    public T getById(long id) {
        return getById(new Long(id));
    }

    public void refresh(T obj) {
        DBManager.get().refresh(obj);
    }

    public void flush() {
        DBManager.get().flush();
    }

    @SuppressWarnings("unchecked")
    public T getByField(String fieldName, Object value) {
        return (T) DBManager.get().createCriteria(persistentClass).add(Restrictions.eq(fieldName, value)).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return (List<T>) DBManager.get().createCriteria(persistentClass).list();
    }

    @SuppressWarnings("unchecked")
    public List<T> list(int start, int count) {
        return (List<T>) DBManager.get().createCriteria(persistentClass).setFirstResult(start).setMaxResults(count).list();
    }

    public void save(final T obj) {
        DBManager.transact(new DBManager.Do() {

            public void perform(Session session) {
                Long keepId = obj.getId();
                try {
                    session.saveOrUpdate(obj);
                    session.flush();
                } catch (RuntimeException e) {
                    obj.setId(keepId);
                    throw e;
                }
            }
        });
    }

    public void remove(final T obj) {
        DBManager.transact(new DBManager.Do() {

            public void perform(Session session) {
                session.delete(obj);
            }
        });
    }

    public void removeById(long id) {
        removeById(new Long(id));
    }

    public void removeById(Long id) {
        remove(getById(id));
    }

    public List<Long> identities(List<T> ents) {
        List<Long> res = new ArrayList<Long>(ents.size());
        for (T e : ents) res.add(e.getId());
        return res;
    }

    protected Map<Long, Integer> countsByIds(List<Object[]> cnts) {
        Map<Long, Integer> cntById = new HashMap<Long, Integer>(cnts.size() * 2);
        for (Object[] o : cnts) {
            cntById.put((Long) o[0], (Integer) o[1]);
        }
        return cntById;
    }
}
