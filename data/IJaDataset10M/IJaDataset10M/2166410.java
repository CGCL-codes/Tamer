package org.escapek.client.cmdb;

import java.util.Hashtable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.escapek.client.cmdb.interfaces.ICIClassManager;
import org.escapek.core.dto.cmdb.definitions.CIClassDTO;
import org.escapek.logger.LoggerPlugin;
import org.escapek.logger.LoggingConstants;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CMDBActivator extends Plugin {

    public static final String PLUGIN_ID = "org.escapek.client.cmdb";

    private static CMDBActivator plugin;

    private IExtensionRegistry registry;

    private String ELEM_MANAGED_CLASS = "managedClass";

    private String ATT_NAME = "name";

    private String ATT_MANAGER_CLASS = "managerClass";

    private String ATT_CICLASS_NAME = "CIClassName";

    private Hashtable<String, ICIClassManager> CIClassManagers;

    private Hashtable<String, String> managedClasses;

    /**
	 * The constructor
	 */
    public CMDBActivator() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        registry = Platform.getExtensionRegistry();
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
    public static CMDBActivator getDefault() {
        return plugin;
    }

    /**
	 * Return the ICIClassManager which corresponds to the given CI class
	 * Manager ->CIClass is read from available extension of CIClassManager point
	 * @param aCIClass
	 */
    public ICIClassManager getCIClassManager(CIClassDTO aCIClass) {
        IExtensionPoint extPoint = registry.getExtensionPoint("org.escapek.client.cmdb.CIClassManager");
        if (CIClassManagers == null || managedClasses == null) {
            CIClassManagers = new Hashtable<String, ICIClassManager>();
            managedClasses = new Hashtable<String, String>();
            IExtension[] extensions = extPoint.getExtensions();
            for (int i = 0; i < extensions.length; i++) {
                IExtension extension = extensions[i];
                IConfigurationElement[] elements = extension.getConfigurationElements();
                for (int j = 0; j < elements.length; j++) {
                    IConfigurationElement element = elements[j];
                    String managerName = element.getAttribute(ATT_NAME);
                    String managerClassName = element.getAttribute(ATT_MANAGER_CLASS);
                    try {
                        Class managerClass = Class.forName(managerClassName);
                        ICIClassManager manager = (ICIClassManager) managerClass.newInstance();
                        CIClassManagers.put(managerName, manager);
                    } catch (Exception e) {
                        LoggerPlugin.LogError(CMDBActivator.PLUGIN_ID, LoggingConstants.UNEXPECTED_EXCEPTION, "Error while creating CI class manager '" + managerName + "'", e);
                    }
                    IConfigurationElement[] managedClass = element.getChildren(ELEM_MANAGED_CLASS);
                    String CIClassName;
                    if (managedClass != null) {
                        for (int k = 0; k < managedClass.length; k++) {
                            IConfigurationElement elem = managedClass[k];
                            CIClassName = elem.getAttribute(ATT_CICLASS_NAME);
                            managedClasses.put(CIClassName, managerName);
                        }
                    }
                }
            }
        }
        String managerName = managedClasses.get(aCIClass.getClassName());
        if (managerName != null) return CIClassManagers.get(managerName);
        return null;
    }
}
