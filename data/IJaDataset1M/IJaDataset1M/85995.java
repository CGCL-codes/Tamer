package net.woodstock.rockapi.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import net.woodstock.rockapi.sys.SysLogger;
import net.woodstock.rockapi.util.BeanInfo;
import net.woodstock.rockapi.util.FieldInfo;

public abstract class ObjectUtils {

    public static final int HASH_PRIME = 31;

    private static final char PROPERTY_SEPARATOR = '.';

    public static void copyAttributes(Object from, Object to, Class<?>[] ignoredTypes) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        BeanInfo beanInfoFrom = BeanInfo.getBeanInfo(from.getClass());
        BeanInfo beanInfoTo = BeanInfo.getBeanInfo(to.getClass());
        outer: for (FieldInfo fieldInfo : beanInfoFrom.getFieldsInfo()) {
            try {
                FieldInfo fieldInfoTo = beanInfoTo.getFieldInfo(fieldInfo.getFieldName());
                if (ignoredTypes != null) {
                    for (Class<?> c : ignoredTypes) {
                        if ((c.isAssignableFrom(fieldInfo.getFieldType())) || (c.isAssignableFrom(fieldInfoTo.getFieldType()))) {
                            continue outer;
                        }
                    }
                }
                if (!fieldInfoTo.getFieldType().isAssignableFrom(fieldInfo.getFieldType())) {
                    continue;
                }
                Object tmp = fieldInfo.getFieldValue(from);
                fieldInfoTo.setFieldValue(to, tmp);
            } catch (NoSuchFieldException e) {
                SysLogger.getLogger().debug(e.getMessage(), e);
            }
        }
    }

    public static void copyAttributes(Object from, Object to, String[] ignoredAttributes) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        BeanInfo beanInfoFrom = BeanInfo.getBeanInfo(from.getClass());
        BeanInfo beanInfoTo = BeanInfo.getBeanInfo(to.getClass());
        outer: for (FieldInfo fieldInfo : beanInfoFrom.getFieldsInfo()) {
            try {
                FieldInfo fieldInfoTo = beanInfoTo.getFieldInfo(fieldInfo.getFieldName());
                if (ignoredAttributes != null) {
                    for (String s : ignoredAttributes) {
                        if (s.equals(fieldInfo.getFieldName())) {
                            continue outer;
                        }
                    }
                }
                if (!fieldInfoTo.getFieldType().isAssignableFrom(fieldInfo.getFieldType())) {
                    continue;
                }
                Object tmp = fieldInfo.getFieldValue(from);
                fieldInfoTo.setFieldValue(to, tmp);
            } catch (NoSuchFieldException e) {
                SysLogger.getLogger().debug(e.getMessage(), e);
            }
        }
    }

    public static boolean equals(Object o1, Object o2) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if ((o1 == null) || (o2 == null)) {
            return false;
        }
        if (!o1.getClass().isAssignableFrom(o2.getClass())) {
            return false;
        }
        BeanInfo beanInfo = BeanInfo.getBeanInfo(o1.getClass());
        Collection<FieldInfo> fields = beanInfo.getFieldsInfo();
        for (FieldInfo field : fields) {
            Object v1 = field.getFieldValue(o1);
            Object v2 = field.getFieldValue(o2);
            if (v1 != null) {
                if (v2 == null) {
                    return false;
                }
                if (!v1.equals(v2)) {
                    return false;
                }
            } else if (v2 != null) {
                return false;
            }
        }
        return true;
    }

    public static int hashCode(Object obj) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int result = 1;
        BeanInfo beanInfo = BeanInfo.getBeanInfo(obj.getClass());
        Collection<FieldInfo> fields = beanInfo.getFieldsInfo();
        for (FieldInfo field : fields) {
            Object value = field.getFieldValue(obj);
            if (value != null) {
                result = ObjectUtils.HASH_PRIME * result + value.hashCode();
            } else {
                result = ObjectUtils.HASH_PRIME * result;
            }
        }
        return result;
    }

    private ObjectUtils() {
    }

    public static Object getObjectAttribute(Object o, String name) throws NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (o == null) {
            return null;
        }
        BeanInfo beanInfo = BeanInfo.getBeanInfo(o.getClass());
        if (name.indexOf(ObjectUtils.PROPERTY_SEPARATOR) != -1) {
            String fieldName = name.substring(0, name.indexOf(ObjectUtils.PROPERTY_SEPARATOR));
            name = name.substring(name.indexOf(ObjectUtils.PROPERTY_SEPARATOR) + 1);
            FieldInfo fieldInfo = beanInfo.getFieldInfo(fieldName);
            Object tmp = fieldInfo.getFieldValue(o);
            return ObjectUtils.getObjectAttribute(tmp, name);
        }
        FieldInfo fieldInfo = beanInfo.getFieldInfo(name);
        return fieldInfo.getFieldValue(o);
    }
}
