package com.nw.dsl4j.generator;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

public class TemplateEngine {

    private final VelocityEngine engine = new VelocityEngine();

    private final Generator generator;

    public TemplateEngine(final Generator generator) {
        super();
        this.generator = generator;
        initVelocity();
    }

    public Generator getGenerator() {
        return generator;
    }

    private void initVelocity() {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            engine.init(properties);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("deprecation")
    public String processTemplateByName(String templateName, ContextVar... vars) {
        try {
            InputStream templateInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateName);
            String template = IOUtils.toString(templateInputStream);
            return processTemplate(template, vars);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String processTemplate(String templateString, List<ContextVar> vars) {
        return processTemplate(templateString, vars.toArray(new ContextVar[0]));
    }

    @SuppressWarnings("deprecation")
    public String processTemplate(String templateString, ContextVar... vars) {
        Context context = prepareContext(vars);
        StringWriter stringWriter = new StringWriter();
        try {
            engine.evaluate(context, stringWriter, "adsf", templateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    private Context prepareContext(ContextVar... vars) {
        ImportResolvingVelocityContext importResolvingVelocityContext = new ImportResolvingVelocityContext(generator, generator.getImports());
        Context context = new VelocityContext(importResolvingVelocityContext);
        for (ContextVar v : vars) {
            context.put(v.getName(), v.getValue());
        }
        context.put("Utils", new TemplateUtils());
        return context;
    }
}
