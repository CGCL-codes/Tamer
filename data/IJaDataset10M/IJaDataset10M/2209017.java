package com.mu.jacob.core.builder;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.google.inject.Inject;
import com.mu.jacob.core.generator.Config;
import com.mu.jacob.core.generator.GeneratorException;
import com.mu.jacob.core.renderer.AbstractRenderer;
import com.mu.jacob.core.renderer.FreeMarkerRenderer;
import com.mu.jacob.core.renderer.IRenderer;
import com.mu.jacob.core.renderer.StringTemplateRenderer;

/**
 * Abstract builder class.
 * This should be the super class for all builders used in Jacob.
 * It configures renderer unsing properties and context
 * defined in build.xml files.
 * Every builder is instantiated using Guice framework, so it support
 * dynamic injection that can be configured using custom module 
 * defined in jacob task.
 * @author Adam Smyczek
 */
public abstract class AbstractBuilder implements IBuilder {

    @Inject
    private Config config;

    private IRenderer renderer;

    private Properties properties = new Properties();

    private Map<String, Object> contextMap = new HashMap<String, Object>();

    private String templateFile = null;

    private String templateRoot = null;

    private String globalTemplateRoot = null;

    /**
	 * Default constructor for initialization using ant
	 */
    public AbstractBuilder() {
        super();
    }

    /**
	 * Use this constructor with JacobRunner
	 * @param engine the rendering engine
	 * @param templateFile
	 */
    public AbstractBuilder(final IRenderer renderer, final String templateFile, final String templateRoot) {
        this();
        this.renderer = renderer;
        this.templateFile = templateFile;
        this.templateRoot = templateRoot;
    }

    /**
	 * Renderer setter
	 * @param renderer
	 */
    public void setRenderer(final IRenderer renderer) {
        this.renderer = renderer;
    }

    /**
	 * Renderer has to be initializes before calling it
	 * @return renderer
	 */
    protected IRenderer getRenderer() {
        if (renderer == null) {
            throw new GeneratorException("Renderer is not initialized!");
        }
        return renderer;
    }

    /**
	 * Set properties
	 */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
	 * @return properties
	 */
    public Properties getProperties() {
        return properties;
    }

    /**
	 * Add context used in renderer
	 */
    public void addContext(String name, Object context) {
        contextMap.put(name, context);
    }

    /**
	 * @return returns context map
	 */
    public Map<String, Object> getContextMap() {
        return Collections.unmodifiableMap(contextMap);
    }

    /**
	 * Simple property accessor for templates
	 * @param key
	 */
    public String prop(String key) {
        return getProperties().getProperty(key);
    }

    /**
	 * Simple property accessor for templates, with default value
	 * @param key
	 * @param defaultValue
	 * @return the value for key or defaultValue if key does not exist
	 */
    public String prop(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    /**
	 * Renderer accessor, lazy initializes renderer unsing injected factory
	 * @return the renderer
	 * 
	 * TODO: make the initialization more general
	 */
    public void initRenderer() {
        if (renderer instanceof AbstractRenderer) {
            ((AbstractRenderer) renderer).initRenderer(new File(getTemplateRoot()));
        }
        if (getGlobalTemplateRoot() != null) {
            if (renderer instanceof FreeMarkerRenderer) {
                ((FreeMarkerRenderer) renderer).setGlobalTemplateGroup(new File(getGlobalTemplateRoot()));
            }
            if (renderer instanceof StringTemplateRenderer) {
                ((StringTemplateRenderer) renderer).setGlobalTemplateGroup(new File(getGlobalTemplateRoot()));
            }
        }
    }

    /**
	 * @return build root dir from properties
	 */
    public File getBaseDir() {
        return new File(prop("basedir", "."));
    }

    /**
	 * Template file setter
	 * @param templateFile
	 */
    public void setTemplateFile(final String templateFile) {
        this.templateFile = templateFile;
    }

    /**
	 * @return template file from properties
	 */
    public String getTemplateFile() {
        if (templateFile == null) {
            templateFile = prop(PROPERTY_TEMPLATE_FILE);
            if (templateFile != null) {
                File tf = new File(new File(getBaseDir(), getTemplateRoot()), templateFile);
                if (!tf.exists()) {
                    templateFile = null;
                    throw new GeneratorException("Template file " + tf.getAbsolutePath() + " does not exist!");
                }
            } else {
                throw new GeneratorException("PROPERTY_TEMPLATE_FILE template property is not defined!");
            }
        }
        return templateFile;
    }

    /**
	 * @return template root from properties
	 */
    public String getTemplateRoot() {
        if (templateRoot == null) {
            templateRoot = prop(PROPERTY_TEMPLATE_ROOT);
            if (templateRoot == null) {
                throw new GeneratorException("PROPERTY_TEMPLATE_ROOT template root property is not defined!");
            }
            if (!new File(getBaseDir(), templateRoot).exists()) {
                templateRoot = null;
                throw new GeneratorException("Template root dir does not exist!");
            }
        }
        return templateRoot;
    }

    /**
	 * Global template root dir setter
	 * 
	 * @param gloablTemplateRoot
	 */
    public void setGlobalTemplateRoot(final String gloablTemplateRoot) {
        this.globalTemplateRoot = gloablTemplateRoot;
    }

    /**
	 * @return global template root from properties
	 */
    public String getGlobalTemplateRoot() {
        if (globalTemplateRoot == null) {
            globalTemplateRoot = prop(PROPERTY_TEMPLATE_ROOT_GLOBAL);
        }
        if (globalTemplateRoot != null) {
            File templateRootGlobalFile = new File(getBaseDir(), globalTemplateRoot);
            if (!templateRootGlobalFile.exists()) {
                globalTemplateRoot = null;
                logger.error("PROPERTY_TEMPLATE_ROOT_GLOBAL: " + templateRootGlobalFile.getAbsolutePath() + " directory does not exist!");
            }
        }
        return globalTemplateRoot;
    }

    private static final Logger logger = Logger.getLogger(AbstractBuilder.class);
}
