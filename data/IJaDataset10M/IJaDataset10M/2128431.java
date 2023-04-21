package net.sf.hibernate4gwt.testApplication.server;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * Global application context
 * @author bruno.marchesson
 *
 */
public class ApplicationContext {

    public enum Configuration {

        stateful, stateless, java5, proxy, other_test
    }

    ;

    /**
	 * The stateful configuration file
	 */
    private static final String STATEFUL_CONTEXT_FILE = "net/sf/hibernate4gwt/testApplication/resources/stateful/applicationContext.xml";

    /**
	 * The stateless configuration file
	 */
    private static final String STATELESS_CONTEXT_FILE = "net/sf/hibernate4gwt/testApplication/resources/stateless/applicationContext.xml";

    /**
	 * The Java5 configuration file
	 */
    private static final String JAVA5_CONTEXT_FILE = "net/sf/hibernate4gwt/testApplication/resources/java5/applicationContext.xml";

    /**
	 * The Proxy configuration file
	 */
    private static final String PROXY_CONTEXT_FILE = "net/sf/hibernate4gwt/testApplication/resources/proxy/applicationContext.xml";

    /**
	 * The other test configuration file
	 */
    private static final String TEST_CONTEXT_FILE = "net/sf/hibernate4gwt/testApplication/resources/test/applicationContext.xml";

    /**
	 * Unique instance of the singleton
	 */
    private static ApplicationContext _instance;

    /**
	 * Current configuration
	 */
    private Configuration _configuration = Configuration.stateless;

    /**
	 * Spring context
	 */
    protected GenericApplicationContext _springContext;

    /**
	 * @return the unique of the instance
	 */
    public static final synchronized ApplicationContext getInstance() {
        if (_instance == null) {
            _instance = new ApplicationContext();
        }
        return _instance;
    }

    /**
	 * @return the application configuration
	 */
    public Configuration getConfiguration() {
        return _configuration;
    }

    /**
	 * @param configuration the configuration to set
	 */
    public void setConfiguration(Configuration configuration) {
        if (_configuration != configuration) {
            _configuration = configuration;
            initContextFile();
        }
    }

    /**
	 * Constructor
	 */
    protected ApplicationContext() {
        initContextFile();
    }

    /**
	 * Get a bean from its name
	 */
    public Object getBean(String beanName) {
        return _springContext.getBean(beanName);
    }

    /**
	 * Init Spring context
	 */
    private void initContextFile() {
        _springContext = new GenericApplicationContext();
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(_springContext);
        xmlReader.loadBeanDefinitions(new ClassPathResource(getContextFile()));
        _springContext.refresh();
    }

    /**
	 * @return the appropriate context file associated with the current application configuration
	 */
    private String getContextFile() {
        if (_configuration == Configuration.stateful) {
            return STATEFUL_CONTEXT_FILE;
        } else if (_configuration == Configuration.stateless) {
            return STATELESS_CONTEXT_FILE;
        } else if (_configuration == Configuration.proxy) {
            return PROXY_CONTEXT_FILE;
        } else if (_configuration == Configuration.java5) {
            return JAVA5_CONTEXT_FILE;
        } else {
            return TEST_CONTEXT_FILE;
        }
    }
}
