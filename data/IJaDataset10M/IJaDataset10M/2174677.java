package org.nomadpim.core.entity.adapter;

import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.util.converter.ConversionException;
import org.nomadpim.core.util.converter.IConverter;

public class EntityToAdapterConverter<T> implements IConverter<IEntity, T> {

    private final Class<T> clazz;

    public EntityToAdapterConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T convert(IEntity entity) throws ConversionException {
        return entity.getGenericAdapter(clazz);
    }
}
