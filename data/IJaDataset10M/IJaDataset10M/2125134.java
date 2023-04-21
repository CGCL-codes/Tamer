package com.completex.objective.components.persistency.key.impl;

import com.completex.objective.components.persistency.PersistentObject;
import com.completex.objective.components.persistency.key.SimpleNaturalKey;
import com.completex.objective.components.persistency.key.SimpleNaturalKeyFactory;

/**
 * Natural Key Factory Implementation
 * @see SimpleNaturalKeyFactory
 * 
 * @author Gennady Krizhevsky
 */
public class SimpleNaturalKeyFactoryImpl implements SimpleNaturalKeyFactory {

    public SimpleNaturalKey newSimpleNaturalKey(String className, Object[] values) {
        return new SimpleNaturalKeyImpl(className, values);
    }

    public SimpleNaturalKey newSimpleNaturalKey(PersistentObject persistent, int[] keyIndeces) {
        return new SimpleNaturalKeyImpl(persistent, keyIndeces);
    }
}
