package org.datanucleus.store;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.ObjectManagerFactoryImpl;
import org.datanucleus.util.ClassUtils;

/**
 * Factory for creating StoreManagers.
 */
public class StoreManagerFactory {

    /**
     * Default Constructor.
     **/
    private StoreManagerFactory() {
    }

    /**
     * Accessor for the StoreManager. Creates an instance of StoreManager if necessary
     * @param managerClassName Name of the class name for the Store Manager
     * @param clr the ClassLoaderResolver
     * @param omf Object Manager Factory
     * @return The Store Manager.
     **/
    public static synchronized StoreManager getStoreManager(String managerClassName, ClassLoaderResolver clr, ObjectManagerFactoryImpl omf) {
        Class managerCls = clr.classForName(managerClassName, ObjectManagerFactoryImpl.class.getClassLoader());
        Class[] ctrArgTypes = new Class[] { ClassLoaderResolver.class, ObjectManagerFactoryImpl.class };
        Object[] ctrArgs = new Object[] { clr, omf };
        return (StoreManager) ClassUtils.newInstance(managerCls, ctrArgTypes, ctrArgs);
    }
}
