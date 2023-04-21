package com.sanctuary.services;

import java.io.OutputStream;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sanctuary.interfaces.Product;
import com.sanctuary.interfaces.Service;
import com.sanctuary.interfaces.TemplateEngine;
import com.sanctuary.tools.BaseTool;

public abstract class BaseService implements Service {

    private static final Log LOG = LogFactory.getLog(BaseService.class);

    HttpServletRequest request;

    HttpServletResponse response;

    OutputStream outputStream = null;

    Writer writer = null;

    TemplateEngine template;

    public abstract Product execute() throws Exception;

    public abstract boolean isWriter();

    public void setRequest(HttpServletRequest request) {
        this.request = request;
        if (template == null) {
            template = (TemplateEngine) BaseTool.IOC_ENGINE.getBean("templateEngine");
        }
        template.setContent(request.getParameterMap());
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }
}
