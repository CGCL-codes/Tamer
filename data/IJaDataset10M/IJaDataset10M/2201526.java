package org.j2eespider.build.templateengine;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerTemplateEngine implements TemplateEngine {

    private static TemplateEngine instance;

    public static TemplateEngine getInstance() {
        if (instance == null) {
            instance = new FreemarkerTemplateEngine();
        }
        return instance;
    }

    public String getAction() {
        return "generate file....: ";
    }

    public void runFile(String pathTemplate, String pathWriterFile, HashMap<String, Object> mapVariables) throws Exception {
        int lastSeparator = pathTemplate.lastIndexOf("/");
        if (lastSeparator == -1) {
            lastSeparator = pathTemplate.lastIndexOf("\\");
        }
        String nameTemplate = pathTemplate.substring(lastSeparator + 1);
        pathTemplate = pathTemplate.substring(0, lastSeparator);
        Configuration configuration = new Configuration();
        configuration.setDirectoryForTemplateLoading(new File(pathTemplate));
        Template template = configuration.getTemplate(nameTemplate);
        FileWriter fileWriter = new FileWriter(pathWriterFile);
        template.process(mapVariables, fileWriter, new DefaultObjectWrapper());
        fileWriter.flush();
        fileWriter.close();
    }

    public String runFile(String pathTemplate, HashMap<String, Object> mapVariables) throws Exception {
        int lastSeparator = pathTemplate.lastIndexOf("/");
        if (lastSeparator == -1) {
            lastSeparator = pathTemplate.lastIndexOf("\\");
        }
        String nameTemplate = pathTemplate.substring(lastSeparator + 1);
        pathTemplate = pathTemplate.substring(0, lastSeparator);
        Configuration configuration = new Configuration();
        configuration.setDirectoryForTemplateLoading(new File(pathTemplate));
        Template template = configuration.getTemplate(nameTemplate);
        StringWriter stringWriter = new StringWriter();
        template.process(mapVariables, stringWriter, new DefaultObjectWrapper());
        return stringWriter.toString();
    }

    public String runInLine(String lineTemplate, HashMap<String, Object> mapVariables) throws Exception {
        Configuration configuration = new Configuration();
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        FileTemplateLoader fileTemplateLoader = new FileTemplateLoader();
        TemplateLoader templateLoader = new MultiTemplateLoader(new TemplateLoader[] { stringTemplateLoader, fileTemplateLoader });
        stringTemplateLoader.putTemplate("temp:param", lineTemplate);
        configuration.setTemplateLoader(templateLoader);
        Template template = configuration.getTemplate("temp:param");
        Writer out = new StringWriter();
        template.process(mapVariables, out, new DefaultObjectWrapper());
        return out.toString();
    }
}
