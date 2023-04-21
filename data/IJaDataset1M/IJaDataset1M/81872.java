package org.ufacekit.ui.core.conversion.impl;

import java.util.HashMap;
import java.util.Map;
import org.ufacekit.ui.core.conversion.UIConverterFactory;
import org.ufacekit.ui.core.conversion.UIConverterFactoryRegistry;

/**
 * Default implementation for a {@link UIConverterFactoryRegistry} backed by a
 * simple {@link Map}.
 *
 * @since 1.0
 */
public class DefaultUIConverterFactoryRegistry implements UIConverterFactoryRegistry {

    private Map<Entry, UIConverterFactory> converterFactoryDictionary;

    /**
	 * Constructor.
	 *
	 * @since 1.0
	 */
    public DefaultUIConverterFactoryRegistry() {
        this.converterFactoryDictionary = new HashMap<Entry, UIConverterFactory>();
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see UIConverterFactoryRegistry#getConverterFactory(Object, Object)
	 */
    public UIConverterFactory getConverterFactory(Object modelType, Object targetType) {
        Entry key = new Entry(modelType, targetType);
        return this.converterFactoryDictionary.get(key);
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see UIConverterFactoryRegistry#registerConverterFactory(UIConverterFactory)
	 */
    public UIConverterFactoryRegistry registerConverterFactory(UIConverterFactory converterFactory) {
        Entry key = new Entry(converterFactory.getModelType(), converterFactory.getTargetType());
        this.converterFactoryDictionary.put(key, converterFactory);
        return this;
    }

    private static class Entry {

        private final Object modelType;

        private final Object targetType;

        public Entry(Object modelType, Object targetType) {
            this.modelType = modelType;
            this.targetType = targetType;
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o == this) {
                return true;
            }
            boolean equals = false;
            if (o instanceof Entry) {
                Entry other = (Entry) o;
                if (other.modelType == null) {
                    equals = modelType == null;
                } else {
                    equals = other.modelType.equals(modelType);
                }
                if (other.targetType == null) {
                    equals = targetType == null;
                } else {
                    equals = other.targetType.equals(targetType);
                }
            }
            return equals;
        }

        public int hashCode() {
            int hashCode = 37;
            if (modelType != null) {
                hashCode = hashCode + modelType.hashCode();
            }
            if (targetType != null) {
                hashCode = hashCode + targetType.hashCode();
            }
            return hashCode;
        }
    }
}
