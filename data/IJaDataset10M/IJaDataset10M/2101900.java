package org.middleheaven.process.web.server;

import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.process.web.UrlMapping;
import org.middleheaven.process.web.server.filters.HttpFilter;
import org.middleheaven.process.web.server.global.HttpCultureResolver;
import org.middleheaven.web.rendering.RenderingProcessor;
import org.middleheaven.web.rendering.RenderingProcessorResolver;

/**
 * Service to process server-side HTTP requests.
 *
 */
@Service
public interface HttpServerService {

    public void addFilter(HttpFilter filter);

    public void removeFilter(HttpFilter filter);

    public void setHttpCultureResolver(HttpCultureResolver httpCultureResolveService);

    public HttpCultureResolver getHttpCultureResolver();

    public void registerHttpProcessor(String processorID, HttpProcessor procesor, UrlMapping mapping);

    public void unRegisterHttpProcessor(String processorID);

    public void addRenderingProcessorResolver(String resolverID, RenderingProcessorResolver resolver, UrlMapping mapping);

    public void removeRenderingProcessorResolver(String resolverID);

    public void removeAllRenderingProcessors();

    /**
	 * Discovers and returns the ViewProcessor that can, according to it's registered UrlMapping, render the given url.
	 * @param incomingUrl the filename from the request
	 * @param outcomeUrl the view name from the outcome
	 * @param contentType the contentType for the result content.
	 * 
	 * @return or <code>null</code> if none matches 
	 */
    public RenderingProcessor resolverRenderingProcessor(String incomingUrl, String viewName, String contentType);

    /**
	 * Discovers and returns the HttpProcessor that can, according to it's registered UrlMapping, process the given url.
	 * @param url the url to process.
	 * @return or <code>null</code> if none matches 
	 */
    public HttpProcessor resolveControlProcessor(String url);

    /**
	 * Set the availability state of this service.
	 * 
	 * If the service is available requests will be processed.
	 * If not, a TEMPORARY_UNAVAILABLE HTTP error code will be returned.
	 * @param available the availability status of the {@link HttpServerService}.
	 */
    public void setAvailable(boolean available);

    /**
	 * 
	 * @return {@code true} if the service is available, {@code false} otherwise
	 */
    public boolean isAvailable();

    /**
	 * Initialize the server and put in an available state
	 * 
	 *  @see #setAvailable(boolean)
	 */
    public void start();

    /**
	 * Renders the server inert. No further requests will be processed.
	 * After calling this method this service instance is useless and should be removed.
	 */
    public void stop();
}
