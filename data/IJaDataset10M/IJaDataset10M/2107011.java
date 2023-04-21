package com.javaeedev.web.filter;

import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This FilterToBeanProxy is copied from org.acegisecurity.util.FilterToBeanProxy. 
 * The lazy-init has been removed.
 * 
 * @author Ben Alex, modified by Xuefeng.
 */
public class FilterToBeanProxy implements Filter {

    private Filter delegate;

    private FilterConfig filterConfig;

    public void destroy() {
        if (delegate != null) {
            delegate.destroy();
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        delegate.doFilter(request, response, chain);
    }

    private synchronized void doInit() throws ServletException {
        String targetBean = filterConfig.getInitParameter("targetBean");
        if ("".equals(targetBean)) {
            targetBean = null;
        }
        ApplicationContext ctx = this.getContext(filterConfig);
        String beanName = null;
        if ((targetBean != null) && ctx.containsBean(targetBean)) {
            beanName = targetBean;
        } else if (targetBean != null) {
            throw new ServletException("targetBean '" + targetBean + "' not found in context");
        } else {
            String targetClassString = filterConfig.getInitParameter("targetClass");
            if ((targetClassString == null) || "".equals(targetClassString)) {
                throw new ServletException("targetClass or targetBean must be specified");
            }
            Class targetClass;
            try {
                targetClass = Thread.currentThread().getContextClassLoader().loadClass(targetClassString);
            } catch (ClassNotFoundException ex) {
                throw new ServletException("Class of type " + targetClassString + " not found in classloader");
            }
            Map beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx, targetClass, true, true);
            if (beans.size() == 0) {
                throw new ServletException("Bean context must contain at least one bean of type " + targetClassString);
            }
            beanName = (String) beans.keySet().iterator().next();
        }
        Object object = ctx.getBean(beanName);
        if (!(object instanceof Filter)) {
            throw new ServletException("Bean '" + beanName + "' does not implement javax.servlet.Filter");
        }
        delegate = (Filter) object;
        delegate.init(filterConfig);
    }

    /**
     * Allows test cases to override where application context obtained from.
     * 
     * @param filterConfig which can be used to find the <code>ServletContext</code>
     * 
     * @return the Spring application context
     */
    protected ApplicationContext getContext(FilterConfig filterConfig) {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        doInit();
    }
}
