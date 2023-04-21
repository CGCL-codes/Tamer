package net.lshift.jsp.taglib.yatl.filters;

import javax.servlet.*;
import java.io.IOException;
import java.util.*;

public class YatlFilter implements Filter {

    protected String templateDataAttributeName;

    protected String templateDataType;

    public YatlFilter() {
    }

    public void init(FilterConfig fc) throws ServletException {
        ServletContext sc = fc.getServletContext();
        this.templateDataAttributeName = sc.getInitParameter("templateDataAttributeName");
        this.templateDataType = fc.getInitParameter("templateDataType");
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getAttribute(templateDataAttributeName) == null) req.setAttribute(templateDataAttributeName, "HashMap".equals(templateDataType) ? (Object) new HashMap() : (Object) new Hashtable());
        chain.doFilter(req, res);
    }
}
