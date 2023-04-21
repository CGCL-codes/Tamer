package org.esfinge.comparison.reader;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import javax.persistence.Entity;
import org.esfinge.comparison.ComparisonDescriptor;
import org.esfinge.comparison.utils.BeanUtils;

public class JPAComparisonMetadataReader implements ComparisonMetadataReader {

    @Override
    public void populateContainer(Class c, ComparisonDescriptor descriptor) {
        descriptor.setIdProp(BeanUtils.getIdProp(c));
        for (String prop : descriptor.getProperties()) {
            try {
                Method m = c.getMethod(BeanUtils.propertyToGetter(prop));
                Class returnType = m.getReturnType();
                if (returnType.isAnnotationPresent(Entity.class)) {
                    descriptor.getPropertyDescriptor(prop).setDeepComparison(true);
                } else if (Collection.class.isAssignableFrom(returnType)) {
                    configureCollectionComparison(descriptor, prop, m);
                }
            } catch (Exception e) {
                throw new RuntimeException("Problemas ao recuperar o m�todo", e);
            }
        }
    }

    private void configureCollectionComparison(ComparisonDescriptor descriptor, String prop, Method m) {
        descriptor.getPropertyDescriptor(prop).setCollectionComparison(true);
        Class genericParam = (Class) ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0];
        if (genericParam.isAnnotationPresent(Entity.class)) {
            descriptor.getPropertyDescriptor(prop).setDeepComparison(true);
            descriptor.getPropertyDescriptor(prop).setAssociateType(genericParam);
        }
    }
}
