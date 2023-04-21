package org.depunit;

import java.lang.reflect.*;
import java.util.*;

public class BeanUtil {

    public static void initializeClass(Class klass, Map<String, ? extends Object> dataSet, Object instance) throws InitializationException {
        Method me = null;
        try {
            Method[] methods = klass.getMethods();
            Map<String, Method> methodMap = new HashMap();
            for (Method m : methods) methodMap.put(m.getName().toLowerCase(), m);
            Set<String> paramNames = dataSet.keySet();
            for (String param : paramNames) {
                me = methodMap.get("set" + param.toLowerCase());
                if (me == null) throw new InitializationException("Unable to locate method on " + klass.getName() + " to set param " + param);
                if (dataSet.get(param) instanceof String) {
                    Class type = me.getParameterTypes()[0];
                    if (type == String.class) me.invoke(instance, dataSet.get(param)); else if (type == int.class) me.invoke(instance, new Integer((String) dataSet.get(param))); else if (type == boolean.class) me.invoke(instance, new Boolean((String) dataSet.get(param))); else System.out.println("Param type = " + type);
                } else me.invoke(instance, dataSet.get(param));
            }
        } catch (IllegalAccessException iae) {
            throw new InitializationException(iae);
        } catch (InvocationTargetException ite) {
            throw new InitializationException(ite.getCause());
        }
    }
}
