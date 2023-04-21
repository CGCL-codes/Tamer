package org.nomadpim.core.internal.entity.io;

import org.nomadpim.core.entity.io.IFieldConverter;
import org.nomadpim.core.entity.io.ILocalResolver;
import org.nomadpim.core.util.converter.ConversionException;
import org.nomadpim.core.util.converter.ITwoWayConverter;

public class ConverterFieldConverterAdapter<T> implements IFieldConverter<T> {

    private final ITwoWayConverter<T, String> delegate;

    public ConverterFieldConverterAdapter(ITwoWayConverter<T, String> delegate) {
        this.delegate = delegate;
    }

    public String format(T t) throws ConversionException {
        return delegate.convertFrom(t);
    }

    public T parse(String value, ILocalResolver resolver) throws ConversionException {
        return delegate.convertTo(value);
    }
}
