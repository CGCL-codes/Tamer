package org.magicdroid.services;

import org.magicdroid.features.EntityFeature;
import org.magicdroid.features.IdentifyFeature.EntityId;
import org.magicdroid.server.MagicdroidServerService;

public interface CommitService extends MagicdroidServerService {

    interface Converter {

        Object convert(Class<? extends EntityFeature> type, String property, Object value);
    }

    void commit(EntityFeature[] changed, EntityId[] deleted);
}
