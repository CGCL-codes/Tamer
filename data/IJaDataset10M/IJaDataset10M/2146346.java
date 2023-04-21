package com.googlecode.bdoc.report;

import java.io.StringWriter;
import java.util.Map;
import com.googlecode.bdoc.BDocException;
import com.googlecode.bdoc.doc.report.AbstractHtmlReport;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

public class BDocReportUtils {

    private static Configuration cfg = new Configuration();

    static {
        MultiTemplateLoader multiTemplateLoader = BDocReportUtils.createTemplateLoader();
        cfg.setTemplateLoader(multiTemplateLoader);
        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    private BDocReportUtils() {
    }

    private static MultiTemplateLoader createTemplateLoader() {
        final MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(new TemplateLoader[] { new ClassTemplateLoader(BDocReport.class, ""), new ClassTemplateLoader(AbstractHtmlReport.class, "") });
        return multiTemplateLoader;
    }

    public static Configuration cfg() {
        return cfg;
    }

    public static String createContentFrom(String template, Map<String, Object> model) {
        try {
            StringWriter xhtml = new StringWriter();
            cfg().getTemplate(template).process(model, xhtml);
            return xhtml.toString();
        } catch (Exception e) {
            throw new BDocException(e);
        }
    }
}
