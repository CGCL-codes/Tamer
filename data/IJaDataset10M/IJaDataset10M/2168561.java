package com.adhocit.webapp.jsp;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;

/**
 * {@link ELResolver} which escapes XML in String values.
 */
public class EscapeXmlELResolver extends ELResolver {

    private ELResolver originalResolver;

    private ThreadLocal<Boolean> gettingValue = new ThreadLocal<Boolean>() {

        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    private ELResolver getOriginalResolver(ELContext context) {
        if (originalResolver == null) {
            originalResolver = context.getELResolver();
        }
        return originalResolver;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return getOriginalResolver(context).getCommonPropertyType(context, base);
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return getOriginalResolver(context).getFeatureDescriptors(context, base);
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
        return getOriginalResolver(context).getType(context, base, property);
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
        if (gettingValue.get()) {
            return null;
        }
        gettingValue.set(true);
        Object value = getOriginalResolver(context).getValue(context, base, property);
        gettingValue.set(false);
        if (value instanceof String) {
            value = EscapeXml.escape((String) value);
        }
        return value;
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
        return getOriginalResolver(context).isReadOnly(context, base, property);
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) throws NullPointerException, PropertyNotFoundException, PropertyNotWritableException, ELException {
        getOriginalResolver(context).setValue(context, base, property, value);
    }
}
