package org.carp.assemble;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.List;

public class ShortAssemble implements Assemble {

    public void setValue(ResultSet rs, List<Object> data, int index) throws Exception {
        data.add(rs.getShort(index));
    }

    public Object setFieldValue(ResultSet rs, Object entity, Method m, int index) throws Exception {
        short value = rs.getShort(index);
        m.invoke(entity, new Object[] { value });
        return value;
    }

    public Object setFieldValue(ResultSet rs, Object entity, Field f, int index) throws Exception {
        boolean isAccess = f.isAccessible();
        Object value = rs.getObject(index);
        Short b = null;
        if (value != null) b = new Short(value.toString());
        f.setAccessible(true);
        f.set(entity, b);
        f.setAccessible(isAccess);
        return b;
    }

    public Object setFieldValue(Object entity, Field f, Object value) throws Exception {
        boolean isAccess = f.isAccessible();
        Short b = null;
        if (value != null) b = new Short(value.toString());
        f.setAccessible(true);
        f.set(entity, b);
        f.setAccessible(isAccess);
        return b;
    }
}
