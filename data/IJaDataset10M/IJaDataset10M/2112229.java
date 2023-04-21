package org.taak.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import org.apache.log4j.Logger;
import org.taak.*;
import org.taak.error.*;
import org.taak.operator.*;

public class ObjectUtil {

    private static Logger log = Logger.getLogger(ObjectUtil.class);

    /**
     * Returns the named member of on object.
     */
    public static Object getMember(Object obj, String name) {
        if (obj == null) {
            throw new NullArgument("First argument to getMember() is null");
        }
        if (name == null) {
            throw new NullArgument("Name argument to getMember() is null");
        }
        Object member;
        if (obj instanceof Obj) {
            member = ((Obj) obj).get(name);
        } else {
            Class type = obj.getClass();
            Object accessor = TypeUtil.getAccessor(type, name);
            if (accessor == null) {
                throw new TypeError("No accessible member " + name + " of type " + type.getName());
            }
            if (accessor instanceof Field) {
                try {
                    Field field = (Field) accessor;
                    member = field.get(obj);
                } catch (Exception e) {
                    throw new TypeError(e.getMessage(), e);
                }
            } else if (accessor instanceof Method) {
                try {
                    Method method = (Method) accessor;
                    member = method.invoke(obj, Code.EMPTY_OBJECT_ARRAY);
                } catch (Exception e) {
                    throw new TypeError(e.getMessage(), e);
                }
            } else {
                throw new TypeError("No accessible member " + name + " of type " + type.getName());
            }
        }
        return member;
    }
}
