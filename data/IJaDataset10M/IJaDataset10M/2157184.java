package com.volantis.mcs.servlet;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.marlin.sax.MCSInternalContentHandler;
import com.volantis.mcs.service.ServiceDefinition;
import com.volantis.mcs.service.ServiceDefinitionHelper;
import com.volantis.shared.content.ContentStyle;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import org.xml.sax.SAXException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides common XDIME processing facilities required by various servlets
 * and servlet filters.
 */
public class SimpleXDIMERequestProcessor implements XDIMERequestProcessor {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(SimpleXDIMERequestProcessor.class);

    /**
     * The exception localizer used by this class.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(SimpleXDIMERequestProcessor.class);

    /**
     * The list of MIME types denoting XDIME markup.
     */
    private final List<String> xdimeMIMETypes;

    /**
     * delegate helper object - this object is being used to enable this to
     * class to be tested.
     */
    protected XDIMERequestProcessorHelper xdimeRequestProcessorHelper;

    /**
     * Initializes the new instance using the given xdimeMIMETypes. This
     * constructor will initialise a default xdime request processor which
     * well the be delegated to inorder to perform the processing
     *
     * @param servletContext servlet context
     * @param xdimeMIMETypes the MIME types that represent XDIME
     */
    public SimpleXDIMERequestProcessor(ServletContext servletContext, List<String> xdimeMIMETypes) {
        this.xdimeMIMETypes = new ArrayList<String>(xdimeMIMETypes);
        xdimeRequestProcessorHelper = new DefaultXDIMERequestProcessorHelper(servletContext);
    }

    public boolean isXDIME(String mimeType) {
        return xdimeMIMETypes.contains(mimeType);
    }

    /**
     * Configures the environment context with any PMSS service information
     * that is needed
     * @param marinerRequestContext the MarnierRequestContext
     * @param environmentContext the Environment Context
     */
    private static void configureServiceDefintion(MarinerServletRequestContext marinerRequestContext, EnvironmentContext environmentContext) {
        HttpServletRequest request = marinerRequestContext.getHttpRequest();
        ServiceDefinition service = ServiceDefinitionHelper.retrieveService(request);
        if (service != null) {
            ExpressionContext expressionContext = environmentContext.getExpressionContext();
            expressionContext.setProperty(ServiceDefinition.class, service, false);
        }
    }

    public void processXDIME(final ServletContext servletContext, final ServletRequest request, final ServletResponse response, final CachedContent xdimeContent, final String characterSet, final boolean disableResponseCaching) throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            String xdimeContentValue = (xdimeContent.getContentStyle() == ContentStyle.TEXT) ? new String(xdimeContent.getAsCharArray()) : new String(xdimeContent.getAsByteArray());
            logger.debug("XDIME content to be processed: " + xdimeContentValue);
        }
        MarinerServletRequestContext marinerRequestContext = null;
        try {
            marinerRequestContext = xdimeRequestProcessorHelper.createServletRequestContext(servletContext, request, response);
            final EnvironmentContext environmentContext = ContextInternals.getEnvironmentContext(marinerRequestContext);
            configureServiceDefintion(marinerRequestContext, environmentContext);
            final ResponseCachingDirectives cachingDirectives = environmentContext.getCachingDirectives();
            if (cachingDirectives != null && disableResponseCaching) {
                cachingDirectives.disable();
            }
            processXDIME(marinerRequestContext, xdimeContent, characterSet);
        } catch (MarinerContextException e) {
            logger.error("mariner-context-exception", e);
            throw new ServletException(exceptionLocalizer.format("mariner-context-exception"), e);
        } catch (SAXException se) {
            Exception cause = se.getException();
            logger.error("sax-exception-caught", se);
            if (cause != null) {
                logger.error("root-cause", cause);
                throw new ServletException(exceptionLocalizer.format("root-cause"), cause);
            }
            throw new ServletException(exceptionLocalizer.format("sax-exception-caught"), se);
        } catch (Throwable t) {
            logger.error("unexpected-exception", t);
            throw new ServletException(t);
        } finally {
            if (marinerRequestContext != null) {
                marinerRequestContext.release();
            }
        }
    }

    /**
     * Processes the response as XDIME.
     * @param marinerRequestContext the mariner request context     
     * @param xdimeContent used to read out the XDIME
     * @param characterSet the character encoding
     * @throws IOException if an error occurs
     * @throws ServletException if an error occurs
     * @throws SAXException if an error occurs
     */
    protected void processXDIME(final MarinerServletRequestContext marinerRequestContext, final CachedContent xdimeContent, final String characterSet) throws IOException, ServletException, SAXException {
        MCSInternalContentHandler contentHandler = xdimeRequestProcessorHelper.getContentHandler(marinerRequestContext);
        xdimeRequestProcessorHelper.parseXDIME(marinerRequestContext, xdimeContent, contentHandler);
    }
}
