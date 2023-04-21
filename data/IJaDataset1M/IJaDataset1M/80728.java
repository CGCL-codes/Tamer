package net.sf.doolin.util;

import net.sf.sido.PropertyAccessor;
import org.apache.commons.lang.StringUtils;

public abstract class AbstractPropertySet implements PropertyAccessor {

    protected abstract PropertyAccessor createPropertySet(PropertyAccessor parentSet, String nameInParent);

    protected abstract Object getSimple(String name);

    @Override
    public Object getValue(String propertyPath) {
        if (StringUtils.isBlank(propertyPath)) {
            throw new IllegalArgumentException("propertyPath must not be blank");
        }
        String prefix = StringUtils.substringBefore(propertyPath, SEPARATOR);
        String leftOver = StringUtils.substringAfter(propertyPath, SEPARATOR);
        if (StringUtils.isNotBlank(leftOver)) {
            Object prefixProperty = getSimple(prefix);
            if (prefixProperty == null) {
                return null;
            } else {
                return Utils.getProperty(prefixProperty, leftOver);
            }
        } else {
            return getSimple(prefix);
        }
    }

    protected abstract void setSimple(String name, Object value);

    @Override
    public PropertyAccessor setValue(String propertyPath, Object value) {
        if (StringUtils.isBlank(propertyPath)) {
            throw new IllegalArgumentException("propertyPath must not be blank");
        }
        String prefix = StringUtils.substringBefore(propertyPath, SEPARATOR);
        String leftOver = StringUtils.substringAfter(propertyPath, SEPARATOR);
        if (StringUtils.isNotBlank(leftOver)) {
            Object prefixProperty = getSimple(prefix);
            if (prefixProperty == null) {
                PropertyAccessor prefixPropertySet = createPropertySet(this, prefix);
                setSimple(prefix, prefixPropertySet);
                Utils.setProperty(prefixPropertySet, leftOver, value);
            } else {
                Utils.setProperty(prefixProperty, leftOver, value);
            }
        } else {
            setSimple(prefix, value);
        }
        return this;
    }
}
