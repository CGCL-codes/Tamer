package org.t18n.filter;

import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.t18n.translator.CacheTranslator;
import org.t18n.translator.GoogleLanguageToolsTranslator;
import org.t18n.translator.MockTranslator;
import org.t18n.translator.Translator;

/**
 * Application Lifecycle Listener implementation class T18nSerlvetStartupListener
 *
 */
public class T18nSerlvetStartupListener implements ServletContextListener {

    public static final String TRANSLATOR_CLASSNAME = "org.t18n.translator.class";

    public static final String SOURCE_LOCALE_ID = "org.t18n.source.locale.id";

    public static final String SOURCE_LOCALE_OBJECT = "org.t18n.locale.object";

    public static final String TRANSLATOR_OBJECT = "org.t18n.translator.object";

    /**
     * Default constructor. 
     */
    public T18nSerlvetStartupListener() {
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        this.configSourceLocale(context);
        this.configTranslatorImplementation(context);
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {
        Object translator = event.getServletContext().getAttribute(TRANSLATOR_OBJECT);
        if (translator instanceof CacheTranslator) {
            CacheTranslator cacheTranslator = (CacheTranslator) translator;
            cacheTranslator.save();
        }
    }

    protected void configSourceLocale(ServletContext config) {
        String sourceLocaleId = config.getInitParameter(SOURCE_LOCALE_ID);
        Locale source = new Locale(sourceLocaleId);
        config.setAttribute(SOURCE_LOCALE_OBJECT, source);
    }

    protected void configTranslatorImplementation(ServletContext config) {
        String className = config.getInitParameter(TRANSLATOR_CLASSNAME);
        Translator translator;
        if (className == null) translator = new MockTranslator(); else try {
            Class translatorClass = this.getClass().getClassLoader().loadClass(className);
            Translator workerTranslator = (Translator) translatorClass.newInstance();
            if (workerTranslator instanceof GoogleLanguageToolsTranslator) {
                GoogleLanguageToolsTranslator googleTranslator = (GoogleLanguageToolsTranslator) workerTranslator;
                String proxyHost = config.getInitParameter("org.t18n.translator.GoogleLanguageToolsTranslator.proxyHost");
                if (proxyHost != null) {
                    googleTranslator.setProxyHost(proxyHost);
                    String proxyPort = config.getInitParameter("org.t18n.translator.GoogleLanguageToolsTranslator.proxyPort");
                    googleTranslator.setProxyPort(proxyPort);
                }
            }
            Translator cacheTranslator = new CacheTranslator(workerTranslator);
            translator = cacheTranslator;
        } catch (ClassNotFoundException e) {
            translator = new MockTranslator();
            e.printStackTrace();
        } catch (InstantiationException e) {
            translator = new MockTranslator();
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            translator = new MockTranslator();
            e.printStackTrace();
        }
        config.setAttribute(TRANSLATOR_OBJECT, translator);
    }
}
