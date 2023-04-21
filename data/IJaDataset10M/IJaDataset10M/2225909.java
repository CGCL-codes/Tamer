package com.trendsoft.eye;

import java.lang.reflect.Method;

/**
 * Runtime module callback interface. Notifies module that method is about to be executed or that
 * method execution is complete.
 *
 * @author vasiliy
 * @param <CC> class context
 * @param <IC> instance context
 */
public interface EyeMethodCallback<CC, IC> {

    /**
     * Notifies about method execution before actual excution.
     *
     * @param instance instance being invoked
     * @param method method being invoked
     * @param classContext context associated with class.
     * @param instanceContext context associated with instance
     * @return call context to be passed to the {@link #end(Object, Method, Object, Object, Object)}
     *         method.
     */
    Object start(Object instance, Method method, CC classContext, IC instanceContext);

    /**
     * @param instance instance being invoked
     * @param method method being invoked
     * @param classContext context associated with class.
     * @param instanceContext context associated with instance
     * @param callContext context created by {@link #start(Object, Method, Object, Object)} method.
     */
    void end(Object instance, Method method, CC classContext, IC instanceContext, Object callContext);
}
