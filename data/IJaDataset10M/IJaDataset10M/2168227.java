package org.rococoa;

import java.lang.reflect.Proxy;
import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Enhancer;
import org.rococoa.internal.OCInvocationCallbacks;
import org.rococoa.internal.ObjCObjectInvocationHandler;
import org.rococoa.internal.VarArgsUnpacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Static factory for creating Java wrappers for Objective-C instances, and Objective-C
 * wrappers for Java instances. <strong>START HERE</strong>.
 * 
 * @author duncan
 *
 */
public abstract class Rococoa {

    private static Logger logging = LoggerFactory.getLogger("org.rococoa.proxy");

    /**
     * Create a Java NSClass representing the Objective-C class with ocClassName
     */
    public static <T extends ObjCClass> T createClass(String ocClassName, Class<T> type) {
        return wrap(Foundation.getClass(ocClassName), type, false);
    }

    /**
     * Create a Java NSObject representing an instance of the Objective-C class
     * ocClassName. The Objective-C instance is created by calling the static 
     * factory method named ocMethodName, passing args.
     */
    public static <T extends ObjCObject> T create(String ocClassName, Class<T> javaClass, String ocMethodName, Object... args) {
        boolean weOwnObject = Foundation.selectorNameMeansWeOwnReturnedObject(ocMethodName);
        boolean retain = !weOwnObject;
        return create(ocClassName, javaClass, ocMethodName, retain, args);
    }

    /**
     * Create a Java NSObject representing an instance of the Objective-C class
     * ocClassName, created with the class method <code>+new</code>.
     */
    public static <T extends ObjCObject> T create(String ocClassName, Class<T> javaClass) {
        return create(ocClassName, javaClass, "new");
    }

    private static <T extends ObjCObject> T create(String ocClassName, Class<T> javaClass, String ocFactoryName, boolean retain, Object... args) {
        if (logging.isTraceEnabled()) {
            logging.trace("creating [{} ({})].{}({})", new Object[] { ocClassName, javaClass.getName(), ocFactoryName, new VarArgsUnpacker(args) });
        }
        ID ocClass = Foundation.getClass(ocClassName);
        ID ocInstance = Foundation.send(ocClass, ocFactoryName, ID.class, args);
        int initialRetainCount = Foundation.cfGetRetainCount(ocInstance);
        T result = wrap(ocInstance, javaClass, retain);
        checkRetainCount(ocInstance, retain ? initialRetainCount + 1 : initialRetainCount);
        return result;
    }

    /**
     * Create a Java NSObject wrapping an existing Objective-C instance, represented
     * by id.
     * 
     * The NSObject is retained, and released when the object is GC'd.
     */
    public static <T extends ObjCObject> T wrap(ID id, Class<T> javaClass) {
        return wrap(id, javaClass, true);
    }

    /**
     * Create a Java NSObject down-casting an existing NSObject to a more derived
     * type.
     */
    public static <T extends ObjCObject> T cast(ObjCObject object, Class<T> desiredType) {
        if (object == null) {
            return null;
        }
        return wrap(object.id(), desiredType, true);
    }

    public static <T extends ObjCObject> T wrap(ID id, Class<T> javaClass, boolean retain) {
        if (id == null || id.isNull()) {
            return null;
        }
        ObjCObjectInvocationHandler invocationHandler = new ObjCObjectInvocationHandler(id, javaClass, retain);
        return createProxy(javaClass, invocationHandler);
    }

    /**
     * Return the ID of a new Objective-C object that will forward messages to
     * javaObject.
     * 
     * Keep hold of the ID all the time that methods may be invoked on the Obj-C
     * object, otherwise the callbacks may be GC'd, with amusing consequences.
     * 
     * @deprecated because the OC proxy object is never released. 
     *  Use {@link Rococoa#proxy} instead.
     */
    @Deprecated
    public static ID wrap(Object javaObject) {
        OCInvocationCallbacks callbacks = new OCInvocationCallbacks(javaObject);
        ID idOfOCProxy = Foundation.newOCProxy(callbacks);
        return new ProxyID(idOfOCProxy, callbacks);
    }

    /**
     * Return a new Objective-C object that will forward messages to javaObject, 
     * for use in delegates, notifications etc.
     * 
     * You need to keep a reference to the returned value for as long as it is
     * active. When it is GC'd, it will release the Objective-C proxy.
     */
    public static ObjCObject proxy(Object javaObject) {
        return proxy(javaObject, ObjCObject.class);
    }

    public static <T extends ObjCObject> T proxy(Object javaObject, Class<T> javaType) {
        ID proxyID = wrap(javaObject);
        return wrap(proxyID, javaType, false);
    }

    /**
     * Create a java.lang.reflect.Proxy or cglib proxy of type, which forwards
     * invocations to invococationHandler.
     */
    @SuppressWarnings("unchecked")
    private static <T> T createProxy(final Class<T> type, ObjCObjectInvocationHandler invocationHandler) {
        if (type.isInterface()) {
            return (T) Proxy.newProxyInstance(invocationHandler.getClass().getClassLoader(), new Class[] { type }, invocationHandler);
        } else {
            Enhancer e = new Enhancer();
            e.setUseCache(true);
            e.setNamingPolicy(new DefaultNamingPolicy() {

                public String getClassName(String prefix, String source, Object key, Predicate names) {
                    if (source.equals(net.sf.cglib.proxy.Enhancer.class.getName())) {
                        return type.getName() + "$$ByRococoa";
                    } else {
                        return super.getClassName(prefix, source, key, names);
                    }
                }
            });
            e.setSuperclass(type);
            e.setCallback(invocationHandler);
            return (T) e.create();
        }
    }

    public static class ProxyID extends ID {

        @SuppressWarnings("unused")
        private OCInvocationCallbacks callbacks;

        public ProxyID() {
        }

        public ProxyID(ID anotherID, OCInvocationCallbacks callbacks) {
            super(anotherID);
            this.callbacks = callbacks;
        }
    }

    private static void checkRetainCount(ID ocInstance, int expected) {
        int retainCount = Foundation.cfGetRetainCount(ocInstance);
        if (retainCount != expected) {
            throw new IllegalStateException("Created an object which had a retain count of " + retainCount + " not " + expected);
        }
    }

    /**
     * Enforce static factory-ness.
     */
    private Rococoa() {
    }
}
