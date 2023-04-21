package org.xfeep.asura.core.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import org.xfeep.asura.core.reflect.TypeItem;

/**
 * DynamicOReference is enough now.
 * this class will be removed at 1.0M2
 * @author zhang yuexiang
 *
 */
@Deprecated
public class ConfigVirtualFieldItem implements TypeItem {

    protected Class<?> hostType;

    protected String configId;

    protected TypeItem[] configProperties;

    public ConfigVirtualFieldItem() {
    }

    public ConfigVirtualFieldItem(Class<?> hostType, String configId, TypeItem[] configProperties) {
        super();
        this.hostType = hostType;
        this.configId = configId;
        this.configProperties = configProperties;
    }

    public Class<?> getHostType() {
        return hostType;
    }

    public void setHostType(Class<?> hostType) {
        this.hostType = hostType;
    }

    public TypeItem[] getConfigProperties() {
        return configProperties;
    }

    public void setConfigProperties(TypeItem[] configProperties) {
        this.configProperties = configProperties;
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return hostType.getAnnotation(annotationType);
    }

    public Annotation[] getAnnotations() {
        return null;
    }

    public Class getDeclaringClass() {
        return hostType;
    }

    public Class[] getMemberTypes() {
        return null;
    }

    public String getName() {
        return configId;
    }

    public Class getType() {
        return ConfigService.class;
    }

    public Object getValue(Object target) throws InvocationTargetException, IllegalAccessException {
        throw new UnsupportedOperationException("getValue(Object target) not support yet");
    }

    public boolean isReadOnly() {
        return false;
    }

    public void setValue(Object target, Object v) throws InvocationTargetException, IllegalAccessException {
        if (v == null) {
            for (TypeItem cp : configProperties) {
                if (!cp.getType().isPrimitive()) {
                    cp.setValue(target, null);
                }
            }
        } else {
            ConfigService c = (ConfigService) v;
            for (TypeItem cp : configProperties) {
                if (c.contains(cp.getName())) {
                    cp.setValue(target, c.getLazyResolvableProperty(cp.getName(), cp.getType(), cp.getMemberTypes()));
                }
            }
        }
    }

    public boolean isWriteOnly() {
        return true;
    }
}
