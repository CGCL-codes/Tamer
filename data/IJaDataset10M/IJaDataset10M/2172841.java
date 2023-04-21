package org.mikha.utils.rmi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Implementation: marshals outgoing calls to remote services.
 * @author dmitrym
 */
public abstract class AbstractRmiProxy implements InvocationHandler {

    private static final Method METHOD_HASHCODE;

    private static final Method METHOD_EQUALS;

    private static final Method METHOD_TO_STRING;

    static {
        try {
            METHOD_HASHCODE = Object.class.getDeclaredMethod("hashCode");
            METHOD_EQUALS = Object.class.getDeclaredMethod("equals", Object.class);
            METHOD_TO_STRING = Object.class.getDeclaredMethod("toString");
        } catch (Exception ex) {
            throw new RuntimeException("WTF?", ex);
        }
    }

    protected final ServiceDefinition definition;

    public AbstractRmiProxy(ServiceDefinition definition) {
        this.definition = definition;
    }

    public ServiceDefinition getDefinition() {
        return this.definition;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Integer methodId = definition.getMethodToId().get(method);
        if (methodId == null) {
            if (method == METHOD_HASHCODE) {
                return hashCode();
            }
            if (method == METHOD_EQUALS) {
                return equals(args[0]);
            }
            if (method == METHOD_TO_STRING) {
                return toString();
            }
            throw new NoSuchMethodException(method.toGenericString());
        }
        RemoteCallResult rc = invoke(methodId.intValue(), args);
        rc.waitForCompletion();
        if (rc.getException() != null) {
            throw rc.getException();
        } else {
            return rc.getResult();
        }
    }

    protected abstract RemoteCallResult invoke(int methodId, Object[] args) throws Throwable;
}
