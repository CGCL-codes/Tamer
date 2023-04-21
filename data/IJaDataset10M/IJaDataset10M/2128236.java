package tk.srmi;

import java.lang.reflect.Method;

/**
 * Represents data about invocation of a methodName of the Remote interface.
 * @version 0.0.3, 05 Jan 2008
 * @author Andrew Kartashev
 * @param <Remote> interface whose methods may be invoked from a non-local
 * virtual machine.
 */
class UnidirectionalInvocation<Remote> implements Invocation<Remote> {

    /**
     * Name of the invoked method.
     */
    private final String methodName;

    /**
     * Invocation parameters.
     */
    private final Object[] params;

    /**
     * Parent invocation.
     */
    private final UnidirectionalInvocation parent;

    /**
     * Types of parameters.
     */
    private final Class[] paramTypes;

    /**
     * Creates a new instance of the class.
     * @param parent parent invocation.
     * @param method invoked method.
     * @param params list of parameters.
     */
    UnidirectionalInvocation(final UnidirectionalInvocation parent, final Method method, final Object[] params) {
        this.parent = parent;
        this.paramTypes = method.getParameterTypes();
        this.methodName = method.getName();
        this.params = params;
    }

    /**
     * Invokes stored method of specified intance with stored parameters.
     * @param target instance method of which is invoked.
     * @return information about result of invocation.
     */
    public InvocationResult execute(final Remote target) {
        try {
            Object returnedValue;
            if (parent == null) {
                Method m = target.getClass().getMethod(methodName, paramTypes);
                returnedValue = m.invoke(target, params);
            } else {
                InvocationResult result = parent.execute(target);
                Object returnedObject = result.getReturnedObject();
                Method m = returnedObject.getClass().getMethod(methodName, paramTypes);
                returnedValue = m.invoke(returnedObject, params);
            }
            return new InvocationResultImpl(true, null, returnedValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
