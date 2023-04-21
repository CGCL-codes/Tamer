package org.jmesa.web;

import java.io.Writer;
import java.util.Locale;
import java.util.Map;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public interface WebContext {

    public Object getApplicationInitParameter(String name);

    public Object getApplicationAttribute(String name);

    public void setApplicationAttribute(String name, Object value);

    public void removeApplicationAttribute(String name);

    public Object getPageAttribute(String name);

    public void setPageAttribute(String name, Object value);

    public void removePageAttribute(String name);

    public String getParameter(String name);

    public Map<?, ?> getParameterMap();

    public void setParameterMap(Map<?, ?> parameterMap);

    public Object getRequestAttribute(String name);

    public void setRequestAttribute(String name, Object value);

    public void removeRequestAttribute(String name);

    public Object getSessionAttribute(String name);

    public void setSessionAttribute(String name, Object value);

    public void removeSessionAttribute(String name);

    public Writer getWriter();

    public Locale getLocale();

    public void setLocale(Locale locale);

    public String getContextPath();

    public String getRealPath(String path);

    public Object getBackingObject();
}
