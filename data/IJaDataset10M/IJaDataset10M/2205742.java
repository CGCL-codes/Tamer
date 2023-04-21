package org.caleigo.toolkit.tunnel;

import java.lang.reflect.*;
import java.util.*;
import org.caleigo.toolkit.log.*;

public class ProxyHandler implements IMessageConsumer {

    protected int mCurrentProxyID;

    protected ITunnel mTunnel;

    protected Hashtable mProxyObjects;

    protected Hashtable mRemoteInvocationHandlers;

    public ProxyHandler(ITunnel tunnel) {
        mTunnel = tunnel;
        mProxyObjects = new Hashtable();
        mRemoteInvocationHandlers = new Hashtable();
        mTunnel.addMessageConsumer(this);
    }

    /** Creates a proxy for all interfaces that the provided object and its
     * superclasses implement. Invocations on methods in the interfaces will be
     * forwarded to the provided object.
     */
    public Object createProxy(Object obj) {
        if (!(obj instanceof IDistributable)) throw new IllegalArgumentException("Object must be an IDistributable");
        List proxyInterfaces = new ArrayList();
        Class currentClass = obj.getClass();
        while (currentClass != null) {
            Class[] interfaces = currentClass.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) proxyInterfaces.add(interfaces[i]);
            currentClass = currentClass.getSuperclass();
        }
        Class[] proxyInterfaceClasses = (Class[]) proxyInterfaces.toArray(new Class[0]);
        Log.print(this, "Creating proxy for interfaces:");
        for (int i = 0; i < proxyInterfaceClasses.length; i++) Log.print(this, proxyInterfaceClasses[i].getName());
        int proxyID = this.generateProxyID();
        Object proxy = Proxy.newProxyInstance(obj.getClass().getClassLoader(), proxyInterfaceClasses, new DynamicRemoteProxy(proxyID, this, proxyInterfaceClasses));
        mProxyObjects.put(new Integer(proxyID), obj);
        return proxy;
    }

    /** Creates a proxy for the provided interfaces.
     *
     * @param handler   object that will handle invocations on the created proxy.
     *                  The handler is allowed to be <code>null</code> in which
     *                  case invocation handlers must be registered for the
     *                  interfaces.
     */
    public Object createProxy(Class[] interfaceClasses, Object handler) {
        if (interfaceClasses.length == 0) throw new IllegalArgumentException("No interface classes found");
        Log.print(this, "Creating proxy for interfaces:");
        for (int i = 0; i < interfaceClasses.length; i++) Log.print(this, interfaceClasses[i].getName());
        int proxyID = this.generateProxyID();
        Object proxy = Proxy.newProxyInstance(interfaceClasses[0].getClassLoader(), interfaceClasses, new DynamicRemoteProxy(proxyID, this, interfaceClasses));
        if (handler != null) mProxyObjects.put(new Integer(proxyID), handler);
        return proxy;
    }

    public void finishProxy(Object proxy) {
        Log.print(this, "Proxy finished");
        mProxyObjects.remove(new Integer(((DynamicRemoteProxy) Proxy.getInvocationHandler(proxy)).getID()));
    }

    /** Register an object that handles remote invokations for the provided
     * interface.
     */
    public void registerRemoteInvocationHandler(Class interfaceClass, Object handler) {
        if (!interfaceClass.isInterface()) throw new IllegalArgumentException("The interface class must be an interface");
        if (!interfaceClass.isInstance(handler)) throw new IllegalArgumentException("Handler must be an instance of the interface class");
        mRemoteInvocationHandlers.put(interfaceClass.getName(), handler);
    }

    public ITunnel getTunnel() {
        return mTunnel;
    }

    /**
     * Returns <code>true</code> if this IMessageConsumer accepts the message.
     * This method is allways called before <code>consumeMessage</code> for any
     * given message.
     */
    public boolean acceptsMessage(Object message) {
        return (message instanceof DynamicRemoteProxy.IProxyInvocationMessage || message instanceof DynamicRemoteProxy.IProxyCleanUpMessage);
    }

    /**
     * Tells the IMessageConsumer to consume a message.
     */
    public void consumeMessage(Object message) {
        Log.print(this, "Clean up message received");
        mProxyObjects.remove(new Integer(((DynamicRemoteProxy.IProxyCleanUpMessage) message).getProxyID()));
    }

    /**
     * Tells the IMessageConsumer to consume a message and returns a new message
     * that will be delivered to the sender of the originial message.
     */
    public Object answerMessage(Object message) {
        Log.print(this, "answerMessage");
        String[] interfaceClassNames = ((DynamicRemoteProxy.IProxyInvocationMessage) message).getInterfaceClassNames();
        String methodName = ((DynamicRemoteProxy.IProxyInvocationMessage) message).getMethodName();
        Method method = null;
        Object target = null;
        Object[] arguments = ((DynamicRemoteProxy.IProxyInvocationMessage) message).getArguments();
        if (arguments != null) for (int i = 0; i < arguments.length; i++) if (arguments[i] != null && Proxy.isProxyClass(arguments[i].getClass())) ((DynamicRemoteProxy) Proxy.getInvocationHandler(arguments[i])).setProxyHandler(this);
        target = mProxyObjects.get(new Integer(((DynamicRemoteProxy.IProxyInvocationMessage) message).getProxyID()));
        if (target != null) method = this.findMatchingMethod(target.getClass(), methodName, arguments);
        for (int i = 0; i < interfaceClassNames.length && method == null; i++) if (mRemoteInvocationHandlers.get(interfaceClassNames[i]) != null) try {
            method = this.findMatchingMethod(Class.forName(interfaceClassNames[i]), methodName, arguments);
            if (method != null) target = mRemoteInvocationHandlers.get(interfaceClassNames[i]);
        } catch (ClassNotFoundException e) {
            Log.printError(this, "Couldn't find class", e);
        }
        try {
            Log.print(this, "Invoking method...");
            Object returnValue = method.invoke(target, arguments);
            Log.print(this, "done!");
            if (returnValue instanceof IDistributable) returnValue = this.createProxy(returnValue);
            return new DynamicRemoteProxy.DefaultProxyReturnMessage(returnValue);
        } catch (InvocationTargetException targetException) {
            Log.printError(this, "Couldn't invoke target method", targetException);
            return new DynamicRemoteProxy.DefaultProxyExceptionMessage(targetException.getTargetException().getClass(), targetException.getTargetException().getMessage());
        } catch (Throwable t) {
            Log.printError(this, "Unknown error", t);
            return new DynamicRemoteProxy.DefaultProxyExceptionMessage(t.getClass(), t.getMessage());
        }
    }

    protected synchronized int generateProxyID() {
        if (mCurrentProxyID < Integer.MAX_VALUE) mCurrentProxyID++; else mCurrentProxyID = 1;
        return mCurrentProxyID;
    }

    protected Method findMatchingMethod(Class targetClass, String methodName, Object[] arguments) {
        Method method = null;
        try {
            Method[] methods = targetClass.getMethods();
            for (int m = 0; m < methods.length && method == null; m++) {
                if (methods[m].getName().compareTo(methodName) == 0) {
                    Class[] paramClasses = methods[m].getParameterTypes();
                    if (arguments == null && paramClasses.length > 0 || arguments != null && paramClasses.length != arguments.length) continue;
                    boolean match = true;
                    for (int c = 0; c < paramClasses.length && match; c++) if (arguments[c] != null && !paramClasses[c].isInstance(arguments[c])) match = false;
                    if (match) method = methods[m];
                }
            }
        } catch (Exception e) {
        }
        return method;
    }
}
