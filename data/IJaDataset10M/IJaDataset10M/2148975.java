package com.potix.web.init;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import com.potix.util.resource.Labels;
import com.potix.web.util.resource.ServletLabelLocator;
import com.potix.web.util.resource.ServletLabelResovler;

/**
 * Used to hook a label locator to locate resources from the servlet context.
 *
 * <p>Note: you don't need to specify this in web.xml if you use ZK
 * because com.potix.zk.ui.DHtmlLayoutServlet will register the label locator
 * automatically.
 *
 * <p>If you don't use ZK, you could declare
<pre><code>
&lt;listener&gt;
	&lt;description&gt;Load i3-label.properties from this Web app&lt;/description&gt;
	&lt;display-name&gt;Locating i3-label.properties&lt;/display-name&gt;
	&lt;listener-class&gt;com.potix.web.init.LabelLocatorHook&lt;/listener-class&gt;
&lt;/listener&gt;
</code></pre>
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public class LabelLocatorHook implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent sce) {
    }

    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext ctx = sce.getServletContext();
        Labels.register(new ServletLabelLocator(ctx));
        Labels.setVariableResolver(new ServletLabelResovler());
    }
}
