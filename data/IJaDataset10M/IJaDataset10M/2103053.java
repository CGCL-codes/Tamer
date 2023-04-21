package nz.ac.massey.cs.barrio.extensionFinder;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * Utility to find extensions that implement a given interface or subclass a given class.
 * @author <a href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich </a>
 * @version 1.1
 * @since 1.1
 */
public class ExtensionFinderUtil<T> {

    public List<T> findExtensionObjects(String extensionPointId, String implementationClassAttributeName) {
        List<T> list = new ArrayList<T>();
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint(extensionPointId);
        if (point == null) System.err.println("[xfinder]: Cannot find extension point " + extensionPointId); else System.out.println("[xfinder]: Extension point found " + extensionPointId);
        IExtension[] extensions = point.getExtensions();
        if (extensions == null) System.err.println("[xfinder]: Extensions for " + extensionPointId + " are null");
        System.out.println("[xfinder]: number extensions = " + extensions.length);
        for (int i = 0; i < extensions.length; i++) {
            IExtension x = extensions[i];
            System.out.println("[xfinder]: Extension found " + x.getUniqueIdentifier());
            IConfigurationElement[] attributes = x.getConfigurationElements();
            String pluginId = x.getNamespace();
            for (int j = 0; j < attributes.length; j++) {
                IConfigurationElement p = attributes[j];
                String clazz = p.getAttribute(implementationClassAttributeName);
                try {
                    Class c = Platform.getBundle(pluginId).loadClass(clazz);
                    T inst = (T) c.newInstance();
                    list.add(inst);
                } catch (Exception ex) {
                    System.err.println("[xfinder]: " + ex + " Error loading extension for extension point: " + extensionPointId);
                    ex.printStackTrace();
                }
            }
        }
        return list;
    }
}
