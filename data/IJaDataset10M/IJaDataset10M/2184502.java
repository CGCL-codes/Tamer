package org.magiclabs.domain.entity;

import org.magiclabs.domain.entity.EntityFeature.EntityId;
import org.magiclabs.mosaic.Mosaic.Mixins;

@Mixins(IdGeneratorService.Mixin.class)
public interface IdGeneratorService {

    EntityId next(Class<?> namespace);

    class Mixin implements IdGeneratorService {

        static long counter = 1;

        public EntityId next(Class<?> namespace) {
            return new EntityId(namespace.getSimpleName() + "-" + counter++);
        }
    }
}
