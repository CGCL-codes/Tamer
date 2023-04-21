package org.h3t.aspect;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.CallbackFilter;

public class EntityCallbackFilter implements CallbackFilter {

    public int accept(Method method) {
        if (method.getDeclaringClass().equals(EntitySerializer.class)) {
            return 1;
        } else {
            return 0;
        }
    }
}
