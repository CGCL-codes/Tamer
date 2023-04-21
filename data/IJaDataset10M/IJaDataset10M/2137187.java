package org.xaloon.wicket.component.application;

import javax.servlet.http.HttpServletResponse;
import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.protocol.http.BufferedWebResponse;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xaloon.wicket.component.mounting.PageAnnotationScannerContainer;
import org.xaloon.wicket.component.plugin.MenuPluginObserver;
import org.xaloon.wicket.component.plugin.PluginManager;

/**
 * http://www.xaloon.org
 * 
 * @author vytautas racelis
 */
public abstract class AbstractWebApplication extends WebApplication {

    private static WebApplicationContext context;

    private static final String[] BOT_AGENTS = { "googlebot", "msnbot", "slurp", "jeeves", "appie", "architext", "jeeves", "bjaaland", "ferret", "gulliver", "harvest", "htdig", "linkwalker", "lycos_", "moget", "muscatferret", "myweb", "nomad", "scooter", "yahoo!\\sslurp\\schina", "slurp", "weblayers", "antibot", "bruinbot", "digout4u", "echo!", "ia_archiver", "jennybot", "mercator", "netcraft", "msnbot", "petersnews", "unlost_web_crawler", "voila", "webbase", "webcollage", "cfetch", "zyborg", "wisenutbot", "robot", "crawl", "spider" };

    @Autowired
    private PluginManager pluginManager;

    @Autowired
    private PageAnnotationScannerContainer annotationScannerContainer;

    @Override
    protected void init() {
        super.init();
        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getMarkupSettings().setCompressWhitespace(true);
        getMarkupSettings().setStripComments(true);
        getMarkupSettings().setStripWicketTags(true);
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        addComponentInstantiationListener(new SpringComponentInjector(this));
        context = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
        pluginManager.init(context);
        annotationScannerContainer.addObserver(new MenuPluginObserver(pluginManager));
        annotationScannerContainer.scan(this, context);
    }

    public static WebApplicationContext getWebApplicationContext() {
        return context;
    }

    public static boolean isAgent(final String agent) {
        if (agent != null) {
            final String lowerAgent = agent.toLowerCase();
            for (final String bot : BOT_AGENTS) {
                if (lowerAgent.indexOf(bot) != -1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected WebResponse newWebResponse(HttpServletResponse servletResponse) {
        return new BufferedWebResponse(servletResponse) {

            @Override
            public CharSequence encodeURL(final CharSequence url) {
                final String agent = ((WebRequest) RequestCycle.get().getRequest()).getHttpServletRequest().getHeader("User-Agent");
                return isAgent(agent) ? url : super.encodeURL(url);
            }
        };
    }

    public static AbstractWebApplication get() {
        return (AbstractWebApplication) WebApplication.get();
    }

    public Class<? extends Page> getLayoutPageClass() {
        return null;
    }
}
