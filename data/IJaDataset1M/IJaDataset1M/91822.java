package org.dy.servlet.mvc.freemarker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * @author Kerry Ding
 */
public class TemplateHandler {

    private static final String DEFAULT_TEMPLATE_DIR = "template";

    private static final String DEFAULT_CONFIG_FILE = "ftl.properties";

    TaglibFactory taglib;

    private String fileDir;

    private String encoding = "gbk";

    private Configuration cfg;

    public TemplateHandler(String dir) {
        if (dir == null) fileDir = DEFAULT_TEMPLATE_DIR; else fileDir = dir;
        init(null, dir);
    }

    public TemplateHandler(ServletContext context, String dir) {
        if (dir == null) fileDir = DEFAULT_TEMPLATE_DIR; else fileDir = dir;
        init(context, dir);
    }

    protected void init(ServletContext context, String dir) {
        cfg = new Configuration();
        try {
            if (context == null) cfg.setDirectoryForTemplateLoading(new File(fileDir)); else cfg.setServletContextForTemplateLoading(context, dir);
            if (context != null) {
                String ftlConfPath = context.getRealPath("WEB-INF/" + DEFAULT_CONFIG_FILE);
                System.out.println("Configure file path:" + ftlConfPath);
                File configFile = new File(ftlConfPath);
                if (configFile.exists()) {
                    Properties props = new Properties();
                    props.load(new FileInputStream(configFile));
                    cfg.setSettings(props);
                } else {
                    cfg.setNumberFormat("0.########");
                    cfg.setDefaultEncoding(encoding);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void printOut(Map map, Writer out, String fileName) throws Exception {
        Template temp = cfg.getTemplate(fileName);
        temp.process(map, out);
    }

    public static void main(String[] args) {
        TemplateHandler model = new TemplateHandler(null);
        String fileName = "sample.ftl";
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("name", "dingyong");
        root.put("id", 12222);
        Writer out = new OutputStreamWriter(System.out);
        try {
            model.printOut(root, out, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Configuration getConfiguration() {
        return this.cfg;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public TaglibFactory getTaglib() {
        return taglib;
    }

    public void setTaglib(TaglibFactory taglib) {
        this.taglib = taglib;
    }
}
