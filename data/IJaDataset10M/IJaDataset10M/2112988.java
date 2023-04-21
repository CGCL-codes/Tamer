package org.simpleframework.http.session;

import java.lang.reflect.Constructor;
import org.simpleframework.util.net.Cookie;

/**
 * The <code>StoreFactory</code> retrieves the <code>Store</code>
 * implementation for the system. This is used so that a storage
 * strategy can be imposed on the system using the command line. 
 * This has a number of advantages. For one it enables storage to
 * be configured transparently without affecting the sessions.
 * <p>
 * In order to define a system wide implementation a property is
 * needed to define the object. This uses the <code>System</code>
 * properties to define the class name for the default instance.
 * The property is the <code>org.simpleframework.http.session.store</code> 
 * property that can be set using an argument to the VM.
 * <pre>
 * java -Dorg.simpleframework.http.session.store=demo.example.ExampleStore
 * </pre>
 * This will set the <code>System</code> property to the class 
 * name <code>demo.example.ExampleStore</code>. When the factory
 * method <code>getInstance</code> is invoked it will return an
 * implementation of this object or if the implementation cannot
 * be loaded by this classes class loader a default implementation
 * <code>DefaultStore</code> is returned instead. 
 * 
 * @author Niall Gallagher
 */
final class StoreFactory {

    /**   
    * This is a factory object used to create stores. To ensure
    * that instances can be created rapidly the factory is
    * created once to aggregate the Java reflection overhead.
    */
    private Constructor factory;

    /**
    * Constructor for the <code>StoreFactory</code> object. This
    * will create a suitable <code>Constructor</code> object so
    * that instances can be created quickly without incurring the
    * overhead of reflectively creating a constructor each time.
    */
    public StoreFactory() {
        this.factory = getConstructor();
    }

    /**
    * This will produce an implementation of the <code>Store</code>
    * object. This is a reasonably fast factory method for producing
    * arbitrary <code>Store</code> instances. This simply uses an
    * existing <code>Constructor</code> to reflectively create the
    * instance. However if the instance cannot be created due to a
    * constructor problem then this uses a default implementation.
    *
    * @param cookie this is the cookie that references the session
    *
    * @return returns the <code>Store</code> to store session data
    */
    public Store getInstance(Cookie cookie) {
        Object[] list = new Object[] { cookie };
        if (factory == null) {
            return new DefaultStore(cookie);
        }
        try {
            return (Store) factory.newInstance(list);
        } catch (Exception e) {
            return new DefaultStore(cookie);
        }
    }

    /**
    * This is used to produce a <code>Constructor</code> factory to
    * create <code>Store</code> instances. The goal is reduce the
    * overhead of Java Reflection by creating a single factory that
    * can be used to create instances. The type is determined by 
    * the <code>org.simpleframework.http.session.store</code> system property.
    * The <code>DefaultStore</code> implementation is used if the
    * specified class cannot be loaded from the class path.
    *
    * @return a factory used to create <code>Store</code> objects
    */
    private Constructor getConstructor() {
        String property = "org.simpleframework.http.session.store";
        String className = System.getProperty(property);
        try {
            return getConstructor(className);
        } catch (Exception e) {
            return null;
        }
    }

    /**
    * Here a <code>ClassLoader</code> is selected to load the class.
    * This will load the class specified using this classes class
    * loader. If there are no problems in loading the class then a
    * <code>Constructor</code> is created from the loaded class.
    * <p>
    * The constructor for any <code>Store</code> implementation
    * must contain a single argument constructor that takes a
    * <code>Cookie</code> object. If such a constructor does not 
    * exist then this will throw an <code>Exception</code>.
    * 
    * @param className the name of the store implementation 
    *
    * @return this returns a constructor for the specified class
    */
    private Constructor getConstructor(String className) throws Exception {
        return getConstructor(Class.forName(className, false, StoreFactory.class.getClassLoader()));
    }

    /**
    * Creates the <code>Constructor</code> for the implementation
    * so that an instance can be created. This will use the class
    * which has been previously loaded to acquire the constructor.
    * The constructor acquired must take a single argument of a
    * <code>Cookie</code>, which represents the session reference.
    *
    * @param type this is the implementation class to be used
    *
    * @return this returns a constructor for the specified class
    */
    private Constructor getConstructor(Class type) throws Exception {
        Class[] types = new Class[] { Cookie.class };
        return type.getDeclaredConstructor(types);
    }
}
