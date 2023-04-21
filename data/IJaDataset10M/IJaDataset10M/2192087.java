package org.nomadpim.core.util.plugin.extension;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

public class ExtensionAccessFacade {

    public static ExtensionAccessFacade create() {
        return new ExtensionAccessFacade();
    }

    private final IExtensionRegistry extensionRegistry;

    private final IBundleResolver resolver;

    private ExtensionAccessFacade() {
        this.extensionRegistry = Platform.getExtensionRegistry();
        this.resolver = new PlatformBundleResolver();
    }

    public void analyzeExtension(IConfigurationElementAnalyzer analyzer) {
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(analyzer.getExtensionName());
        for (IExtension extension : extensionPoint.getExtensions()) {
            for (IConfigurationElement configuration : extension.getConfigurationElements()) {
                try {
                    analyzer.analyze(configuration);
                } catch (InvalidConfigurationException e) {
                    handleConfigurationException(analyzer, extension, e);
                } catch (CoreException e) {
                    handleConfigurationException(analyzer, extension, e);
                }
            }
        }
    }

    private ILog getLog(String bundleID) {
        return Platform.getLog(resolver.getBundle(bundleID));
    }

    private void handleConfigurationException(IConfigurationElementAnalyzer analyzer, IExtension extension, Exception e) {
        String bundleID = extension.getNamespace();
        String message = "unable to load extension " + analyzer.getExtensionName() + ": " + e.getMessage();
        Status status = new Status(IStatus.ERROR, bundleID, 0, message, e);
        getLog(bundleID).log(status);
    }
}
