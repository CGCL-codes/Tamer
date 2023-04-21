package org.piuframework.service.impl;

import java.lang.reflect.Constructor;
import org.piuframework.context.ApplicationContext;
import org.piuframework.service.InstantiationException;
import org.piuframework.util.ClassUtils;

/**
 * Internal helper factory used to instantiate interceptor objects, lifecycle handler objects, invocation handler objects
 *
 * @author Dirk Mascher
 */
class InternalObjectFactory {

    static Object createInternalObject(String className, ApplicationContext context) throws InstantiationException {
        return createInternalObject(null, className, context);
    }

    static Object createInternalObject(String name, String className, ApplicationContext context) throws InstantiationException {
        try {
            Class clazz = ClassUtils.forName(className);
            Constructor constructor = clazz.getConstructor(new Class[] { ApplicationContext.class, String.class });
            Object[] constructorArgs = new Object[] { context, name };
            return constructor.newInstance(constructorArgs);
        } catch (Throwable t) {
            throw new InstantiationException(name, className, t);
        }
    }
}
