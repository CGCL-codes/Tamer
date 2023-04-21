package fi.foyt.hibernate.gae.search.persistence.dao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import fi.foyt.hibernate.gae.search.persistence.domainmodel.AbstractObject;

public class GenericDAO<T extends AbstractObject> {

    public GenericDAO(String kind, boolean cached) {
        this.kind = kind;
        this.cached = cached;
    }

    public String getKind() {
        return kind;
    }

    protected DatastoreService getDatastoreService() {
        return DatastoreServiceFactory.getDatastoreService();
    }

    public void delete(T object) {
        if (object != null && object.getKey() != null) {
            getDatastoreService().delete(object.getKey());
            if (cached) {
                purgeCachedEntity(object.getKey());
            }
        }
    }

    protected T persist(T object) {
        Key key = persist(object.toEntity());
        object.setKey(key);
        if (cached == true) {
            purgeCachedEntity(key);
            cacheEntity(object.toEntity());
        }
        return object;
    }

    protected T findObjectByKey(Key key) {
        Entity entity = findEntityByKey(key);
        return createObjectFromEntity(entity);
    }

    protected T getSingleObject(Query query) {
        Entity entity = getSingleEntity(query);
        if (entity == null) return null;
        if (cached) {
            Entity cachedEntity = getCachedEntity(entity.getKey());
            if (cachedEntity != null) {
                entity = cachedEntity;
            } else {
                try {
                    entity = getDatastoreService().get(entity.getKey());
                    cacheEntity(entity);
                } catch (EntityNotFoundException e) {
                    entity = null;
                }
            }
        }
        return createObjectFromEntity(entity);
    }

    protected List<T> getObjectList(Query query) {
        return listObjects(prepareQuery(query).asList(FetchOptions.Builder.withDefaults()));
    }

    private List<T> listObjects(List<Entity> entities) {
        List<T> result = new ArrayList<T>();
        if (cached) {
            for (Entity entity : entities) {
                Entity cachedEntity = getCachedEntity(entity.getKey());
                if (cachedEntity != null) {
                    entity = cachedEntity;
                } else {
                    try {
                        entity = getDatastoreService().get(entity.getKey());
                        cacheEntity(entity);
                    } catch (EntityNotFoundException e) {
                        entity = null;
                    }
                }
                result.add(createObjectFromEntity(entity));
            }
        } else {
            for (Entity entity : entities) {
                result.add(createObjectFromEntity(entity));
            }
        }
        return result;
    }

    private PreparedQuery prepareQuery(Query query) {
        if (cached) return getDatastoreService().prepare(query.setKeysOnly()); else return getDatastoreService().prepare(query);
    }

    private Entity getCachedEntity(Key key) {
        try {
            MemcacheService cache = getEntityCache();
            return (Entity) cache.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    private Key persist(Entity entity) {
        return getDatastoreService().put(entity);
    }

    private Entity findEntityByKey(Key key) {
        try {
            if (cached == true) {
                Entity cachedEntity = getCachedEntity(key);
                if (cachedEntity == null) {
                    Entity entity = getDatastoreService().get(key);
                    cacheEntity(entity);
                    return entity;
                } else {
                    return cachedEntity;
                }
            } else {
                return getDatastoreService().get(key);
            }
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    private Entity getSingleEntity(Query query) {
        List<Entity> entities = prepareQuery(query).asList(FetchOptions.Builder.withLimit(1));
        if (entities.size() == 1) return entities.get(0);
        return null;
    }

    private void cacheEntity(Entity entity) {
        try {
            MemcacheService cache = getEntityCache();
            cache.put(entity.getKey(), entity);
        } catch (Exception e) {
        }
    }

    private void purgeCachedEntity(Key key) {
        try {
            MemcacheService cache = getEntityCache();
            cache.delete(key);
        } catch (Exception e) {
        }
    }

    private MemcacheService getEntityCache() {
        return MemcacheServiceFactory.getMemcacheService("search-entities");
    }

    private MemcacheService getLookupCache() {
        return MemcacheServiceFactory.getMemcacheService("search-lookup");
    }

    protected MemcacheService getCustomCache() {
        return MemcacheServiceFactory.getMemcacheService("search-custom");
    }

    protected Key createNullLookupKey() {
        return KeyFactory.createKey("NULL", -1);
    }

    protected boolean isNullLookupKey(Key key) {
        return "NULL".equals(key.getKind()) && key.getId() == -1;
    }

    protected Key getLookupKey(Object lookupKey) {
        try {
            return (Key) getLookupCache().get(lookupKey);
        } catch (Exception e) {
            return null;
        }
    }

    protected void putLookupKey(Object lookupKey, Key key) {
        try {
            getLookupCache().put(lookupKey, key);
        } catch (Exception e) {
        }
    }

    protected void removeLookupKey(Object lookupKey) {
        try {
            getLookupCache().delete(lookupKey);
        } catch (Exception e) {
        }
    }

    @SuppressWarnings("unchecked")
    private T createObjectFromEntity(Entity entity) {
        if (entity != null) {
            try {
                T object = (T) getGenericTypeClass().newInstance();
                object.loadFromEntity(entity);
                return object;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends AbstractObject> getGenericTypeClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return ((Class<? extends AbstractObject>) parameterizedType.getActualTypeArguments()[0]);
    }

    private String kind;

    private boolean cached;
}
