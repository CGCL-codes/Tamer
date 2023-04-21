package com.samskivert.depot;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.samskivert.io.PersistenceException;
import com.samskivert.util.StringUtil;
import com.samskivert.jdbc.ConnectionProvider;
import com.samskivert.jdbc.DatabaseLiaison;
import com.samskivert.jdbc.JDBCUtil;
import com.samskivert.jdbc.LiaisonRegistry;
import com.samskivert.depot.CacheAdapter.CacheCategory;
import com.samskivert.depot.CacheAdapter.CachedValue;
import com.samskivert.depot.annotation.TableGenerator;
import com.samskivert.depot.impl.DepotMarshaller;
import com.samskivert.depot.impl.DepotMetaData;
import com.samskivert.depot.impl.DepotMigrationHistoryRecord;
import com.samskivert.depot.impl.DepotTypes;
import com.samskivert.depot.impl.Fetcher;
import com.samskivert.depot.impl.KeyCacheKey;
import com.samskivert.depot.impl.Modifier;
import com.samskivert.depot.impl.Operation;
import com.samskivert.depot.impl.SQLBuilder;
import static com.samskivert.depot.Log.log;

/**
 * Defines a scope in which global annotations are shared.
 */
public class PersistenceContext {

    /** Allow toggling of query logging and other debug output via a system property. */
    public static final boolean DEBUG = Boolean.getBoolean("com.samskivert.depot.debug");

    /** Allow toggling of cache-related logging via a system property. */
    public static final boolean CACHE_DEBUG = Boolean.getBoolean("com.samskivert.depot.cache_debug");

    /** Map {@link TableGenerator} instances by name. */
    public Map<String, TableGenerator> tableGenerators = Maps.newHashMap();

    /** Used by {@link PersistenceContext#PersistenceContext(CanMigrate)}. */
    public static enum CanMigrate {

        /** Schema migrations are allowed (the default). */
        ALLOWED, /** Schema migrations are disallowed and warnings are issued if it is discovered at runtime
         * that they are needed, but cannot be peformed. */
        WARN, /** Schema migrations are disallowed and a runtime exception will be thrown if a record
         * that requires migrations is used at runtime. */
        FAIL
    }

    ;

    /**
     * A cache listener is notified when cache entries change. Its purpose is typically to do
     * further invalidation of dependent entries in other caches.
     */
    public static interface CacheListener<T> {

        /**
         * The given entry (which is never null) has just been evicted from the cache.
         *
         * This method is most commonly used to trigger custom cache invalidation of records that
         * depend on the one that was just invalidated.
         */
        public void entryInvalidated(T oldEntry);

        /**
         * The given entry, which may be an explicit null, has just been placed into the cache. The
         * previous cache entry, if any, is also supplied.
         *
         * This method is most likely used by repositories to index entries by attribute for quick
         * cache invalidation when brute force is unrealistically time consuming.
         */
        public void entryCached(T newEntry, T oldEntry);
    }

    /**
     * The callback for {@link #cacheTraverse}; this is called for each entry in a given cache.
     */
    public static interface CacheTraverser<T extends Serializable> {

        /**
         * Performs whatever cache-related tasks need doing for this cache entry. This method
         * is called for each cache entry in a full-cache enumeration.
         */
        public void visitCacheEntry(PersistenceContext ctx, String cacheId, Serializable key, T record);
    }

    /**
     * A simple implementation of {@link CacheTraverser} that selectively deletes entries in
     * a cache depending on the return value of {@link #testForEviction}.
     */
    public abstract static class CacheEvictionFilter<T extends Serializable> implements CacheTraverser<T> {

        public void visitCacheEntry(PersistenceContext ctx, String cacheId, Serializable key, T record) {
            if (testForEviction(key, record)) {
                ctx.cacheInvalidate(cacheId, key);
            }
        }

        /**
         * Decides whether or not this entry should be evicted and returns true if yes, false if
         * no.
         */
        protected abstract boolean testForEviction(Serializable key, T record);
    }

    /**
     * Creates an uninitialized persistence context. {@link #init} must later be called on this
     * context to prepare it for operation.
     */
    public PersistenceContext() {
        this(CanMigrate.ALLOWED);
    }

    /**
     * Creates an uninitialized persistence context. {@link #init} must later be called on this
     * context to prepare it for operation.
     *
     * @param canMigrate configures whether schema migrations are allowed for this persistence
     * context. See {@link CanMigrate} for details.
     */
    public PersistenceContext(CanMigrate canMigrate) {
        _canMigrate = canMigrate;
    }

    /**
     * Creates and initializes a persistence context. See {@link #init}.
     */
    public PersistenceContext(String ident, ConnectionProvider conprov, CacheAdapter adapter) {
        this(CanMigrate.ALLOWED);
        init(ident, conprov, adapter);
    }

    /**
     * Returns the cache adapter used by this context or null if caching is disabled.
     */
    public CacheAdapter getCacheAdapter() {
        return _cache;
    }

    /**
     * Returns whether schema migrations are allowed in this context.
     */
    public CanMigrate canMigrate() {
        return _canMigrate;
    }

    /**
     * Initializes this context with its connection provider and cache adapter.
     *
     * @param ident the identifier to provide to the connection provider when requesting a
     * connection.
     * @param conprov provides JDBC {@link Connection} instances.
     * @param adapter an optional adapter to a cache management system.
     */
    public void init(String ident, ConnectionProvider conprov, CacheAdapter adapter) {
        String url = conprov.getURL(ident);
        if (url == null) {
            throw new IllegalArgumentException("No database URL found for ident '" + ident + "'.");
        }
        _ident = ident;
        _conprov = conprov;
        _liaison = LiaisonRegistry.getLiaison(url);
        _cache = adapter;
        _meta.init(this);
    }

    /**
     * Shuts this persistence context down, shutting down any caching system in use and shutting
     * down the JDBC connection pool.
     */
    public void shutdown() {
        try {
            if (_cache != null) {
                _cache.shutdown();
            }
        } catch (Throwable t) {
            log.warning("Failure shutting down Depot cache.", t);
        }
        if (_conprov != null) {
            _conprov.shutdown();
        }
    }

    /**
     * Returns a snapshot of our current runtime statistics.
     */
    public Stats.Snapshot getStats() {
        return _stats.getSnapshot();
    }

    /**
     * Creates and return a new {@link SQLBuilder} for the appropriate dialect.
     */
    public SQLBuilder getSQLBuilder(DepotTypes types) {
        return _meta.getSQLBuilder(types, _liaison);
    }

    /**
     * Registers a schema migration for the specified entity class.
     *
     * <p> This method must be called <b>before</b> an Entity is used by any repository. Thus you
     * should register all migrations in the constructor of the repository that declares them in
     * its {@link DepotRepository#getManagedRecords} method.
     *
     * <p> Note that the migration process is as follows:
     *
     * <ul><li> Note the difference between the entity's declared version and the version recorded
     * in the database.
     * <li> Run all registered pre-migrations
     * <li> Perform all default migrations (column and index additions, index removals)
     * <li> Run all registered post-migrations </ul>
     *
     * Thus you must either be prepared for the entity to be at <b>any</b> version prior to your
     * migration target version because we may start up, find the schema at version 1 and the
     * Entity class at version 8 and do all "standard" migrations in one fell swoop. So if a column
     * got added in version 2 and renamed in version 6 and your migration was registered for
     * version 6 to do that migration, it must be prepared for the column not to exist at all.
     *
     * <p> If you want a completely predictable migration process, never use the default migrations
     * and register a pre-migration for every single schema migration and they will then be
     * guaranteed to be run in registration order and with predictable pre- and post-conditions.
     *
     * <p> Note that if {@link PersistenceContext#initializeRepositories} is used, then all schema
     * migrations for all known repositories will be run and then all data migrations for all known
     * repositories will be run. This is recommeneded because schema migrations may fail and it is
     * generally better to have not yet done the data migration rather than having schema and data
     * migrations interleaved and potentially leaving the database in a strange state.
     */
    public <T extends PersistentRecord> void registerMigration(Class<T> type, SchemaMigration migration) {
        DepotMarshaller<T> marshaller = getRawMarshaller(type);
        if (marshaller.isInitialized()) {
            throw new IllegalStateException("Migrations must be registered before initializeRepositories() is called.");
        }
        marshaller.registerMigration(migration);
    }

    /**
     * Returns the marshaller for the specified persistent object class, creating and initializing
     * it if necessary.
     */
    public <T extends PersistentRecord> DepotMarshaller<T> getMarshaller(Class<T> type) throws DatabaseException {
        checkAreInitialized();
        DepotMarshaller<T> marshaller = getRawMarshaller(type);
        try {
            if (!marshaller.isInitialized()) {
                marshaller.init(this, _meta);
                if (marshaller.getTableName() != null && _warnOnLazyInit) {
                    log.warning("Record initialized lazily", "type", type.getName(), new Exception());
                }
            }
        } catch (DatabaseException pe) {
            throw (DatabaseException) new DatabaseException("Failed to initialize marshaller [type=" + type + "].").initCause(pe);
        }
        return marshaller;
    }

    /**
     * Invokes a non-modifying query and returns its result.
     */
    public <T> T invoke(Fetcher<T> fetcher) throws DatabaseException {
        T result = fetcher.getCachedResult(this);
        if (result != null) {
            fetcher.updateStats(_stats);
            return result;
        }
        return invoke(fetcher, true);
    }

    /**
     * Invokes a modifying query and returns the number of rows modified.
     */
    public int invoke(Modifier modifier) throws DatabaseException {
        return invoke(modifier, true);
    }

    /**
     * Returns true if there is a {@link CacheAdapter} configured, false otherwise.
     */
    public boolean isUsingCache() {
        return _cache != null;
    }

    /**
     * Looks up an entry in the cache by the given key.
     */
    public <T> T cacheLookup(CacheKey key) {
        if (_cache == null) {
            return null;
        }
        CacheAdapter.CachedValue<T> ref = _cache.lookup(key.getCacheId(), key.getCacheKey());
        return (ref == null) ? null : ref.getValue();
    }

    /**
     * Stores a new entry indexed by the given key.
     */
    public <T> void cacheStore(CacheCategory category, CacheKey key, T entry) {
        if (_cache == null) {
            return;
        }
        if (key == null) {
            log.warning("Cache key must not be null [entry=" + entry + "]", new Exception());
            return;
        }
        log.debug("storing", "key", key, "value", entry);
        CacheAdapter.CachedValue<T> element = _cache.lookup(key.getCacheId(), key.getCacheKey());
        T oldEntry = (element != null ? element.getValue() : null);
        _cache.store(category, key.getCacheId(), key.getCacheKey(), entry);
        Set<CacheListener<?>> listeners = _listenerSets.get(key.getCacheId());
        if (listeners != null && listeners.size() > 0) {
            for (CacheListener<?> listener : listeners) {
                log.debug("cascading", "listener", listener);
                @SuppressWarnings("unchecked") CacheListener<T> casted = (CacheListener<T>) listener;
                casted.entryCached(entry, oldEntry);
            }
        }
    }

    /**
     * Evicts the cache entry indexed under the given key, if there is one.  The eviction may
     * trigger further cache invalidations.
     */
    public void cacheInvalidate(Key<?> key) {
        if (key == null) {
            log.warning("Cache key to invalidate must not be null.", new Exception());
        } else {
            cacheInvalidate(new KeyCacheKey(key));
        }
    }

    /**
     * Evicts the cache entry indexed under the given key, if there is one.  The eviction may
     * trigger further cache invalidations.
     */
    public void cacheInvalidate(CacheKey key) {
        if (key == null) {
            log.warning("Cache key to invalidate must not be null.", new Exception());
        } else {
            cacheInvalidate(key.getCacheId(), key.getCacheKey());
        }
    }

    /**
     * Evicts the cache entry indexed under the given cache id and cache key, if there is one.  The
     * eviction may trigger further cache invalidations.
     */
    public <T extends Serializable> void cacheInvalidate(String cacheId, Serializable cacheKey) {
        if (_cache == null) {
            return;
        }
        if (CACHE_DEBUG) {
            log.info("Invalidating", "id", cacheId, "key", cacheKey);
        }
        CacheAdapter.CachedValue<T> element = _cache.lookup(cacheId, cacheKey);
        if (element != null) {
            T oldEntry = element.getValue();
            if (oldEntry != null) {
                Set<CacheListener<?>> listeners = _listenerSets.get(cacheId);
                if (listeners != null && listeners.size() > 0) {
                    for (CacheListener<?> listener : listeners) {
                        log.debug("cascading", "listener", listener);
                        @SuppressWarnings("unchecked") CacheListener<T> casted = (CacheListener<T>) listener;
                        casted.entryInvalidated(oldEntry);
                    }
                }
            }
        }
        _cache.remove(cacheId, cacheKey);
    }

    /**
     * Brutally iterates over the entire contents of the cache associated with the given class,
     * invoking the callback for each cache entry.
     */
    public <T extends Serializable> void cacheTraverse(Class<? extends PersistentRecord> pClass, CacheTraverser<T> filter) {
        cacheTraverse(pClass.getName(), filter);
    }

    /**
     * Brutally iterates over the entire contents of the cache identified by the given cache id,
     * invoking the callback for each cache entry.
     */
    public <T extends Serializable> void cacheTraverse(String cacheId, CacheTraverser<T> filter) {
        if (_cache == null) {
            return;
        }
        for (Serializable key : _cache.enumerate(cacheId)) {
            CachedValue<T> result = _cache.lookup(cacheId, key);
            if (result != null && result.getValue() != null) {
                filter.visitCacheEntry(this, cacheId, key, result.getValue());
            }
        }
    }

    /**
     * Requests that the cache for the specified persistent record be cleared. Note that this
     * clears only the <em>by-primary-key</em> cache for the index in question. It does not clear
     * the <em>keyset</em> or <em>contents</em> query caches. Use {@link Query#clearCache} to clear
     * these caches.
     *
     * @param localOnly if true, only the cache in this JVM will be cleared, no broadcast message
     * will be sent to instruct all distributed nodes to also clear this cache.
     */
    public void cacheClear(Class<? extends PersistentRecord> pClass, boolean localOnly) {
        cacheClear(pClass.getName(), localOnly);
    }

    /**
     * Requests that the cache with the specified id be cleared.
     *
     * @param localOnly if true, only the cache in this JVM will be cleared, no broadcast message
     * will be sent to instruct all distributed nodes to also clear this cache.
     */
    public void cacheClear(String cacheId, boolean localOnly) {
        if (_cache != null) {
            _cache.clear(cacheId, localOnly);
        }
    }

    /**
     * Registers a new cache listener with the cache associated with the given class.
     */
    public <T extends Serializable> void addCacheListener(Class<T> pClass, CacheListener<T> listener) {
        addCacheListener(pClass.getName(), listener);
    }

    /**
     * Registers a new cache listener with the identified cache.
     */
    public <T extends Serializable> void addCacheListener(String cacheId, CacheListener<T> listener) {
        Set<CacheListener<?>> listenerSet = _listenerSets.get(cacheId);
        if (listenerSet == null) {
            listenerSet = Sets.newHashSet();
            _listenerSets.put(cacheId, listenerSet);
        }
        listenerSet.add(listener);
    }

    /**
     * Initializes all repositories that have been created and registered with this persistence
     * context. Any repositories that are constructed after this call will be immediately
     * initialized at the time they are constructed (which is probably undesirable). When a
     * repository is initialized, schema migrations for all of its managed persistent records are
     * run. It is best to do all schema migrations when the system initializes which is why lazy
     * initialization of repositories is undesirable.
     *
     * @param warnOnLazyInit if true, any repositories are constructed after this method is called
     * will result in a warning so that the application developer can restructure their code to
     * ensure that those repositories are properly created prior to this call.
     */
    public void initializeRepositories(boolean warnOnLazyInit) throws DatabaseException {
        getMarshaller(DepotMigrationHistoryRecord.class);
        for (DepotRepository repo : _repositories) {
            repo.resolveRecords();
        }
        for (DepotRepository repo : _repositories) {
            repo.init();
        }
        _warnOnLazyInit = warnOnLazyInit;
        _repositories = null;
    }

    /**
     * Called when a depot repository is created. We register all persistent record classes used by
     * the repository so that systems that desire it can force the resolution of all database
     * tables rather than allowing resolution to happen on demand.
     */
    protected void repositoryCreated(DepotRepository repo) {
        if (_repositories == null) {
            if (_warnOnLazyInit) {
                log.warning("Repository created lazily: " + repo.getClass().getName());
            }
            repo.resolveRecords();
            repo.init();
        } else {
            _repositories.add(repo);
        }
    }

    /**
     * Looks up and creates, but does not initialize, the marshaller for the specified Entity type.
     */
    protected <T extends PersistentRecord> DepotMarshaller<T> getRawMarshaller(Class<T> type) {
        @SuppressWarnings("unchecked") DepotMarshaller<T> marshaller = (DepotMarshaller<T>) _marshallers.get(type);
        if (marshaller == null) {
            _marshallers.put(type, marshaller = new DepotMarshaller<T>(type, this));
        }
        return marshaller;
    }

    /**
     * Internal invoke method that takes care of transient retries for both queries and modifiers.
     */
    protected <T> T invoke(Operation<T> op, boolean retryOnTransientFailure) throws DatabaseException {
        checkAreInitialized();
        boolean isReadOnly = op.isReadOnly();
        Connection conn;
        long preConnect = System.nanoTime();
        try {
            conn = _conprov.getConnection(_ident, isReadOnly);
        } catch (PersistenceException pe) {
            throw new DatabaseException("Failed get connection [ident=" + _ident + ", isRO=" + isReadOnly + "]", pe);
        }
        List<Statement> stmts = Lists.newArrayListWithCapacity(1);
        conn = JDBCUtil.makeCollector(conn, stmts);
        synchronized (conn) {
            long preInvoke = System.nanoTime();
            try {
                T value;
                try {
                    value = op.invoke(this, conn, _liaison);
                } finally {
                    for (Statement stmt : stmts) {
                        stmt.close();
                    }
                }
                if (!conn.getAutoCommit()) {
                    conn.commit();
                }
                _stats.noteOp(isReadOnly, preConnect, preInvoke, System.nanoTime());
                op.updateStats(_stats);
                return value;
            } catch (SQLException sqe) {
                if (!isReadOnly) {
                    if (_liaison.isDuplicateRowException(sqe)) {
                        throw new DuplicateKeyException(sqe.getMessage());
                    }
                }
                _conprov.connectionFailed(_ident, isReadOnly, conn, sqe);
                conn = null;
                if (retryOnTransientFailure && _liaison.isTransientException(sqe)) {
                    String msg = StringUtil.split(String.valueOf(sqe), "\n")[0];
                    log.info("Transient failure executing op, retrying [error=" + msg + "].");
                } else {
                    throw new DatabaseException("Operation failure " + op, sqe);
                }
            } finally {
                if (conn != null) {
                    _conprov.releaseConnection(_ident, isReadOnly, conn);
                }
            }
        }
        return invoke(op, false);
    }

    protected void checkAreInitialized() {
        if (_conprov == null) {
            throw new IllegalStateException("This persistence context has not yet been initialized. You are probably " + "doing something too early, like in a repository's constructor. Don't do that.");
        }
    }

    protected final CanMigrate _canMigrate;

    protected String _ident;

    protected ConnectionProvider _conprov;

    protected DatabaseLiaison _liaison;

    protected DepotMetaData _meta = new DepotMetaData();

    protected boolean _warnOnLazyInit;

    /** Used to track various statistics. */
    protected Stats _stats = new Stats();

    /** The object through which all our caching is relayed, or null, for no caching. */
    protected CacheAdapter _cache;

    /** Tracks repositories during the pre-initialization phase. */
    protected List<DepotRepository> _repositories = Lists.newArrayList();

    /** A mapping from persistent record class to resolved marshaller. */
    protected Map<Class<?>, DepotMarshaller<?>> _marshallers = Maps.newHashMap();

    /** A mapping of cache listeners by cache id. */
    protected Map<String, Set<CacheListener<?>>> _listenerSets = Maps.newHashMap();
}
