package org.databene.commons.converter;

import java.lang.reflect.Method;
import org.databene.commons.BeanUtil;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;

/**
 * {@link Converter} implementation which invokes a static method of an arbitrary class 
 * with the object to be converted as argument.<br/><br/>
 * Created: 27.02.2010 06:49:13
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class StaticTargetClassMethodInvoker<S, T> extends ThreadSafeConverter<S, T> {

    private Method method;

    protected StaticTargetClassMethodInvoker(Class<S> sourceType, Class<T> targetType, Method method) {
        super(sourceType, targetType);
        this.method = method;
    }

    @SuppressWarnings("unchecked")
    public T convert(S sourceValue) throws ConversionException {
        return (T) BeanUtil.invoke(null, method, false, sourceValue);
    }
}
