package org.easy.framework.generate.impl.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.easy.framework.generate.TemplateEngine;
import org.easy.framework.generate.TemplateRenderingContext;
import org.easy.framework.metadata.definition.TemplateSetting;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemakerTemplateEngine implements TemplateEngine {

    private FreemakerManager freemakerManager;

    public void setFreemakerManager(FreemakerManager freemakerManager) {
        this.freemakerManager = freemakerManager;
    }

    public void renderTemplate(TemplateRenderingContext templateContext, org.easy.framework.generate.Template myTemplate) throws Exception {
        Configuration configuration = freemakerManager.getConfiguration(templateContext.getTemplateSetting());
        configuration.setDirectoryForTemplateLoading(new File(myTemplate.getTemplateDir()));
        Template template = configuration.getTemplate(myTemplate.getTemplateName());
        setTemplateContext(templateContext);
        Writer out = null;
        try {
            TemplateSetting templateSetting = templateContext.getTemplateSetting();
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(myTemplate.getSrcOutPutFileName()), templateSetting.getEncoding()));
            template.process(templateContext.getParameters(), out);
        } finally {
            if (out != null) out.close();
        }
    }

    private void setTemplateContext(TemplateRenderingContext context) {
        context.add("application", context.getApplication());
        context.add("allModule", context.getModules());
        context.add("allEntity", context.getAllEntity());
        context.add("context", context);
    }
}
