package com.volantis.mcs.ibm.websphere.portalserver;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mcs.localization.LocalizationFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import com.ibm.wps.engine.RunData;
import com.ibm.wps.engine.portalfilter.PortalFilter;
import com.ibm.wps.engine.portalfilter.PortalFilterChain;
import com.ibm.wps.engine.portalfilter.PortalFilterConfig;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.marlin.sax.MarlinSAXHelper;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.mcs.servlet.MarinerServletApplication;
import com.volantis.mcs.servlet.MCSFilter;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;

/**
 * Class that can be plugged into the IBM WebSphere Portal Server servlet chain.
 * It takes the contents of the response, which will contain Marlin markup
 * and parses it into the correct device specific markup.
 * 
 * @author mat
 */
public class MCSPortalFilter implements PortalFilter {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(MCSPortalFilter.class);

    /**
     * The exception localizer used by this class.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(MCSFilter.class);

    /**
     * Holds configuration information for the filter.
     */
    private PortalFilterConfig filterConfig;

    /**
     * Create a new MCSPortalFilter
     */
    public MCSPortalFilter() {
    }

    public void init(PortalFilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    /**
     * This method will be called during processing of the filter chain.
     * We will be passed a response containing the Marlin markup for the page.
     * This needs to be parsed by Mariner into the correct markup for the 
     * device.
     * If Nariner has not been started, this method will initialise it.
     * 
     * An XMLPipelineContext will be created, with the base URI set to the
     * value of 
     * 
     * <pre>filterConfig.getServletContext().getResource("/")</pre>
     * 
     * This will normally point to the location of the WPS web application.
     * 
     * @param request The request
     * @param response The response
     * @param portalFilterChain The chain for the portal
     * @throws ServletException Servlet problem
     * @throws IOException An IO problem
     * 
     */
    public void doFilter(ServletRequest request, ServletResponse response, PortalFilterChain portalFilterChain) throws ServletException, IOException {
        MarinerServletApplication application = MarinerServletApplication.getInstance(filterConfig.getServletContext());
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String characterSet = RunData.from(request).getCharSet();
        if (logger.isDebugEnabled()) {
            logger.debug("WPS selected character set is " + characterSet);
        }
        MCSPortalResponseWrapper responseWrapper = new MCSPortalResponseWrapper(httpResponse, characterSet);
        MCSPortalRequestWrapper requestWrapper = new MCSPortalRequestWrapper(httpRequest, characterSet);
        portalFilterChain.doFilter(request, responseWrapper);
        responseWrapper.flushBuffer();
        int responseStatus = responseWrapper.getStatus();
        if (responseStatus >= 300 && responseStatus < 400) {
            if (logger.isDebugEnabled()) {
                logger.debug("Received redirect HTTP response header.  Returning without action.");
            }
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Contents received from portal: " + responseWrapper.getContentsAsString());
        }
        try {
            MarinerServletRequestContext servletRequestContext = new MarinerServletRequestContext(filterConfig.getServletContext(), requestWrapper, response);
            XMLReader xmlReader = MarlinSAXHelper.getXMLReader(servletRequestContext, null);
            EnvironmentContext environmentContext = ContextInternals.getEnvironmentContext(servletRequestContext);
            XMLPipelineContext pipelineContext = environmentContext.getPipelineContext();
            try {
                URL baseURI = filterConfig.getServletContext().getResource("/");
                String baseURIAsString = baseURI.toExternalForm();
                if (logger.isDebugEnabled()) {
                    logger.debug("Setting Base URI " + baseURIAsString);
                }
                pipelineContext.pushBaseURI(baseURIAsString);
            } catch (MalformedURLException e) {
                throw new ServletException(e);
            }
            Reader reader = responseWrapper.getReader();
            InputSource inputSource = new InputSource(reader);
            xmlReader.parse(inputSource);
            servletRequestContext.release();
        } catch (MarinerContextException e) {
            logger.error("portal-filter-exception", new Object[] { responseWrapper.getContentsAsString() });
            logger.error("mariner-context-exception", e);
            throw new ServletException(exceptionLocalizer.format("mariner-context-exception"), e);
        } catch (SAXException se) {
            logger.error("portal-filter-exception", new Object[] { responseWrapper.getContentsAsString() });
            logger.error("sax-exception-caught", se);
            Exception cause = se.getException();
            if (cause != null) {
                logger.error("root-cause", cause);
                throw new ServletException(exceptionLocalizer.format("root-cause"), cause);
            }
            throw new ServletException(exceptionLocalizer.format("sax-exception-caught"), se);
        }
    }

    /**
     * Destroy this filter
     * 
     */
    public void destroy() {
    }
}
