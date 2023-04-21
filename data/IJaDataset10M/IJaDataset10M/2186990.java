package org.josef.web.jsf.facelets.tag;

import java.net.URL;
import com.sun.facelets.impl.DefaultResourceResolver;
import com.sun.facelets.impl.ResourceResolver;

/**
 * Custom Resource Resolver for Facelets.
 * <br />The Default Resource Resolver will only locate facelets within the
 * same war. This implementation overcomes this limitation. To use this
 * resolver add the following code to your web.xml.<pre><code>
 * <context-param>
 *   <param-name>facelets.RESOURCE_RESOLVER</param-name>
 *   <param-value>CustomResourceResolver</param-value>
 * </context-param></code></pre>
 * @author Kees Schotanus
 * @version 1.0 $Revision: 584 $
 */
public class CustomResourceResolver extends DefaultResourceResolver implements ResourceResolver {

    /**
     * Resolves the supplied resource to a URL.
     * @param resource The resource to resolve to a URL.
     * @return The URL corresponding to the supplied resource or null when the
     *  resource could not be located.
     */
    @Override
    public URL resolveUrl(final String resource) {
        URL resourceUrl = super.resolveUrl(resource);
        if (resourceUrl == null) {
            final String relativeResource = resource.startsWith("/") ? resource.substring(1) : resource;
            resourceUrl = Thread.currentThread().getContextClassLoader().getResource(relativeResource);
        }
        return resourceUrl;
    }
}
