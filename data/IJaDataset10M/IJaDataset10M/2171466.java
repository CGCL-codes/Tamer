package org.jlense.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * Utility methods for finding registered application components, starting 
 * application components, and stopping application components.
 * 
 * @see org.jlense.util.IApplicationComnponent
 * 
 * @author  ted stockwell [emorning@yahoo.com]
 */
public class ApplicationComponentUtils {

    /**
     * A collection of IApplicationComponents indexed by component id.
     */
    private static Map _applicationComponents = new HashMap();

    /**
     * Starts all application components used by an application or application 
     * component.
     * @param  configurationElement The application's or component's configuration 
     *          element.  The given element should have &lt;component&gt; child 
     *          elements.
     * 
     * @throws CoreException  If there is any configuration problem or problem 
     *              starting the components.
     */
    public static synchronized void startApplicationComponents(IConfigurationElement configurationElement) throws CoreException {
        IApplicationComponent[] components = getApplicationComponents(configurationElement);
        for (int i = 0; i < components.length; i++) {
            IApplicationComponent component = components[i];
            startApplicationComponent(component);
        }
    }

    /**
     * Stops all application components used by an application or application 
     * component.
     * @param  configurationElement The application's or component's configuration 
     *          element.  The given element should have &lt;component&gt; child 
     *          elements.
     */
    public static synchronized void stopApplicationComponents(IConfigurationElement configurationElement) throws CoreException {
        IApplicationComponent[] components = getApplicationComponents(configurationElement);
        for (int i = 0; i < components.length; i++) {
            IApplicationComponent component = components[i];
            try {
                stopApplicationComponent(component);
            } catch (CoreException x) {
                IPluginDescriptor pluginDescriptor = component.getDeclaringExtension().getDeclaringPluginDescriptor();
                IStatus status = new Status(IStatus.ERROR, pluginDescriptor.getUniqueIdentifier(), Platform.PLUGIN_ERROR, "Error while stopping application component", x);
                pluginDescriptor.getPlugin().getLog().log(status);
            }
        }
    }

    /**
     * Generates a list of application components used by an application or 
     * application component.
     * @param  configurationElement The application's or component's configuration 
     *          element.  This method assumes that there is a <run> child element 
     *          that contains <component> elements. 
     * 
     * @return a collection of IApplicationComponents
     * @throws CoreException  If there is any configuration problem.
     */
    public static IApplicationComponent[] getApplicationComponents(IConfigurationElement configurationElement) throws CoreException {
        Collection components = new ArrayList();
        IConfigurationElement[] preactivates = configurationElement.getChildren("component");
        for (int ipreactivate = 0; preactivates != null && ipreactivate < preactivates.length; ipreactivate++) {
            IConfigurationElement preactivateElement = preactivates[ipreactivate];
            String componentId = preactivateElement.getAttribute("id");
            if (componentId == null || componentId.length() <= 0) {
                throw new CoreException(new Status(IStatus.ERROR, configurationElement.getDeclaringExtension().getDeclaringPluginDescriptor().getUniqueIdentifier(), Platform.PLUGIN_ERROR, "<component> element is missing id attribute", null));
            }
            IApplicationComponent component = getApplicationComponent(componentId);
            components.add(component);
        }
        IApplicationComponent[] aComponents = new IApplicationComponent[components.size()];
        components.toArray(aComponents);
        return aComponents;
    }

    public static synchronized void startApplicationComponent(IApplicationComponent component) throws CoreException {
        IConfigurationElement[] configurationElements = component.getConfigurationElements();
        for (int i = 0; i < configurationElements.length; i++) startApplicationComponents(configurationElements[i]);
        if (!component.isRunning()) component.start();
    }

    public static synchronized void stopApplicationComponent(IApplicationComponent component) throws CoreException {
        IConfigurationElement[] configurationElements = component.getConfigurationElements();
        for (int i = 0; i < configurationElements.length; i++) stopApplicationComponents(configurationElements[i]);
        if (component.isRunning()) component.stop();
    }

    protected static IApplicationComponent getApplicationComponent(String componentId) throws CoreException {
        IApplicationComponent component = (IApplicationComponent) _applicationComponents.get(componentId);
        if (component != null) return component;
        IPluginRegistry registry = Platform.getPluginRegistry();
        IExtensionPoint extentionPoint = registry.getExtensionPoint("org.jlense.util.components");
        IExtension[] extensions = extentionPoint.getExtensions();
        if (extensions != null) {
            for (int i = 0; component == null && i < extensions.length; i++) {
                IExtension managerExtension = extensions[i];
                if (!componentId.equals(managerExtension.getUniqueIdentifier())) continue;
                IConfigurationElement[] managerElements = managerExtension.getConfigurationElements();
                IPluginDescriptor managerPluginDescriptor = managerExtension.getDeclaringPluginDescriptor();
                try {
                    for (int iconf = 0; component == null && iconf < managerElements.length; iconf++) {
                        IConfigurationElement element = managerElements[iconf];
                        if (element.getName().equalsIgnoreCase("component")) {
                            component = (IApplicationComponent) element.createExecutableExtension("run");
                            _applicationComponents.put(componentId, component);
                        }
                    }
                } catch (Exception x) {
                    if (x instanceof CoreException) throw (CoreException) x;
                    IStatus status = new Status(IStatus.ERROR, managerPluginDescriptor.getUniqueIdentifier(), Platform.PLUGIN_ERROR, "Could not create application component:" + componentId, x);
                    throw new CoreException(status);
                }
            }
        }
        if (component == null) {
            IStatus status = new Status(IStatus.ERROR, org.jlense.util.internal.UtilPlugin.getDefault().getDescriptor().getUniqueIdentifier(), Platform.PLUGIN_ERROR, "Unknown application component:" + componentId, null);
            throw new CoreException(status);
        }
        return component;
    }
}
