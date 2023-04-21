package com.m4f.utils.i18n.dao.impl.jdo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.m4f.business.domain.BaseEntity;
import com.m4f.business.domain.ifc.Taggeable;
import com.m4f.business.persistence.PMF;
import com.m4f.utils.dao.GaeFilter;
import com.m4f.utils.i18n.annotations.DeleteMultilanguage;
import com.m4f.utils.i18n.dao.ifc.I18nDAOSupport;

public class JdoI18nDAO implements I18nDAOSupport {

    private static final Logger LOGGER = Logger.getLogger(JdoI18nDAO.class.getName());

    public <T extends BaseEntity> T createInstance(Class<T> clazz) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        return pm.newInstance(clazz);
    }

    public <T extends BaseEntity> List<Long> getAllIds(Class<T> clazz, String ordering, String filter, String params, Object[] values) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = null;
        if (filter != null && !("").equals(filter)) {
            q = pm.newQuery("select id from " + clazz.getName() + " where " + filter);
            q.declareParameters(params);
        } else {
            q = pm.newQuery("select id from " + clazz.getName());
        }
        this.setOrdering(q, ordering);
        List<Long> ids = null;
        if (filter != null && !("").equals(filter)) {
            ids = (List<Long>) q.executeWithArray(values);
        } else {
            ids = (List<Long>) q.execute();
        }
        return ids;
    }

    public <T extends BaseEntity> List<Long> getAllIds(Class<T> clazz, String ordering, String filter, String params, Object[] values, int init, int end) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = null;
        if (filter != null && !("").equals(filter)) {
            q = pm.newQuery("select id from " + clazz.getName(), filter);
            q.declareParameters(params);
        } else {
            q = pm.newQuery("select id from " + clazz.getName());
        }
        this.setOrdering(q, ordering);
        if ((init < end) && (init >= 0)) {
            q.setRange(init, end);
        }
        List<Long> ids = null;
        if (filter != null && !("").equals(filter)) {
            ids = (List<Long>) q.executeWithArray(values);
        } else {
            ids = (List<Long>) q.execute();
        }
        return ids;
    }

    public void saveOrUpdate(BaseEntity entity, Locale locale) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(entity);
        } catch (Exception e) {
            throw e;
        } finally {
            pm.flush();
            pm.close();
        }
    }

    public <T extends BaseEntity> void saveOrUpdateCollection(Collection<T> entities, Locale locale) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistentAll(entities);
        } catch (Exception e) {
            throw e;
        } finally {
            pm.flush();
            pm.close();
        }
    }

    @DeleteMultilanguage
    public void delete(BaseEntity entity, Locale locale) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.deletePersistent(entity);
        } catch (Exception e) {
            throw e;
        } finally {
            pm.flush();
            pm.close();
        }
    }

    public <T extends BaseEntity> void delete(Collection<T> objs, Locale locale) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.deletePersistentAll(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            pm.flush();
            pm.close();
        }
    }

    public <T extends BaseEntity> void erasure(Class<T> clazz) throws Exception {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query(clazz.getSimpleName());
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            datastore.delete(result.getKey());
        }
    }

    public <T extends BaseEntity> T findById(Class<T> clazz, Locale locale, Long id) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        T obj = null, detached = null;
        try {
            obj = pm.getObjectById(clazz, id);
            detached = pm.detachCopy(obj);
        } catch (Exception e) {
            throw e;
        } finally {
            pm.close();
        }
        return detached;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> findAll(Class<T> clazz, Locale locale, String ordering) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(clazz);
        this.setOrdering(query, ordering);
        List<T> results = new ArrayList<T>(), detached = new ArrayList<T>();
        try {
            results.addAll((List<T>) query.execute());
            detached = (List<T>) pm.detachCopyAll(results);
        } catch (Exception e) {
            throw e;
        } finally {
            query.closeAll();
            pm.close();
        }
        return detached;
    }

    /**
	 * Find an entity of a given class using a filter.
	 * @param <T> type of entity to look up.
	 * @param entityClass class of entity to be found.
	 * @param filter (JDOQL) filter to apply to entity lookup.
	 * @param params declarations of parameters in filter string.
	 * @param values values of parameters (in order of appearance in
	 * 	'params' string).
	 * @return entity or <code>null</code> if no entity of the provided class
	 * 	exists matching the provided filter. 
	 */
    public <T extends BaseEntity> T findEntity(Class<T> entityClass, Locale locale, String filter, String params, Object[] values) {
        Collection<T> entities = findEntities(entityClass, locale, filter, params, values, null);
        if (entities.isEmpty()) return null;
        return entities.iterator().next();
    }

    /**
	 * Find all entities of a given class in a given order, matching a filter.
	 * @param <T> type of entity to look up.
	 * @param entityClass class of entities to be found.
	 * @param filter (JDOQL) filter to apply to entity lookup.
	 * @param params declarations of parameters in filter string.
	 * @param values values of parameters (in order of appearance in
	 * 	'params' string).
	 * @param ordering ordering to be applied to results (can be <code>null</code>).
	 * @return entity or <code>null</code> if no entity of the provided class
	 * 	exists matching the provided filter. 
	 */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> Collection<T> findEntities(Class<T> entityClass, Locale locale, String filter, String params, Object[] values, String ordering) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(entityClass, filter);
        query.declareParameters(params);
        this.setOrdering(query, ordering);
        Collection<T> entities = (Collection<T>) query.executeWithArray(values);
        Collection<T> detached = pm.detachCopyAll(entities);
        pm.close();
        return detached;
    }

    public <T extends BaseEntity> long count(Class<T> entityClass, Map<String, Object> propertyMap) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query(entityClass.getSimpleName());
        for (String propertyName : propertyMap.keySet()) {
            q.addFilter(propertyName, com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, propertyMap.get(propertyName));
        }
        PreparedQuery pq = datastore.prepare(q);
        return pq.countEntities(FetchOptions.Builder.withLimit(100000));
    }

    public <T extends BaseEntity> long count(Class<T> entityClass, List<GaeFilter> filters) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query(entityClass.getSimpleName());
        if (filters != null) {
            for (GaeFilter filter : filters) {
                q.addFilter(filter.getField(), filter.getOperator(), filter.getValue());
            }
        }
        PreparedQuery pq = datastore.prepare(q);
        return pq.countEntities(FetchOptions.Builder.withLimit(100000));
    }

    public <T extends BaseEntity> long count(Class<T> entityClass) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query(entityClass.getSimpleName());
        PreparedQuery pq = datastore.prepare(q);
        return pq.countEntities(FetchOptions.Builder.withLimit(100000));
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> Collection<T> findEntitiesByRange(Class<T> entityClass, Locale locale, int init, int end, String ordering) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(entityClass);
        this.setOrdering(query, ordering);
        if ((init < end) && (init >= 0)) {
            query.setRange(init, end);
        }
        Collection<T> entities = (Collection<T>) query.execute();
        Collection<T> detached = pm.detachCopyAll(entities);
        pm.close();
        return detached;
    }

    @Override
    public <T extends BaseEntity> Collection<T> findEntitiesByRange(Class<T> entityClass, Locale locale, String filter, String params, Object[] values, int init, int end, String ordering) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(entityClass, filter);
        query.declareParameters(params);
        this.setOrdering(query, ordering);
        if ((init < end) && (init >= 0)) {
            query.setRange(init, end);
        }
        Collection<T> entities = (Collection<T>) query.executeWithArray(values);
        Collection<T> detached = pm.detachCopyAll(entities);
        pm.close();
        return detached;
    }

    @Override
    public Collection<Category> getCategories(Class<? extends Taggeable> clazz, String fieldName, Locale locale) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        StringBuffer sb = new StringBuffer("select ").append(fieldName).append(" from ").append(clazz.getName());
        Query q = pm.newQuery(sb.toString());
        Collection<Category> tags = (Collection<Category>) q.execute();
        q.closeAll();
        return tags;
    }

    @Override
    public <T extends BaseEntity> Collection<T> findEntitiesByIds(Class<T> entityClass, Locale locale, String idField, List<Long> ids, int init, int end, String ordering) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        new StringBuffer("select from ").append(entityClass.getName()).append(" where ").append(":ids.contains(").append(idField).append(")");
        Collection<T> objs = new ArrayList<T>();
        Query query = pm.newQuery(entityClass, ":ids.contains(" + idField + ")");
        if ((ids != null) && (ids.size() > 0)) {
            objs.addAll((Collection<T>) query.execute(ids));
        }
        this.setOrdering(query, ordering);
        if ((init < end) && (init >= 0)) {
            query.setRange(init, end);
        }
        Collection<T> detached = pm.detachCopyAll(objs);
        pm.close();
        return detached;
    }

    public <T extends BaseEntity> long countByIds(Class<T> entityClass, String idField, List<Long> ids) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query(entityClass.getSimpleName());
        q.addFilter(idField, com.google.appengine.api.datastore.Query.FilterOperator.IN, ids);
        PreparedQuery pq = datastore.prepare(q);
        return pq.countEntities(FetchOptions.Builder.withLimit(100000));
    }

    private void setOrdering(Query query, String ordering) {
        if (ordering != null && !("").equals(ordering)) {
            StringBuffer sb = new StringBuffer();
            String[] orderParams = ordering.split("[,]");
            for (String param : orderParams) {
                if (!param.startsWith("-")) sb.append(param).append(" ascending"); else sb.append(param.substring(1)).append(" descending");
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            query.setOrdering(sb.toString());
        }
    }
}
