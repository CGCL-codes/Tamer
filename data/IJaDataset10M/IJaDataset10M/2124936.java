package fi.arcusys.acj.util.spring;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ListIterator;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.Lifecycle;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import fi.arcusys.acj.util.LoggerFactoryUtil;

/**
 * The actual profile-aware root application context initializer that
 * is used by {@link ProfileAwareContextLoaderListener}.
 * 
 * <p>Profiles can be used to specify different runtime environments for
 * integration testing, development and production. It is achieved by
 * specifying alternate Spring application context configurations for
 * different profiles.</p>
 * 
 * <a name="profileLookupSequence"></a>
 * <p>The active profile lookup sequence is following (first found remains
 * active):</p>
 * <ol>
 * <li>Value of system property <code><em>application-name</em>.profile</code>, 
 *     where <em>application-name</em> is name of the application</li>
 * <li>Value of property <code>application.profile</code> in resource
 *     <code>application.local.properties</code> (if exists) or in resource
 *     <code>application.properties</code></li>
 * <li>Value of property <code>application.defaultProfile</code> in resource
 *     <code>application.properties</code></li>
 * <li>Default value "production"</li>
 * </ol>
 * 
 * <a name="applicationNameLookupSequence"></a>
 * <p>The application name lookup sequence is following (first found remains
 * active):</p>
 * <ol>
 * <li>Value of context initialization parameter <code>applicationName</code>
 *     </li>
 * <li>Value of property <code>application.name</code> in resource
 *     <code>application.properties</code></li>
 * <li>Value of element <code>display-name</code> in <code>web.xml</code></li>
 * </ol>
 * 
 * <p>To set the active profile using system properties, specify JVM
 * argument such as "-Dmy-application.profile=test" in server's startup
 * arguments. To set the active profile using resource 
 * <code>application.properties</code>, use property 
 * <code>application.property</code>. For instance:</p>
 * <pre>
 * # application.properties
 * application.name=my-webapp
 * application.profile=test
 * </pre> 
 * 
 * <p>This class extends the default {@link ContextLoader} class and
 * supports its default functionality without any additional configuration.</p>
 * 
 * <p>Locations of profile-specific configuration resources are specified
 * using the standard <code>contextConfigLocation</code>
 * <code>context-param</code> elements in the application's 
 * <code>web.xml</code> resource. By default, configuration resource 
 * <code>/WEB-INF/applicationContext.xml</code>, is included in
 * every profile.</p>
 * 
 * 
 * <p>It's recommended to use the following profile names:</p>
 * <ul>
 * <li>FIXME REMOVE THIS FIXME<code>common</code> commons for all environments</li>
 * <li><code>test</code> for integration testing environment</li>
 * <li><code>development</code> for development environment</li>
 * <li><code>production</code> for production environment</li>
 * </ul>
 * 
 * <p>Context initialization parameters 
 * <code>contextConfigLocation.<em>profile-name</em></code> (with 
 * <em>profile-name</em> being name of the configured profile) can be used to
 * override the default profile configuration resource locations for certain
 * profiles.</p>.
 * 
 * <p>Context initialization parameter <code>contextConfigLocation</code> 
 * specifies the default pattern for creating per-profile configuration 
 * resource location, if not overriden with a profile specific 
 * <code>contextConfigLocation.<em>profile-name</em></code> parameter. 
 * The pattern may contain a placeholder <code>{0}</code>, which
 * is substituted by a profile name at runtime. Default value of
 * <code>contextConfigLocation</code> is following:</p>
 * <pre>
 *   /WEB-INF/applicationContext.xml
 *   /WEB-INF/profiles/{0}/applicationContext.xml
 * </pre>
 * 
 * <p>For maximum flexibility and reusability of configuration elements, Spring
 * can be configured to include all the configuration resources in
 * "context.d" directories with following initialization parameter in
 * <code>web.xml</code>:</p>
 * 
 * <pre>
 * &lt;context-param>
 *   &lt;param-name>contextConfigLocation&lt;/param-name>
 *	 &lt;param-value>
 *     /WEB-INF/context.d/*.xml
 *     /WEB-INF/profiles/{0}/context.d/*.xml
 *   &lt;/param-value>
 * &lt;/context-param>
 * </pre>
 * 
 * <p>By specifying the previous 
 * <code>contextConfigLocation</code> value, the following
 * structure can be used to specify the context configuration:</p>
 * <ul>
 * <li><code>/WEB-INF/context.d/</code> contains common configuration resources
 *     that are included in every profile</li>
 * <li><code>/WEB-INF/profiles/<em>[profile]<em>/context.d</code> contains
 *     profile-specific configuration resources for profile <em>[profile]</em>
 *     </li>
 * </ul>
 * 
 * <p>Typical layout of the "context.d" -based layout pattern would be
 * following:</p>
 * <pre>
 *   /WEB-INF/context.d
 *   /WEB-INF/profiles/test/context.d
 *   /WEB-INF/profiles/development/context.d
 *   /WEB-INF/profiles/production/context.d
 * </pre>
 *
 * <p>An example application would have following configuration files:</p>
 * <pre>
 *   /WEB-INF/context.d/000-bootstrap.xml
 *   /WEB-INF/context.d/dao.xml
 *   /WEB-INF/context.d/service.xml
 *   /WEB-INF/profiles/test/000-bootstrap.xml
 *   /WEB-INF/profiles/test/dataSource.xml
 *   /WEB-INF/profiles/development/dataSource.xml
 *   /WEB-INF/profiles/production/dataSource.xml
 * </pre>
 * 
 * 
 * <p>An example of specifying custom locations for per-profile configuration
 * resources in <code>web.xml</code>:</p>
 * <pre>
 * &lt;context-param>
 *	&lt;param-name>contextConfigLocation.development</param-name>
 *	&lt;param-value>/WEB-INF/development-dataSource.xml</param-value>
 * &lt;/context-param>
 * &lt;context-param>
 *	&lt;param-name>contextConfigLocation.production</param-name>
 *	&lt;param-value>/WEB-INF/production-dataSource.xml</param-value>
 * &lt;/context-param>
 * </pre>
 * 
 * <p>Multiple locations can be specified by separating values with
 * commas, semicolons or white spaces.</p>
 * 
 * <a name="applicationListener"></a>
 * <p>Context parameter <code>applicationListenerClass</code> can be used
 * to specify a class of a per-context {@link ApplicationListener} listener
 * for handling context events. The listener instance is created and
 * registered in method 
 * {@link #registerApplicationListener(ServletContext, ConfigurableWebApplicationContext)},
 * which can be overriden in subclasses.</p>
 * 
 * <p>Finally, a {@link ApplicationProperties} bean with the current
 * configuration is injected to the configuration as a singleton bean with
 * name "applicationProperties". By default the bean is a mutable map of
 * type <code>java.util.map&lt;String,String></code>. Currently there are no 
 * configuration options for the <code>ApplicationProperties/<code> bean 
 * registration (unless this class is overriden and one reimplements the
 * {@link #registerApplicationPropertiesBean(ServletContext, ConfigurableWebApplicationContext)}
 * method). <strong>NOTE: this behaviour is experimental and therefore subject
 * to change in later version.</strong></p>
 * 
 * @see ProfileAwareContextLoaderListener
 * @see ContextLoader
 * @since 0.10
 * @author mikko
 * @copyright (C) 2009 Arcusys Oy
 */
public class ProfileAwareContextLoader extends ContextLoader {

    private static final Logger LOG = LoggerFactoryUtil.getLoggerIfAvailable(ProfileAwareContextLoader.class);

    public static final String APPLICATION_PROPERTIES_BEAN_NAME = "applicationProperties";

    /**
	 * Prefix of context configuration parameter for per-profile context
	 * configuration location overrides: "contextConfigLocation.".
	 * @see ContextLoader#CONFIG_LOCATION_PARAM
	 */
    public static final String PROFILE_CONFIG_LOCATION_PARAM_PREFIX = "contextConfigLocation.";

    /**
	 * Name of the context configuration parameter for the application name:
	 * "applicationName".
	 */
    public static final String APLICATION_NAME_PARAM = "applicationName";

    /**
	 * Name of the context configuration parameter for the application listener
	 * class name: "applicationListenerClass".
	 */
    public static final String APLICATION_LISTENER_CLASS_PARAM = "applicationListenerClass";

    /**
	 * Suffix of the system property name that is used to specify name of
	 * the active profile: ".profile".
	 */
    public static final String ACTIVE_PROFILE_SYSTEM_PROPERTY_SUFFIX = ".profile";

    /**
	 * Default active profile: "production".
	 */
    public static final String DEFAULT_ACTIVE_PROFILE = "production";

    protected static final String DEFAULT_CONTEXT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml\n/WEB-INF/profiles/{0}/applicationContext.xml";

    private ApplicationProperties applicationProperties;

    /**
	 * Resolve per-profile context locations for the specified profile.
	 * 
	 * @param profileName name of the profile
	 * @param servletContext the current <code>ServletContext</code>
	 * @return collection of configuration resources (may be an empty list).
	 */
    protected Collection<String> getProfileContextConfigLocations(String profileName, ServletContext servletContext) {
        if (null == profileName || 0 == profileName.length()) {
            return Collections.emptyList();
        }
        String paramName = PROFILE_CONFIG_LOCATION_PARAM_PREFIX + profileName;
        String value = servletContext.getInitParameter(paramName);
        if (null != value) {
            return Arrays.asList(value.split("[,;\\s]+"));
        } else {
            return Collections.emptyList();
        }
    }

    /**
	 * Resolve the current application name as described in
	 * <a href="#applicationNameLookupSequence">the class documentation.</a>
	 * @param servletContext the current {@code ServletContext}
	 * @return the application name
	 */
    public String resolveApplicationName(ServletContext servletContext) {
        String name = null, nameSource = "unknown";
        name = servletContext.getInitParameter(APLICATION_NAME_PARAM);
        if (null != name) {
            nameSource = "<context-param>/applicationName";
        } else {
            ApplicationProperties ap = getApplicationProperties();
            name = ap.getProperty(ApplicationProperties.PROPERTY_NAME);
            if (null != name) {
                nameSource = "application.properties/application.name";
            } else {
                name = servletContext.getServletContextName();
                if (null != name) {
                    nameSource = "<display-name>";
                }
            }
        }
        if (null == name) {
            throw new ApplicationContextException("Can't resolve application name, please specify it" + " in web.xml/<context-param>/applicationName" + ", application.properties/application.name" + " or in web.xml/<display-name>");
        }
        LOG.info("Using applicationName from {}: {}", nameSource, name);
        return name;
    }

    protected synchronized ApplicationProperties getApplicationProperties() {
        if (null == applicationProperties) {
            this.applicationProperties = new ApplicationProperties();
            try {
                this.applicationProperties.load(false);
            } catch (IOException e) {
                LOG.error("An exception occurred while loading \"{}\"", "application.properties", e);
            }
        }
        return applicationProperties;
    }

    /**
	 * Resolve the currently active application profile as described
	 * in <a href="#profileLookupSequence">the class documentation.</a>
	 * @param applicationName the application name
	 * @param servletContext the current {@code ServletContext}
	 * @return the active profile name
	 */
    public String resolveActiveProfile(String applicationName, ServletContext servletContext) {
        String sysPropertyName = applicationName + ACTIVE_PROFILE_SYSTEM_PROPERTY_SUFFIX;
        String profile, profileSource = "unknown";
        profile = System.getProperty(sysPropertyName);
        if (null != profile) {
            profileSource = "-D" + sysPropertyName;
        } else {
            ApplicationProperties ap = getApplicationProperties();
            final String[] propnames = { ApplicationProperties.PROPERTY_CURRENT_PROFILE, ApplicationProperties.PROPERTY_DEFAULT_PROFILE };
            for (String prop : propnames) {
                profile = ap.getProperty(prop);
                if (null != profile) {
                    profileSource = prop;
                    break;
                }
            }
            if (null == profile) {
                profileSource = "default";
                profile = DEFAULT_ACTIVE_PROFILE;
            }
        }
        LOG.info("Using profile '{}' from {}", profileSource, profile);
        return profile;
    }

    @Override
    protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {
        final String applicationName = resolveApplicationName(servletContext);
        final String profile = resolveActiveProfile(applicationName, servletContext);
        LOG.info("Activating profile '{}'", profile);
        ApplicationProperties ap = getApplicationProperties();
        ap.setProperty(ApplicationProperties.PROPERTY_NAME, applicationName);
        ap.setProperty(ApplicationProperties.PROPERTY_CURRENT_PROFILE, profile);
        ArrayList<String> configLocations = new ArrayList<String>();
        configLocations.addAll(Arrays.asList(applicationContext.getConfigLocations()));
        Collection<String> profileConfigResources = getProfileContextConfigLocations(profile, servletContext);
        if (!profileConfigResources.isEmpty()) {
            LOG.info("Using overriden {}{} for configuration resources", PROFILE_CONFIG_LOCATION_PARAM_PREFIX, profile);
            configLocations.clear();
            configLocations.addAll(profileConfigResources);
        }
        if (configLocations.isEmpty()) {
            LOG.error("No configuration resources found");
            throw new ApplicationContextException("No configuration resources found for active profiles '" + profile + "'.");
        }
        for (ListIterator<String> it = configLocations.listIterator(); it.hasNext(); ) {
            String configLocation = it.next();
            configLocation = MessageFormat.format(configLocation, profile);
            it.set(configLocation);
        }
        applicationContext.setConfigLocations(configLocations.toArray(new String[configLocations.size()]));
        LOG.info("Using context configurations: {}", applicationContext.getConfigLocations());
        registerApplicationPropertiesBean(servletContext, applicationContext);
        registerApplicationListener(servletContext, applicationContext);
    }

    /**
	 * Register the {@link ApplicationProperties} bean as described in 
	 * documentation of this class.
	 * 
	 * <p>Default implementation of this method adds a 
	 * {@link BeanFactoryPostProcessor} instance that registers a singleton
	 * bean of the current {@link ApplicationProperties} instance.</p>
	 * 
	 * @param servletContext the current <code>ServletContext</code>
	 * @param applicationContext the application context that's been
	 *        configured
	 */
    protected void registerApplicationPropertiesBean(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {
        final ApplicationProperties ap = getApplicationProperties();
        applicationContext.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {

            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                beanFactory.registerSingleton(APPLICATION_PROPERTIES_BEAN_NAME, ap);
            }
        });
    }

    /**
	 * Register a {@link ApplicationListener} instance of the configured
	 * class as described in <a href="#applicationListener"> the class 
	 * documentation.</a>
	 * @param servletContext the current <code>ServletContext</code>
	 * @param applicationContext the current <code>AppilcationContext</code>
	 */
    protected void registerApplicationListener(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {
        String className = servletContext.getInitParameter(APLICATION_LISTENER_CLASS_PARAM);
        if (null != className) {
            LOG.info("Registering an ApplicationListener instance of class {}", className);
            try {
                Class<? extends ApplicationListener> clazz = Class.forName(className).asSubclass(ApplicationListener.class);
                ApplicationListener l = clazz.newInstance();
                applicationContext.addApplicationListener(l);
            } catch (ClassNotFoundException e) {
                LOG.error("ApplicationListener class '{}' not found", className);
                throw new ApplicationContextException("Failed to register ApplicationListener", e);
            } catch (InstantiationException e) {
                LOG.error("Failed to instantiate ApplicationListener class '{}'", className);
                throw new ApplicationContextException("Failed to register ApplicationListener", e);
            } catch (IllegalAccessException e) {
                LOG.error("Can't access ApplicationListener class '{}'", className);
                throw new ApplicationContextException("Failed to register ApplicationListener", e);
            } finally {
            }
        } else {
            LOG.debug("Not registering ApplicationListener (none specified)");
        }
    }

    @Override
    public WebApplicationContext initWebApplicationContext(ServletContext servletContext) throws IllegalStateException, BeansException {
        WebApplicationContext wac = super.initWebApplicationContext(servletContext);
        startWebApplicationContext(wac);
        return wac;
    }

    @Override
    public void closeWebApplicationContext(ServletContext servletContext) {
        Object ctxObj = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (null != ctxObj && (ctxObj instanceof WebApplicationContext)) {
            stopWebApplicationContext((WebApplicationContext) ctxObj);
        }
        super.closeWebApplicationContext(servletContext);
    }

    protected void startWebApplicationContext(WebApplicationContext wac) {
        if (wac instanceof Lifecycle) {
            ((Lifecycle) wac).start();
        }
    }

    protected void stopWebApplicationContext(WebApplicationContext wac) {
        if (wac instanceof Lifecycle) {
            ((Lifecycle) wac).stop();
        }
    }
}
