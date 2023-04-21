package ch.trackedbean.business;

import ch.simpleel.accessors.*;
import ch.trackedbean.copier.*;
import ch.trackedbean.copier.annotations.*;

/**
 * Resolver using {@link BeanMapperManager} to resolve mapped fields.<br>
 * You may also define property keys by using {@link #addELMapping(String, Class, String)} or {@link #addPropertyMapping(String, Class, String)}.<br>
 * You may also have a look at {@link SimpleResolver}.
 * 
 * @author M. Hautle
 */
public class MappingResolver extends StaticResolver {

    /**
     * Adds all mappings from the bean type to the in {@link SourceClass#value()} specified class (including it's parents).<br>
     * The path property of the mappings will be used as property key.<br>
     * <b>Already existing definitions will be overridden!</b>
     * 
     * @param bean The bean type
     */
    public void addMappedProperties(Class bean) {
        for (IMappingHolder h : BeanMapperManager.getMapper(bean).getMappingHolders(true)) addAccessorMapping(h.getSourceAccessor().getPath(), bean, h.getDestinationAccessor());
    }

    /**
     * Adds all mappings from the bean type to the given base class.<br>
     * The path property of the mappings will be used as property key.
     * 
     * @param bean The bean type
     * @param base The base class of the source bean
     * @param includeInherited True if the inherited mappings should be included
     * @param override True if existing definitions should be overriden
     */
    public void addMappedProperties(Class bean, Class base, boolean includeInherited, boolean override) {
        for (IMappingHolder h : BeanMapperManager.getMapper(bean).getMappingHolders(base, includeInherited)) addAccessorMapping(h.getSourceAccessor().getPath(), bean, h.getDestinationAccessor(), override);
    }

    /**
     * Fetches the value of the given bean denoted by the passed property key.<br>
     * If the key is not (yet) defined then {@link #addMappedProperties(Class)} will be called, hoping that there is a corresponding mapping.
     * 
     * @param <S> The type
     * @param key The property key
     * @param bean The bean from which to fetch the value
     * @return The fetched value
     * @throws IllegalArgumentException If the specified key is not defined for the given bean
     */
    @Override
    @SuppressWarnings("unchecked")
    public <S> S get(String key, Object bean, S fallback) throws IllegalArgumentException {
        final IValueAccessor accessor = getOrCreateHolder(key, bean.getClass());
        return accessor != null ? (S) accessor.getValue(bean, fallback) : fallback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath(String key, Class type) {
        return getOrCreateHolder(key, type).getPath();
    }

    /**
     * Returns the expression holder for the specified key. If the key is not yet defined it will be created.
     * 
     * @param key The property key
     * @param type The type on which the key gets applied
     * @return The expression holder
     * @throws IllegalArgumentException If the specified key is not defined for the given bean
     */
    private IValueAccessor getOrCreateHolder(String key, Class type) throws IllegalArgumentException {
        Object pk = buildKey(key, type);
        IValueAccessor p = getAccessor(pk);
        if (p != null) return p;
        addMappedProperties(type);
        p = getAccessor(pk);
        if (p == null) throw new IllegalArgumentException(key + " is not defined for " + type.getName());
        return p;
    }
}
