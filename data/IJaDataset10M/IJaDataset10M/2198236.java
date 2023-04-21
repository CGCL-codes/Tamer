package org.skyfree.ghyll.tcard.core.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.skyfree.ghyll.tcard.core.storage.ITCardStorageProvider;

/**
 * This class ...
 */
public class TCardStorage {

    static String TYPE_DEFAULT = "FileSystem";

    static ITCardStorageProvider selectProvider;

    static String TYPE_SELECT = TYPE_DEFAULT;

    static ITCardStorageProvider getProvider() {
        if (selectProvider == null) {
            IExtensionRegistry reg = Platform.getExtensionRegistry();
            if (reg == null) {
                return null;
            }
            IConfigurationElement[] extensions = reg.getConfigurationElementsFor("org.skyfree.ghyll.tcard.core.storage");
            for (int i = 0; i < extensions.length; i++) {
                IConfigurationElement element = extensions[i];
                String type = element.getAttribute("type");
                if (type.equalsIgnoreCase(TYPE_SELECT)) {
                    try {
                        selectProvider = (ITCardStorageProvider) element.createExecutableExtension("class");
                        selectProvider.setType(type);
                    } catch (CoreException e) {
                        Platform.getLog(Platform.getBundle("org.skyfree.ghyll.tcard.core")).log(e.getStatus());
                    }
                }
            }
        }
        return selectProvider;
    }
}
