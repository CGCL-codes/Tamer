package com.volantis.mcs.servlet;

import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.configuration.RenderedPageCacheConfiguration;
import com.volantis.mcs.runtime.configuration.ServletFilterConfiguration;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.url.URLConfigurationFactory;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Class that can be plugged into an application server servlet chain.
 * It takes the contents of the response, which may contain Marlin markup
 * and, if so, parses it into the correct device specific markup. Otherwise
 * the response is passed through untouched.
 */
public class MCSFilter implements Filter {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(MCSFilter.class);

    /**
     * The exception localizer used by this class.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(MCSFilter.class);

    /**
     * Holds configuration information for the filter.
     */
    private FilterConfig filterConfig;

    /**
     * The MarinerServletApplication used by the filter.
     */
    private static MarinerServletApplication application;

    /**
     * The list of excluded devices. An excluded device is one for which no
     * MCS processing of markup is done.
     */
    private List excludedDevices;

    /**
     * A flag indicating whether the filter is operating in "pass through" mode.
     * If there is no servlet configuration in mcs-config.xml, this flag is
     * set to true.
     */
    private boolean passThroughMode;

    /**
     * The object that actually handles XDIME request processing. Will be
     * null when the filter is operating in pass-through mode.
     */
    private XDIMERequestProcessor xdimeRequestProcessor = null;

    /**
     * Constant value for the HTTP HEAD method value returned by
     * {@link HttpServletRequest#getMethod} when an HTTP HEAD request
     * is being serviced.
     */
    private static final String HTTP_HEAD_METHOD = "HEAD";

    /**
     * Create a new MCSFilter.
     */
    public MCSFilter() {
    }

    /**
     * Initialise this filter. This is done once by the application server
     * when it starts up. A list of all descendant devices for all excluded
     * devices configured in MCS is determined here.
     *
     * @param filterConfig The filterConfig
     * @throws ServletException if there is a problem during initialisation
     *
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        ServletContext servletContext = filterConfig.getServletContext();
        try {
            application = MarinerServletApplication.getInstance(filterConfig.getServletContext());
        } catch (Throwable t) {
            throw new ServletException(t);
        }
        Volantis volantis = ApplicationInternals.getVolantisBean(application);
        ServletFilterConfiguration config = volantis.getServletFilterConfiguration();
        if (config != null) {
            passThroughMode = false;
            excludedDevices = config.getExcludedDevices();
            if (logger.isDebugEnabled()) {
                Iterator it = excludedDevices.iterator();
                while (it.hasNext()) {
                    String device = (String) it.next();
                    logger.debug("Read excluded device: " + device);
                }
            }
            List xdimeMimeTypes = config.getMimeTypes();
            if (logger.isDebugEnabled()) {
                Iterator it = xdimeMimeTypes.iterator();
                while (it.hasNext()) {
                    String type = (String) it.next();
                    logger.debug("Read mime type: " + type);
                }
            }
            try {
                addDescendantDevices();
            } catch (RepositoryException e) {
                logger.error("mcs-filter-child-device-error", e);
                throw new ServletException(exceptionLocalizer.format("mcs-filter-child-device-error"), e);
            }
            RenderedPageCacheConfiguration cacheConfig = config.getRenderedPageCacheConfig();
            if (cacheConfig != null) {
                xdimeRequestProcessor = new CachingXDIMERequestProcessor(servletContext, xdimeMimeTypes, cacheConfig, config.getJsessionIdName());
            } else {
                xdimeRequestProcessor = new SimpleXDIMERequestProcessor(servletContext, xdimeMimeTypes);
            }
        } else {
            passThroughMode = true;
            if (logger.isDebugEnabled()) {
                logger.debug("servlet-configuration not present. " + "Filter operating in pass-through mode.");
            }
        }
    }

    /**
     * This method will be called during processing of the filter chain.
     * We will be passed a response containing the Marlin markup for the page.
     * This needs to be parsed by Mariner into the correct markup for the
     * device. If Mariner has not been started, this method will initialise it.
     *
     * An XMLPipelineContext will be created, with the base URI set to the
     * value of:
     *
     * <pre>filterConfig.getServletContext().getResource("/")</pre>
     *
     * This will normally point to the location of the WPS web application.
     *
     * @param request The request
     * @param response The response
     * @param filterChain the filter chain in use
     * @throws ServletException Servlet problem
     * @throws IOException An IO problem
     *
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!passThroughMode) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            final String header = httpRequest.getHeader(URLConfigurationFactory.VISITED_MCS_INSTANCES_HEADER_NAME);
            if (containsCurrentInstanceIdentifier(header)) {
                logger.warn("reentrant-request-found", httpRequest.getRequestURL().toString());
            }
            MCSCapabilitiesHandler capabilitiesHandler = new MCSCapabilitiesHandler(httpRequest, httpResponse);
            if (capabilitiesHandler.isMCSCapabilitiesRequest()) {
                capabilitiesHandler.processMCSCapabilitiesRequest();
            } else {
                Device device;
                try {
                    device = application.getDevice(httpRequest);
                } catch (RepositoryException e) {
                    logger.error("mcs-filter-request-device-error", e);
                    throw new ServletException(exceptionLocalizer.format("mcs-filter-request-device-error"), e);
                }
                final String deviceName = device.getName();
                final boolean excluded = excludedDevices.contains(deviceName);
                if (!excluded) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Device " + deviceName + " is not excluded.");
                    }
                    CachingResponseWrapper cachingResponse = new CachingResponseWrapper(httpResponse);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Passing wrapped response through.");
                    }
                    MCSRequestWrapper requestWrapper = new MCSRequestWrapper(httpRequest);
                    filterChain.doFilter(requestWrapper, cachingResponse);
                    int responseStatus = cachingResponse.getStatus();
                    if (logger.isDebugEnabled()) {
                        logger.debug("Response status is: " + responseStatus);
                    }
                    if (responseStatus >= 300 && responseStatus < 400) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Received redirect HTTP response header. " + "Returning without action.");
                        }
                        return;
                    }
                    String mimeType = cachingResponse.getMimeTypeFromContentType();
                    final boolean processXDIME = xdimeRequestProcessor.isXDIME(mimeType);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Response mime type is: " + mimeType);
                    }
                    cachingResponse.flushBuffer();
                    if (isHttpHeadRequest(request)) {
                        httpResponse = new EmptyBodyResponseWrapper(httpResponse);
                    }
                    if (processXDIME) {
                        final boolean disableResponseCaching = !ServletEnvironmentContext.VARY_HEADER_NAMES.containsAll(((DefaultDevice) device).getIdentificationHeaderNames());
                        xdimeRequestProcessor.processXDIME(filterConfig.getServletContext(), requestWrapper, httpResponse, cachingResponse, cachingResponse.getCharacterEncoding(), disableResponseCaching);
                    } else {
                        cachingResponse.writeTo(httpResponse);
                    }
                    if (isHttpHeadRequest(request)) {
                        ((EmptyBodyResponseWrapper) httpResponse).updateContentLength();
                    }
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Device " + deviceName + " is excluded.");
                    }
                    passThrough(request, response, filterChain);
                }
            }
        } else {
            passThrough(request, response, filterChain);
        }
    }

    /**
     * Returns true iff the specified comma-separated identifier list contains
     * the current MCS instance identifier.
     *
     * @param identifierList the list of MCS instance identifiers, may be null
     * @return true, iff the list contains the current identifier
     */
    private boolean containsCurrentInstanceIdentifier(final String identifierList) {
        boolean found = false;
        if (identifierList != null && identifierList.trim().length() > 0) {
            final Volantis volantis = ApplicationInternals.getVolantisBean(application);
            final String mcsInstanceIdentifier = volantis.getMCSInstanceIdentifier();
            final StringTokenizer tokenizer = new StringTokenizer(identifierList, ", \t");
            while (tokenizer.hasMoreTokens() && !found) {
                final String identifier = tokenizer.nextToken();
                if (identifier.equals(mcsInstanceIdentifier)) {
                    found = true;
                }
            }
        }
        return found;
    }

    /**
     * Passes the original request and response through the filter chain
     * unmodified.
     * @param request the request
     * @param response the response
     * @param filterChain the filter chain to use
     * @throws IOException
     * @throws ServletException
     */
    private void passThrough(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("Passing original response through.");
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Adds all the descendant devices of the devices in the exclusion list.
     * @throws RepositoryException if there is a problem acessing the device
     * repository
     */
    private void addDescendantDevices() throws RepositoryException {
        RepositoryConnection connection = null;
        try {
            Volantis volantis = ApplicationInternals.getVolantisBean(application);
            connection = volantis.getDeviceConnection();
            List excCopy = new ArrayList(excludedDevices);
            if (logger.isDebugEnabled()) {
                Iterator it = excludedDevices.iterator();
                while (it.hasNext()) {
                    logger.debug("Added parent device for exclusion: " + it.next());
                }
            }
            Iterator it = excCopy.iterator();
            while (it.hasNext()) {
                String deviceName = (String) it.next();
                List children = enumerateAllDevicesChildren(deviceName, connection);
                excludedDevices.addAll(children);
            }
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Get a list of all the descendant children of the given device.
     * @param deviceName the device of interest
     * @param conn the repository connection to use
     * @return the list of devices
     * @throws RepositoryException if there is a problem accessing the device
     * repository
     */
    private List enumerateAllDevicesChildren(final String deviceName, final RepositoryConnection conn) throws RepositoryException {
        RepositoryEnumeration immediateChildren = null;
        try {
            Volantis volantis = ApplicationInternals.getVolantisBean(application);
            immediateChildren = volantis.getDeviceRepositoryAccessor().enumerateDevicesChildren(conn, deviceName);
            final List allChildren = immediateChildren.hasNext() ? new ArrayList() : Collections.EMPTY_LIST;
            while (immediateChildren.hasNext()) {
                final String child = (String) immediateChildren.next();
                allChildren.add(child);
                if (logger.isDebugEnabled()) {
                    logger.debug("Added child device for exclusion: " + child);
                }
                allChildren.addAll(enumerateAllDevicesChildren(child, conn));
            }
            return allChildren;
        } finally {
            if (immediateChildren != null) {
                immediateChildren.close();
            }
        }
    }

    /**
     * Returns true if the supplied request is an HTTP HEAD request;
     * otherwise returns false.
     *
     * @param request The request to be tested for being an HTTP HEAD request.
     *
     * @return true if HTTP HEAD request; otherwise false.
     */
    private boolean isHttpHeadRequest(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        return HTTP_HEAD_METHOD.equals(httpRequest.getMethod());
    }

    /**
     * This method returns the instance of the MarinerServletApplication
     * created at initialisation of the ServletFilter.
     * <p>This is useful in a JSR168
     * environment where a user can not create an Instance of
     * MarinerServletApplication.</p>
     *
     * @return The MarinerServletApplication
     */
    public MarinerServletApplication getMarinerServletApplication() {
        return application;
    }

    /**
     * Destroy this filter
     */
    public void destroy() {
    }
}
