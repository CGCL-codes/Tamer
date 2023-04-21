package org.eclipse.equinox.http.registry.internal;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.*;
import org.osgi.framework.*;

public class ResourceManager implements ExtensionPointTracker.Listener {

    private static final String RESOURCES_EXTENSION_POINT = "org.eclipse.equinox.http.registry.resources";

    private static final String HTTPCONTEXT_NAME = "httpcontext-name";

    private static final String BASE_NAME = "base-name";

    private static final String ALIAS = "alias";

    private static final String RESOURCE = "resource";

    private static final String HTTPCONTEXT_ID = "httpcontextId";

    private static final String SERVICESELECTOR = "serviceSelector";

    private static final String CLASS = "class";

    private static final String FILTER = "filter";

    private ExtensionPointTracker tracker;

    private List registered = new ArrayList();

    private HttpRegistryManager httpRegistryManager;

    private ServiceReference reference;

    public ResourceManager(HttpRegistryManager httpRegistryManager, ServiceReference reference, IExtensionRegistry registry) {
        this.httpRegistryManager = httpRegistryManager;
        this.reference = reference;
        tracker = new ExtensionPointTracker(registry, RESOURCES_EXTENSION_POINT, this);
    }

    public void start() {
        tracker.open();
    }

    public void stop() {
        tracker.close();
    }

    public void added(IExtension extension) {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (int i = 0; i < elements.length; i++) {
            IConfigurationElement serviceSelectorElement = elements[i];
            if (!SERVICESELECTOR.equals(serviceSelectorElement.getName())) continue;
            org.osgi.framework.Filter serviceSelector = null;
            String clazz = serviceSelectorElement.getAttribute(CLASS);
            if (clazz != null) {
                try {
                    serviceSelector = (org.osgi.framework.Filter) serviceSelectorElement.createExecutableExtension(CLASS);
                } catch (CoreException e) {
                    e.printStackTrace();
                    return;
                }
            } else {
                String filter = serviceSelectorElement.getAttribute(FILTER);
                if (filter == null) return;
                try {
                    serviceSelector = FrameworkUtil.createFilter(filter);
                } catch (InvalidSyntaxException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (!serviceSelector.match(reference)) return;
            break;
        }
        for (int i = 0; i < elements.length; i++) {
            IConfigurationElement resourceElement = elements[i];
            if (!RESOURCE.equals(resourceElement.getName())) continue;
            String alias = resourceElement.getAttribute(ALIAS);
            if (alias == null) continue;
            String baseName = resourceElement.getAttribute(BASE_NAME);
            if (baseName == null) baseName = "";
            String httpContextId = resourceElement.getAttribute(HTTPCONTEXT_ID);
            if (httpContextId == null) {
                httpContextId = resourceElement.getAttribute(HTTPCONTEXT_NAME);
            }
            if (httpContextId != null && httpContextId.indexOf('.') == -1) httpContextId = resourceElement.getNamespaceIdentifier() + "." + httpContextId;
            if (httpRegistryManager.addResourcesContribution(alias, baseName, httpContextId, extension.getContributor())) registered.add(resourceElement);
        }
    }

    public void removed(IExtension extension) {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (int i = 0; i < elements.length; i++) {
            IConfigurationElement resourceElement = elements[i];
            if (registered.remove(resourceElement)) {
                String alias = resourceElement.getAttribute(ALIAS);
                httpRegistryManager.removeContribution(alias);
            }
        }
    }
}
