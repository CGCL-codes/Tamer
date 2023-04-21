package edu.mit.osidimpl.provider.repository;

/**
 * OsidLoader loads a specific implementation of an Open Service Interface
 * Definition (OSID) with its getManager method. The getManager method loads
 * an instance of the OSID's OsidManager, assigns the manager's  OsidContext,
 * assigns any configuration information, and returns the instance of the OSID
 * implementation.  This usage of the getManager method in the OsidLoader is
 * how applications should bind a particular implementation to an OSID.  The
 * value of this approach is that an application can defer which specific OSID
 * implementation is used until runtime. The specific implementation package
 * name can then be part of the configuration information rather than being
 * hard coded.  Changing implementations is simplified with this approach.
 * 
 * <p>
 * As an example, in order to create a new Hierarchy, an application does not
 * use the new operator.  It uses the OsidLoader getManager method to get an
 * instance of a class that implements HierarchyManager (a subclass of
 * OsidManager). The application uses the HierarchyManager instance to create
 * the Hierarchy.  It is the createHierarchy() method in some package (e.g.
 * org.osid.hierarchy.impl.HierarchyManager) which uses the new operator on
 * org.osid.hierarchy.impl.Hierarchy, casts it as
 * org.osid.hierarchy.Hierarchy, and returns it to the application.  This
 * indirection offers the significant value of being able to change
 * implementations in one spot with one modification, namely by using a
 * implementation package name argument for the OsidLoader getManager method.
 * </p>
 * 
 * <p>
 * Sample:
 * <blockquote>
 * org.osid.OsidContext myContext = new org.osid.OsidContext();<br/
 * >String key = "myKey";<br/
 * >myContext.assignContext(key, "I want to save this string as context");<br/
 * >String whatWasMyContext = myContext.getContext(key);<br/
 * >org.osid.hierarchy.HierarchyManager hierarchyManager =
 * <blockquote>
 * org.osid.OsidLoader.getManager("org.osid.hierarchy.HierarchyManager","org.osid.shared.impl",myContext,null);
 * </blockquote>
 * org.osid.hierarchy.Hierarchy myHierarchy =
 * hierarchyManager.createHierarchy(...);<br/>
 * </blockquote>
 * </p>
 * 
 * <p>
 * A similar technique can be used for creating other objects.  OSIDs that have
 * OsidManager implementations loaded by OsidLoader, will define an
 * appropriate interface to create these objects.
 * </p>
 * 
 * <p>
 * The arguments to OsidLoader.getManager method are the OSID OsidManager
 * interface name, the implementing package name, the OsidContext, and any
 * additional configuration information.
 * </p>
 * 
 * <p>
 * OSID Version: 2.0
 * </p>
 * 
 * <p>
 * Licensed under the {@link org.osid.SidImplementationLicenseMIT MIT
 * O.K.I&#46; OSID Definition License}.
 * </p>
 */
public class OsidLoader implements java.io.Serializable {

    /**
     * Returns an instance of the OsidManager of the OSID specified by the OSID
     * package OsidManager interface name and the implementation package name.
     * The implementation class name is constructed from the SID package
     * Manager interface name. A configuration file name is constructed in a
     * similar manner and if the file exists it is loaded into the
     * implementation's OsidManager's configuration.
     * 
     * <p>
     * Example:  To load an implementation of the org.osid.Filing OSID
     * implemented in a package "xyz", one would use:
     * </p>
     * 
     * <p>
     * org.osid.filing.FilingManager fm =
     * (org.osid.filing.FilingManager)org.osid.OsidLoader.getManager(
     * </p>
     * 
     * <p>
     * "org.osid.filing.FilingManager" ,
     * </p>
     * 
     * <p>
     * "xyz" ,
     * </p>
     * 
     * <p>
     * new org.osid.OsidContext());
     * </p>
     *
     * @param osidPackageManagerName osidPackageManagerName is a fully
     *        qualified OsidManager interface name
     * @param implPackageName implPackageName is a fully qualified
     *        implementation package name
     * @param context
     * @param additionalConfiguration
     *
     * @return OsidManager
     *
     * @throws org.osid.OsidException An exception with one of the following
     *         messages defined in org.osid.OsidException:  {@link
     *         org.osid.OsidException#OPERATION_FAILED OPERATION_FAILED},
     *         {@link org.osid.OsidException#NULL_ARGUMENT NULL_ARGUMENT},
     *         {@link org.osid.OsidException#VERSION_ERROR VERSION_ERROR},
     *         ={@link org.osid.OsidException#INTERFACE_NOT_FOUND
     *         INTERFACE_NOT_FOUND}, ={@link
     *         org.osid.OsidException#MANAGER_NOT_FOUND MANAGER_NOT_FOUND},
     *         ={@link org.osid.OsidException#MANAGER_INSTANTIATION_ERROR
     *         MANAGER_INSTANTIATION_ERROR}, ={@link
     *         org.osid.OsidException#ERROR_ASSIGNING_CONTEXT
     *         ERROR_ASSIGNING_CONTEXT}, ={@link
     *         org.osid.OsidException#ERROR_ASSIGNING_CONFIGURATION
     *         ERROR_ASSIGNING_CONFIGURATION}
     */
    public static org.osid.OsidManager getManager(String osidPackageManagerName, String implPackageName, org.osid.OsidContext context, java.util.Properties additionalConfiguration, ClassLoader loader) throws org.osid.OsidException {
        org.osid.OsidManager manager;
        Class managerClass;
        Class osidInterface;
        java.util.Properties configuration;
        if ((osidPackageManagerName == null) || (implPackageName == null)) {
            throw new org.osid.OsidException(org.osid.OsidException.NULL_ARGUMENT);
        }
        String osidInterfaceName = osidPackageManagerName;
        String className = makeClassName(osidPackageManagerName);
        String managerClassName = makeFullyQualifiedClassName(implPackageName, className);
        try {
            osidInterface = loader.loadClass(osidInterfaceName);
        } catch (Exception e) {
            System.err.println("cannot load interface: " + e.getMessage());
            throw new org.osid.OsidException(org.osid.OsidException.INTERFACE_NOT_FOUND);
        }
        if (osidInterface == null) {
            throw new org.osid.OsidException(org.osid.OsidException.INTERFACE_NOT_FOUND);
        }
        try {
            managerClass = loader.loadClass(managerClassName);
        } catch (Exception e) {
            System.err.println("cannot load impl: " + e.getMessage());
            throw new org.osid.OsidException(org.osid.OsidException.MANAGER_NOT_FOUND);
        }
        if (managerClass == null) {
            System.err.println("class is null for some reason");
            throw new org.osid.OsidException(org.osid.OsidException.MANAGER_NOT_FOUND);
        }
        if (!osidInterface.isAssignableFrom(managerClass)) {
            throw new org.osid.OsidException(org.osid.OsidException.MANAGER_INSTANTIATION_ERROR);
        }
        try {
            manager = (org.osid.OsidManager) managerClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new org.osid.OsidException(org.osid.OsidException.MANAGER_INSTANTIATION_ERROR);
        }
        if (manager == null) {
            throw new org.osid.OsidException(org.osid.OsidException.MANAGER_INSTANTIATION_ERROR);
        }
        try {
            manager.osidVersion_2_0();
        } catch (Throwable ex) {
            throw new org.osid.OsidException(org.osid.OsidException.VERSION_ERROR);
        }
        if (context != null) {
            try {
                manager.assignOsidContext(context);
            } catch (Exception ex) {
                throw new org.osid.OsidException(org.osid.OsidException.ERROR_ASSIGNING_CONTEXT);
            }
        }
        try {
            configuration = getConfiguration(manager);
        } catch (org.osid.OsidException oe) {
            throw new org.osid.OsidException(org.osid.OsidException.ERROR_ASSIGNING_CONFIGURATION);
        }
        if (configuration == null) {
            configuration = new java.util.Properties();
        }
        if (additionalConfiguration != null) {
            java.util.Enumeration e = additionalConfiguration.propertyNames();
            while (e.hasMoreElements()) {
                java.io.Serializable key = (java.io.Serializable) e.nextElement();
                if (key != null) {
                    java.io.Serializable value = (java.io.Serializable) additionalConfiguration.get(key);
                    if (value != null) {
                        configuration.put(key, value);
                    }
                }
            }
        }
        try {
            manager.assignConfiguration(configuration);
        } catch (org.osid.OsidException oe) {
            throw new org.osid.OsidException(org.osid.OsidException.ERROR_ASSIGNING_CONFIGURATION);
        }
        return manager;
    }

    private static String makeClassName(String packageManagerName) throws org.osid.OsidException {
        String className = packageManagerName;
        if (className != null) {
            className = (className.endsWith(".") ? className.substring(0, className.length() - 1) : className);
            int lastdot = className.lastIndexOf(".");
            if (lastdot != -1) {
                className = className.substring(lastdot + 1);
            }
        }
        return className;
    }

    private static String makeFullyQualifiedClassName(String packageName, String className) throws org.osid.OsidException {
        String cName = className;
        if (packageName != null) {
            String pName = (packageName.endsWith(".") ? packageName : new String(packageName + "."));
            cName = pName + className;
        }
        return cName;
    }

    private static java.util.Properties getConfiguration(org.osid.OsidManager manager) throws org.osid.OsidException {
        java.util.Properties properties = null;
        if (null != manager) {
            Class managerClass = manager.getClass();
            try {
                String managerClassName = managerClass.getName();
                int index = managerClassName.lastIndexOf(".");
                if (index != -1) {
                    managerClassName = managerClassName.substring(index + 1);
                }
                java.io.InputStream is = managerClass.getResourceAsStream(managerClassName + ".properties");
                if (is != null) {
                    properties = new java.util.Properties();
                    properties.load(is);
                }
            } catch (Throwable ex) {
            }
        }
        return properties;
    }
}
