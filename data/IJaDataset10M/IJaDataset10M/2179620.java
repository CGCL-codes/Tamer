package com.acciente.induction.template;

import com.acciente.induction.init.config.Config;
import com.acciente.induction.view.Template;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.ServletConfig;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An implementation of the Induction template engine interface that plugs in the Freemarker templating engine.
 *
 * @created Apr 17, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class FreemarkerTemplatingEngine implements TemplatingEngine {

    private Configuration _oConfiguration;

    public FreemarkerTemplatingEngine(Config.Templating oConfig, ClassLoader oClassLoader, ServletConfig oServletConfig) throws IOException, ClassNotFoundException {
        Log oLog;
        oLog = LogFactory.getLog(FreemarkerTemplatingEngine.class);
        _oConfiguration = new Configuration();
        List oTemplateLoaderList = new ArrayList(oConfig.getTemplatePath().getList().size());
        for (Iterator oIter = oConfig.getTemplatePath().getList().iterator(); oIter.hasNext(); ) {
            Object oLoaderPathItem = oIter.next();
            if (oLoaderPathItem instanceof Config.Templating.TemplatePath.Dir) {
                Config.Templating.TemplatePath.Dir oDir = (Config.Templating.TemplatePath.Dir) oLoaderPathItem;
                if (!oDir.getDir().exists()) {
                    oLog.warn("freemarker > template load path > ignoring missing directory > " + oDir.getDir());
                } else {
                    oLog.info("freemarker > template load path > adding directory > " + oDir.getDir());
                    oTemplateLoaderList.add(new FileTemplateLoader(oDir.getDir()));
                }
            } else if (oLoaderPathItem instanceof Config.Templating.TemplatePath.LoaderClass) {
                Config.Templating.TemplatePath.LoaderClass oLoaderClass = (Config.Templating.TemplatePath.LoaderClass) oLoaderPathItem;
                Class oClass = Class.forName(oLoaderClass.getLoaderClassName());
                oLog.info("freemarker > template load path > adding class > " + oLoaderClass.getLoaderClassName() + ", prefix: " + oLoaderClass.getPath());
                oTemplateLoaderList.add(new ClassTemplateLoader(oClass, oLoaderClass.getPath()));
            } else if (oLoaderPathItem instanceof Config.Templating.TemplatePath.WebappPath) {
                Config.Templating.TemplatePath.WebappPath oWebappPath = (Config.Templating.TemplatePath.WebappPath) oLoaderPathItem;
                oLog.info("freemarker > template load path > adding webapp path > " + oWebappPath.getPath());
                oTemplateLoaderList.add(new WebappTemplateLoader(oServletConfig.getServletContext(), oWebappPath.getPath()));
            } else {
                throw new IllegalArgumentException("Unexpected template path type in configuration: " + oLoaderPathItem.getClass());
            }
        }
        TemplateLoader[] oTemplateLoaderArray = new TemplateLoader[oTemplateLoaderList.size()];
        oTemplateLoaderList.toArray(oTemplateLoaderArray);
        _oConfiguration.setTemplateLoader(new MultiTemplateLoader(oTemplateLoaderArray));
        DefaultObjectWrapper oDefaultObjectWrapper = new DefaultObjectWrapper();
        oDefaultObjectWrapper.setExposeFields(oConfig.isExposePublicFields());
        oLog.info("freemarker > expose public fields > " + oConfig.isExposePublicFields());
        _oConfiguration.setObjectWrapper(oDefaultObjectWrapper);
        if (oConfig.getLocale() != null) {
            _oConfiguration.setLocale(oConfig.getLocale());
            oLog.info("freemarker > using configured locale > " + oConfig.getLocale());
        } else {
            oLog.warn("freemarker > no locale configured, using default > " + _oConfiguration.getLocale());
        }
    }

    public void process(Template oTemplate, Writer oWriter) throws TemplatingEngineException, IOException {
        try {
            _oConfiguration.getTemplate(oTemplate.getTemplateName()).process(oTemplate, oWriter);
        } catch (TemplateException e) {
            throw new TemplatingEngineException("View " + oTemplate.getClass().getName() + ", freemaker error: ", e);
        }
    }

    /**
    * This method was added to allow classes to extend this class to modify the Freemarker configuration
    * @return
    */
    public Configuration getConfiguration() {
        return _oConfiguration;
    }
}
