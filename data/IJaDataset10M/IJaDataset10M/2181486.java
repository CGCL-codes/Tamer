package jp.go.aist.six.util.castor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import jp.go.aist.six.util.BeansUtil;
import jp.go.aist.six.util.persist.Dao;
import jp.go.aist.six.util.persist.Persistable;
import jp.go.aist.six.util.persist.PersistenceException;
import jp.go.aist.six.util.search.Binding;
import jp.go.aist.six.util.search.Limit;
import jp.go.aist.six.util.search.Order;
import jp.go.aist.six.util.search.SearchCriteria;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.TimeStampable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: CastorDao.java 301 2011-03-02 07:31:03Z nakamura5akihito $
 */
public class CastorDao<K, T extends Persistable<K>> extends ExtendedCastorDaoSupport implements Dao<K, T> {

    /**
     * Logger.
     */
    private static final Logger _LOG_ = LoggerFactory.getLogger(CastorDao.class);

    private Class<? extends T> _objectType;

    private String _objectTypeName;

    private PersistenceHelper<? super T> _helper;

    private DaoRegistry _registry;

    private String _daoProfile;

    /**
     * Constructor.
     */
    public CastorDao() {
    }

    /**
     * Constructor.
     */
    public CastorDao(final Class<? extends T> type) {
        this(type, new PersistenceHelper<T>());
    }

    /**
     * Constructor.
     */
    public CastorDao(final Class<? extends T> type, final PersistenceHelper<? super T> helper) {
        setObjectType(type);
        setPersistenceHelper(helper);
    }

    public void setDaoRegistry(final DaoRegistry registry) {
        _registry = registry;
    }

    public <L, S extends Persistable<L>> CastorDao<L, S> getForwardingDao(final Class<S> type) {
        return _registry.getDao(type);
    }

    /**
     */
    public void setObjectType(final Class<? extends T> type) {
        if (type == null) {
            throw new IllegalArgumentException("null object type");
        }
        if (_objectType != null) {
            throw new IllegalStateException("object type already set: " + _objectType.getName());
        }
        _objectType = type;
        _objectTypeName = _objectType.getName();
        _daoProfile = _objectTypeName + ", DAO=" + getClass().getName();
        if (_LOG_.isDebugEnabled()) {
            _LOG_.debug(_daoProfile);
        }
    }

    /**
     */
    public void setPersistenceHelper(final PersistenceHelper<? super T> helper) {
        if (helper == null) {
            throw new IllegalArgumentException("null helper");
        }
        if (_helper != null) {
            throw new IllegalStateException("helper already set: " + _helper.getClass().getName());
        }
        _helper = helper;
        if (_LOG_.isDebugEnabled()) {
            _LOG_.debug("helper=" + (_helper == null ? "null" : _helper.getClass().getName()) + ", " + (_daoProfile == null ? getClass().getName() : _daoProfile));
        }
    }

    /**
     * Converts the type of specified object to array.
     * If the type of the object is array, it is simply casted.
     * Otherwise, an unary array that contains only the specified object
     * is created.
     */
    private Object[] _asArray(final Object obj) {
        Object[] array = null;
        if (obj instanceof Object[]) {
            array = (Object[]) obj;
            if (array.length == 0) {
                throw new IllegalArgumentException("ERROR: empty array");
            }
        } else {
            array = new Object[] { obj };
        }
        return array;
    }

    /**
     */
    protected final boolean _jdoIsPersistent(final Object object) throws PersistenceException {
        boolean persistent = false;
        try {
            persistent = getExtendedCastorTemplate().isPersistent(object);
        } catch (DataAccessException ex) {
            throw new PersistenceException(ex.getMostSpecificCause());
        }
        return persistent;
    }

    /**
     * Load the object of the specified identity.
     * If no such object exist, this method returns NULL.
     */
    protected T _jdoLoad(final K id) throws PersistenceException {
        if (id == null) {
            return null;
        }
        Object p_object = null;
        try {
            p_object = getCastorTemplate().load(_objectType, id);
        } catch (DataAccessException ex) {
            Throwable cause = ex.getMostSpecificCause();
            if (ObjectNotFoundException.class.isInstance(cause)) {
                p_object = null;
            } else {
                throw new PersistenceException(cause);
            }
        }
        T typedObject = _objectType.cast(p_object);
        return typedObject;
    }

    /**
     * Executes the specified OQL query.
     */
    private List<Object> _jdoExecuteQuery(final String oql, final Object[] params) throws PersistenceException {
        List<Object> results = null;
        try {
            results = getExtendedCastorTemplate().findByQuery(oql, params);
        } catch (DataAccessException ex) {
            throw new PersistenceException(ex.getMostSpecificCause());
        }
        return results;
    }

    /**
     */
    private void _jdoCreate(final Object object) throws PersistenceException {
        try {
            getCastorTemplate().create(object);
        } catch (DataAccessException ex) {
            Throwable cause = ex.getMostSpecificCause();
            if (DuplicateIdentityException.class.isInstance(cause)) {
                throw new PersistenceException(cause);
            } else {
                throw new PersistenceException(cause);
            }
        }
    }

    private void _jdoUpdate(final T object) throws PersistenceException {
        try {
            getCastorTemplate().update(object);
        } catch (DataAccessException ex) {
            throw new PersistenceException(ex.getMostSpecificCause());
        }
    }

    private void _jdoRemove(final T object) throws PersistenceException {
        try {
            getCastorTemplate().remove(object);
        } catch (DataAccessException ex) {
            throw new PersistenceException(ex.getMostSpecificCause());
        }
    }

    /**
     *
     */
    private List<Object> _search(final SearchCriteria criteria) throws PersistenceException {
        OQL oql = new OQL(_objectType, "o", criteria);
        String oqlStatement = oql.getStatement();
        Object[] params = oql.getParameterValues();
        List<Object> pObjs = _jdoExecuteQuery(oqlStatement, params);
        if (pObjs != null && pObjs.size() > 0) {
            for (Object pObj : pObjs) {
                if (_objectType.isInstance(pObj)) {
                    @SuppressWarnings("unchecked") T obj = (T) pObj;
                    _daoAfterLoad(obj);
                }
            }
        }
        return pObjs;
    }

    /**
     */
    private List<T> _find(final Binding filter, final List<? extends Order> ordering, final Limit limit) throws PersistenceException {
        List<K> ids = _findIdentity(filter, ordering, limit);
        List<T> objs = _loadAll(ids);
        return objs;
    }

    /**
     */
    private List<K> _findIdentity(final Binding filter, final List<? extends Order> ordering, final Limit limit) throws PersistenceException {
        String oqlFilter = null;
        String oqlOrdering = null;
        Object[] params = null;
        if (filter != null || ordering != null) {
            SearchCriteria criteria = new SearchCriteria();
            criteria.setBinding(filter);
            criteria.setOrders(ordering);
            OQL oql = new OQL(_objectType, "o", criteria);
            oqlFilter = oql.getWhereClause();
            oqlOrdering = oql.getOrdering();
            params = oql.getParameterValues();
        }
        List<K> ids = _findIdentity(oqlFilter, params, oqlOrdering);
        if (ids != null) {
            if (limit != null) {
                ids = limit.apply(ids);
            }
        }
        return ids;
    }

    /**
     */
    private List<K> _findIdentity(final String filter, final Object[] params, final String ordering) throws PersistenceException {
        StringBuilder oqlTemp = new StringBuilder();
        oqlTemp.append(_helper.getIdentitySelector()).append(" ");
        oqlTemp.append(" FROM " + _objectTypeName + " o ");
        if (filter != null) {
            oqlTemp.append(filter);
        }
        if (ordering != null) {
            oqlTemp.append(ordering);
        }
        String oql = oqlTemp.toString();
        if (_LOG_.isDebugEnabled()) {
            _LOG_.debug("OQL statement: " + oql);
            _LOG_.debug("OQL params: " + Arrays.toString(params));
        }
        @SuppressWarnings("unchecked") List<K> ids = (List<K>) _jdoExecuteQuery(oql, params);
        return ids;
    }

    /**
     * TEMPLATE:
     */
    protected void _daoAfterLoad(final T p_object) throws PersistenceException {
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace("NOP: " + _daoProfile);
        }
    }

    /**
     */
    private T _load(final K id) throws PersistenceException {
        if (id == null) {
            return null;
        }
        T obj = _jdoLoad(id);
        if (obj != null) {
            _daoAfterLoad(obj);
        }
        return obj;
    }

    /**
     */
    private final List<T> _loadAll(final List<? extends K> ids) throws PersistenceException {
        List<T> objs = new ArrayList<T>();
        for (K id : ids) {
            T obj = _load(id);
            objs.add(obj);
        }
        return objs;
    }

    /**
     */
    private T _loadByUnique(final T object) throws PersistenceException {
        if (!_helper.hasUnique()) {
            return null;
        }
        String filter = _helper.getUniqueFilter();
        Object[] params = _asArray(_helper.getUnique(object));
        List<K> ids = _findIdentity(filter, params, null);
        T obj = null;
        if (ids != null && ids.size() > 0) {
            if (ids.size() > 1) {
                final String message = "uniqueness integrity violation";
                PersistenceException ex = new PersistenceException(message);
                if (_LOG_.isErrorEnabled()) {
                    _LOG_.error(message);
                }
                throw ex;
            }
            K id = ids.iterator().next();
            obj = _load(id);
        }
        return obj;
    }

    /**
     */
    private T _loadCorrespondent(final T object) throws PersistenceException {
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace("object: " + object);
        }
        T p_object = _load(object.getPersistentID());
        if (p_object == null) {
            p_object = _loadByUnique(object);
        }
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace("correspondent: " + p_object);
        }
        return p_object;
    }

    /**
     * Template method:
     * Loads the correspondent object, or creates if it has not persisted.
     *
     * @return
     *  the correspondent object, or null if the object is created.
     */
    protected <L, S extends Persistable<L>> S _daoLoadOrCreate(final Class<S> type, final S object) throws PersistenceException {
        CastorDao<L, S> dao = getForwardingDao(type);
        S p_object = dao._loadCorrespondent(object);
        if (p_object == null) {
            dao._create(object);
        }
        return p_object;
    }

    /**
     * TEMPLATE:
     */
    protected void _daoBeforeCreate(final T object) throws PersistenceException {
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace("NOP: " + _daoProfile);
        }
    }

    /**
     */
    private K _create(final T object) throws PersistenceException {
        if (_jdoIsPersistent(object)) {
            return object.getPersistentID();
        }
        _daoBeforeCreate(object);
        _jdoCreate(object);
        return object.getPersistentID();
    }

    protected <L, S extends Persistable<L>> void _update(final Class<S> type, final S object) throws PersistenceException {
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace("type: " + type.getName());
            _LOG_.trace("object: " + object);
        }
        CastorDao<L, S> dao = getForwardingDao(type);
        dao._update(object);
    }

    /**
     */
    private void _update(final T object) throws PersistenceException {
        if (!TimeStampable.class.isInstance(object)) {
            if (_LOG_.isDebugEnabled()) {
                _LOG_.debug("NOT TimeStampable: " + object);
            }
            return;
        }
        _daoBeforeUpdate(object);
        _jdoUpdate(object);
    }

    /**
     * TEMPLATE:
     */
    protected void _daoBeforeUpdate(final T object) {
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace("NOP: " + _daoProfile);
        }
    }

    protected <L, S extends Persistable<L>> S _sync(final Class<S> type, final S object) throws PersistenceException {
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace("type: " + type.getName());
            _LOG_.trace("object: " + object);
        }
        if (object == null) {
            return null;
        }
        CastorDao<L, S> dao = getForwardingDao(type);
        S p_object = dao._sync(object);
        return p_object;
    }

    /**
     * @return
     *  null if the specified object is created.
     *  Otherwise, the persisted object loaded from the store.
     */
    private T _sync(final T object) throws PersistenceException {
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace("object: " + object);
        }
        if (_jdoIsPersistent(object)) {
            if (_LOG_.isDebugEnabled()) {
                _LOG_.debug("already persistent: " + object);
            }
            return null;
        }
        T p_object = _loadCorrespondent(object);
        if (p_object != null) {
            _daoBeforeSync(object, p_object);
        }
        if (p_object == null) {
            _create(object);
        }
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace("persistent object: " + p_object);
        }
        return p_object;
    }

    /**
     * TEMPLATE:
     */
    protected void _daoBeforeSync(final T object, final T p_object) throws PersistenceException {
        _syncProperties(object, p_object);
    }

    /**
     * Template method:
     * Copies the properties.
     */
    protected void _syncProperties(final T object, final T p_object) {
        BeansUtil.copyPropertiesExcept(p_object, object, new String[] { "persistentID" });
    }

    /**
     * TEMPLATE:
     */
    protected void _daoBeforeRemove(final T object) {
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace("NOP: " + _daoProfile);
        }
    }

    private void _remove(final T object) throws PersistenceException {
        T p_object = _load(object.getPersistentID());
        if (p_object != null) {
            _daoBeforeRemove(p_object);
            _jdoRemove(p_object);
        }
    }

    /**
     */
    protected void _daoAssertOperation(final String operation, final boolean condition, final String errorMessage) throws PersistenceException {
        if (_LOG_.isDebugEnabled()) {
            _LOG_.debug(operation + ": " + _daoProfile);
        }
        if (!condition) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Override
    public K create(final T object) throws PersistenceException {
        _daoAssertOperation("create", object != null, "null object");
        K id = _create(object);
        return id;
    }

    @Override
    public void update(final T object) throws PersistenceException {
        _daoAssertOperation("update", object != null, "null object");
        _update(object);
    }

    @Override
    public void remove(final T object) throws PersistenceException {
        _daoAssertOperation("remove", object != null, "null object");
        _remove(object);
    }

    @Override
    public T sync(final T object) throws PersistenceException {
        _daoAssertOperation("sync", object != null, "null object");
        T p_object = _sync(object);
        return (p_object == null ? object : p_object);
    }

    @Override
    public final List<T> syncAll(final List<? extends T> objects) throws PersistenceException {
        _daoAssertOperation("syncAll", objects != null, "null object list");
        List<T> p_objects = new ArrayList<T>();
        if (objects.size() > 0) {
            for (T object : objects) {
                T p_object = _sync(object);
                p_objects.add(p_object);
            }
        }
        return p_objects;
    }

    @Override
    public final int count() throws PersistenceException {
        return count(null);
    }

    @Override
    public int count(final Binding filter) throws PersistenceException {
        _daoAssertOperation("count", true, "not an error");
        Collection<K> p_ids = _findIdentity(filter, null, null);
        return p_ids.size();
    }

    @Override
    public T load(final K id) throws PersistenceException {
        _daoAssertOperation("load", id != null, "null identity");
        return _load(id);
    }

    @Override
    public final List<T> loadAll(final List<? extends K> ids) throws PersistenceException {
        _daoAssertOperation("loadAll", ids != null, "null identity list");
        return _loadAll(ids);
    }

    @Override
    public final Collection<T> find() throws PersistenceException {
        return find(null);
    }

    @Override
    public final Collection<T> find(final Binding filter) throws PersistenceException {
        return find(filter, null, null);
    }

    @Override
    public Collection<T> find(final Binding filter, final List<? extends Order> ordering, final Limit limit) throws PersistenceException {
        _daoAssertOperation("find", true, "not an error");
        return _find(filter, ordering, limit);
    }

    @Override
    public final Collection<K> findIdentity() {
        return findIdentity(null, null, null);
    }

    @Override
    public final Collection<K> findIdentity(final Binding filter) throws PersistenceException {
        return findIdentity(filter, null, null);
    }

    @Override
    public Collection<K> findIdentity(final Binding filter, final List<? extends Order> ordering, final Limit limit) throws PersistenceException {
        _daoAssertOperation("findIdentity", true, "not an error");
        return _findIdentity(filter, ordering, limit);
    }

    @Override
    public final List<Object> search(final SearchCriteria criteria) throws PersistenceException {
        _daoAssertOperation("search", true, "not an error");
        return _search(criteria);
    }
}
