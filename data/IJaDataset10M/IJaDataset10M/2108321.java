package sqlTools.orm;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ReflectionHelperORM {

    public static ORMObject getSampleInstance(Class clazz) {
        if (!(ORMObject.class.isAssignableFrom(clazz))) {
            throw new RuntimeException("[ReflectionHelperORM.getSampleInstance] " + clazz + " is not derived from ORMObject");
        }
        try {
            ORMObject obj = (ORMObject) instanceMap.get(clazz);
            if (obj == null) {
                obj = (ORMObject) clazz.newInstance();
                obj.getTableName();
                obj.getORMFields();
                obj.getPrimaryKeys();
                obj.getOneToOneRelations();
                instanceMap.put(clazz, obj);
            }
            return ORMObject.cloneORM(obj);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            throw new RuntimeException("[ReflectionHelperORM.getSampleInstance] (IllegalAccessException) can't create instance of:" + clazz);
        } catch (InstantiationException ine) {
            ine.printStackTrace();
            throw new RuntimeException("[ReflectionHelperORM.getSampleInstance] (InstantiationException) can't create instance of:" + clazz);
        }
    }

    private static final HashMap instanceMap = new HashMap();

    static void setPrimaryKeyValues(ORMObject orm, Object[] primKeys) {
        ORMField[] fields = orm.getPrimaryKeys();
        if (primKeys.length != fields.length) {
            throw new RuntimeException("[ORMObject.get] provided primary keys don't match up. You provided: " + primKeys.length + " " + orm.getTableName() + " has " + fields.length);
        }
        for (int i = 0; i != fields.length; ++i) {
            fields[i].setFieldValue(orm, primKeys[i]);
        }
    }

    static Object[] getPrimaryKeyValues(ORMObject orm) {
        ORMField[] fields = orm.getPrimaryKeys();
        return getFieldValues(orm, fields);
    }

    static Object[] getFieldValues(ORMObject orm, ORMField[] fields) {
        Object[] ret = new Object[fields.length];
        for (int i = 0; i != fields.length; ++i) {
            ret[i] = fields[i].getValue(orm);
        }
        return ret;
    }

    static ORMObject getFieldValue(ORMObject orm, java.lang.reflect.Field field) {
        try {
            return (ORMObject) field.get(orm);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            throw new RuntimeException("[ReflectionHelperORM.getFieldValue] (IllegalAccessException) can't access: " + field.getName() + " on object: " + orm.getTableName());
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            throw new RuntimeException("[ReflectionHelperORM.getFieldValue] (IllegalArgumentException) can't access: " + field.getName() + " on object: " + orm.getTableName());
        }
    }

    static void setField(Field f, ORMObject orm, ORMObject value) {
        try {
            f.set(orm, value);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            throw new RuntimeException("[ReflectionHelperORM.setField] (IllegalAccessException) trying to set: " + f + " to: " + value);
        }
    }
}
