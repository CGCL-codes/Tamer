package net.woodstock.rockapi.jsp.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import net.woodstock.rockapi.sys.SysLogger;
import org.apache.commons.logging.Log;

public abstract class BaseFilter implements Filter {

    private FilterConfig filterConfig;

    public void destroy() {
        this.getLogger().info("Filter: " + this.getClass().getName() + " destroyed");
    }

    public void init(FilterConfig filterConfig) {
        this.getLogger().info("Filter: " + this.getClass().getName() + " initialized");
        this.filterConfig = filterConfig;
    }

    protected FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

    protected String getInitParameter(String name) {
        return this.filterConfig.getInitParameter(name);
    }

    protected ServletContext getServletContext() {
        return this.filterConfig.getServletContext();
    }

    protected Log getLogger() {
        return SysLogger.getLogger();
    }
}
