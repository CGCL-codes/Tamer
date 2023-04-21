package jp.go.aist.six.util.castor;

import java.util.Collection;
import java.util.List;
import jp.go.aist.six.util.persist.Datastore;
import jp.go.aist.six.util.persist.Persistable;
import jp.go.aist.six.util.persist.PersistenceException;
import jp.go.aist.six.util.search.Binding;
import jp.go.aist.six.util.search.Limit;
import jp.go.aist.six.util.search.Order;
import jp.go.aist.six.util.search.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * A data store implementation.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: CastorDatastore.java 317 2011-06-16 03:01:05Z nakamura5akihito $
 */
public class CastorDatastore implements Datastore {

    /**
     * Logger.
     */
    private static final Logger _LOG_ = LoggerFactory.getLogger(CastorDatastore.class);

    private PlatformTransactionManager _txManager;

    private DaoRegistry _daoRegistry;

    /**
     * Constructor.
     */
    public CastorDatastore() {
    }

    public void setTransactionManager(final PlatformTransactionManager manager) {
        _txManager = manager;
    }

    public PlatformTransactionManager getTransactionManager() {
        return _txManager;
    }

    public void setDaoRegistry(final DaoRegistry registry) {
        _daoRegistry = registry;
    }

    public <K, T extends Persistable<K>> CastorDao<K, T> getDao(final Class<T> type) throws PersistenceException {
        CastorDao<K, T> dao = _daoRegistry.getDao(type);
        dao.setDaoRegistry(_daoRegistry);
        return dao;
    }

    @Override
    public <K, T extends Persistable<K>> K create(final Class<T> type, final T object) throws PersistenceException {
        K p_id = _executeTx("create", type, object, new TransactionCallback<K>() {

            @Override
            public K doInTransaction(final TransactionStatus status) {
                return getDao(type).create(object);
            }
        });
        return p_id;
    }

    @Override
    public <K, T extends Persistable<K>> void update(final Class<T> type, final T object) throws PersistenceException {
        _executeTx("update", type, object, new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(final TransactionStatus status) {
                getDao(type).update(object);
            }
        });
    }

    @Override
    public <K, T extends Persistable<K>> void remove(final Class<T> type, final T object) throws PersistenceException {
        _executeTx("remove", type, object, new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(final TransactionStatus status) {
                getDao(type).remove(object);
            }
        });
    }

    @Override
    public <K, T extends Persistable<K>> T sync(final Class<T> type, final T object) throws PersistenceException {
        T p_object = _executeTx("sync", type, object, new TransactionCallback<T>() {

            @Override
            public T doInTransaction(final TransactionStatus status) {
                return getDao(type).sync(object);
            }
        });
        return p_object;
    }

    @Override
    public <K, T extends Persistable<K>> List<T> syncAll(final Class<T> type, final List<? extends T> objects) throws PersistenceException {
        List<T> p_objects = _executeTx("syncAll", type, new TransactionCallback<List<T>>() {

            @Override
            public List<T> doInTransaction(final TransactionStatus status) {
                return getDao(type).syncAll(objects);
            }
        });
        return p_objects;
    }

    @Override
    public <K, T extends Persistable<K>> int count(final Class<T> type) throws PersistenceException {
        Integer p_count = _executeTx("countAll", type, new TransactionCallback<Integer>() {

            @Override
            public Integer doInTransaction(final TransactionStatus status) {
                return getDao(type).count();
            }
        });
        return p_count.intValue();
    }

    @Override
    public <K, T extends Persistable<K>> int count(final Class<T> type, final Binding filter) throws PersistenceException {
        Integer p_count = _executeTx("count", type, filter, new TransactionCallback<Integer>() {

            @Override
            public Integer doInTransaction(final TransactionStatus status) {
                return getDao(type).count(filter);
            }
        });
        return p_count.intValue();
    }

    @Override
    public <K, T extends Persistable<K>> T load(final Class<T> type, final K identity) throws PersistenceException {
        T p_object = _executeTx("load", type, identity, new TransactionCallback<T>() {

            @Override
            public T doInTransaction(final TransactionStatus status) {
                return getDao(type).load(identity);
            }
        });
        return p_object;
    }

    @Override
    public <K, T extends Persistable<K>> List<T> loadAll(final Class<T> type, final List<? extends K> identities) throws PersistenceException {
        List<T> p_objects = _executeTx("loadAll", type, identities, new TransactionCallback<List<T>>() {

            @Override
            public List<T> doInTransaction(final TransactionStatus status) {
                return getDao(type).loadAll(identities);
            }
        });
        return p_objects;
    }

    @Override
    public <K, T extends Persistable<K>> Collection<T> find(final Class<T> type) throws PersistenceException {
        Collection<T> p_objects = _executeTx("getAll", type, new TransactionCallback<Collection<T>>() {

            @Override
            public Collection<T> doInTransaction(final TransactionStatus status) {
                return getDao(type).find();
            }
        });
        return p_objects;
    }

    @Override
    public <K, T extends Persistable<K>> Collection<T> find(final Class<T> type, final Binding filter) throws PersistenceException {
        return find(type, filter, null, null);
    }

    @Override
    public <K, T extends Persistable<K>> Collection<T> find(final Class<T> type, final Binding filter, final List<? extends Order> ordering, final Limit limit) throws PersistenceException {
        Collection<T> p_objects = _executeTx("find", type, filter, new TransactionCallback<Collection<T>>() {

            @Override
            public Collection<T> doInTransaction(final TransactionStatus status) {
                return getDao(type).find(filter, ordering, limit);
            }
        });
        return p_objects;
    }

    @Override
    public <K, T extends Persistable<K>> Collection<K> findIdentity(final Class<T> type) throws PersistenceException {
        Collection<K> p_ids = _executeTx("findIdentity", type, new TransactionCallback<Collection<K>>() {

            @Override
            public Collection<K> doInTransaction(final TransactionStatus status) {
                return getDao(type).findIdentity();
            }
        });
        return p_ids;
    }

    @Override
    public <K, T extends Persistable<K>> Collection<K> findIdentity(final Class<T> type, final Binding filter) throws PersistenceException {
        return findIdentity(type, filter, null, null);
    }

    @Override
    public <K, T extends Persistable<K>> Collection<K> findIdentity(final Class<T> type, final Binding filter, final List<? extends Order> ordering, final Limit limit) throws PersistenceException {
        Collection<K> p_ids = _executeTx("findIdentity", type, filter, new TransactionCallback<Collection<K>>() {

            @Override
            public Collection<K> doInTransaction(final TransactionStatus status) {
                return getDao(type).findIdentity(filter, ordering, limit);
            }
        });
        return p_ids;
    }

    @Override
    public <K, T extends Persistable<K>> List<Object> search(final Class<T> type, final SearchCriteria criteria) throws PersistenceException {
        List<Object> p_objects = _executeTx("search", type, criteria, new TransactionCallback<List<Object>>() {

            @Override
            public List<Object> doInTransaction(final TransactionStatus status) {
                return getDao(type).search(criteria);
            }
        });
        return p_objects;
    }

    /**
     * Executes the specified action in a new transaction.
     */
    protected <T> T _executeTx(final String operation, final Class<? extends Persistable<?>> type, final TransactionCallback<T> action) throws PersistenceException {
        return _executeTx(operation, type, null, action);
    }

    protected <T> T _executeTx(final String operation, final Class<? extends Persistable<?>> type, final Object value, final TransactionCallback<T> action) throws PersistenceException {
        Tx<T> tx = new Tx<T>(operation, type, value, action, getTransactionManager());
        return tx.execute();
    }

    /**
     * A helper object for a transaction.
     */
    private static class Tx<T> {

        private final String _message;

        private final String _value;

        private final TransactionCallback<T> _action;

        private final TransactionTemplate _template;

        public Tx(final String operation, final Class<? extends Persistable<?>> type, final Object value, final TransactionCallback<T> action, final PlatformTransactionManager txmgr) {
            _message = ": " + operation + " - " + type.getName();
            _action = action;
            _template = new TransactionTemplate(txmgr);
            _template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            _value = (value == null ? "" : (" - " + String.valueOf(value)));
        }

        public T execute() throws PersistenceException {
            if (_LOG_.isInfoEnabled()) {
                _LOG_.info("TX begin" + _message + _value);
            }
            long timestamp = System.currentTimeMillis();
            T result = null;
            try {
                result = _template.execute(_action);
            } catch (TransactionException ex) {
                if (_LOG_.isErrorEnabled()) {
                    _LOG_.error(ex.getMessage());
                }
                throw new PersistenceException(ex.getMostSpecificCause());
            }
            if (_LOG_.isInfoEnabled()) {
                timestamp = System.currentTimeMillis() - timestamp;
                _LOG_.info("TX end" + _message + ": elapsed time (ms)=" + timestamp);
            }
            return result;
        }
    }
}
