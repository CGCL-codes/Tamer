package es.caib.signatura.provider.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

public class DummyLogFactoryImpl extends LogFactory {

    public DummyLogFactoryImpl() {
    }

    /**
	 * Return the configuration attribute with the specified name (if any), or
	 * <code>null</code> if there is no such attribute.
	 * 
	 * @param name
	 *            Name of the attribute to return
	 */
    public Object getAttribute(String name) {
        return null;
    }

    /**
	 * Return an array containing the names of all currently defined
	 * configuration attributes. If there are no such attributes, a zero length
	 * array is returned.
	 */
    public String[] getAttributeNames() {
        return null;
    }

    /**
	 * Convenience method to derive a name from the specified class and call
	 * <code>getInstance(String)</code> with it.
	 * 
	 * @param clazz
	 *            Class for which a suitable Log name will be derived
	 * 
	 * @exception LogConfigurationException
	 *                if a suitable <code>Log</code> instance cannot be
	 *                returned
	 */
    public Log getInstance(Class clazz) throws LogConfigurationException {
        return new DummyLog();
    }

    /**
	 * <p>
	 * Construct (if necessary) and return a <code>Log</code> instance, using
	 * the factory's current set of configuration attributes.
	 * </p>
	 * 
	 * <p>
	 * <strong>NOTE</strong> - Depending upon the implementation of the
	 * <code>LogFactory</code> you are using, the <code>Log</code> instance
	 * you are returned may or may not be local to the current application, and
	 * may or may not be returned again on a subsequent call with the same name
	 * argument.
	 * </p>
	 * 
	 * @param name
	 *            Logical name of the <code>Log</code> instance to be returned
	 *            (the meaning of this name is only known to the underlying
	 *            logging implementation that is being wrapped)
	 * 
	 * @exception LogConfigurationException
	 *                if a suitable <code>Log</code> instance cannot be
	 *                returned
	 */
    public Log getInstance(String name) throws LogConfigurationException {
        return new DummyLog();
    }

    /**
	 * Release any internal references to previously created {@link Log}
	 * instances returned by this factory. This is useful in environments like
	 * servlet containers, which implement application reloading by throwing
	 * away a ClassLoader. Dangling references to objects in that class loader
	 * would prevent garbage collection.
	 */
    public void release() {
    }

    /**
	 * Remove any configuration attribute associated with the specified name. If
	 * there is no such attribute, no action is taken.
	 * 
	 * @param name
	 *            Name of the attribute to remove
	 */
    public void removeAttribute(String name) {
    }

    /**
	 * Set the configuration attribute with the specified name. Calling this
	 * with a <code>null</code> value is equivalent to calling
	 * <code>removeAttribute(name)</code>.
	 * 
	 * @param name
	 *            Name of the attribute to set
	 * @param value
	 *            Value of the attribute to set, or <code>null</code> to
	 *            remove any setting for this attribute
	 */
    public void setAttribute(String name, Object value) {
    }

    /**
	 * <p>
	 * Construct (if necessary) and return a <code>LogFactory</code> instance,
	 * using the following ordered lookup procedure to determine the name of the
	 * implementation class to be loaded.
	 * </p>
	 * <ul>
	 * <li>The <code>org.apache.commons.logging.LogFactory</code> system
	 * property.</li>
	 * <li>The JDK 1.3 Service Discovery mechanism</li>
	 * <li>Use the properties file <code>commons-logging.properties</code>
	 * file, if found in the class path of this class. The configuration file is
	 * in standard <code>java.util.Properties</code> format and contains the
	 * fully qualified name of the implementation class with the key being the
	 * system property defined above.</li>
	 * <li>Fall back to a default implementation class (<code>org.apache.commons.logging.impl.LogFactoryImpl</code>).</li>
	 * </ul>
	 * 
	 * <p>
	 * <em>NOTE</em> - If the properties file method of identifying the
	 * <code>LogFactory</code> implementation class is utilized, all of the
	 * properties defined in this file will be set as configuration attributes
	 * on the corresponding <code>LogFactory</code> instance.
	 * </p>
	 * 
	 * <p>
	 * <em>NOTE</em> - In a multithreaded environment it is possible that two
	 * different instances will be returned for the same classloader
	 * environment.
	 * </p>
	 * 
	 * @exception LogConfigurationException
	 *                if the implementation class is not available or cannot be
	 *                instantiated.
	 */
    public static LogFactory getFactory() throws LogConfigurationException {
        return new DummyLogFactoryImpl();
    }

    /**
	 * Convenience method to return a named logger, without the application
	 * having to care about factories.
	 * 
	 * @param clazz
	 *            Class from which a log name will be derived
	 * 
	 * @exception LogConfigurationException
	 *                if a suitable <code>Log</code> instance cannot be
	 *                returned
	 */
    public static Log getLog(Class clazz) throws LogConfigurationException {
        return new DummyLog();
    }

    /**
	 * Convenience method to return a named logger, without the application
	 * having to care about factories.
	 * 
	 * @param name
	 *            Logical name of the <code>Log</code> instance to be returned
	 *            (the meaning of this name is only known to the underlying
	 *            logging implementation that is being wrapped)
	 * 
	 * @exception LogConfigurationException
	 *                if a suitable <code>Log</code> instance cannot be
	 *                returned
	 */
    public static Log getLog(String name) throws LogConfigurationException {
        return new DummyLog();
    }

    /**
	 * Release any internal references to previously created {@link LogFactory}
	 * instances that have been associated with the specified class loader (if
	 * any), after calling the instance method <code>release()</code> on each
	 * of them.
	 * 
	 * @param classLoader
	 *            ClassLoader for which to release the LogFactory
	 */
    public static void release(ClassLoader classLoader) {
    }

    /**
	 * Release any internal references to previously created {@link LogFactory}
	 * instances, after calling the instance method <code>release()</code> on
	 * each of them. This is useful in environments like servlet containers,
	 * which implement application reloading by throwing away a ClassLoader.
	 * Dangling references to objects in that class loader would prevent garbage
	 * collection.
	 */
    public static void releaseAll() {
    }

    /**
	 * Return a new instance of the specified <code>LogFactory</code>
	 * implementation class, loaded by the specified class loader. If that
	 * fails, try the class loader used to load this (abstract) LogFactory.
	 * <p>
	 * <h2>ClassLoader conflicts</h2>
	 * Note that there can be problems if the specified ClassLoader is not the
	 * same as the classloader that loaded this class, ie when loading a
	 * concrete LogFactory subclass via a context classloader.
	 * <p>
	 * The problem is the same one that can occur when loading a concrete Log
	 * subclass via a context classloader.
	 * <p>
	 * The problem occurs when code running in the context classloader calls
	 * class X which was loaded via a parent classloader, and class X then calls
	 * LogFactory.getFactory (either directly or via LogFactory.getLog). Because
	 * class X was loaded via the parent, it binds to LogFactory loaded via the
	 * parent. When the code in this method finds some LogFactoryYYYY class in
	 * the child (context) classloader, and there also happens to be a
	 * LogFactory class defined in the child classloader, then LogFactoryYYYY
	 * will be bound to LogFactory@childloader. It cannot be cast to
	 * LogFactory@parentloader, ie this method cannot return the object as the
	 * desired type. Note that it doesn't matter if the LogFactory class in the
	 * child classloader is identical to the LogFactory class in the parent
	 * classloader, they are not compatible.
	 * <p>
	 * The solution taken here is to simply print out an error message when this
	 * occurs then throw an exception. The deployer of the application must
	 * ensure they remove all occurrences of the LogFactory class from the child
	 * classloader in order to resolve the issue. Note that they do not have to
	 * move the custom LogFactory subclass; that is ok as long as the only
	 * LogFactory class it can find to bind to is in the parent classloader.
	 * <p>
	 * 
	 * @param factoryClass
	 *            Fully qualified name of the <code>LogFactory</code>
	 *            implementation class
	 * @param classLoader
	 *            ClassLoader from which to load this class
	 * @param contextClassLoader
	 *            is the context that this new factory will manage logging for.
	 * 
	 * @exception LogConfigurationException
	 *                if a suitable instance cannot be created
	 * @since 1.1
	 */
    protected static LogFactory newFactory(final String factoryClass, final ClassLoader classLoader, final ClassLoader contextClassLoader) throws LogConfigurationException {
        return new DummyLogFactoryImpl();
    }

    /**
	 * Method provided for backwards compatibility; see newFactory version that
	 * takes 3 parameters.
	 * <p>
	 * This method would only ever be called in some rather odd situation. Note
	 * that this method is static, so overriding in a subclass doesn't have any
	 * effect unless this method is called from a method in that subclass.
	 * However this method only makes sense to use from the getFactory method,
	 * and as that is almost always invoked via LogFactory.getFactory, any
	 * custom definition in a subclass would be pointless. Only a class with a
	 * custom getFactory method, then invoked directly via
	 * CustomFactoryImpl.getFactory or similar would ever call this. Anyway,
	 * it's here just in case, though the "managed class loader" value output to
	 * the diagnostics will not report the correct value.
	 */
    protected static LogFactory newFactory(final String factoryClass, final ClassLoader classLoader) {
        return new DummyLogFactoryImpl();
    }

    /**
	 * Implements the operations described in the javadoc for newFactory.
	 * 
	 * @param factoryClass
	 * 
	 * @param classLoader
	 *            used to load the specified factory class. This is expected to
	 *            be either the TCCL or the classloader which loaded this class.
	 *            Note that the classloader which loaded this class might be
	 *            "null" (ie the bootloader) for embedded systems.
	 * 
	 * @return either a LogFactory object or a LogConfigurationException object.
	 * @since 1.1
	 */
    protected static Object createFactory(String factoryClass, ClassLoader classLoader) {
        return new DummyLogFactoryImpl();
    }

    /**
	 * Returns a string that uniquely identifies the specified object, including
	 * its class.
	 * <p>
	 * The returned string is of form "classname@hashcode", ie is the same as
	 * the return value of the Object.toString() method, but works even when the
	 * specified object's class has overidden the toString method.
	 * 
	 * @param o
	 *            may be null.
	 * @return a string of form classname@hashcode, or "null" if param o is
	 *         null.
	 * @since 1.1
	 */
    public static String objectId(Object o) {
        if (o == null) {
            return "null";
        } else {
            return o.getClass().getName() + "@" + System.identityHashCode(o);
        }
    }
}
