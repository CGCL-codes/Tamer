package net.asfun.jvalog.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import net.asfun.jvalog.entity.Model;
import net.asfun.jvalog.frame.JdoFactory;
import static net.asfun.jvalog.common.log.logger;

public class Jdoer<M> {

    private PersistenceManager pm;

    private Class<M> clazz = null;

    protected Jdoer(Class<M> clazz) {
        this.clazz = clazz;
    }

    public void save(Model model) {
        pm = JdoFactory.open();
        pm.makePersistent(model);
    }

    public M load(Object id) {
        try {
            pm = JdoFactory.open();
            return pm.getObjectById(clazz, id);
        } catch (JDOObjectNotFoundException ex) {
            logger.warning(ex.getMessage());
            return null;
        }
    }

    public void remove(Object id) {
        try {
            pm = JdoFactory.open();
            pm.deletePersistent(pm.getObjectById(clazz, id));
        } catch (JDOObjectNotFoundException ex) {
            logger.info("No " + clazz.getName() + " with id " + id + " exist to be deleted.");
        }
    }

    public Query prepareQuery() {
        pm = JdoFactory.open();
        return pm.newQuery(clazz);
    }

    public M unique() {
        pm = JdoFactory.open();
        return unique(pm.newQuery(clazz));
    }

    @SuppressWarnings("unchecked")
    public M unique(String condition) {
        Query q;
        pm = JdoFactory.open();
        if (condition == null || "".equals(condition.trim())) q = pm.newQuery(clazz); else q = pm.newQuery(clazz, condition);
        q.setUnique(true);
        try {
            return (M) q.execute();
        } catch (JDOUserException je) {
            logger.info("Deleting ..........all NOTunique items");
            q.setUnique(false);
            Collection<M> items = (Collection<M>) q.execute();
            pm.deletePersistentAll(items);
            return null;
        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage(), e.getCause());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public M unique(Query query, Object... params) {
        query.setUnique(true);
        try {
            if (params.length == 0) {
                return (M) query.execute();
            }
            return (M) query.executeWithArray(params);
        } catch (JDOUserException qe) {
            logger.info("Deleting ..........all NOTunique items");
            query.setUnique(false);
            Collection<M> items;
            if (params.length == 0) {
                items = (Collection<M>) query.execute();
            } else {
                items = (Collection<M>) query.executeWithArray(params);
            }
            pm.deletePersistentAll(items);
            return null;
        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage(), e.getCause());
            return null;
        }
    }

    public Collection<M> all() {
        pm = JdoFactory.open();
        return more(pm.newQuery(clazz));
    }

    @SuppressWarnings("unchecked")
    public Collection<M> more(String condition) {
        Query q;
        pm = JdoFactory.open();
        if (condition == null || "".equals(condition.trim())) q = pm.newQuery(clazz); else q = pm.newQuery(clazz, condition);
        try {
            return (Collection<M>) q.execute();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e.getCause());
            return new ArrayList<M>();
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<M> more(Query query, Object... params) {
        try {
            if (params.length == 0) {
                return (Collection<M>) query.execute();
            }
            return (Collection<M>) query.executeWithArray(params);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e.getCause());
            return new ArrayList<M>();
        }
    }

    public void execute(Query query, Object... params) {
        try {
            if (params.length == 0) {
                query.execute();
            } else {
                query.executeWithArray(params);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e.getCause());
        }
    }

    @SuppressWarnings("unchecked")
    public void delete(Collection pcs) {
        try {
            pm = JdoFactory.open();
            pm.deletePersistentAll(pcs);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e.getCause());
        }
    }

    public void delete(Model model) {
        try {
            pm = JdoFactory.open();
            pm.deletePersistent(model);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e.getCause());
        }
    }

    public void flush() {
        pm = JdoFactory.open();
        pm.flush();
    }
}
