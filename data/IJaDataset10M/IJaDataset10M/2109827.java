package org.blojsom.plugin.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.blojsom.blog.BlogUser;
import org.blojsom.blog.BlojsomConfiguration;
import org.blojsom.plugin.BlojsomPluginException;
import org.blojsom.util.BlojsomUtils;
import javax.servlet.ServletConfig;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * VelocityPlugin
 *
 * @author David Czarnecki
 * @version $Id: VelocityPlugin.java,v 1.5 2006-01-04 16:53:00 czarneckid Exp $
 */
public abstract class VelocityPlugin extends IPBanningPlugin {

    protected Log _logger = LogFactory.getLog(VelocityPlugin.class);

    private static final String BLOG_VELOCITY_PROPERTIES_IP = "velocity-properties";

    private static final String DEFAULT_VELOCITY_PROPERTIES = "/WEB-INF/velocity.properties";

    protected String _installationDirectory;

    protected String _baseConfigurationDirectory;

    protected String _templatesDirectory;

    protected Properties _velocityProperties;

    /**
     * Initialize this plugin. This method only called when the plugin is instantiated.
     *
     * @param servletConfig        Servlet config object for the plugin to retrieve any initialization parameters
     * @param blojsomConfiguration {@link org.blojsom.blog.BlojsomConfiguration} information
     * @throws org.blojsom.plugin.BlojsomPluginException
     *          If there is an error initializing the plugin
     */
    public void init(ServletConfig servletConfig, BlojsomConfiguration blojsomConfiguration) throws BlojsomPluginException {
        super.init(servletConfig, blojsomConfiguration);
        _baseConfigurationDirectory = blojsomConfiguration.getBaseConfigurationDirectory();
        _installationDirectory = blojsomConfiguration.getInstallationDirectory();
        _templatesDirectory = blojsomConfiguration.getTemplatesDirectory();
        _logger.debug("Using templates directory: " + _templatesDirectory);
        String velocityConfiguration = servletConfig.getInitParameter(BLOG_VELOCITY_PROPERTIES_IP);
        if (BlojsomUtils.checkNullOrBlank(velocityConfiguration)) {
            velocityConfiguration = DEFAULT_VELOCITY_PROPERTIES;
        }
        _velocityProperties = new Properties();
        InputStream is = servletConfig.getServletContext().getResourceAsStream(velocityConfiguration);
        try {
            _velocityProperties.load(is);
            is.close();
        } catch (Exception e) {
            _logger.error(e);
        }
        _logger.debug("Initialized Velocity plugin");
    }

    /**
     * Return a path appropriate for the Velocity file resource loader
     *
     * @param userId User ID
     * @return blojsom installation directory + base configuration directory + user id + templates directory
     */
    protected String getVelocityFileLoaderPath(String userId) {
        StringBuffer fileLoaderPath = new StringBuffer();
        fileLoaderPath.append(_installationDirectory);
        fileLoaderPath.append(BlojsomUtils.removeInitialSlash(_baseConfigurationDirectory));
        fileLoaderPath.append(userId).append("/");
        fileLoaderPath.append(BlojsomUtils.removeInitialSlash(_templatesDirectory));
        fileLoaderPath.append(", ");
        fileLoaderPath.append(_installationDirectory);
        fileLoaderPath.append(BlojsomUtils.removeInitialSlash(_baseConfigurationDirectory));
        fileLoaderPath.append(BlojsomUtils.removeInitialSlash(_templatesDirectory));
        return fileLoaderPath.toString();
    }

    /**
     * Merge a given template for the user with the appropriate context
     *
     * @param template Template
     * @param user {@link BlogUser} information
     * @param context Context with objects for use in the template
     * @return Merged template or <code>null</code> if there was an error setting properties, loading the template, or merging
     * the template
     */
    protected String mergeTemplate(String template, BlogUser user, Map context) {
        VelocityEngine velocityEngine = new VelocityEngine();
        try {
            Properties updatedVelocityProperties = (Properties) _velocityProperties.clone();
            updatedVelocityProperties.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, getVelocityFileLoaderPath(user.getId()));
            velocityEngine.init(updatedVelocityProperties);
        } catch (Exception e) {
            _logger.error(e);
            return null;
        }
        StringWriter writer = new StringWriter();
        VelocityContext velocityContext = new VelocityContext(context);
        if (!velocityEngine.templateExists(template)) {
            _logger.error("Could not find template for user: " + template);
            return null;
        } else {
            try {
                velocityEngine.mergeTemplate(template, UTF8, velocityContext, writer);
            } catch (Exception e) {
                _logger.error(e);
                return null;
            }
        }
        _logger.debug("Merged template: " + template);
        return writer.toString();
    }
}
