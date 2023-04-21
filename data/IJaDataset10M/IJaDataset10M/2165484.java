package org.ignition.blojsom.dispatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.ignition.blojsom.util.BlojsomUtils;
import org.ignition.blojsom.blog.Blog;
import org.ignition.blojsom.BlojsomException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * VelocityDispatcher
 *
 * @author David Czarnecki
 * @version $Id: VelocityDispatcher.java,v 1.17 2003-08-04 15:37:26 intabulas Exp $
 */
public class VelocityDispatcher implements GenericDispatcher {

    private static final String BLOG_VELOCITY_PROPERTIES_IP = "velocity-properties";

    private Log _logger = LogFactory.getLog(VelocityDispatcher.class);

    /**
     * Create a new VelocityDispatcher
     */
    public VelocityDispatcher() {
    }

    /**
     * Initialization method for blojsom dispatchers
     *
     * @param servletConfig ServletConfig for obtaining any initialization parameters
     * @param blog {@link Blog} information
     * @throws BlojsomException If there is an error initializing the dispatcher
     */
    public void init(ServletConfig servletConfig, Blog blog) throws BlojsomException {
        String velocityConfiguration = servletConfig.getInitParameter(BLOG_VELOCITY_PROPERTIES_IP);
        Properties velocityProperties = new Properties();
        InputStream is = servletConfig.getServletContext().getResourceAsStream(velocityConfiguration);
        try {
            velocityProperties.load(is);
            Velocity.init(velocityProperties);
            is.close();
        } catch (Exception e) {
            _logger.error(e);
        }
        _logger.debug("Initialized Velocity dispatcher");
    }

    /**
     * Dispatch a request and response. A context map is provided for the BlojsomServlet to pass
     * any required information for use by the dispatcher. The dispatcher is also
     * provided with the template for the requested flavor along with the content type for the
     * specific flavor. For example, the RSS flavor uses text/xml as its content type.
     *
     * @param httpServletRequest Request
     * @param httpServletResponse Response
     * @param context Context map
     * @param flavorTemplate Template to dispatch to for the requested flavor
     * @param flavorContentType Content type for the requested flavor
     * @throws IOException If there is an exception during IO
     * @throws ServletException If there is an exception in dispatching the request
     */
    public void dispatch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Map context, String flavorTemplate, String flavorContentType) throws IOException, ServletException {
        httpServletResponse.setContentType(flavorContentType);
        StringWriter sw = new StringWriter();
        String flavorTemplateForPage = null;
        if (BlojsomUtils.getRequestValue(PAGE_PARAM, httpServletRequest) != null) {
            flavorTemplateForPage = BlojsomUtils.getTemplateForPage(flavorTemplate, BlojsomUtils.getRequestValue(PAGE_PARAM, httpServletRequest));
            _logger.debug("Retrieved template for page: " + flavorTemplateForPage);
        }
        VelocityContext velocityContext = new VelocityContext(context);
        try {
            if (flavorTemplateForPage != null) {
                Velocity.mergeTemplate(flavorTemplateForPage, UTF8, velocityContext, sw);
            } else {
                Velocity.mergeTemplate(flavorTemplate, UTF8, velocityContext, sw);
            }
        } catch (ResourceNotFoundException e) {
            _logger.error(e);
            if (flavorTemplateForPage != null) {
                _logger.debug("Trying to fallback to original flavor template: " + flavorTemplate);
                try {
                    Velocity.mergeTemplate(flavorTemplate, UTF8, velocityContext, sw);
                } catch (Exception internale) {
                    _logger.error(internale);
                }
            }
        } catch (Exception e) {
            _logger.error(e);
        }
        String content = sw.toString();
        byte[] contentBytes = null;
        try {
            contentBytes = content.getBytes(UTF8);
        } catch (UnsupportedEncodingException e) {
            _logger.error(e);
        }
        httpServletResponse.addIntHeader("Content-Length", (contentBytes == null ? content.length() : contentBytes.length));
        OutputStreamWriter osw = new OutputStreamWriter(httpServletResponse.getOutputStream(), UTF8);
        osw.write(content);
        osw.flush();
    }
}
