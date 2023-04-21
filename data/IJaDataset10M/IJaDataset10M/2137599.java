package net.sf.grudi.persistence.dao;

import java.io.Serializable;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sf.grudi.model.vo.VO;
import net.sf.grudi.persistence.PersistencePlugin;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;

public abstract class AbstractDAO<T extends VO, S> {

    /**
	 * Return the ID of the configuration that this DAO will be use.
	 * 
	 * @return configuration ID (extension point)
	 */
    protected abstract String getConfigurationId();

    /**
	 * Return the entity class to use into Criteria and others that
	 * depends of an object Class.
	 * 
	 * @return class of the entity, like T parameter
	 */
    protected abstract Class<T> getEntityClass();

    /**
	 * This method is implemented to the correct implementation
	 * of the SearchVO to this DAO.
	 * 
	 * @param searchVO
	 * @return list of VO using the given SearchVO
	 */
    public abstract List<T> list(S searchVO);

    /**
	 * Find the Hibernate session based on the getConfigurationID().
	 * 
	 * @return unique session to the configuration
	 */
    private final Session openSession() {
        QualifiedName qualifiedName = new QualifiedName(PersistencePlugin.PLUGIN_ID, getConfigurationId());
        try {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            SessionFactory sessionFactory = (SessionFactory) root.getSessionProperty(qualifiedName);
            return sessionFactory.openSession();
        } catch (CoreException e) {
            Status status = new Status(IStatus.ERROR, PersistencePlugin.PLUGIN_ID, "Error during load configurations for Hibernate, please check ID", e);
            PersistencePlugin.getDefault().getLog().log(status);
            return null;
        }
    }

    private final void closeSession(Session session) {
        session.close();
    }

    /**
	 * Save or update an entity to a database.
	 * 
	 * @param entity
	 */
    public void saveOrUpdate(final T entity) {
        executeTransaction(new AbstractTransaction() {

            @Override
            public void execute(Session session) {
                session.saveOrUpdate(entity);
            }
        });
    }

    /**
	 * Delete the given entity from database;
	 * 
	 * @param entity
	 */
    public void delete(final Serializable entity) {
        executeTransaction(new AbstractTransaction() {

            @Override
            public void execute(Session session) {
                session.delete(entity);
            }
        });
    }

    /**
	 * List entity based in a Criterion.
	 * 
	 * @param criterion to query
	 */
    @SuppressWarnings("unchecked")
    protected List<T> list(final Criterion criterion) {
        return executeQuery(new AbstractQuery<T>() {

            @Override
            public List<T> execute(Session session) {
                Criteria criteria = session.createCriteria(getEntityClass());
                criteria.add(criterion);
                List<T> list = new ArrayList<T>();
                for (Object o : criteria.list()) {
                    list.add((T) o);
                }
                return list;
            }
        });
    }

    /**
	 * List all entities to this DAO.
	 * 
	 * @return list of all entities
	 */
    @SuppressWarnings("unchecked")
    public List<T> listAll() {
        return executeQuery(new AbstractQuery<T>() {

            @Override
            public List<T> execute(Session session) {
                Criteria criteria = session.createCriteria(getEntityClass());
                List<T> list = new ArrayList<T>();
                for (Object o : criteria.list()) {
                    list.add((T) o);
                }
                return list;
            }
        });
    }

    public void executeTransaction(AbstractTransaction transaction) {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        try {
            transaction.execute(session);
            tx.commit();
            session.flush();
        } catch (Exception e) {
            if (e instanceof BatchUpdateException) {
                PersistencePlugin.getDefault().getLog().log(new Status(IStatus.ERROR, PersistencePlugin.PLUGIN_ID, "Error during save to database.", ((BatchUpdateException) e).getNextException()));
            }
            PersistencePlugin.getDefault().getLog().log(new Status(IStatus.ERROR, PersistencePlugin.PLUGIN_ID, "Error during save to database.", e));
            tx.rollback();
        } finally {
            closeSession(session);
        }
    }

    public List<T> executeQuery(AbstractQuery<T> query) {
        List<T> list = Collections.emptyList();
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        try {
            list = query.execute(session);
            tx.commit();
            session.flush();
        } catch (Exception e) {
            if (e instanceof BatchUpdateException) {
                PersistencePlugin.getDefault().getLog().log(new Status(IStatus.ERROR, PersistencePlugin.PLUGIN_ID, "Error during save to database.", ((BatchUpdateException) e).getNextException()));
            }
            tx.rollback();
        } finally {
            closeSession(session);
        }
        return list;
    }

    public T executeUniqueResult(AbstractUniqueResult<T> query) {
        T result = null;
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        try {
            result = query.executeUniqueResult(session);
            tx.commit();
            session.flush();
        } catch (Exception e) {
            if (e instanceof BatchUpdateException) {
                PersistencePlugin.getDefault().getLog().log(new Status(IStatus.ERROR, PersistencePlugin.PLUGIN_ID, "Error during execute unique result in database.", ((BatchUpdateException) e).getNextException()));
            }
            tx.rollback();
        } finally {
            closeSession(session);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public T load(long id) {
        Session session = openSession();
        T entity = (T) session.load(getEntityClass(), id);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public List sqlQuery(String sql) {
        Object result = null;
        Session session = openSession();
        try {
            result = session.createSQLQuery(sql).list();
        } finally {
            closeSession(session);
        }
        return (List) result;
    }
}
