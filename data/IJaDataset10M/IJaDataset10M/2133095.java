package com.mdiss.bolsatrabajo.bussines;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class ServiceFinder {

    private static ApplicationContext ctx = null;

    public static Object getBean(ServletRequest request, String beanName) {
        if (ctx == null) {
            if (!(request instanceof HttpServletRequest)) {
                throw new IllegalArgumentException("Can only process HttpServletRequest");
            }
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            ctx = getContext(httpRequest);
        }
        return (Object) ctx.getBean(beanName);
    }

    /**
	 * Allows test cases to override where application context obtained from.
	 *
	 * @param httpRequest which can be used to find the
	 * <code>ServletContext </code>
	 *
	 * @return the Spring application context
	 */
    public static ApplicationContext getContext(HttpServletRequest httpRequest) {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(httpRequest.getSession().getServletContext());
    }
}
