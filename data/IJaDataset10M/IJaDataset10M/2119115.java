package org.jaffa.persistence.engines.jdbcengine.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import org.jaffa.persistence.IPersistent;

/** This class is a factory for obtaining IPersistent instances.
 */
public class PersistentInstanceFactory {

    /** Generates an appropriate instance for the input persistentClass.
     * If the persistentClass is a 'Class', then it should implement the 'IPersistent' interface. The persistence engine will simply instantiate the class.
     * If the persistentClass is an 'Interface', then the persistence engine will generate a dynamic proxy, to implement the IPersistent and the 'persistentClass' interfaces.
     * A RuntimeException will be thrown if any error occurs during instantiation.
     * @param persistentClass The actual persistentClass which can represent a 'Class' or an 'Interface'
     * @return an instance implementing the IPersistent interface.
     */
    public static IPersistent newPersistentInstance(Class persistentClass) {
        try {
            if (persistentClass.isInterface()) {
                Class[] interfaces = IPersistent.class.isAssignableFrom(persistentClass) ? new Class[] { persistentClass } : new Class[] { IPersistent.class, persistentClass };
                return (IPersistent) Proxy.newProxyInstance(persistentClass.getClassLoader(), interfaces, new PersistentInstanceInvocationHandler(persistentClass));
            } else {
                return (IPersistent) persistentClass.newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in instantiating a persistent class", e);
        }
    }

    /** This is a helper method to determine the actual class which was used to create an IPersistent instance.
     * It is quite possible that the input object is a dynamic proxy.
     * @param object The object which implements the IPersistent instance.
     * @return The class which was used for instantiating the instance.
     */
    public static Class getActualPersistentClass(IPersistent object) {
        Class clazz = object.getClass();
        if (Proxy.isProxyClass(clazz)) {
            Class[] interfaces = clazz.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (interfaces[i] != IPersistent.class) {
                    clazz = interfaces[i];
                    break;
                }
            }
        }
        return clazz;
    }

    /** This is used by the MoldingService to initialize the Persistent instance.
     * This will merely add the attribute/value to the Map in the PersistentInstanceInvocationHandler.
     * This will bypass the validations performed during invocation of the setXyz() method on the persistent object.
     * Nothing will be done if the input object is not a Proxy or if its InvocationHandler is not an instance of PersistentInstanceInvocationHandler.
     * @param object The Proxy object for an IPersistence interface.
     * @param attributeName The attribute whose value will be set.
     * @param value The value to be set.
     */
    public static void setInstanceValue(IPersistent object, String attributeName, Object value) {
        if (Proxy.isProxyClass(object.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(object);
            if (handler instanceof PersistentInstanceInvocationHandler) ((PersistentInstanceInvocationHandler) handler).addAttributeValue(attributeName, value);
        }
    }
}
