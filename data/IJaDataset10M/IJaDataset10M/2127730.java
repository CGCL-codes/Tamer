package net.sf.logsaw.dialect.log4j;

import net.sf.logsaw.core.dialect.ILogDialectFactory;
import net.sf.logsaw.core.dialect.ILogFieldProvider;
import net.sf.logsaw.dialect.log4j.jboss.JBossDialectFactory;
import net.sf.logsaw.dialect.log4j.pattern.Log4JPatternLayoutDialectFactory;
import net.sf.logsaw.dialect.log4j.xml.Log4JXMLLayoutDialectFactory;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public final class Log4JDialectPlugin extends Plugin {

    private ILogFieldProvider fieldProvider = new Log4JFieldProvider();

    private ILogDialectFactory xmlLayoutDialectFactory = new Log4JXMLLayoutDialectFactory();

    private ILogDialectFactory patternLayoutDialectFactory = new Log4JPatternLayoutDialectFactory();

    private ILogDialectFactory jBossDialectFactory = new JBossDialectFactory();

    private static Log4JDialectPlugin plugin;

    public static final String PLUGIN_ID = "net.sf.logsaw.dialect.log4j";

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
    public static Log4JDialectPlugin getDefault() {
        return plugin;
    }

    /**
	 * @return the xmlLayoutDialectFactory
	 */
    public ILogDialectFactory getXMLLayoutDialectFactory() {
        return xmlLayoutDialectFactory;
    }

    /**
	 * @return the patternLayoutDialectFactory
	 */
    public ILogDialectFactory getPatternLayoutDialectFactory() {
        return patternLayoutDialectFactory;
    }

    /**
	 * @return the JBossDialectFactory
	 */
    public ILogDialectFactory getJBossDialectFactory() {
        return jBossDialectFactory;
    }

    /**
	 * @return the fieldProvider
	 */
    public ILogFieldProvider getFieldProvider() {
        return fieldProvider;
    }
}
