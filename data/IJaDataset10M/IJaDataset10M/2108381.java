package com.ibm.celldt.environment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import com.ibm.celldt.environment.control.ITargetControl;
import com.ibm.celldt.environment.core.TargetElement;
import com.ibm.celldt.environment.core.TargetEnvironmentManager;
import com.ibm.celldt.environment.core.TargetTypeElement;
import com.ibm.celldt.environment.extension.ITargetTypeExtension;
import com.ibm.celldt.utils.extensionpoints.IProcessMemberVisitor;
import com.ibm.celldt.utils.extensionpoints.ProcessExtensions;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Ricardo M. Matinata
 * @since 1.1
 */
public class EnvironmentPlugin extends Plugin {

    public static final String FILENAME = "environments.xml";

    public static final String EXT_CONTROLS_ID = "com.ibm.celldt.environment.core.cellEnvironmentControlDelegate";

    public static final String EXT_PROVIDER_ID = "com.ibm.celldt.environment.core.cellTargetProvider";

    private static final String ID = "com.ibm.celldt.environment.core";

    private static EnvironmentPlugin plugin;

    private TargetEnvironmentManager manager;

    /**
	 * The constructor.
	 */
    public EnvironmentPlugin() {
        plugin = this;
    }

    public Map getControls() {
        final Map controls = new HashMap();
        ProcessExtensions.process(EXT_CONTROLS_ID, new IProcessMemberVisitor() {

            public Object process(IExtension extension, IConfigurationElement member) {
                Object mprovider;
                try {
                    mprovider = member.createExecutableExtension("class");
                    if (ITargetTypeExtension.class.isAssignableFrom(mprovider.getClass())) {
                        controls.put(member.getAttribute("name"), mprovider);
                    }
                } catch (CoreException e) {
                    mprovider = null;
                }
                return mprovider;
            }
        });
        return controls;
    }

    public TargetEnvironmentManager getTargetsManager() {
        if (manager == null) manager = new TargetEnvironmentManager();
        return manager;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        manager.writeToFile();
        plugin = null;
    }

    /**
	 * Notifies all target elements of the plugin that includes
	 * the parameter class. This parameter class must implement the
	 * ITargetTypeExtension interface.
	 * 
	 * @param Class The class that implements the ITargetTypeExtension interface
	 */
    public synchronized void destroyTypeElements(Class extensionClass) {
        List typeList = getTargetsManager().getTypeElements();
        for (Iterator typeIt = typeList.iterator(); typeIt.hasNext(); ) {
            TargetTypeElement typeElement = (TargetTypeElement) typeIt.next();
            if (typeElement.getExtension().getClass().equals(extensionClass)) {
                List elemList = typeElement.getElements();
                for (Iterator elemIt = elemList.iterator(); elemIt.hasNext(); ) {
                    TargetElement el = (TargetElement) elemIt.next();
                    try {
                        ITargetControl ctl = el.getControl();
                        ctl.destroy();
                    } catch (Throwable t) {
                    }
                }
            }
        }
    }

    /**
	 * Returns the shared instance.
	 */
    public static EnvironmentPlugin getDefault() {
        return plugin;
    }

    public static String getUniqueIdentifier() {
        return ID;
    }

    public String getEnvironmentUniqueID() {
        long envID = System.currentTimeMillis();
        return String.valueOf(envID);
    }

    public static final String ATTR_CORE_ENVIRONMENTID = "core-environmentid";
}
