package nuts.ext.struts2.views.freemarker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.views.freemarker.StrutsClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * FreemarkerManager
 * 
 * @see org.apache.struts2.views.freemarker.FreemarkerManager
 */
public class FreemarkerManager extends org.apache.struts2.views.freemarker.FreemarkerManager {

    private static final Log log = LogFactory.getLog(FreemarkerManager.class);

    private static LinkedList<TemplateLoader> staticTemplateLoaderList = new LinkedList<TemplateLoader>();

    private LinkedList<TemplateLoader> customTemplateLoaderList = new LinkedList<TemplateLoader>();

    /**
	 * @return the staticTemplateLoaderList
	 */
    public static LinkedList<TemplateLoader> getStaticTemplateLoaderList() {
        return staticTemplateLoaderList;
    }

    /**
	 * @param staticTemplateLoaderList the staticTemplateLoaderList to set
	 */
    public static void setStaticTemplateLoaderList(LinkedList<TemplateLoader> staticTemplateLoaderList) {
        FreemarkerManager.staticTemplateLoaderList = staticTemplateLoaderList;
    }

    /**
	 * @return the customTemplateLoaderList
	 */
    public LinkedList<TemplateLoader> getCustomTemplateLoaderList() {
        return customTemplateLoaderList;
    }

    /**
	 * @param customTemplateLoaderList the customTemplateLoaderList to set
	 */
    public void setCustomTemplateLoaderList(LinkedList<TemplateLoader> customTemplateLoaderList) {
        this.customTemplateLoaderList = customTemplateLoaderList;
    }

    /**
	 * The default template loader is a MultiTemplateLoader which includes a ClassTemplateLoader and
	 * a WebappTemplateLoader (and a FileTemplateLoader depending on the init-parameter
	 * 'TemplatePath').
	 * <p/>
	 * The ClassTemplateLoader will resolve fully qualified template includes that begin with a
	 * slash. for example /com/company/template/common.ftl
	 * <p/>
	 * The WebappTemplateLoader attempts to resolve templates relative to the web root folder
	 * <p/>
	 * The CustomTemplateLoaderList
	 * <p/>
	 * The StaticTemplateLoaderList
	 */
    protected TemplateLoader getTemplateLoader(ServletContext servletContext) {
        List<TemplateLoader> tls = new ArrayList<TemplateLoader>();
        String templatePath = servletContext.getInitParameter("TemplatePath");
        if (templatePath == null) {
            templatePath = servletContext.getInitParameter("templatePath");
        }
        if (templatePath != null) {
            String[] tps = StringUtils.split(templatePath);
            for (String tp : tps) {
                try {
                    tls.add(new FileTemplateLoader(new File(tp)));
                } catch (IOException e) {
                    log.error("Invalid template path specified: " + e.getMessage(), e);
                }
            }
        }
        if (customTemplateLoaderList != null) {
            tls.addAll(customTemplateLoaderList);
        }
        if (staticTemplateLoaderList != null) {
            tls.addAll(staticTemplateLoaderList);
        }
        tls.add(new WebappTemplateLoader(servletContext));
        tls.add(new StrutsClassTemplateLoader());
        return new MultiTemplateLoader(tls.toArray(new TemplateLoader[tls.size()]));
    }

    /**
     * Create the instance of the freemarker Configuration object.
     * <p/>
     * this implementation
     * <ul>
     * <li>obtains the default configuration from Configuration.getDefaultConfiguration()
     * <li>sets up template loading from a ClassTemplateLoader and a WebappTemplateLoader
     * <li>sets up the object wrapper to be the BeansWrapper
     * <li>loads settings from the classpath file /freemarker.properties
     * </ul>
     *
     * @param servletContext
     */
    protected freemarker.template.Configuration createConfiguration(ServletContext servletContext) throws TemplateException {
        freemarker.template.Configuration configuration = super.createConfiguration(servletContext);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return configuration;
    }
}
