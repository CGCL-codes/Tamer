package org.pustefixframework.config.application.parser.internal;

import java.net.URI;
import java.util.List;
import org.pustefixframework.extension.StaticResourceExtension;
import org.pustefixframework.extension.StaticResourceExtensionPoint;
import org.pustefixframework.extension.support.AbstractExtension;
import org.pustefixframework.resource.InputStreamResource;
import org.pustefixframework.resource.ResourceLoader;

/**
 * Extension for static resource extension point.  
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class StaticResourceExtensionImpl extends AbstractExtension<StaticResourceExtensionPoint, StaticResourceExtensionImpl> implements StaticResourceExtension {

    private List<String> modulePathPrefixes;

    private List<StaticResourceExtensionPointImpl> extensionPoints;

    private ResourceLoader resourceLoader;

    private String requestPathPrefix;

    private static final URI MODULE_URI_PREFIX = URI.create("bundle:///PUSTEFIX-INF/");

    public StaticResourceExtensionImpl() {
        setExtensionPointType(StaticResourceExtensionPoint.class);
    }

    public void setStaticResourceExtensionPoints(List<StaticResourceExtensionPointImpl> extensionPoints) {
        this.extensionPoints = extensionPoints;
    }

    public void setModulePathPrefixes(List<String> modulePathPrefixes) {
        this.modulePathPrefixes = modulePathPrefixes;
    }

    public void setRequestPathPrefix(String requestPathPrefix) {
        if (requestPathPrefix == null) {
            this.requestPathPrefix = null;
            return;
        }
        requestPathPrefix = requestPathPrefix.trim();
        if (requestPathPrefix.startsWith("/")) {
            requestPathPrefix = requestPathPrefix.substring(1);
        }
        if (requestPathPrefix.length() == 0) {
            this.requestPathPrefix = null;
            return;
        }
        if (!requestPathPrefix.endsWith("/")) {
            requestPathPrefix += "/";
        }
        this.requestPathPrefix = requestPathPrefix;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public InputStreamResource getResource(URI resourceURI) {
        URI shortResourceURI = null;
        if (requestPathPrefix != null) {
            if (!resourceURI.getPath().startsWith(requestPathPrefix)) {
                return null;
            }
            String path = resourceURI.getPath().substring(requestPathPrefix.length());
            while (path.length() > 0 && path.charAt(0) == '/') {
                path = path.substring(1);
            }
            if (path.length() == 0) {
                return null;
            }
            shortResourceURI = URI.create(path);
        } else {
            shortResourceURI = resourceURI;
        }
        for (String prefix : modulePathPrefixes) {
            if (prefix.startsWith("/")) {
                prefix = prefix.substring(1);
            }
            if (!prefix.endsWith("/")) {
                prefix = prefix + "/";
            }
            if (shortResourceURI.getPath().startsWith(prefix)) {
                InputStreamResource resource = resourceLoader.getResource(MODULE_URI_PREFIX.resolve(shortResourceURI), InputStreamResource.class);
                if (resource != null) {
                    return resource;
                }
                break;
            }
        }
        for (StaticResourceExtensionPointImpl extensionPoint : extensionPoints) {
            for (StaticResourceExtension extension : extensionPoint.getExtensions()) {
                InputStreamResource resource = extension.getResource(resourceURI);
                if (resource != null) {
                    return resource;
                }
            }
        }
        return null;
    }
}
