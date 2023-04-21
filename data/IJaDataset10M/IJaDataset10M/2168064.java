package org.magicdroid.app.test;

import java.util.HashMap;
import java.util.Map;
import org.magicdroid.commons.Structures;
import org.magicdroid.features.EntityFeature;
import org.magicdroid.features.IdentifyFeature;
import org.magicdroid.features.IdentifyFeature.EntityId;
import org.magicdroid.services.CommitService;

public class _ServerCommitServiceMock implements CommitService {

    private final Map<Class<?>, Map<IdentifyFeature.EntityId, Map<String, Object>>> db = new Structures.MapWithLazyLoading<Class<?>, Map<IdentifyFeature.EntityId, Map<String, Object>>>() {

        @Override
        protected Map<EntityId, Map<String, Object>> load(Class<?> key) {
            return new HashMap<IdentifyFeature.EntityId, Map<String, Object>>();
        }
    };

    @Override
    public void commit(EntityFeature[] changed, EntityId[] deleted) {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (EntityId id : deleted) this.db.get(id.getType()).remove(id);
        for (EntityFeature entity : changed) {
            Map<EntityId, Map<String, Object>> table = this.db.get(entity.metaEntityType());
            EntityId id = entity.getId();
            if (!table.containsKey(entity.getId())) id = new EntityId(id.getType(), id.getValue());
            table.put(id, entity.metaAsMap());
        }
    }
}
