package org.nakedobjects.metamodel.facets;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Removes the methods from further processing by subsequent {@link Facet}s.
 */
public interface MethodRemover {

    /**
     * Locate all methods (that the implementation should somehow know about) that match the criteria and
     * remove them from the implementation's list so that they are not considered for subsequent scans.
     * 
     * @param methodScope
     *            - whethre looking for <tt>static</tt> (class) or instance-level methods.
     * @return any methods that were removed.
     */
    List<Method> removeMethods(final MethodScope methodScope, final String prefix, final Class<?> returnType, final boolean canBeVoid, final int paramCount);

    /**
     * Locate all methods (that the implementation should somehow know about) that match the criteria and
     * remove them from the implementation's list so that they are not considered for subsequent scans.
     * 
     * @param forClass
     *            - if <tt>true</tt>, then looking for <tt>static</tt> methods (otherwise instance methods).
     */
    void removeMethod(MethodScope methodScope, String methodName, Class<?> returnType, Class<?>[] parameterTypes);

    void removeMethod(Method method);

    void removeMethods(List<Method> methods);
}
