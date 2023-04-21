package org.jmage.dispatcher;

import org.apache.log4j.Logger;
import org.jmage.ImageRequest;
import org.jmage.JmageException;
import org.jmage.filterchain.FilterChainManager;
import org.jmage.resource.ResourceManager;

/**
 * InternalFilteringRequestDispatcher omits encoding and has all other steps defined as optional. If no image is
 * specified, or if it finds an imageURI, it tries to load one through resourceManager. If a chain URI is specified,
 * it filters the image through it, else it returns straight away.
 */
public class InternalRequestDispatcher extends FilteringRequestDispatcher {

    protected static Logger log = Logger.getLogger(InternalRequestDispatcher.class.getName());

    /**
     * Dispatch an ImageRequest for internal purposes.
     */
    public void dispatch(ImageRequest imageRequest) throws JmageException {
        if (imageRequest.getImage() == null || imageRequest.getImageURI() != null) {
            ResourceManager resourceManager = context.obtainResourceManager();
            resourceManager.handle(imageRequest);
            context.releaseResourceManager(resourceManager);
        }
        if (imageRequest.getFilterChainURI() != null) {
            imageRequest.getFilterChainProperties().clear();
            FilterChainManager filterChainManager = context.obtainFilterChainManager();
            filterChainManager.handle(imageRequest);
            context.releaseFilterChainManager(filterChainManager);
        }
    }
}
