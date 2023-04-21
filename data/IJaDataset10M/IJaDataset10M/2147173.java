package fi.arcusys.acj.util.spring;

import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import fi.arcusys.acj.util.LoggerFactoryUtil;
import fi.arcusys.acj.util.TestPropertiesUtil;

/**
 * A simple abstract base class for Spring-supported test cases.
 * 
 * <p>
 * Runtime properties can be specified in a "SpringTestBase.properties" 
 * resource as specified in 
 * {@link TestPropertiesUtil#loadTestProperties(Properties, Class, Class, String)}.
 * Following properties are supported:</p>
 * 
 * <ul>
 * <li>Property <code>configLocations</code> specifies a list of Spring
 * application context locations (resource names or URL's) separated by a 
 * white space. Default value is <code>test-context.xml</code> (specified in
 * resource <code>SpringTestBase.properties</code> of module <code>acj-util
 * </code>). Please note that the default application context resource is
 * relative to the requesting class, in other words, it must exist in the
 * same package. If you want to store application context resources in the
 * root level, include a <code>SpringTestBase.properties</code> file in your
 * project having <code>configLocations</code> set to for example
 * <code>/test-context.xml</code>.</li>
 * </ul>
 * 
 * <p><strong>Note:</strong> ACJ version 0.10 introduced a
 * {@link SpringManagedTestRunner}, which offers more flexible and easier
 * setup of Spring-based tests. More importantly, it runs all the tests
 * in a Spring container.</p>
 * 
 * @author mikko
 * @copyright (C) 2007 Arcusys Oy
 */
public abstract class SpringTestBase {

    protected final Logger log = LoggerFactoryUtil.getLoggerIfAvailable(getClass());

    private final Logger thisLogger = LoggerFactoryUtil.getLoggerIfAvailable(SpringTestBase.class);

    /**
	 * Name of the property specifying white space -separated config locations
	 * for initializing the ApplicationContext for tests.
	 */
    public static final String TEST_PROPERTY_CONFIG_LOCATIONS = "configLocations";

    private ApplicationContext applicationContext;

    private String configLocations;

    private Class<?> callingClass;

    public SpringTestBase() {
        this(null);
    }

    public SpringTestBase(Class<?> callingClass) {
        loadProperties();
    }

    protected Class<?> getCallingClass() {
        return (null != callingClass) ? callingClass : getClass();
    }

    public String getConfigLocations() {
        return configLocations;
    }

    public void setConfigLocations(String configLocations) {
        this.configLocations = configLocations;
    }

    public String[] getConfigLocationsArray() {
        String s = getConfigLocations();
        return (null != s) ? s.split("\\s") : new String[0];
    }

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    protected void setApplicationContext(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }

    protected void setProperty(Properties props, String key, String value) {
        if (null == value) {
            props.remove(key);
        } else {
            props.put(key, value);
        }
    }

    protected void initProperties(Properties props) {
        setProperty(props, TEST_PROPERTY_CONFIG_LOCATIONS, getConfigLocations());
    }

    protected void initFromProperties(Properties props) {
        setConfigLocations(props.getProperty(TEST_PROPERTY_CONFIG_LOCATIONS));
    }

    protected void doLoadProperties(Properties props) {
        TestPropertiesUtil.loadTestProperties(props, getCallingClass(), SpringTestBase.class, "SpringTestBase");
    }

    private void loadProperties() {
        Properties props = new Properties();
        initProperties(props);
        doLoadProperties(props);
        initFromProperties(props);
    }

    /**
	 * Set up the Spring's <code>ApplicationContext</code> based on the
	 * specified <code>contextLocations</code> property.
	 *
	 **/
    @Before
    public void setUpApplicationContext() {
        thisLogger.debug("Setting up ApplicationContext from resources: {}", getConfigLocations());
        applicationContext = new ClassPathXmlApplicationContext(getConfigLocationsArray(), getCallingClass());
    }

    @After
    public void tearDownApplicationContext() {
        try {
            if (null != applicationContext) {
                thisLogger.debug("Tearing down ApplicationContext");
                ((ConfigurableApplicationContext) applicationContext).close();
                applicationContext = null;
            }
        } catch (Exception ex) {
            thisLogger.error("Catched an exception while closing the ApplicationContext", ex);
        }
    }

    public <T> T getBean(String name, Class<T> expectedClazz) {
        Object bean = applicationContext.getBean(name, expectedClazz);
        if (null == bean) {
            return null;
        } else {
            return expectedClazz.cast(bean);
        }
    }

    public final Object getBean(String name) {
        return getBean(name, Object.class);
    }
}
