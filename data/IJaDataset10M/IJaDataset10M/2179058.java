package org.jminer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class JMinerPlugin extends AbstractUIPlugin {

    private static final String ID = "org.jminer";

    private static JMinerPlugin singleton;

    private static JMiner jminer;

    public static JMinerPlugin getDefault() {
        return singleton;
    }

    /** 
	 * The constructor. 
	 */
    public JMinerPlugin() {
        if (singleton == null) {
            singleton = this;
            jminer = new JMinerImpl();
            IExtensionRegistry registry = Platform.getExtensionRegistry();
            IExtensionPoint extensionPoint = registry.getExtensionPoint("org.jminer.NodeInfo");
            IExtension extension[] = extensionPoint.getExtensions();
            for (IExtension ex : extension) {
                IConfigurationElement[] configElements = ex.getConfigurationElements();
                try {
                    jminer.addNodeInfo((JMinerNodeInfo) configElements[0].createExecutableExtension("className"));
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ImageDescriptor getImageDescriptor(String imageFilePath) {
        return imageDescriptorFromPlugin(ID, imageFilePath);
    }

    public static JMiner getJMiner() {
        return jminer;
    }
}
