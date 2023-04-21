package org.bionote.service.context;

import javax.servlet.ServletContext;

/**
 * @author mbreese
 *
 */
public class WebAppContext implements ContextWrapper {

    ServletContext context;

    public WebAppContext(ServletContext c) {
        context = c;
    }

    public String getRealPath(String path) {
        return context.getRealPath(path);
    }
}
