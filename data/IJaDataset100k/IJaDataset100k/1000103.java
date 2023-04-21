package clear.messaging.io.proxy;

import java.math.BigDecimal;
import java.util.UUID;
import clear.type.Enum;
import clear.type.EnumValue;
import org.hibernate.Hibernate;
import org.hibernate.collection.PersistentSet;
import clear.collections.DataCollection;
import flex.messaging.io.BeanProxy;

public class BaseObjectProxy extends BeanProxy {

    public BaseObjectProxy() {
        super();
    }

    public BaseObjectProxy(Object instance) {
        super(instance);
    }

    public Object getValue(Object instance, String propertyName) {
        if (instance == null || propertyName == null) return null;
        BeanProperty bp = getBeanProperty(instance, propertyName);
        if (bp != null) {
            Object object;
            try {
                object = bp.get(instance);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            if (object instanceof UUID) {
                return ((UUID) object).toString();
            }
            if (object instanceof PersistentSet) if (!Hibernate.isInitialized(object)) {
                return null;
            }
            if (!Hibernate.isInitialized(object)) {
                return null;
            }
            return getBeanValue(instance, bp);
        }
        return null;
    }

    public void setValue(Object instance, String propertyName, Object value) {
        BeanProperty bp = getBeanProperty(instance, propertyName);
        if (bp == null) {
            super.setValue(instance, propertyName, value);
            return;
        }
        Class<?> fieldClass = bp.getType();
        Class<?> superClass = fieldClass.getSuperclass();
        if (value instanceof DataCollection) {
            if (((DataCollection) value).isNull()) return;
        }
        if (fieldClass == UUID.class && value instanceof String) {
            if (value != null) {
                super.setValue(instance, propertyName, UUID.fromString((String) value));
                return;
            }
        }
        if (fieldClass == BigDecimal.class) {
            if (value == null) {
                super.setValue(instance, propertyName, BigDecimal.ZERO);
                return;
            }
        }
        if (superClass != null) {
            if (superClass == Enum.class) {
                if (value == null) return;
            }
        }
        if (superClass != null) {
            if (superClass == Enum.class) {
                if (value == null) return;
                if (value instanceof EnumValue) {
                    Enum e;
                    try {
                        e = ((Enum) fieldClass.newInstance());
                        e.setEnumValue(((EnumValue) value).getOrdinal());
                        super.setValue(instance, propertyName, e);
                        return;
                    } catch (InstantiationException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        super.setValue(instance, propertyName, value);
    }

    @Override
    public boolean getIncludeReadOnly() {
        return true;
    }

    private static final long serialVersionUID = 2093322268148362636L;
}
