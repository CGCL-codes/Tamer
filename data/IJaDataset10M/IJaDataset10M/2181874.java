package org.blojsom.dispatcher.groovy;

import groovy.lang.Binding;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.BlojsomException;
import org.blojsom.blog.Blog;
import org.blojsom.dispatcher.Dispatcher;
import org.blojsom.util.BlojsomConstants;
import org.blojsom.util.BlojsomUtils;
import org.codehaus.groovy.syntax.SyntaxException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * GroovyDispatcher
 *
 * @author David Czarnecki
 * @since blojsom 3.0
 * @version $Id: GroovyDispatcher.java,v 1.1 2006/03/23 14:27:56 czarneckid Exp $
 */
public class GroovyDispatcher implements Dispatcher {

    private Log _logger = LogFactory.getLog(GroovyDispatcher.class);

    private ServletConfig _servletConfig;

    private Properties _blojsomProperties;

    private String _templatesDirectory;

    private String _blogsDirectory;

    /**
     * Default constructor
     */
    public GroovyDispatcher() {
    }

    /**
     * Set the properties in use by blojsom
     *
     * @param blojsomProperties Properties in use by blojsom
     */
    public void setBlojsomProperties(Properties blojsomProperties) {
        _blojsomProperties = blojsomProperties;
    }

    /**
     * Set the {@link javax.servlet.ServletConfig}
     *
     * @param servletConfig {@link javax.servlet.ServletConfig}
     */
    public void setServletConfig(ServletConfig servletConfig) {
        _servletConfig = servletConfig;
    }

    /**
     * Initialization method for blojsom dispatchers
     *
     * @throws org.blojsom.BlojsomException If there is an error initializing the dispatcher
     */
    public void init() throws BlojsomException {
        _templatesDirectory = _blojsomProperties.getProperty(BlojsomConstants.TEMPLATES_DIRECTORY_IP, BlojsomConstants.DEFAULT_TEMPLATES_DIRECTORY);
        _blogsDirectory = _blojsomProperties.getProperty(BlojsomConstants.BLOGS_DIRECTORY_IP, BlojsomConstants.DEFAULT_BLOGS_DIRECTORY);
    }

    /**
     * Dispatch a request and response. A context map is provided for the BlojsomServlet to pass
     * any required information for use by the dispatcher. The dispatcher is also
     * provided with the template for the requested flavor along with the content type for the
     * specific flavor.
     *
     * @param httpServletRequest  Request
     * @param httpServletResponse Response
     * @param blog                {@link Blog}
     * @param context             Context map
     * @param flavorTemplate      Template to dispatch to for the requested flavor
     * @param flavorContentType   Content type for the requested flavor
     * @throws java.io.IOException            If there is an exception during IO
     * @throws javax.servlet.ServletException If there is an exception in dispatching the request
     */
    public void dispatch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Blog blog, Map context, String flavorTemplate, String flavorContentType) throws IOException, ServletException {
        httpServletResponse.setContentType(flavorContentType);
        ServletContext servletContext = _servletConfig.getServletContext();
        if (!flavorTemplate.startsWith("/")) {
            flavorTemplate = '/' + flavorTemplate;
        }
        String flavorTemplateForPage = null;
        String pageParameter = BlojsomUtils.getRequestValue(BlojsomConstants.PAGE_PARAM, httpServletRequest, true);
        if (pageParameter != null) {
            flavorTemplateForPage = BlojsomUtils.getTemplateForPage(flavorTemplate, pageParameter);
            if (_logger.isDebugEnabled()) {
                _logger.debug("Retrieved template for page: " + flavorTemplateForPage);
            }
        }
        Binding binding = new Binding();
        Iterator contextIterator = context.keySet().iterator();
        String contextKey;
        while (contextIterator.hasNext()) {
            contextKey = (String) contextIterator.next();
            binding.setVariable(contextKey, context.get(contextKey));
        }
        Writer responseWriter = httpServletResponse.getWriter();
        SimpleTemplateEngine simpleTemplateEngine = new SimpleTemplateEngine();
        if (flavorTemplateForPage != null) {
            String templateToLoad = BlojsomConstants.DEFAULT_CONFIGURATION_BASE_DIRECTORY + _blogsDirectory + blog.getBlogId() + _templatesDirectory + BlojsomUtils.removeInitialSlash(flavorTemplateForPage);
            if (servletContext.getResource(templateToLoad) != null) {
                InputStreamReader isr = new InputStreamReader(servletContext.getResourceAsStream(templateToLoad), BlojsomConstants.UTF8);
                try {
                    Template groovyTemplate = simpleTemplateEngine.createTemplate(isr);
                    groovyTemplate.setBinding(context);
                    groovyTemplate.writeTo(responseWriter);
                } catch (SyntaxException e) {
                    if (_logger.isErrorEnabled()) {
                        _logger.error(e);
                    }
                } catch (ClassNotFoundException e) {
                    if (_logger.isErrorEnabled()) {
                        _logger.error(e);
                    }
                }
                if (_logger.isDebugEnabled()) {
                    _logger.debug("Dispatched to flavor page template for user: " + templateToLoad);
                }
                return;
            } else {
                templateToLoad = BlojsomConstants.DEFAULT_CONFIGURATION_BASE_DIRECTORY + BlojsomUtils.removeInitialSlash(_templatesDirectory) + BlojsomUtils.removeInitialSlash(flavorTemplateForPage);
                if (servletContext.getResource(templateToLoad) != null) {
                    InputStreamReader isr = new InputStreamReader(servletContext.getResourceAsStream(templateToLoad), BlojsomConstants.UTF8);
                    try {
                        Template groovyTemplate = simpleTemplateEngine.createTemplate(isr);
                        groovyTemplate.setBinding(context);
                        groovyTemplate.writeTo(responseWriter);
                    } catch (SyntaxException e) {
                        if (_logger.isErrorEnabled()) {
                            _logger.error(e);
                        }
                    } catch (ClassNotFoundException e) {
                        if (_logger.isErrorEnabled()) {
                            _logger.error(e);
                        }
                    }
                    if (_logger.isDebugEnabled()) {
                        _logger.debug("Dispatched to flavor page template for user: " + templateToLoad);
                    }
                    return;
                } else {
                    if (_logger.isErrorEnabled()) {
                        _logger.error("Unable to dispatch to flavor page template: " + templateToLoad);
                    }
                }
            }
        } else {
            String templateToLoad = BlojsomConstants.DEFAULT_CONFIGURATION_BASE_DIRECTORY + _blogsDirectory + blog.getBlogId() + _templatesDirectory + BlojsomUtils.removeInitialSlash(flavorTemplate);
            if (servletContext.getResource(templateToLoad) != null) {
                InputStreamReader isr = new InputStreamReader(servletContext.getResourceAsStream(templateToLoad), BlojsomConstants.UTF8);
                try {
                    Template groovyTemplate = simpleTemplateEngine.createTemplate(isr);
                    groovyTemplate.setBinding(context);
                    groovyTemplate.writeTo(responseWriter);
                } catch (SyntaxException e) {
                    if (_logger.isErrorEnabled()) {
                        _logger.error(e);
                    }
                } catch (ClassNotFoundException e) {
                    if (_logger.isErrorEnabled()) {
                        _logger.error(e);
                    }
                }
                if (_logger.isDebugEnabled()) {
                    _logger.debug("Dispatched to flavor template for user: " + templateToLoad);
                }
                return;
            } else {
                templateToLoad = BlojsomConstants.DEFAULT_CONFIGURATION_BASE_DIRECTORY + BlojsomUtils.removeInitialSlash(_templatesDirectory) + BlojsomUtils.removeInitialSlash(flavorTemplate);
                if (servletContext.getResource(templateToLoad) != null) {
                    InputStreamReader isr = new InputStreamReader(servletContext.getResourceAsStream(templateToLoad), BlojsomConstants.UTF8);
                    try {
                        Template groovyTemplate = simpleTemplateEngine.createTemplate(isr);
                        groovyTemplate.setBinding(context);
                        groovyTemplate.writeTo(responseWriter);
                    } catch (SyntaxException e) {
                        if (_logger.isErrorEnabled()) {
                            _logger.error(e);
                        }
                    } catch (ClassNotFoundException e) {
                        if (_logger.isErrorEnabled()) {
                            _logger.error(e);
                        }
                    }
                    if (_logger.isDebugEnabled()) {
                        _logger.debug("Dispatched to flavor template: " + templateToLoad);
                    }
                    return;
                } else {
                    if (_logger.isErrorEnabled()) {
                        _logger.error("Unable to dispatch to flavor template: " + templateToLoad);
                    }
                }
            }
        }
        responseWriter.flush();
    }
}
