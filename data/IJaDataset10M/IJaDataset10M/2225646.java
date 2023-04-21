package com.germinus.xpression.cms.jcr.conversions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class ArrayListTypeConverter extends AbstractJCRTypeConverter implements JCRTypeConverter {

    public Object convertValue(Object sourceValue) throws JCRTypeConversionException {
        if (sourceValue == null) return null;
        Class sourceClazz = sourceValue.getClass();
        if (sourceClazz.isArray()) {
            return convertFromArray((Object[]) sourceValue);
        } else if (Collection.class.isAssignableFrom(sourceClazz)) {
            return convertFromCollection((Collection) sourceValue);
        }
        throw new JCRTypeConversionException("Incompatible type [" + sourceClazz + "]");
    }

    private ArrayList convertFromArray(Object[] sourceValue) {
        Collection newList = Arrays.asList(sourceValue);
        return convertFromCollection(newList);
    }

    private ArrayList convertFromCollection(Collection newList) {
        final BasicJCRPropertyConverter basicJCRPropertyConverter = new BasicJCRPropertyConverter();
        ArrayList arrayList = new ArrayList();
        Iterator it = newList.iterator();
        while (it.hasNext()) {
            Object object = (Object) it.next();
            try {
                object = basicJCRPropertyConverter.convertFromJCR(object, Object.class);
            } catch (JCRTypeConversionException e) {
            }
            arrayList.add(object);
        }
        return arrayList;
    }
}
