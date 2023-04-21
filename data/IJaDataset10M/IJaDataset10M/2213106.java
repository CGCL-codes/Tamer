package com.enterprise.app.framework.infra.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author 189833
 * 
 */
public class GenericDaoUtils {

    protected static Logger logger = Logger.getLogger(GenericDaoUtils.class);

    public static boolean hasId(Object entity, String idPropertyName) {
        BeanWrapper bw = new BeanWrapperImpl(entity);
        Object propertyValue = bw.getPropertyValue(idPropertyName);
        if (propertyValue == null) {
            logger.debug("hasId: no id was found returning false");
            return false;
        }
        if (propertyValue instanceof Number) {
            boolean isPrimitive = bw.getPropertyType(idPropertyName).isPrimitive();
            Number number = (Number) propertyValue;
            if (isPrimitive && number.longValue() <= 0) {
                logger.debug("hasId: an id was found and it was a number, but it was less than zero returning false");
                return false;
            } else {
                logger.debug("hasId: an id was found and it was a number returning true");
                return true;
            }
        }
        return true;
    }

    public static String searchFieldsForPK(Class<?> aType) {
        String pkName = null;
        Field[] fields = aType.getDeclaredFields();
        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                pkName = field.getName();
                break;
            }
        }
        if (pkName == null && aType.getSuperclass() != null) {
            pkName = searchFieldsForPK((Class<?>) aType.getSuperclass());
        }
        return pkName;
    }

    public static String getEntityName(Class<?> aType) {
        Entity entity = aType.getAnnotation(Entity.class);
        if (entity == null) {
            return aType.getSimpleName();
        }
        String entityName = entity.name();
        if (entityName == null) {
            return aType.getSimpleName();
        } else if (!(entityName.length() > 0)) {
            return aType.getSimpleName();
        } else {
            return entityName;
        }
    }

    public static String searchMethodsForPK(Class<?> aType) {
        String pkName = null;
        Method[] methods = aType.getDeclaredMethods();
        for (Method method : methods) {
            Id id = method.getAnnotation(Id.class);
            if (id != null) {
                pkName = method.getName().substring(4);
                pkName = method.getName().substring(3, 4).toLowerCase() + pkName;
                break;
            }
        }
        if (pkName == null && aType.getSuperclass() != null) {
            pkName = searchMethodsForPK(aType.getSuperclass());
        }
        return pkName;
    }
}
