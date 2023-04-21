package net.sf.joyaop.framework;

import java.lang.reflect.Method;

/**
 * @author Shen Li
 */
public interface Pointcut {

    boolean matches(Class clazz);

    boolean matches(Class clazz, Method method);
}
