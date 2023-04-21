package org.magicdroid.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.magicdroid.commons.Refactor;
import org.magicdroid.features.EntityFeature;

public interface MetaModel extends Serializable {

    class MetaProperty<T extends EntityFeature> {

        private final Class<T> type;

        private final Method method;

        private final String name;

        public MetaProperty(Class<T> type, Method method) {
            this.type = type;
            this.method = method;
            this.name = Refactor.extractPropName(method.getName());
        }

        public String getName() {
            return this.name;
        }

        public Method getMethod() {
            return this.method;
        }

        public Class getReturnType() {
            return this.method.getReturnType();
        }

        @Override
        public String toString() {
            return this.type.getSimpleName() + "." + this.name + ":" + this.getReturnType().getSimpleName();
        }
    }

    Class<EntityFeature>[] listModels();

    <T extends EntityFeature> MetaProperty<T>[] listProperties(Class<T> type);
}
