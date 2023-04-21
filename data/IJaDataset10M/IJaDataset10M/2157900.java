package com.tchepannou.rails.core.impl;

import com.tchepannou.rails.core.api.ServiceContext;
import com.tchepannou.rails.core.exception.InitializationException;
import com.tchepannou.rails.core.exception.RenderException;
import com.tchepannou.rails.core.service.RenderService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link RenderService} based on <a href="http://velocity.apache.org">Jakarta Velocity</a>
 * 
 * @author herve
 */
public class VelocityRenderService extends AbstractService implements RenderService {

    private static final Logger LOG = LoggerFactory.getLogger(VelocityRenderService.class);

    private static final String TEMPLATE_SUFFIX = ".vm";

    private String _encoding = "UTF-8";

    private VelocityEngine _engine = new VelocityEngine();

    public void render(String path, Map data, Writer writer) throws RenderException, IOException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("render(" + path + "," + data + "," + writer + ")");
        }
        if (!isInitialized()) {
            throw new IllegalStateException("The service is not initialized. call init() first");
        }
        try {
            Map cdata = new HashMap(data);
            VelocityContext vc = new VelocityContext(cdata);
            String template = path + TEMPLATE_SUFFIX;
            _engine.mergeTemplate(template, _encoding, vc, writer);
        } catch (ResourceNotFoundException ex) {
            throw new com.tchepannou.rails.core.exception.ResourceNotFoundException(ex.getMessage(), path, -1, ex);
        } catch (ParseErrorException ex) {
            throw new RenderException("Syntax error", path, ex.getLineNumber(), ex);
        } catch (MethodInvocationException ex) {
            throw new RenderException("Method invocation error", path, ex.getLineNumber(), ex);
        } catch (Exception ex) {
            throw new RenderException("Method invocation error", path, -1, ex);
        }
    }

    @Override
    public void init(ServiceContext serviceContext) {
        LOG.info("Initializing");
        super.init(serviceContext);
        try {
            Properties p = loadVelocityProperties();
            _engine = new VelocityEngine(p);
            LOG.info("Initialized");
        } catch (Exception e) {
            throw new InitializationException("Unable to initialize the Render service", e);
        }
    }

    @Override
    public void destroy() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("destroy()");
        }
        super.destroy();
        _engine = null;
    }

    private Properties loadVelocityProperties() throws IOException {
        Properties p = new Properties();
        try {
            p = loadConfigurationAsProperties("velocity.properties");
            if (LOG.isDebugEnabled()) {
                LOG.debug("Velocity Properties");
                for (Object name : p.keySet()) {
                    LOG.debug(" " + name + "=" + p.get(name));
                }
            }
        } catch (FileNotFoundException e) {
            LOG.warn("Configuration not found");
        }
        ServletContext context = getServiceContext().getContainerContext().getServletContext();
        String rootDir = context.getRealPath("/");
        p.setProperty("file.resource.loader.path", rootDir);
        return p;
    }
}
