package org.magicdroid.server.impl;

import java.util.ArrayList;
import java.util.List;
import org.magicdroid.commons.Collect;
import org.magicdroid.commons.Structures;
import org.magicdroid.features.EntityFeature;
import org.magicdroid.features.IdentifyFeature;
import org.magicdroid.features.IdentifyFeature.EntityId;
import org.magicdroid.features.MetaIterableFeature;
import org.magicdroid.services.CommitService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ServerCommitServiceImpl implements CommitService {

    public static final Structures.Lazy<Key> ROOT = new Structures.Lazy<Key>() {

        @Override
        protected synchronized Key load() {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            return KeyFactory.createKey("Application", "Magicdroid");
        }
    };

    @Override
    public void commit(final EntityFeature[] changed, final EntityId[] deleted) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        deleteKeys(deleted, datastore);
        if (changed.length == 0) return;
        updateObjects(changed, datastore);
    }

    protected List<Key> updateObjects(EntityFeature[] changed, DatastoreService datastore) {
        List<Entity> dbentitiesToBeUpdated = convertToDBEntityToBeWritten(changed);
        List<Key> key = datastore.put(dbentitiesToBeUpdated);
        return key;
    }

    protected void deleteKeys(EntityId[] deleted, DatastoreService datastore) {
        List<Key> listKeysToBeDeleted = Collect.apply(deleted, new ArrayList<Key>(), new ConvertIdToKey());
        datastore.delete(listKeysToBeDeleted);
    }

    private List<Entity> convertToDBEntityToBeWritten(EntityFeature[] changed) {
        List<Entity> result = new ArrayList<Entity>();
        for (final EntityFeature entity : changed) {
            Entity dbEntity = createDBEntityKey(entity.metaEntityType(), entity.getId());
            entity.forEachProperty(dbEntity, new MetaIterableFeature.PropertyProcessor<Entity>() {

                @Override
                public void process(Entity result, String key, Object value) {
                    if (key.equals(IdentifyFeature.Keys.ID)) return;
                    if (value == null) {
                        result.setProperty(key, null);
                        return;
                    }
                    if (value instanceof EntityFeature) {
                        result.setProperty(key, createRef((EntityFeature) value));
                        return;
                    }
                    result.setProperty(key, ServerMapping.MAPPING.lookup(value.getClass()).convertToProperty(value));
                }
            });
            result.add(dbEntity);
        }
        return result;
    }

    private Entity createDBEntityKey(Class<?> type, EntityId id) {
        return new Entity(createKey(type, id));
    }

    private Key createRef(EntityFeature entity) {
        return KeyFactory.createKey(ROOT.get(), entity.metaEntityType().getName(), entity.getId().getValue());
    }

    private final class ConvertIdToKey implements Collect.Function<EntityId, Key> {

        @Override
        public Key process(EntityId input) {
            return createKey(input.getType(), input);
        }
    }

    public static Key createKey(Class<?> type, EntityId id) {
        return KeyFactory.createKey(ROOT.get(), type.getName(), id.getValue());
    }
}
