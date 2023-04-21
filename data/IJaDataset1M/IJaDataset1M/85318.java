package com.ibm.celldt.sputiming.extension;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import com.ibm.celldt.utils.extensionpoints.ExtensionPointEnumeration;

/**
 * @author Richard Maciel
 *
 */
public class ExternalTools {

    /**
	 * Fetch all classes from plugins that extend the 
	 * observer extension point, calling their {@link ISPUTimingObserver#afterFileGeneration(IPath)}
	 * method, passing the file as parameter.
	 * @param file
	 */
    public static void callExtensions(IPath file) {
        ExtensionPointEnumeration epe = new ExtensionPointEnumeration("com.ibm.celldt.sputiming.observerpoint");
        while (epe.hasMoreElements()) {
            IConfigurationElement ice = (IConfigurationElement) epe.nextElement();
            try {
                Object beholderWOType = ice.createExecutableExtension("class");
                ISPUTimingObserver spubeholder = (ISPUTimingObserver) beholderWOType;
                spubeholder.afterFileGeneration(file);
            } catch (CoreException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
