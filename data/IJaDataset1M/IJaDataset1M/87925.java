package net.sourceforge.greenvine.generator.template.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import net.sourceforge.greenvine.generator.template.TemplateContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class VelocityTemplate extends BaseTemplate implements net.sourceforge.greenvine.generator.template.Template {

    public VelocityTemplate(String templatePath, String exportDirectory) {
        super(templatePath, exportDirectory);
    }

    public void merge(TemplateContext context, String directory, String fileName) throws IOException, MethodInvocationException, ParseErrorException, ResourceNotFoundException, Exception {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.setProperty("runtime.references.strict", "true");
        Velocity.init(p);
        Writer writer = getWriter(directory, fileName);
        VelocityContext ctx = new VelocityContext(context);
        Template template = Velocity.getTemplate(templatePath);
        template.merge(ctx, writer);
        writer.flush();
        writer.close();
    }
}
