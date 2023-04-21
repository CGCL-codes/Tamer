package ldapbeans.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class SynchronizedObjectFactory {

    /**
     * Invocation handler that synchronize all access to the proxy instance of
     * the object
     * 
     * @param <T>
     *            Type of the proxy objects
     */
    private static class SynchronizedObjectInvocationHandler<T> implements InvocationHandler {

        private final T m_Object;

        private final Object m_Mutex = new Object();

        /**
	 * Construct a proxy over the object to synchronize
	 * 
	 * @param p_Object
	 *            The object to synchronize
	 */
        SynchronizedObjectInvocationHandler(T p_Object) {
            m_Object = p_Object;
        }

        /**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 *      java.lang.reflect.Method, java.lang.Object[])
	 */
        public Object invoke(Object p_Proxy, Method p_Method, Object[] p_Params) throws Throwable {
            synchronized (m_Mutex) {
                return p_Method.invoke(m_Object, p_Params);
            }
        }
    }

    /** unique instance of this class */
    private static final SynchronizedObjectFactory INSTANCE;

    /** static constructor */
    static {
        INSTANCE = new SynchronizedObjectFactory();
    }

    /**
     * Return the unique instance of the factory
     * 
     * @return the unique instance of this class
     */
    public static SynchronizedObjectFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Constructor that disallow to instanciate this class.
     */
    private SynchronizedObjectFactory() {
    }

    /**
     * Returns a synchronized (thread-safe) object backed by the specified
     * object. In order to guarantee serial access, it is critical that
     * <strong>all</strong> access to the backing object is accomplished through
     * the returned collection.
     * <p>
     * 
     * The returned object will be serializable if the specified collection is
     * serializable.
     * 
     * @param <T>
     *            Type of the object to synchronize
     * @param p_Object
     *            the object to be "wrapped" in a synchronized object.
     * @return a synchronized view of the specified object.
     */
    @SuppressWarnings("unchecked")
    public <T> T create(T p_Object) {
        ClassLoader cld = Thread.currentThread().getContextClassLoader();
        Class<?>[] classes = p_Object.getClass().getInterfaces();
        T synchronizedObject = (T) Proxy.newProxyInstance(cld, classes, new SynchronizedObjectInvocationHandler<T>(p_Object));
        return synchronizedObject;
    }
}
