package com.genia.toolbox.config.autoconfiguration.process;

import java.beans.IntrospectionException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Log4jConfigurer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.genia.toolbox.basics.bean.ConfigurableClassLoader;
import com.genia.toolbox.basics.tools.crypto.symmetric.Base64;
import com.genia.toolbox.basics.tools.shared.PropertiesClass;
import com.genia.toolbox.config.autoconfiguration.bean.ConfigurationBean;
import com.genia.toolbox.config.autoconfiguration.bean.DatabaseConfigurationBean;
import com.genia.toolbox.constants.client.Charset;
import com.genia.toolbox.web.jndi_config.StringContainer;

/**
 * the process handling the configuration of the application.
 */
public class ProcessConfiguration {

    /**
	 * the <code>Log</code> variabe.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessConfiguration.class);

    /**
	 * the qualified name of the jcr plugin class.
	 */
    private static final String JCR_PLUGIN_QUALIFIED_NAME = "com.genia.toolbox.persistence.jcr.spring.provider.plugin.JcrPersistenceSpringProviderPlugin";

    /**
	 * the qualified name of the hibernate plugin class.
	 */
    private static final String HIBERNATE_PLUGIN_QUALIFIED_NAME = "com.genia.toolbox.persistence.hibernate.spring.provider.plugin.HibernatePersistenceSpringProviderPlugin";

    /**
	 * Configuration properties file for OsCache.
	 */
    private static final String OS_CACHE_CONFIGIRATION = "/com/genia/toolbox/config/autoconfiguration/plugin/oscache.properties";

    /**
	 * the name of the OSCache configuration parameter that describe the
	 * location of the cache on disk.
	 */
    private static final String OS_CACHE_CACHE_PATH = "cache.path";

    /**
	 * the name of the property of the {@link StringContainer} containing the
	 * directory used by this application for keeping its configuration.
	 */
    public static final String CONFIGURATION_DIRECTORY = "configurationDirectory";

    /**
	 * the name of the file containing the configuration.
	 */
    public static final String CONFIGURATION_FILE_NAME = "configuration.xml";

    /**
	 * the name of the jcr directory inside the configuration directory.
	 */
    public static final String JCR_DIRECTORY = "jcr-directory";

    /**
	 * the {@link BeanReader} used to read the configuration.
	 */
    private static BeanReader beanReader = null;

    /**
	 * the name of the file containing the betwixt configuration file to handle
	 * reading and writing xml files.
	 */
    private static final String BETWIXT_CONFIGURATION_FILE = "/com/genia/toolbox/config/autoconfiguration/bean/ConfigurationBean.betwixt";

    /**
	 * a lock to prevent concurrent creation of {@link BeanReader}s.
	 */
    private static final Object BETWIXT_LOCK = new Object();

    /**
	 * default log4j configuration file.
	 */
    private static final String DEFAULT_LOG4J_CONFIGURATION_FILE = "/com/genia/toolbox/config/autoconfiguration/plugin/log4j.xml";

    /**
	 * default log4j configuration file.
	 */
    private static final String DEFAULT_OFF_LOG4J_CONFIGURATION_FILE = "/com/genia/toolbox/config/autoconfiguration/plugin/log4j_off.xml";

    /**
	 * the base directory containing all the configuration.
	 */
    private static File configurationDirectory = null;

    /**
	 * the name of the JNDI context containing the {@link StringContainer}.
	 */
    private static final String JNDI_CONTEXT = "java:comp/env";

    /**
	 * the JNDI name of the {@link StringContainer}.
	 */
    private static final String JNDI_STRING_CONTAINER_NAME = "string/configurationContainer";

    /**
	 * the JNDI name of the fakse {@link DataSource} containing the string
	 * informations.
	 */
    private static final String JNDI_DATASOURCE_STRING_CONTAINER_NAME = "jdbc/configurationContainer";

    /**
	 * the {@link StringContainer} containing the base configuration (the name
	 * of the directory that will contain all the configuration).
	 */
    private static StringContainer stringContainer = null;

    /**
	 * the {@link DataSource} containing the base configuration (the name of the
	 * directory that will contain all the configuration).
	 */
    private static DataSource stringContainerDataSource = null;

    /**
	 * the {@link File}s to delete once the application is shutdown.
	 */
    private static final List<File> FILES_TO_DELETE = new ArrayList<File>();

    /**
	 * the {@link ConfigurableClassLoader} that allow to modify dynamically the
	 * classpath.
	 */
    private static final ThreadLocal<ConfigurableClassLoader> CONFIGURABLE_CLASS_LOADER = new ThreadLocal<ConfigurableClassLoader>();

    /**
	 * Adding a shutdown thread.
	 */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            /**
			 * the method called when the JVM is shutdown.
			 * 
			 * @see java.lang.Thread#run()
			 */
            @Override
            public void run() {
                for (File file : FILES_TO_DELETE) {
                    deleteDirectory(file);
                }
            }
        });
    }

    /**
	 * returns the {@link ConfigurableClassLoader} associated to the current
	 * thread.
	 * 
	 * @return the {@link ConfigurableClassLoader} associated to the current
	 *         thread
	 */
    private static ConfigurableClassLoader getConfigurableClassLoader() {
        ConfigurableClassLoader configurableClassLoader = CONFIGURABLE_CLASS_LOADER.get();
        if (configurableClassLoader == null) {
            configurableClassLoader = new ConfigurableClassLoader(Thread.currentThread().getContextClassLoader());
            CONFIGURABLE_CLASS_LOADER.set(configurableClassLoader);
            Thread.currentThread().setContextClassLoader(configurableClassLoader);
        }
        return configurableClassLoader;
    }

    /**
	 * returns the current {@link ConfigurationBean}.
	 * 
	 * @return the current {@link ConfigurationBean}
	 * @throws IOException
	 *             if an error occured
	 * @throws SAXException
	 *             if an error occured
	 * @throws IntrospectionException
	 *             if an error occured
	 */
    public static ConfigurationBean getConfigurationBean() throws IOException, SAXException, IntrospectionException {
        File configurationFile = getConfigurationFile();
        if (!configurationFile.exists()) {
            return getNewConfigurationBean();
        }
        final ConfigurationBean configurationBean = (ConfigurationBean) getBeanReader().parse(configurationFile);
        configurationBean.setConfigurationDirectory(getConfigurationDirectory());
        if (configurationBean.getDatabaseConfigurationBean().getEncrypted()) {
            String pwd = configurationBean.getDatabaseConfigurationBean().getPassword();
            configurationBean.getDatabaseConfigurationBean().setPassword(new Base64().decrypt(pwd));
        }
        if (configurationBean.getWsInterceptorActive()) {
            PropertiesClass.setActiveWsInterceptor(Boolean.TRUE);
        }
        if (configurationBean.getSupressReadOnlyTransactions()) {
            PropertiesClass.setSupressReadOnlyTransactions(Boolean.TRUE);
        }
        return configurationBean;
    }

    /**
	 * returns the {@link InputStream} containing the spring configuration.
	 * 
	 * @return the {@link InputStream} containing the spring configuration
	 * @throws IOException
	 *             when an error occured
	 * @throws SAXException
	 *             when an error occured
	 * @throws IntrospectionException
	 *             when an error occured
	 */
    public static InputStream getSpringConfiguration() throws IOException, SAXException, IntrospectionException {
        final ConfigurationBean configurationBean = getConfigurationBean();
        initLog4j(configurationBean);
        final StringBuilder res = new StringBuilder();
        res.append("<?xml version=\"1.0\" encoding=\"").append(Charset.UTF8).append("\"?>");
        res.append("<beans>");
        addContentDirectoryConfiguration(configurationBean, res);
        addNounceConfiguration(configurationBean, res);
        addMailConfiguration(configurationBean, res);
        addHibernateConfiguration(configurationBean, res);
        addJcrConfiguration(configurationBean, res);
        res.append("</beans>");
        return new ByteArrayInputStream(res.toString().getBytes(Charset.UTF8));
    }

    /**
	 * initialize dynamic configuration of log4j.
	 * 
	 * @param configurationBean
	 *            the {@link ConfigurationBean} containing the base directory
	 * @throws IOException
	 *             if an error occured
	 */
    private static void initLog4j(final ConfigurationBean configurationBean) throws IOException {
        configurationBean.getConfigurationDirectory().mkdirs();
        final File log4jFile = new File(configurationBean.getConfigurationDirectory(), "log4j.xml");
        if (!log4jFile.exists()) {
            OutputStream out = new FileOutputStream(log4jFile);
            InputStream in = null;
            if (LOGGER.isErrorEnabled()) {
                in = ProcessConfiguration.class.getResourceAsStream(DEFAULT_LOG4J_CONFIGURATION_FILE);
            } else {
                in = ProcessConfiguration.class.getResourceAsStream(DEFAULT_OFF_LOG4J_CONFIGURATION_FILE);
            }
            byte[] buffer = new byte[2048];
            int i = in.read(buffer);
            while (i >= 0) {
                out.write(buffer, 0, i);
                i = in.read(buffer);
            }
            in.close();
            out.close();
        }
        Log4jConfigurer.initLogging(log4jFile.getCanonicalPath(), 10000);
    }

    /**
	 * release the cached datas.
	 */
    public static void releaseResources() {
        stringContainer = null;
        configurationDirectory = null;
        synchronized (BETWIXT_LOCK) {
            beanReader = null;
        }
    }

    /**
	 * save the given {@link ConfigurationBean} so that it will be reused next
	 * time.
	 * 
	 * @param configurationBean
	 *            the {@link ConfigurationBean} to save
	 * @throws IOException
	 *             if an error occured
	 * @throws SAXException
	 *             if an error occured
	 * @throws IntrospectionException
	 *             if an error occured
	 */
    public static void saveConfigurationBean(ConfigurationBean configurationBean) throws IOException, SAXException, IntrospectionException {
        getBeanWriter().write(configurationBean);
    }

    /**
	 * append the config that allows to access the content directory to the
	 * generated spring config file.
	 * 
	 * @param configurationBean
	 *            the bean containing the configuration
	 * @param appendable
	 *            the {@link Appendable} to write to
	 * @throws IOException
	 *             when an error occured
	 */
    private static void addContentDirectoryConfiguration(final ConfigurationBean configurationBean, final Appendable appendable) throws IOException {
        appendable.append("<bean id=\"contentDirectory\" class=\"java.io.File\" priority=\"10000\">");
        appendable.append("<constructor-arg><value>").append(configurationBean.getConfigurationDirectory().getCanonicalPath()).append("</value></constructor-arg>");
        appendable.append("</bean>");
    }

    /**
	 * append the hibernate config to the generated spring config file.
	 * 
	 * @param configurationBean
	 *            the bean containing the configuration
	 * @param appendable
	 *            the {@link Appendable} to write to
	 * @throws IOException
	 *             when an error occured
	 */
    private static void addHibernateConfiguration(final ConfigurationBean configurationBean, final Appendable appendable) throws IOException {
        if (!hasClass(HIBERNATE_PLUGIN_QUALIFIED_NAME)) {
            return;
        }
        addDataSourceConfiguration(configurationBean, appendable);
        appendable.append("<bean id=\"hibernateSessionFactory\" priority=\"10000\" parent=\"abstractHibernateSessionFactory\">");
        appendable.append("<property name=\"jtaTransactionManager\" ref=\"autoconfigurationJtaTransactionManager\"/>");
        appendable.append("<property name=\"dataSource\" ref=\"autoconfigurationDataSource\"/>");
        appendable.append("<property name=\"schemaUpdate\" value=\"").append(configurationBean.getDatabaseConfigurationBean().getSchemaUpdate().toString()).append("\"/>");
        Properties properties = new Properties();
        if (LOGGER.isTraceEnabled()) {
            properties.setProperty(Environment.SHOW_SQL, "true");
            properties.setProperty(Environment.FORMAT_SQL, "true");
            properties.setProperty(Environment.USE_SQL_COMMENTS, "true");
        }
        addHibernateCacheConfiguration(configurationBean, appendable, properties);
        appendPropertiesProperty(appendable, "hibernateProperties", properties, configurationBean);
        appendable.append("</bean>");
    }

    /**
	 * append a property of type {@link Properties} to the configuration of the
	 * current bean.
	 * 
	 * @param appendable
	 *            the {@link Appendable} to append to
	 * @param propertyName
	 *            the name of the property to add
	 * @param properties
	 *            the value of the property to add
	 * @throws IOException
	 *             if an error occured
	 */
    private static void appendPropertiesProperty(final Appendable appendable, final String propertyName, final Properties properties, final ConfigurationBean configurationBean) throws IOException {
        if (!properties.isEmpty()) {
            appendable.append("<property name=\"").append(propertyName).append("\"><props>");
            for (Entry<Object, Object> propertyEntry : properties.entrySet()) {
                appendable.append("<prop key=\"").append((String) propertyEntry.getKey()).append("\">").append((String) propertyEntry.getValue()).append("</prop>");
            }
            if (propertyName.equals("hibernateProperties") && !configurationBean.getHibernateSessionFactoryProperties().equals("")) {
                appendable.append(configurationBean.getHibernateSessionFactoryProperties().toString());
            }
            appendable.append("</props></property>");
        }
    }

    /**
	 * Add the configuration of the hibernate cache.
	 * 
	 * @param configurationBean
	 *            the bean containing the configuration
	 * @param appendable
	 *            the {@link Appendable} to write to
	 * @param properties
	 *            the place to put the properties to add to the hibernate
	 *            configuration
	 * @throws IOException
	 *             when an error occured
	 */
    private static void addHibernateCacheConfiguration(final ConfigurationBean configurationBean, final Appendable appendable, final Properties properties) throws IOException {
        boolean activateSecondLevelCache = configurationBean.getDatabaseConfigurationBean().getActivateSecondLevelCache();
        properties.put(Environment.USE_SECOND_LEVEL_CACHE, String.valueOf(activateSecondLevelCache));
        if (!activateSecondLevelCache) {
            return;
        }
        Properties osCacheProperties = new Properties();
        osCacheProperties.load(ProcessConfiguration.class.getResourceAsStream(OS_CACHE_CONFIGIRATION));
        osCacheProperties.setProperty(OS_CACHE_CACHE_PATH, createTemporaryDirectory().getAbsolutePath());
        File configurationFile = File.createTempFile("oscache", ".properties");
        FILES_TO_DELETE.add(configurationFile);
        osCacheProperties.store(new FileOutputStream(configurationFile), null);
        getConfigurableClassLoader().addResource(OS_CACHE_CONFIGIRATION, configurationFile);
        appendable.append("<property name=\"cacheProvider\"><bean class=\"").append(com.opensymphony.oscache.hibernate.OSCacheProvider.class.getName()).append("\"/></property>");
        properties.put(com.opensymphony.oscache.hibernate.OSCacheProvider.OSCACHE_CONFIGURATION_RESOURCE_NAME, OS_CACHE_CONFIGIRATION);
    }

    /**
	 * returns the {@link DatabasePoolConfigurator} associated to the giben
	 * {@link DatabaseConfigurationBean}.
	 * 
	 * @param databaseConfigurationBean
	 *            the description of the database connection
	 * @return the {@link DatabasePoolConfigurator} associated to the giben
	 *         {@link DatabaseConfigurationBean}
	 */
    private static DatabasePoolConfigurator getDatabasePoolConfigurator(DatabaseConfigurationBean databaseConfigurationBean) {
        DatabasePoolConfigurator res = null;
        for (DatabasePoolConfigurator databasePoolConfigurator : DatabasePoolConfigurator.values()) {
            if (databasePoolConfigurator.canHandle(databaseConfigurationBean)) {
                res = databasePoolConfigurator;
            }
        }
        return res;
    }

    /**
	 * append the datasource config to the generated spring config file.
	 * 
	 * @param configurationBean
	 *            the bean containing the configuration
	 * @param appendable
	 *            the {@link Appendable} to write to
	 * @throws IOException
	 *             when an error occured
	 */
    private static void addDataSourceConfiguration(final ConfigurationBean configurationBean, final Appendable appendable) throws IOException {
        appendable.append("<bean id=\"autoconfigurationDataSource\" class=\"org.jencks.factory.ConnectionFactoryFactoryBean\">");
        appendable.append("<property name=\"managedConnectionFactory\">");
        getDatabasePoolConfigurator(configurationBean.getDatabaseConfigurationBean()).appendSpringDeclaration(configurationBean.getDatabaseConfigurationBean(), appendable);
        appendable.append("</property>");
        appendable.append("<property name=\"connectionManager\">");
        addPoolConfiguration(configurationBean, appendable);
        appendable.append("</property>");
        appendable.append("</bean>");
    }

    /**
	 * append the JCR config to the generated spring config file.
	 * 
	 * @param configurationBean
	 *            the bean containing the configuration
	 * @param appendable
	 *            the {@link Appendable} to write to
	 * @throws IOException
	 *             when an error occured
	 */
    private static void addJcrConfiguration(final ConfigurationBean configurationBean, final Appendable appendable) throws IOException {
        if (!hasClass(JCR_PLUGIN_QUALIFIED_NAME)) {
            return;
        }
        File jcrHome = new File(configurationBean.getConfigurationDirectory(), JCR_DIRECTORY);
        System.setProperty("derby.stream.error.file", new File(configurationBean.getConfigurationDirectory(), "derby.log").getAbsolutePath());
        appendable.append("<bean id=\"jcrRepository\" priority=\"10000\"  class=\"org.springframework.jca.support.LocalConnectionFactoryBean\">");
        appendable.append("<property name=\"managedConnectionFactory\">");
        appendable.append("<bean class=\"org.apache.jackrabbit.jca.JCAManagedConnectionFactory\">");
        appendable.append("<property name=\"configFile\">");
        appendable.append("<bean class=\"com.genia.toolbox.spring.factory.FilePathFromClassPath\">");
        appendable.append("<property name=\"classPathFile\" value=\"/com/genia/toolbox/config/autoconfiguration/plugin/repository.xml\" />");
        appendable.append("<property name=\"streamManager\" ref=\"streamManager\" />");
        appendable.append("<property name=\"fileManager\" ref=\"fileManager\" />");
        appendable.append("</bean></property>");
        appendable.append("<property name=\"homeDir\" value=\"").append(jcrHome.getCanonicalPath()).append("\" />");
        appendable.append("</bean></property>");
        appendable.append("<property name=\"connectionManager\">");
        addPoolConfiguration(configurationBean, appendable);
        appendable.append("</property>");
        appendable.append("</bean>");
    }

    /**
	 * append the nounce config to the generated spring config file.
	 * 
	 * @param configurationBean
	 *            the bean containing the configuration
	 * @param appendable
	 *            the {@link Appendable} to write to
	 * @throws IOException
	 *             when an error occured
	 */
    private static void addNounceConfiguration(final ConfigurationBean configurationBean, final Appendable appendable) throws IOException {
        appendable.append("<bean priority=\"10000\" name=\"nounce\" class=\"java.lang.String\">");
        appendable.append("<constructor-arg value=\"").append(configurationBean.getNounce()).append("\"/>");
        appendable.append("</bean>");
    }

    /**
	 * append the mail config to the generated spring config file.
	 * 
	 * @param configurationBean
	 *            the bean containing the configuration
	 * @param appendable
	 *            the {@link Appendable} to write to
	 * @throws IOException
	 *             when an error occured
	 */
    private static void addMailConfiguration(final ConfigurationBean configurationBean, final Appendable appendable) throws IOException {
        Properties properties = new Properties();
        if (LOGGER.isDebugEnabled()) {
            properties.setProperty("mail.debug", "true");
        }
        appendable.append("<bean priority=\"10000\" name=\"javaMailSender\" class=\"org.springframework.mail.javamail.JavaMailSenderImpl\">");
        appendable.append("<property name=\"defaultEncoding\" value=\"").append(Charset.UTF8).append("\"/>");
        appendPropertiesProperty(appendable, "javaMailProperties", properties, configurationBean);
        if (configurationBean.getMailConfigurationBean().getMailSessionJNDIName() == null) {
            appendable.append("<property name=\"host\" value=\"").append(configurationBean.getMailConfigurationBean().getMailServer()).append("\" />");
        } else {
            appendable.append("<property name=\"session\">");
            appendable.append("<jee:jndi-lookup jndi-name=\"").append(configurationBean.getMailConfigurationBean().getMailSessionJNDIName()).append("\" lookup-on-startup=\"false\" proxy-interface=\"javax.mail.Session\" />");
            appendable.append("</property>");
        }
        appendable.append("</bean>");
    }

    /**
	 * append the JCA pool config to the generated spring config file.
	 * 
	 * @param configurationBean
	 *            the bean containing the configuration
	 * @param appendable
	 *            the {@link Appendable} to write to
	 * @throws IOException
	 *             when an error occured
	 */
    private static void addPoolConfiguration(final ConfigurationBean configurationBean, final Appendable appendable) throws IOException {
        appendable.append("<bean class=\"org.jencks.factory.ConnectionManagerFactoryBean\">");
        appendable.append("<property name=\"poolMaxSize\" value=\"").append(String.valueOf(configurationBean.getPoolMaxSize())).append("\" />");
        appendable.append("<property name=\"transaction\" value=\"xa\" />");
        appendable.append("<property name=\"transactionManager\" ref=\"autoconfigurationJtaTransactionManager\" />");
        appendable.append("</bean>");
    }

    /**
	 * returns a new temporary directory.
	 * 
	 * @return a new temporary directory
	 * @throws IOException
	 *             if an error occured
	 */
    private static File createTemporaryDirectory() throws IOException {
        final File temporaryDirectory = File.createTempFile("configuration", ".trampoline");
        temporaryDirectory.delete();
        temporaryDirectory.mkdirs();
        FILES_TO_DELETE.add(temporaryDirectory);
        return temporaryDirectory;
    }

    /**
	 * helper method the delete a directory recursively.
	 * 
	 * @param directory
	 *            the directory to delete
	 */
    private static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }

    /**
	 * returns the {@link BeanReader} used for reading the configuration file.
	 * 
	 * @return the {@link BeanReader} used for reading the configuration file
	 * @throws IntrospectionException
	 *             if an error occured
	 * @throws IOException
	 *             if an error occured
	 * @throws SAXException
	 *             if an error occured
	 */
    private static BeanReader getBeanReader() throws IntrospectionException, IOException, SAXException {
        synchronized (BETWIXT_LOCK) {
            if (beanReader == null) {
                beanReader = new BeanReader();
                beanReader.registerMultiMapping(new InputSource(ProcessConfiguration.class.getResourceAsStream(BETWIXT_CONFIGURATION_FILE)));
                beanReader.getBindingConfiguration().setMapIDs(false);
            }
        }
        return beanReader;
    }

    /**
	 * returns a {@link BeanWriter} used to write the configuration to an xml
	 * file.
	 * 
	 * @return a {@link BeanWriter} used to write the configuration to an xml
	 *         file
	 * @throws IOException
	 *             if an error occured
	 * @throws IntrospectionException
	 *             if an error occured
	 * @throws SAXException
	 *             if an error occured
	 */
    private static BeanWriter getBeanWriter() throws IOException, IntrospectionException, SAXException {
        BeanWriter beanWriter = new BeanWriter(new FileOutputStream(getConfigurationFile()));
        beanWriter.setBindingConfiguration(getBeanReader().getBindingConfiguration());
        beanWriter.setXMLIntrospector(getBeanReader().getXMLIntrospector());
        beanWriter.setWriteEmptyElements(false);
        return beanWriter;
    }

    /**
	 * returns the directory that will contain all the configuration.
	 * 
	 * @return the directory that will contain all the configuration
	 * @throws IOException
	 *             if an error occured
	 */
    private static File getConfigurationDirectory() throws IOException {
        if (configurationDirectory == null) {
            StringContainer stringContainer = getStringContainer();
            if ((null != stringContainer) && (null != stringContainer.getValues().get(CONFIGURATION_DIRECTORY))) {
                configurationDirectory = new File(stringContainer.getValues().get(CONFIGURATION_DIRECTORY));
            }
        }
        if (configurationDirectory == null) {
            DataSource dataSource = getStringContainerDataSource();
            if (dataSource != null) {
                Connection connection = null;
                try {
                    connection = dataSource.getConnection();
                    configurationDirectory = new File(connection.nativeSQL(CONFIGURATION_DIRECTORY));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (configurationDirectory == null) {
            configurationDirectory = createTemporaryDirectory();
        }
        configurationDirectory.mkdirs();
        if (!configurationDirectory.exists()) {
            throw new IOException("Unable to create configuration directory: " + configurationDirectory.getCanonicalPath());
        }
        return configurationDirectory;
    }

    /**
	 * returns the configuration file to use.
	 * 
	 * @return the configuration file to use
	 * @throws IOException
	 *             if an error occured
	 */
    private static File getConfigurationFile() throws IOException {
        return new File(getConfigurationDirectory(), CONFIGURATION_FILE_NAME);
    }

    /**
	 * returns a new default {@link ConfigurationBean}.
	 * 
	 * @return a new default {@link ConfigurationBean}
	 * @throws IOException
	 *             if an error occured
	 * @throws SAXException
	 *             if an error occured
	 * @throws IntrospectionException
	 *             if an error occured
	 */
    private static ConfigurationBean getNewConfigurationBean() throws IOException, SAXException, IntrospectionException {
        final ConfigurationBean configurationBean = new ConfigurationBean();
        saveConfigurationBean(configurationBean);
        configurationBean.setConfigurationDirectory(getConfigurationDirectory());
        return configurationBean;
    }

    /**
	 * returns a fake {@link DataSource} that allows to retrieve the
	 * configuration informations.
	 * 
	 * @return a fake {@link DataSource} that allows to retrieve the
	 *         configuration informations
	 */
    private static DataSource getStringContainerDataSource() {
        if (stringContainerDataSource == null) {
            try {
                Context ctx = new InitialContext();
                Context envContext = (Context) ctx.lookup(JNDI_CONTEXT);
                stringContainerDataSource = (DataSource) envContext.lookup(JNDI_DATASOURCE_STRING_CONTAINER_NAME);
            } catch (Exception e) {
            }
            if (stringContainerDataSource == null) {
                try {
                    Context ctx = new InitialContext();
                    stringContainerDataSource = (DataSource) ctx.lookup(JNDI_DATASOURCE_STRING_CONTAINER_NAME);
                } catch (Exception e) {
                }
            }
        }
        return stringContainerDataSource;
    }

    /**
	 * returns the {@link StringContainer} from JNDI containing the name of the
	 * directory that will contain all the configuration.
	 * 
	 * @return the {@link StringContainer} from JNDI containing the name of the
	 *         directory that will contain all the configuration
	 */
    private static StringContainer getStringContainer() {
        if (stringContainer == null) {
            try {
                Context ctx = new InitialContext();
                Context envContext = (Context) ctx.lookup(JNDI_CONTEXT);
                stringContainer = (StringContainer) envContext.lookup(JNDI_STRING_CONTAINER_NAME);
            } catch (Exception e) {
            }
            if (stringContainer == null) {
                try {
                    Context ctx = new InitialContext();
                    stringContainer = (StringContainer) ctx.lookup(JNDI_STRING_CONTAINER_NAME);
                } catch (Exception e) {
                }
            }
        }
        return stringContainer;
    }

    /**
	 * returns whether the class which qualified name is given in parameter is
	 * in the classpath.
	 * 
	 * @param className
	 *            the qualified name of the class to search
	 * @return <code>true</code> if and only if the class which qualified name
	 *         is given in parameter is in the classpath
	 */
    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
        }
        return false;
    }
}
