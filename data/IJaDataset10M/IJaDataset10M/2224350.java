package net.sf.iauthor.core;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * @author Andreas Beckers
 * 
 */
public class Session {

    private EntityManagerFactory _emf;

    private EntityManager _em;

    private final String _dbDir;

    public Session(String dbDir) {
        _dbDir = dbDir;
    }

    public void start() {
        _emf = Persistence.createEntityManagerFactory(_dbDir + "/iauthor.odb");
        _em = _emf.createEntityManager();
    }

    public void persist(Object... objects) {
        boolean active = _em.getTransaction().isActive();
        if (!active) beginTransaction();
        for (Object object : objects) {
            _em.persist(object);
        }
        if (!active) commitTransaction();
    }

    /**
	 * 
	 */
    public void commitTransaction() {
        _em.getTransaction().commit();
    }

    /**
	 * 
	 */
    public void beginTransaction() {
        _em.getTransaction().begin();
    }

    public <T> T findById(Class<T> type, String id) {
        return _em.find(type, id);
    }

    public <T> List<T> findAll(Class<T> type) {
        List<T> result = _em.createQuery("select e from " + type.getName() + " e", type).getResultList();
        return result;
    }

    public <T> T merge(T obj) {
        beginTransaction();
        T r = _em.merge(obj);
        commitTransaction();
        return r;
    }

    public void stop() {
        _em.close();
        _emf.close();
    }

    /**
	 * @param p
	 */
    public void detach(Object p) {
        _em.detach(p);
    }

    /**
	 * @param class1
	 * @param string
	 * @param scene
	 * @return
	 */
    public <T> T findByProperty(Class<T> type, String prop, Object val) {
        String jql = "select e from " + type.getName() + " e where e." + prop + " = :" + prop;
        TypedQuery<T> q = _em.createQuery(jql, type);
        q.setParameter(prop, val);
        List<T> l = q.getResultList();
        return l.isEmpty() ? null : l.get(0);
    }

    public <T> List<T> findAllByProperty(Class<T> type, String prop, Object val) {
        String jql = "select e from " + type.getName() + " e where e." + prop + " = :" + prop;
        TypedQuery<T> q = _em.createQuery(jql, type);
        q.setParameter(prop, val);
        return q.getResultList();
    }
}
