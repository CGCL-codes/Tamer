package org.hydracache.server.data.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.hydracache.server.data.resolver.ConflictResolver;
import org.hydracache.server.data.resolver.ResolutionResult;
import org.hydracache.server.data.versioning.Versioned;

/**
 * A {@link DataBank} implementation using Ehcache library
 * 
 * @author nzhu
 * 
 */
public class EhcacheDataBank implements DataBank {

    private static Logger log = Logger.getLogger(EhcacheDataBank.class);

    /**
     * Ehcache configuration file name
     */
    public static final String DEFAULT_CACHE_CONF_FILE_NAME = "cache.xml";

    /**
     * Ehcache name used by this implementation
     */
    public static final String CACHE_NAME = "hydra";

    private final CacheManager cacheManager;

    private final Cache cache;

    private final ConflictResolver conflictResolver;

    /**
     * By default this constructor implementation loads Ehcache manager
     * configuration from file {@link DEFAULT_CACHE_CONF_FILE_NAME} in classpath
     * 
     * @param conflictResolver
     *            conflict resolver that will be used to resolve data versioning
     *            conflict
     */
    public EhcacheDataBank(final ConflictResolver conflictResolver) {
        this(conflictResolver, CacheManager.create(EhcacheDataBank.class.getClassLoader().getResourceAsStream(DEFAULT_CACHE_CONF_FILE_NAME)));
    }

    /**
     * Create a new {@link EhcacheDataBank} instance using the given
     * {@link CacheManager} and if this given {@link CacheManager} does not
     * contain a specific cache intance named {@link CACHE_NAME} this contructor
     * will create one using the default configuration
     * 
     * @param conflictResolver
     *            conflict resolver that will be used to resolve data versioning
     *            conflict
     * @param cacheManager
     *            cache manager to use
     */
    public EhcacheDataBank(final ConflictResolver conflictResolver, final CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.conflictResolver = conflictResolver;
        if (!this.cacheManager.cacheExists(CACHE_NAME)) this.cacheManager.addCache(CACHE_NAME);
        cache = this.cacheManager.getCache(CACHE_NAME);
    }

    public Data get(final Long keyHash) {
        Validate.isTrue(keyHash != null, "Data key hash can not be null");
        final Element element = cache.get(keyHash);
        if (element == null) return null;
        final Serializable data = element.getValue();
        Validate.isTrue(data instanceof Data, "Cache element contains unknown data[" + data + "]");
        return (Data) data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Data> getAll() {
        List keys = cache.getKeys();
        List<Data> results = new ArrayList<Data>();
        for (Object eachKey : keys) {
            if (eachKey instanceof Long) results.add(get((Long) eachKey)); else log.warn("Skipping unknown key: " + eachKey);
        }
        return results;
    }

    public void put(final Data newData) throws DataStorageException {
        Validate.notNull(newData, "Data object can not be null");
        Data dataToStore = newData;
        if (dataAlreadyExists(newData)) {
            final Data oldData = get(newData.getKeyHash());
            final List<Versioned> conflictList = buildConflictList(newData, oldData);
            final ResolutionResult resolutionResult = conflictResolver.resolve(conflictList);
            if (resolutionResult.stillHasConflict()) throw new DataConflictException("Unresolvable version conflict detected for data[" + newData + "]");
            dataToStore = getMostAliveVersion(resolutionResult);
        }
        final Element element = new Element(newData.getKeyHash(), dataToStore);
        cache.put(element);
    }

    private Data getMostAliveVersion(final ResolutionResult resolutionResult) {
        return (Data) resolutionResult.getAlive().iterator().next();
    }

    private List<Versioned> buildConflictList(final Versioned newData, final Versioned oldData) {
        return Arrays.asList(oldData, newData);
    }

    private boolean dataAlreadyExists(final Data newData) {
        return get(newData.getKeyHash()) != null;
    }
}
