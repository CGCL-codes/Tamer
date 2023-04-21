package com.hmw.spring;

import javax.servlet.ServletException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderServlet;

public class SpringContextLoaderServlet extends ContextLoaderServlet {

    private static final long serialVersionUID = -1741916258154808198L;

    private ContextLoader contextLoader;

    public void init() throws ServletException {
        this.contextLoader = createContextLoader();
        SpringContext.setApplicationContext(this.contextLoader.initWebApplicationContext(getServletContext()));
    }
}
