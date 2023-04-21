package org.yajul.jmx;

import org.yajul.util.ReflectionUtil;

/**
 * Default factory for JMX MBean implementations.
 * <br>
 * User: josh
 * Date: Jan 28, 2009
 * Time: 1:44:34 PM
 */
public class DefaultImplementationProvider implements ImplementationProvider {

    public Class<?> getImplementationClass(String className) throws ClassNotFoundException {
        return ReflectionUtil.getCurrentClassLoader().loadClass(className);
    }

    public <T> T getImplementation(Class<T> implementationClass) {
        return ReflectionUtil.createInstance(implementationClass);
    }
}
