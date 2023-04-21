package net.mainlove.project.web.blog.web.app.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FMarker extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Configuration cfg;

    ;

    public static String servletBasePath;

    @Override
    public void init() throws ServletException {
        super.init();
        cfg = new Configuration();
        cfg.setServletContextForTemplateLoading(getServletContext(), "WEB-INF" + File.separator + "templates");
        servletBasePath = getServletContext().getRealPath("/");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    @SuppressWarnings("unchecked")
    public static void generateStaticHtml(String templateFileName, String filePath, Map root) throws IOException, TemplateException {
        Template template = cfg.getTemplate(templateFileName);
        File staticfile = new File(servletBasePath + "WEB-INF" + File.separator + "staticHtml" + File.separator + "" + File.separator + filePath + ".html");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(staticfile)));
        template.process(root, out);
    }
}
