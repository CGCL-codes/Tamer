package org.strutsconfigreloader.struts.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import org.apache.struts.Globals;
import org.strutsconfigreloader.resource.MultiResourcesReloadable;
import org.strutsconfigreloader.struts.DefaultStrutsConfigFileHelper;
import org.strutsconfigreloader.struts.StrutsConfigReloadHelper;
import org.strutsconfigreloader.struts.StrutsResourcesReloader;

/**
 * <p>
 * Struts config reload support filter.
 * </p>
 * <p>
 * <strong>Usage:<strong>
 * </p>
 * 
 * <pre>
 * &lt;![CDATA[
 * &lt;!-- StrutsConfigReloadFilter start --&gt;
 *  &lt;filter&gt;
 *  	&lt;filter-name&gt;strutsCofigReloadFilter&lt;/filter-name&gt;
 *  	&lt;filter-class&gt;
 *  		org.strutsconfigreloader.struts.filter.StrutsConfigReloadFilter
 *  	&lt;/filter-class&gt;
 *  &lt;/filter&gt;
 *  &lt;filter-mapping&gt;
 *  	&lt;filter-name&gt;strutsCofigReloadFilter&lt;/filter-name&gt;
 *  	&lt;url-pattern&gt;*.do&lt;/url-pattern&gt;
 *  &lt;/filter-mapping&gt;
 *  &lt;!-- StrutsConfigReloadFilter end --&gt;
 * ]]&gt;
 * </pre>
 * 
 * 
 * @author <a href="mailto:ygu@ceno.cn">guyang</a>
 * @version 1.0 2007-2-27 22:45:54
 */
public class StrutsConfigReloadFilter implements Filter {

    private ServletContext servletContext = null;

    private MultiResourcesReloadable resourceReloader = null;

    /**
	 * 
	 * 
	 * @param filterConfig filter config
	 * @throws javax.servlet.ServletException
	 */
    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
        createResourceReloader();
    }

    protected void createResourceReloader() {
        HttpServlet actionServlet = (HttpServlet) servletContext.getAttribute(Globals.ACTION_SERVLET_KEY);
        if (actionServlet == null || resourceReloader != null) {
            return;
        }
        resourceReloader = newResourcesReloader(actionServlet);
        resourceReloader.addListener(new StrutsConfigReloadHelper(actionServlet));
    }

    /**
	 * @param actionServlet
	 * @return
	 */
    protected StrutsResourcesReloader newResourcesReloader(HttpServlet actionServlet) {
        DefaultStrutsConfigFileHelper scfh = new DefaultStrutsConfigFileHelper(actionServlet);
        return new StrutsResourcesReloader(scfh);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (resourceReloader == null) {
            createResourceReloader();
        }
        if (resourceReloader.hasModified()) {
            synchronized (this) {
                resourceReloader.reload();
            }
        }
        filterChain.doFilter(request, response);
    }

    public void destroy() {
    }

    /**
	 * @return the resourceReloader
	 */
    protected MultiResourcesReloadable getResourceReloader() {
        return resourceReloader;
    }
}
