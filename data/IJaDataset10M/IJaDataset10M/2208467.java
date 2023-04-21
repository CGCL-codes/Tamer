package net.sf.balm.web.impl;

import javax.servlet.http.HttpServlet;
import net.sf.balm.web.WebBeanProxyFactory;
import net.sf.balm.web.WebBeanProxyFactoryProvider;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author dz
 */
public class WebBeanProxyFactorySpringProvider implements WebBeanProxyFactoryProvider {

    /**
     * 
     */
    public WebBeanProxyFactory create(HttpServlet servlet) {
        WebApplicationContext webApplicationContxt = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet.getServletContext());
        return new DefaultWebBeanProxyFactory(webApplicationContxt);
    }
}
