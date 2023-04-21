package org.carp.assemble;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Ref;
import java.sql.ResultSet;
import java.util.List;

public class RefAssemble implements Assemble {

    public void setValue(ResultSet rs, List<Object> data, int index) throws Exception {
        data.add(rs.getRef(index));
    }

    public Object setFieldValue(ResultSet rs, Object entity, Method m, int index) throws Exception {
        Ref value = rs.getRef(index);
        m.invoke(entity, new Object[] { value });
        return value;
    }

    public Object setFieldValue(ResultSet rs, Object entity, Field f, int index) throws Exception {
        boolean isAccess = f.isAccessible();
        Object value = rs.getRef(index);
        f.setAccessible(true);
        f.set(entity, value);
        f.setAccessible(isAccess);
        return value;
    }

    public Object setFieldValue(Object entity, Field f, Object value) throws Exception {
        boolean isAccess = f.isAccessible();
        f.setAccessible(true);
        f.set(entity, value);
        f.setAccessible(isAccess);
        return value;
    }
}
