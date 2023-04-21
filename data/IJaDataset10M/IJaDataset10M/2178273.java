package com.germinus.xpression.cms.jcr.conversions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import com.germinus.xpression.cms.jcr.IdReference;
import com.germinus.xpression.cms.jcr.JCRUtil;

public class IdReferenceArrayTypeConverter implements JCRTypeConverter {

    public Object convertValue(Object sourceValue) throws JCRTypeConversionException {
        if (sourceValue == null) return null;
        Class sourceClazz = sourceValue.getClass();
        if (sourceClazz.isArray()) {
            return convertFromArray((Object[]) sourceValue);
        } else if (Collection.class.isAssignableFrom(sourceClazz)) {
            return convertFromCollection((Collection) sourceValue);
        }
        return new JCRTypeConversionException("Source value is not an Array nor Collection");
    }

    private IdReference[] convertFromArray(Object[] sourceValue) {
        Collection newList = Arrays.asList(sourceValue);
        return convertFromCollection(newList);
    }

    private IdReference[] convertFromCollection(Collection newList) {
        Iterator it = newList.iterator();
        Collection convertedIdReferences = new ArrayList();
        JCRPropertyConverter pc = JCRUtil.getPropertyConverter();
        while (it.hasNext()) {
            Object element = (Object) it.next();
            Object converted;
            try {
                converted = pc.convertToJCR(element);
                if (converted instanceof IdReference) {
                    IdReference idreference = (IdReference) converted;
                    convertedIdReferences.add(idreference);
                }
            } catch (JCRTypeConversionException e) {
                e.printStackTrace();
            }
        }
        return (IdReference[]) convertedIdReferences.toArray(new IdReference[] {});
    }

    public boolean isCompatible(Object sourceValue) {
        if (sourceValue == null) return true;
        if (sourceValue.getClass().isArray()) {
            Object[] values = (Object[]) sourceValue;
            if (values.length == 0) return true;
            Object anObject = values[0];
            return testSubElement(anObject);
        } else if (sourceValue instanceof Collection) {
            Collection collection = (Collection) sourceValue;
            if (collection.size() == 0) return false;
            Object anObject = collection.iterator().next();
            return testSubElement(anObject);
        }
        return false;
    }

    private boolean testSubElement(Object anObject) {
        return anObject instanceof IdReference;
    }
}
