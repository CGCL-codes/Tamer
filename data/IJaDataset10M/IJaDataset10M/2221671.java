package net.sf.sidaof.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for DAOs. To use, extend it in DAO classes and implement the
 * required methods.
 * 
 * @param <K>
 *            The primary key class of the entity, e.g. Long.
 * @param <E>
 *            The entity class the DAO persists, e.g. Person.
 */
public abstract class AbstractDao<K, E> implements Dao<K, E> {

    private final Logger LOG = LoggerFactory.getLogger(AbstractDao.class);

    protected static final String QUERY_SINGLERESULT_NOTFOUND_LOGMSG = "runQueryWithSingleResult: entity '{}' not found, query='{}'";

    protected static final String QUERY_SINGLERESULT_MULTFOUND_LOGMSG = "runQueryWithSingleResult: found multiple of entity '{}', query='{}'";

    private static final byte GENERIC_TYPE_ENTITY_ELEMENT = 1;

    /** The type of the entity. */
    protected Class<E> entityClass;

    /** Make a new instance, automatically determining the entity class. */
    public AbstractDao() {
        ParameterizedType genericSuperClass = (ParameterizedType) getClass().getGenericSuperclass();
        LOG.debug("constructor: genericSuperClass={}", genericSuperClass);
        Type[] types = genericSuperClass.getActualTypeArguments();
        LOG.debug("constructor: ActualTypeArguments={}", new Object[] { types });
        Type type = types[GENERIC_TYPE_ENTITY_ELEMENT];
        LOG.debug("constructor: selected type={}", type);
        if (type instanceof ParameterizedType) {
            this.entityClass = (Class<E>) ((ParameterizedType) type).getRawType();
        } else {
            this.entityClass = (Class<E>) type;
        }
        LOG.debug("constructor: entityClass={}", entityClass);
    }

    /** Make a new instance, specifying the entity class. */
    public AbstractDao(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void persist(Collection<E> entities) {
        LOG.debug("persist: entities={}", entities);
        for (E entity : entities) {
            persist(entity);
        }
    }

    /**
     * Get the entityClass.
     * 
     * @see {@link entityClass}.
     * 
     * @return The entityClass.
     */
    public Class<E> getEntityClass() {
        return entityClass;
    }

    @Override
    public void removeById(K id) {
        removeById(id, false);
    }

    @Override
    public void removeById(K id, boolean exceptionIfNotFound) {
        LOG.debug("removeById: id={}, exceptionIfNotFound={}", id, exceptionIfNotFound);
        E entity = findById(id);
        remove(entity);
    }

    @Override
    public Long countAll() {
        Long count = countAll(null);
        return count;
    }

    @Override
    public Long countAll(String where) {
        Long count = countAll(where, null);
        return count;
    }
}
