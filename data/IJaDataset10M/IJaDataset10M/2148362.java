package net.sf.irunninglog.util;

import java.lang.reflect.Constructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory class for creating object instances.  This class uses the
 * application's object factory properties file to create objects via the
 * <code>createObject</code> method.  Any type of object may be declared in the
 * properties file, provided that it has a zero-argument constructor (the
 * constructor need not be public).
 *
 * @author <a href="mailto:allan_e_lewis@yahoo.com">Allan Lewis</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/06/23 01:49:04 $
 * @since iRunningLog 1.0
 */
public final class ObjectFactory {

    /** Log instance for this class. */
    private static final Log LOG = LogFactory.getLog(ObjectFactory.class);

    /** Singleton factory instance. */
    private static final ObjectFactory INSTANCE;

    static {
        INSTANCE = new ObjectFactory();
        if (LOG.isDebugEnabled()) {
            LOG.debug("ObjectFactory successfully initialized");
        }
    }

    /**
     * Get the singleton <code>ObjectFactory</code> instance.
     *
     * @return The singleton instance
     */
    public static ObjectFactory getInstance() {
        return INSTANCE;
    }

    /** Singleton pattern - protect the default constructor. */
    private ObjectFactory() {
        super();
    }

    /**
     * Create a new object instance.  This will instantiate and return an
     * instance of the proper object by reading the object factory properties
     * file.  If an object cannot be created, an <code>ObjectCreationException
     * </code> will be thrown.
     *
     * @param objectName The name identifying the object to be created.  Should
     *                   correspond to the name of a property in the object
     *                   factory properties file
     * @return The new object instance
     * @see ObjectCreationException
     */
    public Object createObject(String objectName) {
        try {
            String bundleName = ResourceManager.BUNDLE_OBJECT_FACTORY;
            ResourceManager manager = ResourceManager.getInstance();
            if (LOG.isDebugEnabled()) {
                LOG.debug("createObject: Creating an object for name " + objectName);
            }
            String className = manager.getString(bundleName, objectName);
            if (LOG.isDebugEnabled()) {
                LOG.debug("createObject: Creating an object of class " + className);
            }
            Class clazz = Class.forName(className);
            Constructor c = clazz.getDeclaredConstructor(null);
            c.setAccessible(true);
            Object obj = c.newInstance(null);
            if (LOG.isDebugEnabled()) {
                LOG.debug("createObject: Created the following object " + obj);
            }
            return obj;
        } catch (Exception ex) {
            LOG.error("Unable to create object for name '" + objectName + "'", ex);
            throw new ObjectCreationException("Unable to create object for" + " name '" + objectName + "'", ex);
        }
    }
}
