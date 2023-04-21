package org.ddljen;

import java.io.Writer;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class SQLGenerator {

    public void generate(Schema s, SQLDialect dialect, boolean drop, Writer writer) throws DDLJenException {
        try {
            VelocityContext context = new VelocityContext();
            context.put("schema", s);
            context.put("dialect", dialect);
            context.put("drop", new Boolean(drop));
            Template template = findTemplate(dialect);
            template.merge(context, writer);
        } catch (Exception e) {
            throw new DDLJenException(e);
        }
    }

    private Template findTemplate(SQLDialect dialect) throws ResourceNotFoundException, ParseErrorException, Exception {
        VelocityEngine velocityEngine = new VelocityEngine();
        Properties p = new Properties();
        p.setProperty("resource.loader", "classpath");
        p.setProperty("classpath.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init(p);
        String templateFile = "org/ddljen/";
        templateFile += dialect.getName().toLowerCase();
        templateFile += "/template.vm";
        Template template = velocityEngine.getTemplate(templateFile);
        return template;
    }
}
