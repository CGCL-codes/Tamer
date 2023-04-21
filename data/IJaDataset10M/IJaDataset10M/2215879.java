package org.ztemplates.web;

import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.zclasspath.ZClassPathScanner;
import org.zclasspath.ZClassRepository;
import org.zclasspath.ZIClassPathFilter;
import org.zclasspath.ZIClassPathItem;
import org.zclasspath.ZIClassRepository;
import org.zclasspath.ZWebappClassPath;
import org.ztemplates.actions.ZActionApplication;
import org.ztemplates.render.ZRenderApplication;
import org.ztemplates.web.application.ZApplication;
import org.ztemplates.web.application.ZApplicationContextWebImpl;
import org.ztemplates.web.application.ZApplicationRepositoryWeb;
import org.ztemplates.web.script.css.ZCachingCssProcessorData;
import org.ztemplates.web.script.javascript.ZCachingJavaScriptProcessorData;
import org.ztemplates.web.script.zscript.ZIJavaScriptRepository;
import org.ztemplates.web.script.zscript.ZJavaScriptRepositoryAnnotationFactory;
import org.ztemplates.web.standalone.ZApplicationRepositoryStandalone;

/**
 * 
 * Assembles the application
 * 
 */
public class ZTemplatesContextListener implements ServletContextListener {

    private static final Logger log = Logger.getLogger(ZTemplatesContextListener.class);

    static final class ZInternalClassPathFilter extends ZDefaultClassPathFilter {

        @Override
        public boolean acceptClass(String name) throws Exception {
            if (super.acceptClass(name)) {
                return true;
            }
            if (name.startsWith("org.apache.") || name.startsWith("freemarker.") || name.startsWith("flex.") || name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("org.junit") || name.startsWith("org.json") || name.startsWith("org.jfree")) {
                return false;
            }
            return true;
        }
    }

    public void contextInitialized(ServletContextEvent ev) {
        ServletContext ctx = ev.getServletContext();
        ZTemplatesContextListener.initContext(ctx);
    }

    public static void initContext(ServletContext ctx) {
        try {
            log.info("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
            log.info("zzz initializing context " + ctx.getContextPath() + " ...");
            log.info("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
            String encoding = ctx.getInitParameter("encoding");
            if (encoding == null) {
                encoding = "UTF-8";
                log.warn("*******************************************************************************************\n" + "*** No context-param named 'encoding' found\n" + "*** Using ztemplates default encoding " + encoding + "\n" + "*******************************************************************************************\n" + "*** To change encoding add the following lines to your web.xml: \n" + "<context-param>\n" + "    <param-name>encoding</param-name>\n" + "    <param-value>ISO-8859-1</param-value>\n" + "</context-param>\n" + "*******************************************************************************************\n" + "*** Don't forget to adjust your IDE/compiler/editor settings to use the selected encoding " + encoding + "\n" + "*******************************************************************************************\n");
            }
            ZClassPathScanner scanner = createClassPathScanner(ctx);
            log.info("creating class repository...");
            final ZIClassRepository classRepository = createClassRepository(scanner);
            final ZApplicationContextWebImpl applicationContext = new ZApplicationContextWebImpl(classRepository, ctx, encoding);
            log.info("creating javascript repository...");
            final ZIJavaScriptRepository javaScriptRepository = new ZJavaScriptRepositoryAnnotationFactory(applicationContext, ctx, classRepository, encoding).createJavaScriptRepository();
            log.info("creating application...");
            ZCachingJavaScriptProcessorData.setInstance(applicationContext, new ZCachingJavaScriptProcessorData());
            ZCachingCssProcessorData.setInstance(applicationContext, new ZCachingCssProcessorData());
            ZActionApplication actionApplication = new ZActionApplication(applicationContext, classRepository);
            ZRenderApplication renderApplication = new ZRenderApplication(applicationContext, classRepository);
            ZApplication application = new ZApplication(classRepository, javaScriptRepository, actionApplication, renderApplication);
            ZApplicationRepositoryWeb.setApplication(ctx, application);
            String applicationName = ctx.getInitParameter("applicationName");
            if (applicationName == null) {
                log.info("No <context-param><param-name>applicationName</param-name></context-param> has been found in web.xml --- Using default application name. This is safe if webapp has its own classloader or you use ztemplates only inside a http request. --- If you share classloader between weapps and need access to ztemplates functionality outside of a http request (for example: a scheduled job) set the init parameter 'applicationName' in web.xml to unique name in each webapp and use the applicationName in ZTemplatesStandalone.init()");
            }
            ZApplicationRepositoryStandalone.setApplication(applicationName, application);
            log.info("context initialized");
        } catch (Exception e) {
            log.error("context not initialized", e);
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent ev) {
        ServletContext ctx = ev.getServletContext();
        String applicationName = ctx.getInitParameter("applicationName");
        if (applicationName == null) {
            applicationName = ZApplicationRepositoryStandalone.DEFAULT_APP_NAME;
        }
        ZApplicationRepositoryWeb.setApplication(ctx, null);
        ZApplicationRepositoryStandalone.setApplication(applicationName, null);
        log.info("context destroyed");
    }

    private static ZClassPathScanner createClassPathScanner(final ServletContext servletContext) throws Exception {
        List<ZIClassPathItem> items = ZWebappClassPath.getItems(servletContext);
        String filterClassPropertyName = ZIClassPathFilter.class.getSimpleName();
        final String filterClass = servletContext.getInitParameter(filterClassPropertyName);
        final ZIClassPathFilter filter;
        if (filterClass != null) {
            log.info("CLASSPATH filter OK --- filtering scanned classes with : " + filterClass + " ( to change modify " + filterClassPropertyName + " in WEB-INF/xml )");
            filter = (ZIClassPathFilter) Class.forName(filterClass).newInstance();
        } else {
            log.warn("CLASSPATH filter --- no init property: " + filterClassPropertyName + ", using default value " + ZInternalClassPathFilter.class.getName());
            log.info("CLASSPATH filter INFO --- to avoid scanning too many jars/classes you could have set the init property '" + filterClassPropertyName + "' in WEB-INF/web.xml to the classname of a implementation of " + ZIClassPathFilter.class.getName() + " (best extending " + ZDefaultClassPathFilter.class.getName() + "), see http://www.ztemplates.org/Wiki.jsp?page=Install");
            filter = new ZInternalClassPathFilter();
        }
        ZClassPathScanner scanner = new ZClassPathScanner();
        scanner.setClassPathItems(items);
        scanner.setFilter(filter);
        return scanner;
    }

    private static ZIClassRepository createClassRepository(ZClassPathScanner scanner) throws Exception {
        ZIClassRepository ret = new ZClassRepository(scanner);
        if (ret.getClasses().isEmpty()) {
            System.err.println("no ztemplates classes found in webapp locations /WEB-INF/lib and /WEB-INF/classes, trying classloader...");
            List<ZIClassPathItem> items = ZClassPathItemResource.getItems();
            scanner = new ZClassPathScanner();
            scanner.setClassPathItems(items);
            ret = new ZClassRepository(scanner);
        }
        if (ret.getClasses().isEmpty()) {
            log.error("###############################################################################");
            log.error("### FATAL ERROR: no ztemplates relevant classes found, check your classpath ###");
            log.error("###############################################################################");
            throw new Exception("FATAL ERROR: no ztemplates relevant classes found, check your classpath");
        }
        return ret;
    }
}
