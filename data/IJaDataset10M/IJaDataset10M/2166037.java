package fi.arcusys.acj.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import fi.arcusys.acj.dao.GenericDao;
import fi.arcusys.acj.dao.Projection;
import fi.arcusys.acj.util.ListOptions;
import fi.arcusys.acj.util.ListResult;
import fi.arcusys.acj.util.search.SearchExpression;
import fi.arcusys.acj.util.search.StringMatchMode;

/**
 * Default implementation of {@link HibernateEntityDao} interface.
 * 
 * <p>This is implemented as a wrapper to {@link DefaultHibernateGenericDao}.</p>
 * 
 * @author mikko
 * @version 1.0 $Rev: 1595 $
 *
 */
public class DefaultHibernateEntityDao<EntityClass> implements HibernateEntityDao<EntityClass> {

    private Class<EntityClass> entityClass;

    private DefaultHibernateGenericDao genericDao;

    /**
	 * Construct a new instance for the specified entity class.
	 * 
	 * <p>The constructed instance is not yet bound to a 
	 * {@link SessionFactory} and it can not be used before method
	 * {@link #setSessionFactory(SessionFactory)} is called.</p>
	 * 
	 * @param entityClass the entity class
	 */
    public DefaultHibernateEntityDao(Class<EntityClass> entityClass) {
        this(entityClass, null);
    }

    /**
	 * Construct a new instance for the specified entity class and a
	 * {@link SessionFactory} instance.
	 * 
	 * @param entityClass the entity class
	 * @param sessionFactory the {@code SessionFactory}
	 */
    public DefaultHibernateEntityDao(Class<EntityClass> entityClass, SessionFactory sessionFactory) {
        if (null == entityClass) {
            throw new NullPointerException("entityClass is null");
        }
        this.entityClass = entityClass;
        this.genericDao = new DefaultHibernateGenericDao(sessionFactory);
    }

    public GenericDao toGenericDao() {
        return new DefaultHibernateGenericDao(getSessionFactory());
    }

    public boolean isBoundDao() {
        return true;
    }

    public boolean isGenericDao() {
        return false;
    }

    public boolean supportsBoundDao() {
        return true;
    }

    public boolean supportsGenericDao() {
        return true;
    }

    public Class<EntityClass> getEntityClass() {
        return entityClass;
    }

    /**
	 * Return the configured {@link SessionFactory} instance.
	 * @return the {@code SessionFactory}
	 */
    public SessionFactory getSessionFactory() {
        return genericDao.getSessionFactory();
    }

    /**
	 * Set the {@link SessionFactory} instance.
	 * @param sessionFactory the {@code SessionFactory} to set
	 */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.genericDao.setSessionFactory(sessionFactory);
    }

    /**
	 * Return the <em>current</em> Hibernate {@link Session}.
	 * 
	 * <p>Default implementation of this method calls
	 * {@link SessionFactory#getCurrentSession()} for the current
	 * {@code sessionFactory} and returns its return value.</p>
	 * 
	 * @return the current {@code Session0}
	 * @throws NullPointerException if {@code sessionFactory} is {@code null}
	 */
    public Session getCurrentSession() {
        Session session;
        SessionFactory sf = getSessionFactory();
        if (null == sf) {
            throw new NullPointerException("sessionFactory is null");
        }
        session = sf.getCurrentSession();
        return session;
    }

    public Session getCurrentSession(boolean nullIsError) {
        Session session = getCurrentSession();
        if (null == session && nullIsError) {
            throw new RuntimeException("currentSession is null");
        }
        return session;
    }

    public Query createQuery(String queryString, ListOptions listOptions) {
        return createQuery(queryString, listOptions, getCurrentSession());
    }

    public Query createQuery(String queryString, ListOptions listOptions, Session session) {
        if (null == queryString) {
            throw new NullPointerException("queryString is null");
        }
        if (null == session) {
            throw new NullPointerException("session is null");
        }
        StringBuilder sb = new StringBuilder(queryString);
        String orderBy = DefaultHibernateGenericDao.createOrderByString(listOptions);
        if (orderBy.length() > 0) {
            sb.append(" ");
            sb.append(orderBy);
        }
        queryString = sb.toString();
        Query q = session.createQuery(queryString);
        if (null != listOptions) {
            q.setFirstResult(listOptions.getFirstResult());
            q.setMaxResults(listOptions.getMaxResults());
        }
        return q;
    }

    public Criteria createCriteria() {
        return createCriteria(getCurrentSession());
    }

    public Criteria createCriteria(Session session) {
        if (null == session) {
            throw new NullPointerException("session is null");
        }
        return session.createCriteria(getEntityClass());
    }

    public <T> HibernateEntityDao<T> newHibernateEntityDao(Class<T> entityClass) {
        return new DefaultHibernateEntityDao<T>(entityClass, getSessionFactory());
    }

    public EntityClass get(Object primaryKey) {
        return genericDao.get(getEntityClass(), primaryKey);
    }

    public EntityClass get(Object primaryKey, Session session) {
        return genericDao.get(getEntityClass(), primaryKey, session);
    }

    public EntityClass findById(Object id) {
        return genericDao.findById(getEntityClass(), id);
    }

    public EntityClass findById(Object id, Session session) {
        return genericDao.findById(getEntityClass(), id, session);
    }

    /**
	 * Perform an <em>unchecked cast</em> on the specified <em>object</em>.
	 * @return the object cast to the specified target class
	 */
    @SuppressWarnings("unchecked")
    public static <T> T uncheckedCast(Class<? extends T> targetClass, Object object) {
        return (T) object;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<? extends T> uncheckedCast(Class<T> targetClass, List<?> list) {
        return (List<T>) list;
    }

    public ListResult<EntityClass> list(ListOptions listOptions) {
        return genericDao.list(getEntityClass(), listOptions);
    }

    public ListResult<EntityClass> list(ListOptions listOptions, Session session) {
        return genericDao.list(getEntityClass(), listOptions, session);
    }

    public int count() {
        return genericDao.count(getEntityClass());
    }

    public int count(Session session) {
        return genericDao.count(getEntityClass(), session);
    }

    public ListResult<EntityClass> findByPropertyEq(String propertyName, Object propertyValue, ListOptions listOptions) {
        return genericDao.findByPropertyEq(getEntityClass(), propertyName, propertyValue, listOptions);
    }

    public EntityClass findUniqueByPropertyEq(String propertyName, Object propertyValue) {
        return genericDao.findUniqueByPropertyEq(getEntityClass(), propertyName, propertyValue);
    }

    public ListResult<EntityClass> findByPropertyEq(String propertyName, Object propertyValue, ListOptions listOptions, Session session) {
        return genericDao.findByPropertyEq(getEntityClass(), propertyName, propertyValue, listOptions, session);
    }

    public ListResult<EntityClass> findByPropertyLike(String propertyName, String propertyValue, boolean ignoreCase, StringMatchMode matchMode, ListOptions listOptions) {
        return genericDao.findByPropertyLike(getEntityClass(), propertyName, propertyValue, ignoreCase, matchMode, listOptions);
    }

    public ListResult<EntityClass> findByPropertyLike(String propertyName, String propertyValue, boolean ignoreCase, StringMatchMode matchMode, ListOptions listOptions, Session session) {
        return genericDao.findByPropertyLike(getEntityClass(), propertyName, propertyValue, ignoreCase, matchMode, listOptions, session);
    }

    public ListResult<EntityClass> findByExpression(SearchExpression expression, ListOptions listOptions) {
        return genericDao.findByExpression(getEntityClass(), expression, listOptions);
    }

    public ListResult<EntityClass> findByExpression(SearchExpression expression, ListOptions listOptions, Session session) {
        return genericDao.findByExpression(getEntityClass(), expression, listOptions, session);
    }

    public Object selectByExpression(Projection projection, SearchExpression expression, ListOptions listOptions) {
        return genericDao.selectByExpression(getEntityClass(), projection, expression, listOptions);
    }

    public Object selectByExpression(Projection projection, SearchExpression expression, ListOptions listOptions, Session session) {
        return genericDao.selectByExpression(getEntityClass(), projection, expression, listOptions, session);
    }

    public int countByExpression(SearchExpression expression) {
        return genericDao.countByExpression(getEntityClass(), expression);
    }

    public int countByExpression(SearchExpression expression, Session session) {
        return genericDao.countByExpression(getEntityClass(), expression, session);
    }

    public void delete(EntityClass entity, Session session) {
        genericDao.delete(entity, session);
    }

    public void delete(EntityClass entity) {
        genericDao.delete(entity);
    }

    public void refresh(EntityClass entity, Session session) {
        genericDao.refresh(entity, session);
    }

    public void refresh(EntityClass entity) {
        genericDao.refresh(entity);
    }

    public EntityClass merge(EntityClass entity, Session session) {
        return genericDao.merge(entity, session);
    }

    public EntityClass merge(EntityClass entity) {
        return genericDao.merge(entity);
    }

    public void persist(EntityClass entity, Session session) {
        genericDao.persist(entity, session);
    }

    public EntityClass persist(EntityClass entity) {
        return genericDao.persist(entity);
    }

    public Serializable save(EntityClass entity, Session session) {
        return genericDao.save(entity, session);
    }

    public Serializable save(EntityClass entity) {
        return genericDao.save(entity);
    }

    public Serializable saveOrUpdate(EntityClass entity, Session session) {
        return genericDao.saveOrUpdate(entity, session);
    }

    public Serializable saveOrUpdate(EntityClass entity) {
        return genericDao.saveOrUpdate(entity);
    }

    public void update(EntityClass entity, Session session) {
        genericDao.update(entity, session);
    }

    public void update(EntityClass entity) {
        genericDao.update(entity);
    }

    public List<EntityClass> findByExpression(SearchExpression expression) {
        return genericDao.findByExpression(getEntityClass(), expression);
    }

    public List<EntityClass> findByPropertyEq(String propertyName, Object propertyValue) {
        return genericDao.findByPropertyEq(getEntityClass(), propertyName, propertyValue);
    }

    public List<EntityClass> findByPropertyLike(String propertyName, String propertyValue, boolean ignoreCase, StringMatchMode matchMode) {
        return genericDao.findByPropertyLike(getEntityClass(), propertyName, propertyValue, ignoreCase, matchMode);
    }

    public List<EntityClass> list() {
        return genericDao.list(getEntityClass());
    }

    public void flush() {
        genericDao.flush();
    }

    public <TAdapter> TAdapter getAdapter(Class<TAdapter> clazz) {
        return genericDao.getAdapter(clazz);
    }
}
