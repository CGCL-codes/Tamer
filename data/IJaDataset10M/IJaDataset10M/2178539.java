package jp.go.aist.six.oval.core.repository.mongodb;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.go.aist.six.util.persist.Datastore;
import jp.go.aist.six.util.persist.Persistable;
import jp.go.aist.six.util.persist.PersistenceException;
import jp.go.aist.six.util.search.Binding;
import jp.go.aist.six.util.search.Limit;
import jp.go.aist.six.util.search.Order;
import jp.go.aist.six.util.search.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.code.morphia.dao.DAO;

/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: MongoDatastore.java 1953 2011-08-13 03:28:26Z nakamura5akihito $
 */
public class MongoDatastore implements DAORegistry, Datastore {

    /**
     * Logger.
     */
    private static final Logger _LOG_ = LoggerFactory.getLogger(MongoDatastore.class);

    /**
     * Class - DAO
     */
    private final Map<Class<?>, DAO<?, ?>> _daoMap = new HashMap<Class<?>, DAO<?, ?>>();

    /**
     * Constructor.
     */
    public MongoDatastore() {
    }

    /**
     */
    public void setDAO(final Collection<? extends DAO<?, ?>> daoList) {
        for (DAO<?, ?> dao : daoList) {
            if (dao == null) {
                continue;
            }
            Class<?> entityClass = dao.getEntityClass();
            _LOG_.debug("adding DAO: " + entityClass);
            _daoMap.put(entityClass, dao);
            if (dao instanceof BaseDAO) {
                BaseDAO.class.cast(dao).setDAORegistry(this);
            }
        }
    }

    @Override
    public <T, K> DAO<T, K> getDAO(final Class<T> entityClass) {
        if (entityClass == null) {
            throw new IllegalArgumentException("null entity class");
        }
        @SuppressWarnings("unchecked") DAO<T, K> dao = (DAO<T, K>) _daoMap.get(entityClass);
        if (dao == null) {
            throw new IllegalArgumentException("unknown entity class: " + entityClass);
        }
        return dao;
    }

    @Override
    public <K, T extends Persistable<K>> K create(final Class<T> type, final T object) throws PersistenceException {
        getDAO(type).save(object);
        return object.getPersistentID();
    }

    @Override
    public <K, T extends Persistable<K>> void update(final Class<T> type, final T object) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> void remove(final Class<T> type, final T object) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> T sync(final Class<T> type, final T object) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> List<T> syncAll(final Class<T> type, final List<? extends T> objects) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> int count(final Class<T> type) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> int count(final Class<T> type, final Binding filter) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> T load(final Class<T> type, final K id) throws PersistenceException {
        T p_object = getDAO(type).get(id);
        return p_object;
    }

    @Override
    public <K, T extends Persistable<K>> List<T> loadAll(final Class<T> type, final List<? extends K> ids) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> Collection<T> find(final Class<T> type) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> Collection<T> find(final Class<T> type, final Binding filter) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> Collection<T> find(final Class<T> type, final Binding filter, final List<? extends Order> ordering, final Limit limit) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> Collection<K> findIdentity(final Class<T> type) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> Collection<K> findIdentity(final Class<T> type, final Binding filter) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> Collection<K> findIdentity(final Class<T> type, final Binding filter, final List<? extends Order> ordering, final Limit limit) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, T extends Persistable<K>> List<Object> search(final Class<T> type, final SearchCriteria criteria) throws PersistenceException {
        throw new UnsupportedOperationException();
    }
}
