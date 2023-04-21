package com.germinus.xpression.cms.taglib;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.taglib.tiles.InsertTag;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 * Insert page controls implemented as tiles templates.
 * The controls will be looked for in the specified context. 
 * 
 * Note that if there
 * is a security manager active it may throw a security exception if
 * there are not enough permissions to access the specified context.
 * 
 * @author Jorge Ferrer
 */
public class InsertControlTag extends InsertTag {

    private static final long serialVersionUID = -1502521203170459088L;

    private String _context;

    public String getContext() {
        return _context;
    }

    public void setContext(String context) {
        this._context = context;
    }

    protected void doInclude(String page) throws ServletException, IOException {
        RequestDispatcher rd = getControlDispatcher(page);
        rd.include(pageContext.getRequest(), pageContext.getResponse());
    }

    protected ServletContext getControlContext() {
        String evaluatedContext = _context;
        try {
            evaluatedContext = (String) ExpressionEvaluatorManager.evaluate("context", getContext(), String.class, this, this.pageContext);
        } catch (JspException e) {
            evaluatedContext = _context;
        }
        ServletContext controlCtx = null;
        if (StringUtils.isNotEmpty(evaluatedContext)) {
            controlCtx = pageContext.getServletContext().getContext(evaluatedContext);
        } else {
            controlCtx = pageContext.getServletContext();
        }
        if (controlCtx == null) {
            throw new RuntimeException("Context " + _context + " not found.");
        }
        return controlCtx;
    }

    protected RequestDispatcher getControlDispatcher(String page) {
        RequestDispatcher rd = getControlContext().getRequestDispatcher(page);
        if (rd == null) {
            throw new RuntimeException("Control context " + _context + " does not contain " + page);
        }
        return rd;
    }
}
