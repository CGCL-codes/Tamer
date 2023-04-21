package org.nomadpim.core.internal.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.entity.IType;
import org.nomadpim.core.util.factory.IAdapterFactory;
import org.nomadpim.core.util.key.IKeyFactory;
import org.nomadpim.core.util.key.KeyFactory;
import org.nomadpim.core.util.properties.Property;

public final class Type implements IType {

    private final Map<Class, IAdapterFactory> adapterFactories;

    private final KeyFactory keyFactory;

    private final String name;

    private final Set<Property> properties;

    public Type(String name, Set<Property> properties, Set<IAdapterFactory<IEntity, ? extends Object>> adapterFactories) {
        assert name != null;
        assert properties != null;
        this.name = name;
        this.properties = Collections.unmodifiableSet(properties);
        this.keyFactory = new KeyFactory(name);
        this.adapterFactories = new HashMap<Class, IAdapterFactory>();
        for (IAdapterFactory<IEntity, ? extends Object> factory : adapterFactories) {
            this.adapterFactories.put(factory.getAdapterClass(), factory);
        }
    }

    private <S> void checkHasAdapter(Class<S> adapterClass) {
        if (!hasAdapterFor(adapterClass)) {
            throw new IllegalArgumentException("no adapter for class " + adapterClass);
        }
    }

    public <S> S createAdapter(Class<S> adapterClass, IEntity object) {
        return getAdapterFactory(adapterClass).createAdapter(object);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Type)) {
            return false;
        }
        Type other = (Type) obj;
        return other.name.equals(name);
    }

    @SuppressWarnings("unchecked")
    private <S> IAdapterFactory<IEntity, S> getAdapterFactory(Class<S> adapterClass) {
        checkHasAdapter(adapterClass);
        if (adapterFactories.containsKey(adapterClass)) {
            return adapterFactories.get(adapterClass);
        }
        for (Class clazz : adapterFactories.keySet()) {
            if (clazz.equals(adapterClass) || adapterClass.isAssignableFrom(clazz)) {
                return adapterFactories.get(clazz);
            }
        }
        throw new RuntimeException("this point must not be reached");
    }

    public IKeyFactory getKeyFactory() {
        return keyFactory;
    }

    public String getName() {
        return name;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public <S> boolean hasAdapterFor(Class<S> adapterClass) {
        for (Class clazz : adapterFactories.keySet()) {
            if (clazz.equals(adapterClass) || adapterClass.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
