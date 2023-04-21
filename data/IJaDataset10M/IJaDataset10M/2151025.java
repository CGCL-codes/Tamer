package net.sf.sasl.applicationserver.tomcat;

import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.Assert;

/**
 * Class could be used to load and start components like MBeans over the springframework
 * by the start of a Tomcat engine (or host).<br>
 * The behavior is similiar to {@link org.springframework.web.context.ContextLoaderListener ContextLoaderListener}, which only works for web applications.<br>
 * A sample configuration for Tomcat might look as the following:<br>
 * &lt;Engine&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;...<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;Listener className="net.sf.sasl.applicationserver.tomcat.SpringLifeCycleListener"<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;xmlApplicationContextFiles="classpath:/com/company/tomcat-spring-beans.xml, file:/etc/tomcat/spring-mbeans.xml"<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;fileDelimiter="," /&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;...<br>
 * &lt;/Engine&gt;<br>
 * 
 * @author Philipp Förmer
 * @since 0.0.1 (sasl-tomcat)
 */
public class SpringLifeCycleListener implements LifecycleListener {

    /**
	 * List of spring bean definition files.
	 * 
	 * @since 0.0.1 (sasl-tomcat)
	 */
    private String xmlApplicationContextFiles;

    /**
	 * Delimiter used to split multiple bean files in xmlApplicationContextFiles.
	 * 
	 * @since 0.0.1 (sasl-tomcat)
	 */
    private String fileDelimiter = ",";

    /**
	 * Spring application context, which is initialised with beans from xmlApplicationContextFiles.
	 * 
	 * @since 0.0.1 (sasl-tomcat)
	 */
    private AbstractApplicationContext applicationContext;

    /**
	 * Used for log output.
	 * 
	 * @since 0.0.1 (sasl-tomcat)
	 */
    private Logger logger = LoggerFactory.getLogger(SpringLifeCycleListener.class);

    /**
	 * @see org.apache.catalina.LifecycleListener#lifecycleEvent(org.apache.catalina.LifecycleEvent)
	 * @since 0.0.1 (sasl-tomcat)
	 */
    public void lifecycleEvent(LifecycleEvent event) {
        if (Lifecycle.BEFORE_START_EVENT.equals(event.getType())) {
            try {
                initApplicationContext();
            } catch (Exception exception) {
                throw new RuntimeException("Error during initializing of application context", exception);
            }
        } else if (Lifecycle.AFTER_STOP_EVENT.equals(event.getType())) {
            if (applicationContext != null) {
                try {
                    applicationContext.close();
                } finally {
                    applicationContext = null;
                }
            }
        }
    }

    /**
	 * @return null or non null.
	 * @see #setXmlApplicationContextFiles(String)
	 * @since 0.0.1 (sasl-tomcat)
	 */
    public String getXmlApplicationContextFiles() {
        return xmlApplicationContextFiles;
    }

    /**
	 * Sets a list of spring xml bean files - separated from each other with the set delimiter -
	 * with which the application context should get initialised.
	 * 
	 * @param xmlApplicationContextFiles
	 *            null or non null.
	 * @since 0.0.1 (sasl-tomcat)
	 */
    public void setXmlApplicationContextFiles(String xmlApplicationContextFiles) {
        this.xmlApplicationContextFiles = xmlApplicationContextFiles;
    }

    /**
	 * @return non null.
	 * @see #setFileDelimiter(String)
	 * @since 0.0.1 (sasl-tomcat)
	 */
    public String getFileDelimiter() {
        return fileDelimiter;
    }

    /**
	 * Sets a delimiter, that separates multiple spring bean files from each other (file1, file2, ..., fileN).
	 * The default delimiter is ",".
	 * 
	 * @param fileDelimiter
	 *            non null.
	 * @throws IllegalArgumentException
	 *             if parameter fileDelimiter is null.
	 * @since 0.0.1 (sasl-tomcat)
	 */
    public void setFileDelimiter(String fileDelimiter) {
        Assert.notNull("Parameter fileDelimiter must be non null!", fileDelimiter);
        this.fileDelimiter = fileDelimiter;
    }

    /**
	 * @return non null.
	 * @see #setLogger(String)
	 * @since 0.0.1 (sasl-tomcat)
	 */
    protected Logger getLogger() {
        return logger;
    }

    /**
	 * Sets the logger which will be used for log output.
	 * 
	 * @param logger
	 *            non null.
	 * @throws IllegalArgumentException
	 *             if parameter logger is null.
	 * @since 0.0.1 (sasl-tomcat)
	 */
    public void setLogger(String logger) {
        Assert.notNull("Parameter logger must be non null!", logger);
        this.logger = LoggerFactory.getLogger(logger);
    }

    /**
	 * Returns the application context, which was initialised at the arrival of the {@link org.apache.catalina.Lifecycle.START_EVENT Lifecycle.START_EVENT}.
	 * 
	 * @return null or non null.
	 * @since 0.0.1 (sasl-tomcat)
	 */
    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
	 * 
	 * @throws IOException
	 * @since 0.0.1 (sasl-tomcat)
	 */
    protected void initApplicationContext() throws IOException {
        if (xmlApplicationContextFiles == null) {
            return;
        }
        String[] contextFiles = xmlApplicationContextFiles.split(Pattern.quote(fileDelimiter));
        logger.info("Trying to initialize spring context with " + contextFiles.length + " bean resource(s).");
        GenericApplicationContext genericApplicationContext = new GenericApplicationContext();
        XmlBeanDefinitionReader beanReader = new XmlBeanDefinitionReader(genericApplicationContext);
        beanReader.setResourceLoader(new DefaultResourceLoader());
        for (String contextFile : contextFiles) {
            String ctxFile = contextFile.trim();
            if ("".equals(ctxFile)) {
                continue;
            }
            logger.info("Delegating loading of '" + ctxFile + "' bean resource");
            beanReader.loadBeanDefinitions(ctxFile);
        }
        genericApplicationContext.refresh();
        applicationContext = genericApplicationContext;
    }
}
