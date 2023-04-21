package com.carbonfive.sstemplates.servlet;

import com.carbonfive.sstemplates.*;
import com.carbonfive.sstemplates.tags.*;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class ServletSsTemplateProcessor extends SsTemplateProcessor {

    private static final Logger log = LoggerFactory.getLogger(ServletSsTemplateProcessor.class);

    private static Map processorCache = Collections.synchronizedMap(new HashMap());

    public static SsTemplateProcessor getInstance(Collection customTags) throws SsTemplateException {
        ServletSsTemplateProcessor processor = (ServletSsTemplateProcessor) processorCache.get(customTags);
        if (processor == null) {
            processor = new ServletSsTemplateProcessor(customTags);
            processorCache.put(customTags, processor);
        }
        return processor;
    }

    protected ServletSsTemplateProcessor(Collection customTags) throws SsTemplateException {
        super(customTags, true);
    }

    public HSSFWorkbook process(HttpServletRequest request, ServletContext context, File templateDir, File templateFile) throws SsTemplateException {
        WorkbookTag renderTree = retrieveRenderTree(templateFile.getAbsolutePath());
        SsTemplateContext templateContext = createTemplateContext(request, context, templateDir);
        renderTree.render(templateContext);
        return templateContext.getWorkbook();
    }

    protected SsTemplateServletContext createTemplateContext(HttpServletRequest request, ServletContext context, File templateDir) {
        return new SsTemplateServletContext(request, context, this, templateDir);
    }
}
