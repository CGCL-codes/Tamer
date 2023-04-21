package org.jcryptool.core.operations.providers;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;

public class ProviderManager2 {

    /** The log4j logger */
    private static final Logger logger = OperationsPlugin.getLogManager().getLogger(ProviderManager2.class.getName());

    private static final String factoryDefaultProvider = "FlexiCore";

    private static ProviderManager2 instance;

    private String defaultProviderName;

    private List<String> availableProviders = new ArrayList<String>();

    private ProviderManager2() {
        loadProviders();
    }

    public static ProviderManager2 getInstance() {
        if (instance == null) instance = new ProviderManager2();
        return instance;
    }

    private void loadProviders() {
        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(OperationsPlugin.PLUGIN_ID, IOperationsConstants.PL_PROVIDERS2);
        IExtension[] extensions = extensionPoint.getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
            for (int j = 0; j < configElements.length; j++) {
                try {
                    String[] attribs = configElements[j].getAttributeNames();
                    for (int k = 0; k < attribs.length; k++) {
                        logger.debug("attribName: " + attribs[k]);
                    }
                    AbstractProviderController controller = (AbstractProviderController) configElements[j].createExecutableExtension("providerController");
                    addProviders(controller.addProviders());
                } catch (CoreException e) {
                    logger.error("CoreException while accessing a provider controller", e);
                }
            }
        }
        logger.debug("activated providers:");
        Iterator<String> it = availableProviders.iterator();
        while (it.hasNext()) {
            logger.debug(it.next());
        }
    }

    private void addProviders(List<String> providers) {
        availableProviders.addAll(providers);
    }

    public Iterator<String> getOrderedProviderNames() {
        return availableProviders.iterator();
    }

    public Provider getProvider(String name) {
        return Security.getProvider(name);
    }

    public Provider getDefaultProvider() {
        return Security.getProvider(defaultProviderName);
    }

    public Provider getFactoryDefaultProvider() {
        return Security.getProvider(factoryDefaultProvider);
    }
}
