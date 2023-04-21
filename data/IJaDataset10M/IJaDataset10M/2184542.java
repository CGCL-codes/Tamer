package org.databene.commons.accessor;

import org.databene.commons.Accessor;
import org.databene.commons.BeanUtil;
import org.databene.commons.Composite;
import org.databene.commons.ConfigurationError;
import org.databene.commons.Context;
import org.databene.commons.Escalator;
import org.databene.commons.LoggerEscalator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Get values from Maps, Contexts, Composites and JavaBeans.<br/>
 * <br/>
 * Created: 12.06.2007 18:36:11
 * @author Volker Bergmann
 */
public class FeatureAccessor<C, V> implements Accessor<C, V> {

    private static Logger logger = LoggerFactory.getLogger(FeatureAccessor.class);

    private static Escalator escalator = new LoggerEscalator();

    private String featureName;

    public FeatureAccessor(String featureName) {
        this(featureName, true);
    }

    public FeatureAccessor(String featureName, boolean strict) {
        if (logger.isDebugEnabled()) logger.debug("FeatureAccessor(" + featureName + ", " + strict + ")");
        this.featureName = featureName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    @SuppressWarnings("unchecked")
    public V getValue(C target) {
        return (V) getValue(target, featureName);
    }

    public static Object getValue(Object target, String featureName) {
        if (logger.isDebugEnabled()) logger.debug("getValue(" + target + ", " + featureName + ")");
        return getValue(target, featureName, true);
    }

    @SuppressWarnings("unchecked")
    public static Object getValue(Object target, String featureName, boolean strict) {
        if (target == null) return null; else if (target instanceof Map) return ((Map<String, Object>) target).get(featureName); else if (target instanceof Context) return ((Context) target).get(featureName); else if (target instanceof Composite) return ((Composite) target).getComponent(featureName); else {
            PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(target.getClass(), featureName);
            if (propertyDescriptor != null) {
                try {
                    return propertyDescriptor.getReadMethod().invoke(target);
                } catch (Exception e) {
                    throw new ConfigurationError("Unable to access feature '" + featureName + "'", e);
                }
            } else {
                Class<?> type = ((target instanceof Class) ? (Class<?>) target : target.getClass());
                Field field = BeanUtil.getField(type, featureName);
                if (field != null) return BeanUtil.getFieldValue(field, target, false);
            }
        }
        if (strict) throw new UnsupportedOperationException(target.getClass() + " does not support a feature '" + featureName + "'"); else escalator.escalate("Feature '" + featureName + "' not found in object " + target, FeatureAccessor.class, null);
        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + featureName + ']';
    }
}
