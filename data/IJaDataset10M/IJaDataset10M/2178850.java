package de.sicari.webservice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import javax.security.auth.Subject;
import org.apache.axis.AxisEngine;
import org.apache.axis.AxisFault;
import org.apache.axis.AxisProperties;
import org.apache.axis.MessageContext;
import org.apache.axis.client.AdminClient;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.XMLStringProvider;
import org.apache.axis.deployment.wsdd.WSDDConstants;
import org.apache.axis.deployment.wsdd.WSDDProvider;
import org.apache.axis.server.AxisServer;
import org.apache.axis.utils.Admin;
import org.apache.axis.utils.Options;
import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;
import de.sicari.util.StringSubstitutionInputStream;
import de.fhg.igd.logging.LogLevel;
import de.fhg.igd.logging.Logger;
import de.fhg.igd.logging.LoggerFactory;
import de.sicari.web.bin.ServletLauncher;
import de.sicari.net.Server;
import de.sicari.authentication.SecurityTokenHandler;
import de.sicari.kernel.Environment;
import de.sicari.kernel.SicariSecurityManager;
import de.sicari.service.AbstractService;
import de.sicari.kernel.Lookup;
import de.sicari.kernel.security.UID;
import de.sicari.kernel.security.UserHistory;
import de.sicari.web.HttpServer;
import de.sicari.web.HttpsServer;
import de.sicari.webservice.servlet.AxisMasterServlet;
import de.sicari.util.ArgsParser;
import de.sicari.util.ArgsParserException;
import de.sicari.util.BadArgsException;
import de.sicari.util.ClassResource;
import de.sicari.util.NoSuchObjectException;
import de.sicari.util.ObjectExistsException;

/**
 * The <code>WebserviceServiceImpl</code> class is a bridge between the
 * <i>SicAri</i> platform and the <i>Apache Axis</i> framework.
 * It is intended to be used exclusively by means of its command line interface.
 * <p>Detailed usage information is available via
 * <p><nobreak><tt>java WebserviceServiceImpl -help</tt></nobreak>
 *
 * @author Matthias Pressfreund
 * @author Jan Peters
 * @author Dennis Bartussek
 * @version "$Id: WebserviceServiceImpl.java 336 2007-09-10 14:09:57Z jp-sfnet $"
 */
public class WebserviceServiceImpl extends AbstractService implements WebserviceService {

    /**
     * The <code>Logger</code> instance for this class
     */
    private static Logger log_ = LoggerFactory.getLogger("webservice");

    /**
     * The local synchronization object
     */
    private final Object lock_ = new Object();

    /**
     * The <tt>-publish</tt> command line parameter
     */
    protected static final String CMDLINE_PUBLICATION_ = "publish";

    /**
     * The <tt>-retract</tt> command line parameter
     */
    protected static final String CMDLINE_RETRACTION_ = "retract";

    /**
     * The <tt>-axisbase</tt> command line parameter
     */
    protected static final String CMDLINE_AXISBASEDIR_ = "axisbase";

    /**
     * The <tt>-axisclientconf</tt> command line parameter
     */
    protected static final String CMDLINE_AXISCLIENTCONF_ = "axisclientconf";

    /**
     * The <tt>-axisurl</tt> command line parameter
     */
    protected static final String CMDLINE_AXISURL_ = "axisurl";

    /**
     * The <tt>-clean</tt> command line parameter
     */
    protected static final String CMDLINE_CLEAN_ = "clean";

    /**
     * The <tt>-https</tt> command line parameter
     */
    protected static final String CMDLINE_HTTPS_ = "https";

    /**
     * The <tt>-http</tt> command line parameter
     */
    protected static final String CMDLINE_HTTP_ = "http";

    /**
     * The <tt>-help</tt> command line parameter
     */
    protected static final String CMDLINE_HELP_ = "help";

    /**
     * The {@link ArgsParser} command line parameter descriptor
     */
    protected static final String DESCR_ = CMDLINE_PUBLICATION_ + ":!," + CMDLINE_RETRACTION_ + ":!," + CMDLINE_AXISBASEDIR_ + ":F," + CMDLINE_AXISCLIENTCONF_ + ":F," + CMDLINE_AXISURL_ + ":s," + CMDLINE_CLEAN_ + ":!," + CMDLINE_HTTPS_ + ":!," + CMDLINE_HTTP_ + ":!," + CMDLINE_HELP_ + ":!";

    /**
     * The {@link de.sicari.util.WhatIs} key of the HTTPS server
     */
    protected static final String HTTPS_WHATIS_ = HttpsServer.WHATIS;

    /**
     * The {@link de.sicari.util.WhatIs} key of the HTTP server
     */
    protected static final String HTTP_WHATIS_ = HttpServer.WHATIS;

    /**
     * The path to the properties resource file
     */
    protected static final String PROPERTIES_RESOURCE_ = "webservice.properties";

    /**
     * The name of the <i>deployment descriptor template</i> property
     */
    protected static final String PROPERTY_DEPLOY_WSDD_TEMPLATE_ = "deploy.wsdd.template";

    /**
     * The name of the <i>undeployment descriptor template</i> property
     */
    protected static final String PROPERTY_UNDEPLOY_WSDD_TEMPLATE_ = "undeploy.wsdd.template";

    /**
     * The name substitute in the descriptor templates
     */
    protected static final String SUBST_NAME_ = "${name}";

    /**
     * The provider substitute in the descriptor templates
     */
    protected static final String SUBST_PROVIDER_ = "${provider}";

    /**
     * The class name substitute in the descriptor templates
     */
    protected static final String SUBST_CLASSNAME_ = "${className}";

    /**
     * The class name substitute in the descriptor templates
     */
    protected static final String SUBST_ALLOWEDMETHODS_ = "${allowedMethods}";

    /**
     * The scope substitute in the descriptor templates
     */
    protected static final String SUBST_SCOPE_ = "${scope}";

    /**
     * The default <i>Axis</i> URL path
     */
    protected static final String DEFAULT_AXIS_URL_ = "axis";

    /**
     * The services URL path (according to definitions in
     * <tt>WEB-INF/web.xml</tt>, required for {@link #availableBaseURLs()})
     */
    protected static final String SERVICES_PATH_ = "services";

    /**
     * The {@link AdminClient} return value after successful processing
     */
    protected static final String ADMIN_CLIENT_SUCCESS_ = "<Admin>Done processing</Admin>";

    /**
     * The name of the <i>Axis</i> administration service
     */
    protected static final String ADMIN_SERVICE_NAME_ = "AdminService";

    /**
     * The AXIS server configuration file name.
     */
    protected static final String AXIS_SERVER_CONFIG_ = "WEB-INF/server-config.wsdd";

    /**
     * The AXIS client configuration file name.
     */
    protected static final String AXIS_CLIENT_CONFIG_ = "client-config.wsdd";

    /**
     * The AXIS-internal log4j log level to be set.
     */
    protected static final String AXIS_LOG4J_LOGLEVEL = "OFF";

    /**
     * The deployment webservice descriptor template
     */
    protected static String deploy_wsdd_template_;

    /**
     * The undeployment webservice descriptor template
     */
    protected static String undeploy_wsdd_template_;

    /**
     * The <i>Axis</i> base directory
     * (required for {@link javax.servlet.ServletContext#getRealPath(String)})
     */
    protected File axisbase_;

    /**
     * The <i>Axis</i> URL (e.g. '<tt>axis</tt>')
     */
    protected String axisurl_;

    /**
     * The <i>Axis</i> client configuration file
     */
    protected File axisclientconf_;

    /**
     * The container for imported service objects
     */
    protected Imports imports_;

    /**
     * The <code>Set</code> contains the names of hidden services
     * which are invisible for users
     */
    protected static Set<String> hiddenServices_;

    /**
     * This <code>Map</code> contains forbidden combinations of method name
     * and parameter signatures in <code>Webservice</code> interfaces
     * (currently all methods of <code>java.lang.Object</code>)
     */
    protected static Map<String, List<Class<?>>> forbiddenMethods_;

    /**
     * This <code>Set</code> contains the classes which are allowed as
     * webservice parameters and return types in addition to primitive
     * Java types
     */
    protected static Set<Class<?>> allowedTypes_;

    /**
     * A hidden reference to the currently published instance
     */
    static WebserviceServiceImpl ref_ = null;

    /**
     * A hidden reference to the proxy instance currently published
     * in the <code>Environment</code>
     */
    static Object proxy_ = null;

    static {
        Method[] methods;
        InputStream is;
        Properties log4jConfig;
        Properties prp;
        int i;
        log4jConfig = new Properties();
        log4jConfig.setProperty("log4j.logger.org.apache.axis", AXIS_LOG4J_LOGLEVEL + ", CONSOLE");
        log4jConfig.setProperty("log4j.logger.org.apache.ws", AXIS_LOG4J_LOGLEVEL + ", CONSOLE");
        log4jConfig.setProperty("log4j.logger.org.apache.xml", AXIS_LOG4J_LOGLEVEL + ", CONSOLE");
        log4jConfig.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
        log4jConfig.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
        PropertyConfigurator.configure(log4jConfig);
        WSDDProvider.registerProvider(new javax.xml.namespace.QName(WSDDConstants.URI_WSDD_JAVA, WSDDJavaSicariProvider.PROVIDER_SICARI), new WSDDJavaSicariProvider());
        log_.debug("Registered " + WSDDJavaSicariProvider.class.getName());
        AxisProperties.setProperty("axis.EngineConfigFactory", "de.sicari.webservice.SicariEngineConfigurationFactory");
        log_.debug("Registered " + SicariEngineConfigurationFactory.class.getName());
        AxisProperties.setProperty("org.apache.axis.components.net.SecureSocketFactory", "de.sicari.webservice.SicariSecureSocketFactory");
        log_.debug("Registered " + SicariSecureSocketFactory.class.getName());
        hiddenServices_ = new HashSet<String>(Arrays.asList(new String[] { ADMIN_SERVICE_NAME_ }));
        log_.debug(hiddenServices_.isEmpty() ? "Disabled webservice hideout" : "Enabled webservice hideout for " + hiddenServices_);
        forbiddenMethods_ = new HashMap<String, List<Class<?>>>();
        methods = Object.class.getDeclaredMethods();
        for (i = 0; i < methods.length; i++) {
            forbiddenMethods_.put(methods[i].getName(), Arrays.asList(methods[i].getParameterTypes()));
        }
        allowedTypes_ = new HashSet<Class<?>>(Arrays.asList(new Class<?>[] { java.lang.Byte.class, java.lang.Double.class, java.lang.Float.class, java.lang.Integer.class, java.lang.Long.class, java.lang.Short.class, java.lang.String.class, java.math.BigDecimal.class, java.math.BigInteger.class, java.util.Calendar.class, java.util.HashMap.class, java.util.Map.class, java.util.Vector.class }));
        prp = new Properties();
        is = ClassResource.getRelativeResourceAsStream(WebserviceServiceImpl.class, PROPERTIES_RESOURCE_);
        try {
            prp.load(is);
            deploy_wsdd_template_ = prp.getProperty(PROPERTY_DEPLOY_WSDD_TEMPLATE_);
            log_.trace("(deploy.wsdd.template): " + deploy_wsdd_template_);
            if (deploy_wsdd_template_ == null) {
                throw new NoSuchElementException("deploy.wsdd.template");
            }
            undeploy_wsdd_template_ = prp.getProperty(PROPERTY_UNDEPLOY_WSDD_TEMPLATE_);
            log_.trace("(undeploy.wsdd.template): " + undeploy_wsdd_template_);
            if (undeploy_wsdd_template_ == null) {
                throw new NoSuchElementException("undeploy.wsdd.template");
            }
        } catch (Exception e) {
            log_.caught(LogLevel.SEVERE, "Initialization failed", e);
            throw new MissingResourceException("Invalid or missing properties resource", PROPERTIES_RESOURCE_, e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log_.caught(LogLevel.ERROR, "Closing InputStream failed", e);
                }
            }
        }
    }

    /**
     * This constructor is hidden since access is limited to
     * the command line interface.
     *
     * @param axisbase The <i>Axis</i> base directory (required for
     *   {@link javax.servlet.ServletContext#getRealPath(String)})
     * @param axisclientconf The <i>Axis</i> client configuration file;
     *   if <code>null</code>, <code>&lt;axisbase&gt;/</code>{@link #AXIS_CLIENT_CONFIG_}
     *   will be used
     * @param axisurl The URL under which the
     *   {@link AxisMasterServlet} should be published;
     *   if <code>null</code>, {@link #DEFAULT_AXIS_URL_} will be used
     *
     * @throws IllegalArgumentException
     *   if at least one of the specified parameters is invalid
     */
    private WebserviceServiceImpl(File axisbase, File axisclientconf, String axisurl) {
        super();
        String msg;
        axisbase_ = axisbase;
        axisurl_ = axisurl;
        if (axisbase_ == null || !axisbase_.isDirectory()) {
            msg = "Invalid Axis base directory: " + axisbase_;
            log_.error(msg);
            throw new IllegalArgumentException(msg);
        }
        if (axisurl_ == null) {
            axisurl_ = DEFAULT_AXIS_URL_;
            log_.debug("No Axis URL specified, using default: " + axisurl_);
        }
        if (axisurl_.startsWith("/")) {
            axisurl_ = axisurl_.substring(1);
        }
        if (axisclientconf == null || axisclientconf.getParentFile() == null || !axisclientconf.getParentFile().canRead()) {
            log_.debug("Given AXIS client configuration file invalid: " + axisclientconf);
            axisclientconf_ = new File(axisbase, AXIS_CLIENT_CONFIG_);
        } else {
            axisclientconf_ = axisclientconf;
        }
        AxisProperties.setProperty("axis.ClientConfigFile", axisclientconf_.getAbsolutePath());
        log_.debug("AXIS client configuration file defined: " + axisclientconf_);
        imports_ = new Imports();
        log_.debug("Instance (" + axisbase_ + "," + axisclientconf + "," + axisurl_ + ") successfully created");
    }

    @Override
    public String info() {
        return ("The WebserviceService provides webservice functionality" + " by means of the Apache Axis framework.");
    }

    @Override
    public String author() {
        return "Matthias Pressfreund and Jan Peters";
    }

    @Override
    public String revision() {
        return "$Revision: 336 $";
    }

    protected static Document getConfiguration(AxisEngine engine) throws AxisException {
        Document configuration;
        String failMessage;
        try {
            configuration = Admin.listConfig(engine);
        } catch (AxisFault ex) {
            failMessage = "Transforming AxisEngine into an XML document failed";
            log_.error(failMessage);
            throw new AxisException(failMessage, ex);
        }
        return configuration;
    }

    protected AxisEngine getClientEngine() {
        AxisEngine engine;
        engine = new AxisClient();
        engine.setShouldSaveConfig(true);
        engine.init();
        return engine;
    }

    protected AxisEngine getServerEngine() throws AxisException {
        AdminClient adminClient;
        AxisEngine engine;
        Options options;
        String failmsg;
        try {
            options = new Options(new String[0]);
            options.setDefaultURL(currentBaseURL());
            adminClient = new AdminClient();
            adminClient.processOpts(options);
            engine = new AxisServer(new XMLStringProvider(adminClient.list()));
        } catch (Exception ex) {
            failmsg = "reading server engine failed";
            log_.caught(LogLevel.ERROR, failmsg, ex);
            throw new AxisException(failmsg, ex);
        }
        return engine;
    }

    /**
     * Lookup the <code>Server</code> instance published for the specified
     * protocol.
     *
     * @param whatisProtocolKey The {@link de.sicari.util.WhatIs} key
     *   of the requested protocol
     *
     * @return The <code>Server</code> or <code>null</code> if no appropriate
     *   object was found
     */
    protected Server lookupServer(String whatisProtocolKey) {
        return Lookup.environmentWhatIs(whatisProtocolKey, Server.class);
    }

    /**
     * Lookup the <code>AxisMasterServlet</code> instance published
     * for the specified protocol.
     *
     * @param whatisProtocolKey The {@link de.sicari.util.WhatIs} key
     *   of the requested protocol
     *
     * @return The <code>AxisMasterServlet</code> or <code>null</code>
     *   if no appropriate object was found
     */
    protected AxisMasterServlet lookupAxisMasterServlet(String whatisProtocolKey) {
        return Lookup.environment(Lookup.whatIs(whatisProtocolKey) + "/" + axisurl_, AxisMasterServlet.class);
    }

    /**
     * Check the validity of the given webservice name. Valid names are
     * not empty and not occupied by {@link #hiddenServices_ hidden services}.
     *
     * @param webserviceName The webservice name to check
     *
     * @return The trimmed webservice name
     *
     * @throws IllegalArgumentException
     *   if the webservice name is invalid
     */
    protected static String checkName(String webserviceName) throws IllegalArgumentException {
        String name;
        String msg;
        name = webserviceName;
        if (name == null) {
            msg = "Webservice name is null";
        } else if ((name = name.trim()).length() == 0) {
            msg = "Webservice name is empty";
        } else if (hiddenServices_.contains(name)) {
            msg = "'" + webserviceName + "' is an invalid webservice name";
        } else {
            msg = null;
        }
        if (msg != null) {
            log_.error(msg);
            throw new IllegalArgumentException(msg);
        }
        return name;
    }

    /**
     * Make sure the given webservice name is currently not in use.
     *
     * @param webserviceName The webservice name to check
     * @param axisinfo The current service info
     *
     * @throws ObjectExistsException
     *   if a webservice with the given name already exists
     */
    protected static void checkNameVacancy(String webserviceName, AxisServiceInfo axisinfo) throws ObjectExistsException {
        String msg;
        if (axisinfo.getEntry(webserviceName) != null) {
            msg = "Webservice '" + webserviceName + "' already exists";
            log_.error(msg);
            throw new ObjectExistsException(msg);
        }
    }

    /**
     * Check if the given webservice name exists.
     *
     * @param webserviceName The webservice name to check
     * @param axisinfo The current service info
     *
     * @throws NoSuchObjectException
     *   if there is no webservice with the given name
     */
    protected static void checkNameExistence(String webserviceName, AxisServiceInfo axisinfo) throws NoSuchObjectException {
        String msg;
        if (axisinfo.getEntry(webserviceName) == null) {
            msg = "Webservice '" + webserviceName + "' does not exist";
            log_.error(msg);
            throw new NoSuchObjectException(msg);
        }
    }

    /**
     * Resolve the given webservice class name.
     *
     * @param webserviceClass The webservice class name
     *
     * @return The corresponding <code>Class</code>
     *
     * @throws IllegalArgumentException
     *   if the webservice class name is invalid
     */
    protected Class<?> resolveClass(String webserviceClass) {
        String msg;
        if (webserviceClass == null) {
            log_.error(msg = "Webservice class is null");
            throw new IllegalArgumentException(msg);
        }
        try {
            return Class.forName(webserviceClass);
        } catch (ClassNotFoundException e) {
            try {
                return imports_.findClass(webserviceClass);
            } catch (ClassNotFoundException e2) {
                msg = "Unresolvable webservice class '" + webserviceClass + "'";
                log_.error(msg);
                throw new IllegalArgumentException(msg);
            }
        }
    }

    /**
     * Check if the given class implements all specified interfaces.
     *
     * @param clazz The class to check
     * @param interfaces The interfaces required to be implemented by
     *   the given class
     *
     * @throws IllegalArgumentException
     *   <ul>
     *   <li>if not all specified classes are interfaces
     *   <li>if the given class does not implement all specified interfaces
     *   </ul>
     */
    @SuppressWarnings("unchecked")
    protected static void checkImplementation(Class<?> clazz, Class<?>[] interfaces) {
        List<Class> iflist;
        String msg;
        if (interfaces == null) {
            log_.error(msg = "Interfaces are null");
            throw new IllegalArgumentException(msg);
        }
        for (Class<?> ifclass : interfaces) {
            if (ifclass == null) {
                msg = "Interfaces contains null";
            } else if (!ifclass.isInterface()) {
                msg = "'" + ifclass + "' is not an interface";
            } else {
                msg = null;
            }
            if (msg != null) {
                log_.error(msg);
                throw new IllegalArgumentException(msg);
            }
        }
        iflist = Arrays.asList(clazz.getInterfaces());
        for (Class<?> ifclass : interfaces) {
            if (!iflist.contains(ifclass)) {
                msg = "'" + ifclass.getName() + "' is not implemented by " + clazz.getName();
                log_.error(msg);
                throw new IllegalArgumentException(msg);
            }
        }
    }

    /**
     * Check the given interfaces for valid methods and types.
     * Valid methods are <em>not</em> listed in {@link #forbiddenMethods_}.
     * Valid types are primitives and all types listed in
     * {@link #allowedTypes_}, as well as their corresponding array types.
     *
     * @param interfaces The interface classes to check
     *
     * @throws IllegalArgumentException
     *   if one or more methods or types are invalid
     */
    protected static void checkInterfaces(Class<?>[] interfaces) {
        List<Class<?>> forbiddenTypes;
        List<Class<?>> types;
        Method[] methods;
        String msg;
        int i;
        for (i = 0; i < interfaces.length; i++) {
            methods = interfaces[i].getMethods();
            for (Method method : methods) {
                types = new ArrayList<Class<?>>(Arrays.asList(method.getParameterTypes()));
                forbiddenTypes = forbiddenMethods_.get(method.getName());
                if ((forbiddenTypes != null) && forbiddenTypes.equals(types)) {
                    msg = "Invalid interface " + interfaces[i].getName() + " (Method '" + method + "' must not be defined)";
                    log_.error(msg);
                    throw new IllegalArgumentException(msg);
                }
                types.add(method.getReturnType());
                for (Class<?> clazz : types) {
                    while (clazz.isArray()) {
                        clazz = clazz.getComponentType();
                    }
                    if (!clazz.isPrimitive() && !allowedTypes_.contains(clazz)) {
                        msg = "Invalid interface " + interfaces[i].getName() + " (Return or parameter type in method " + method + " is not supported)";
                        log_.error(msg);
                        throw new IllegalArgumentException(msg);
                    }
                }
            }
        }
    }

    /**
     * Check if no other webservice object of the same type as the
     * given object has been imported before.
     *
     * @param webservice The webservice to check
     *
     * @throws ObjectExistsException
     *   if another webservice import of the same type already exists
     */
    protected void checkImports(Object webservice) throws ObjectExistsException {
        Class<?> clazz;
        Object obj;
        String msg;
        clazz = webservice.getClass();
        obj = imports_.get(clazz);
        if (obj != null) {
            msg = "Deployment failed since another Webservice instance of type " + clazz.getName() + " already exists";
            log_.error(msg + ": " + obj);
            throw new ObjectExistsException(msg);
        }
    }

    public void deployClientHandler(HandlerDescriptor handler) throws AxisException, ObjectExistsException {
        String failMessage;
        String wsdd;
        WebservicePermission.checkPermission(handler.getPath(), "deployHandler");
        failMessage = "Failed to deploy handler '" + handler + "'";
        wsdd = handler.insertInto(getClientEngine());
        if (wsdd != null) {
            processDescriptor(wsdd, Collections.EMPTY_MAP, failMessage);
            log_.info("Successfully deployed handler '" + handler + "'");
        } else {
            log_.info(failMessage);
        }
    }

    public void deployServerHandler(HandlerDescriptor handler) throws AxisException, ObjectExistsException {
        String failMessage;
        String wsdd;
        WebservicePermission.checkPermission(handler.getPath(), "deployHandler");
        failMessage = "Failed to deploy handler '" + handler + "'";
        wsdd = handler.insertInto(getServerEngine());
        if (wsdd != null) {
            processDescriptor(currentBaseURL() + ADMIN_SERVICE_NAME_, wsdd, Collections.EMPTY_MAP, failMessage);
            log_.info("Successfully deployed handler '" + handler + "'");
        } else {
            log_.info(failMessage);
        }
    }

    public Webservice deployWebservice(String webserviceName, String webserviceClass, String webserviceInterfaces, String scope) throws AxisException, ObjectExistsException {
        log_.entering(new Object[] { webserviceName, webserviceClass, webserviceInterfaces, scope });
        StringTokenizer tokenizer;
        Webservice.Scope wsscope;
        Class<?>[] ifclasses;
        Class<?> clazz;
        Object proxy;
        String baseURL;
        Webservice ws;
        String wsname;
        String msg;
        int cnt;
        wsname = checkName(webserviceName);
        WebservicePermission.checkPermission(wsname, "deploy");
        clazz = resolveClass(webserviceClass);
        if (webserviceInterfaces == null) {
            throw new IllegalArgumentException("Interfaces are null");
        }
        wsscope = Webservice.Scope.toScope(scope);
        if (webserviceInterfaces.equals(WILDCARD)) {
            ifclasses = clazz.getInterfaces();
        } else {
            tokenizer = new StringTokenizer(webserviceInterfaces, ",");
            ifclasses = new Class[tokenizer.countTokens()];
            for (cnt = 0; cnt < ifclasses.length; ) {
                ifclasses[cnt++] = resolveClass(tokenizer.nextToken());
            }
            checkImplementation(clazz, ifclasses);
        }
        checkInterfaces(ifclasses);
        synchronized (lock_) {
            baseURL = currentBaseURL();
            checkNameVacancy(wsname, AxisServiceInfo.parseListReply(baseURL + ADMIN_SERVICE_NAME_));
            if (wsscope.equals(Webservice.Scope.APPLICATION)) {
                try {
                    proxy = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new RemoteInvocationHandler(clazz.newInstance()));
                } catch (Exception e) {
                    msg = "Could not create instance of " + webserviceClass;
                    log_.caught(LogLevel.ERROR, msg, e);
                    throw new IllegalArgumentException(msg);
                }
                checkImports(proxy);
                ws = deployWebservice0(wsname, proxy, ifclasses, baseURL);
            } else {
                ws = deployWebservice0(wsname, webserviceClass, ifclasses, wsscope, AxisServiceInfo.Entry.PROVIDER_JAVA_RPC, baseURL);
            }
            log_.info("Successfully deployed webservice '" + wsname + "'");
        }
        log_.exiting(ws);
        return ws;
    }

    /**
     * This is the actual implementation of
     * {@link #deployWebservice(String,String,String,String)}, performing
     * requests directly on the <i>Axis</i> framework. Valid parameters
     * and synchronization are assumed. Scope values will not be mapped.
     */
    protected Webservice deployWebservice0(String webserviceName, String webserviceClass, Class<?>[] interfaces, Webservice.Scope scope, String provider, String baseURL) throws AxisException {
        AxisServiceInfo.Entry entry;
        StringBuffer allowedMethods;
        Map<String, String> subst;
        Set<String> methods;
        String adminURL;
        String msg;
        methods = new HashSet<String>();
        for (Class<?> ifclass : interfaces) {
            for (Method method : ifclass.getMethods()) {
                methods.add(method.getName());
            }
        }
        allowedMethods = new StringBuffer();
        for (String method : methods) {
            if (allowedMethods.length() > 0) {
                allowedMethods.append(" ");
            }
            allowedMethods.append(method);
        }
        adminURL = baseURL + ADMIN_SERVICE_NAME_;
        subst = new HashMap<String, String>(5);
        subst.put(SUBST_NAME_, webserviceName);
        subst.put(SUBST_PROVIDER_, provider);
        subst.put(SUBST_CLASSNAME_, webserviceClass);
        subst.put(SUBST_ALLOWEDMETHODS_, allowedMethods.toString());
        subst.put(SUBST_SCOPE_, scope.getName());
        processDescriptor(adminURL, deploy_wsdd_template_, subst, msg = "Deploying webservice '" + webserviceName + "' failed");
        entry = AxisServiceInfo.parseListReply(adminURL).getEntry(webserviceName);
        if (entry == null) {
            log_.error(msg);
            throw new AxisException(msg);
        }
        return getWebservice0(entry, baseURL);
    }

    public Webservice deployWebservice(String webserviceName, Object webservice, Class<?>[] interfaces) throws AxisException, ObjectExistsException {
        log_.entering(new Object[] { webserviceName, webservice, interfaces != null ? Arrays.asList(interfaces) : null });
        Webservice ws;
        Class<?> clazz;
        Object proxy;
        String baseURL;
        String wsname;
        String msg;
        wsname = checkName(webserviceName);
        WebservicePermission.checkPermission(wsname, "deploy");
        if (webservice == null) {
            log_.error(msg = "Webservice object is null");
            throw new IllegalArgumentException(msg);
        }
        clazz = webservice.getClass();
        checkImplementation(clazz, interfaces);
        checkInterfaces(interfaces);
        proxy = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new RemoteInvocationHandler(webservice));
        synchronized (lock_) {
            baseURL = currentBaseURL();
            checkNameVacancy(wsname, AxisServiceInfo.parseListReply(baseURL + ADMIN_SERVICE_NAME_));
            checkImports(proxy);
            ws = deployWebservice0(wsname, proxy, interfaces, baseURL);
            log_.info("Successfully deployed webservice '" + wsname + "'");
        }
        log_.exiting(ws);
        return ws;
    }

    /**
     * This is the actual implementation of
     * {@link #deployWebservice(String,Object,Class[])}, performing requests
     * directly on the <i>Axis</i> framework. Valid parameters and
     * synchronization are assumed. Deployed service objects will appear with
     * {@link Webservice.Scope#APPLICATION} in <i>SicAri</i>, whereas with
     * {@link Webservice.Scope#REQUEST} within <i>Axis</i>.
     * .
     */
    protected Webservice deployWebservice0(String webserviceName, Object webserviceProxy, Class<?>[] interfaces, String adminURL) throws AxisException {
        Class<?> clazz;
        clazz = webserviceProxy.getClass();
        if (!imports_.add(webserviceProxy, webserviceName)) {
            log_.warning("Failed importing " + webserviceName);
        }
        try {
            return deployWebservice0(webserviceName, clazz.getName(), interfaces, Webservice.Scope.REQUEST, AxisServiceInfo.Entry.PROVIDER_JAVA_SICARI, adminURL);
        } catch (AxisException e) {
            if (!imports_.remove(clazz, webserviceName)) {
                log_.warning("Failed removing import '" + webserviceName + "'");
            }
            throw e;
        }
    }

    public void undeployClientHandler(HandlerDescriptor handler) throws AxisException {
        String failMessage;
        String wsdd;
        WebservicePermission.checkPermission(handler.getPath(), "undeployHandler");
        failMessage = "Failed to undeploy handler '" + handler + "'";
        wsdd = handler.removeFrom(getClientEngine());
        if (wsdd != null) {
            processDescriptor(wsdd, Collections.EMPTY_MAP, failMessage);
            log_.info("Successfully undeployed handler '" + handler + "'");
        } else {
            log_.info(failMessage);
        }
    }

    public void undeployServerHandler(HandlerDescriptor handler) throws AxisException {
        String failMessage;
        String wsdd;
        WebservicePermission.checkPermission(handler.getPath(), "undeployHandler");
        failMessage = "Failed to undeploy handler '" + handler + "'";
        wsdd = handler.removeFrom(getServerEngine());
        if (wsdd != null) {
            processDescriptor(currentBaseURL() + ADMIN_SERVICE_NAME_, wsdd, Collections.EMPTY_MAP, failMessage);
            log_.info("Successfully undeployed handler '" + handler + "'");
        } else {
            log_.info(failMessage);
        }
    }

    public Webservice undeployWebservice(String webserviceName) throws AxisException, NoSuchObjectException {
        log_.entering(new Object[] { webserviceName });
        AxisServiceInfo axisinfo;
        String baseURL;
        Webservice ws;
        String wsname;
        wsname = checkName(webserviceName);
        WebservicePermission.checkPermission(wsname, "undeploy");
        synchronized (lock_) {
            baseURL = currentBaseURL();
            axisinfo = AxisServiceInfo.parseListReply(baseURL + ADMIN_SERVICE_NAME_);
            checkNameExistence(wsname, axisinfo);
            ws = undeployWebservice0(axisinfo.getEntry(wsname), baseURL);
            log_.info("Successfully undeployed webservice '" + wsname + "'");
        }
        log_.exiting(ws);
        return ws;
    }

    /**
     * This is the actual implementation of
     * {@link #undeployWebservice(String)}, performing requests
     * directly on the <i>Axis</i> framework. Valid parameters and
     * synchronization are assumed.
     */
    protected Webservice undeployWebservice0(AxisServiceInfo.Entry axisinfoEntry, String baseURL) throws AxisException {
        AxisServiceInfo.Entry entry;
        Map<String, String> subst;
        String adminURL;
        Webservice ws;
        String name;
        Object obj;
        String msg;
        ws = getWebservice0(axisinfoEntry, baseURL);
        adminURL = baseURL + ADMIN_SERVICE_NAME_;
        subst = new HashMap<String, String>(1);
        name = axisinfoEntry.getName();
        subst.put(SUBST_NAME_, name);
        processDescriptor(adminURL, undeploy_wsdd_template_, subst, msg = "Undeploying webservice '" + name + "' failed");
        entry = AxisServiceInfo.parseListReply(adminURL).getEntry(name);
        if (entry != null) {
            log_.error(msg);
            throw new AxisException(msg);
        }
        if ((obj = ws.getObject()) != null) {
            if (!imports_.remove(obj.getClass(), name)) {
                log_.warning("Failed removing import '" + name + "'");
            }
        }
        return ws;
    }

    public void undeployAllWebservices() throws AxisException {
        log_.entering();
        AxisServiceInfo axisinfo;
        String[] names;
        String baseURL;
        int i;
        WebservicePermission.checkPermission(null, "undeploy");
        synchronized (lock_) {
            baseURL = currentBaseURL();
            axisinfo = AxisServiceInfo.parseListReply(baseURL + ADMIN_SERVICE_NAME_);
            names = axisinfo.getServiceNames();
            for (i = 0; i < names.length; i++) {
                if (!hiddenServices_.contains(names[i])) {
                    undeployWebservice0(axisinfo.getEntry(names[i]), baseURL);
                    log_.info("Successfully undeployed webservice '" + names[i] + "'");
                }
            }
        }
        log_.exiting();
    }

    public List<HandlerDescriptor> getClientHandlers() throws AxisException {
        WebservicePermission.checkPermission(null, "getHandlers");
        return HandlerDescriptor.listHandlers(getClientEngine());
    }

    public List<HandlerDescriptor> getServerHandlers() throws AxisException {
        WebservicePermission.checkPermission(null, "getHandlers");
        return HandlerDescriptor.listHandlers(getServerEngine());
    }

    public Webservice getWebservice(String webserviceName) throws AxisException, NoSuchObjectException {
        log_.entering(new Object[] { webserviceName });
        AxisServiceInfo axisinfo;
        String baseURL;
        Webservice ws;
        String wsname;
        wsname = checkName(webserviceName);
        WebservicePermission.checkPermission(wsname, "get");
        synchronized (lock_) {
            baseURL = currentBaseURL();
            axisinfo = AxisServiceInfo.parseListReply(baseURL + ADMIN_SERVICE_NAME_);
            checkNameExistence(wsname, axisinfo);
            ws = getWebservice0(axisinfo.getEntry(wsname), baseURL);
        }
        log_.exiting(ws);
        return ws;
    }

    /**
     * This is the actual implementation of {@link #getWebservice(String)}.
     * Valid parameters and synchronization are assumed.
     */
    protected Webservice getWebservice0(AxisServiceInfo.Entry axisinfoEntry, String baseURL) {
        Class<?> clazz;
        Object service;
        String name;
        clazz = resolveClass(axisinfoEntry.getClassName());
        service = AxisServiceInfo.Entry.PROVIDER_JAVA_SICARI.equals(axisinfoEntry.getProvider()) ? imports_.get(clazz) : null;
        return new Webservice(name = axisinfoEntry.getName(), service, clazz, axisinfoEntry.getAllowedMethods(), service != null ? Webservice.Scope.APPLICATION : Webservice.Scope.toScope(axisinfoEntry.getScope()), baseURL + name + "?wsdl");
    }

    public String[] getWebserviceNames() throws AxisException {
        log_.entering();
        Set<String> wsnames;
        WebservicePermission.checkPermission(null, "getNames");
        synchronized (lock_) {
            wsnames = new HashSet<String>(Arrays.asList(AxisServiceInfo.parseListReply(currentBaseURL() + ADMIN_SERVICE_NAME_).getServiceNames()));
            wsnames.removeAll(hiddenServices_);
        }
        log_.exiting(wsnames);
        return wsnames.toArray(new String[0]);
    }

    /**
     * Process the specified deployment descriptor by means of the
     * {@link Admin}.
     *
     * @param wsddtmpl The deployment descriptor template
     * @param subst Substitutions to be applied on the descriptor template
     *   prior to processing
     * @param failmsg The message to be printed/logged in case of a failure
     *
     * @throws AxisException
     *   in case processing the deployment descriptor failed
     */
    protected void processDescriptor(String wsddtmpl, Map<?, ?> subst, String failmsg) throws AxisException {
        MessageContext msgContext;
        AxisEngine engine;
        Document doc;
        Admin admin;
        log_.debug("WSDD substitutes are " + subst);
        try {
            admin = new Admin();
            engine = getClientEngine();
            msgContext = new MessageContext(engine);
            doc = XMLUtils.newDocument(new StringSubstitutionInputStream(new ByteArrayInputStream(wsddtmpl.getBytes()), subst));
            log_.trace("Calling Axis: [Admin].process() ...");
            admin.process(msgContext, doc.getDocumentElement());
            log_.trace("Returned from Axis: [Admin].process()");
        } catch (Exception e) {
            log_.caught(LogLevel.ERROR, failmsg + e.getMessage(), e);
            throw new AxisException(failmsg, e);
        }
    }

    /**
     * Process the specified deployment descriptor by means of the
     * {@link AdminClient}.
     *
     * @param url The default URL
     * @param wsddtmpl The deployment descriptor template
     * @param subst Substitutions to be applied on the descriptor template
     *   prior to processing
     * @param failmsg The message to be printed/logged in case of a failure
     *
     * @throws AxisException
     *   in case processing the deployment descriptor failed
     */
    protected void processDescriptor(String url, String wsddtmpl, Map<?, ?> subst, String failmsg) throws AxisException {
        Options options;
        String reply;
        log_.debug("WSDD substitutes are " + subst);
        try {
            options = new Options(new String[0]);
            options.setDefaultURL(url);
            log_.trace("Calling Axis: [AdminClient@" + url + "].process() ...");
            reply = new AdminClient().process(options, new StringSubstitutionInputStream(new ByteArrayInputStream(wsddtmpl.getBytes()), subst));
            log_.trace("Returned from Axis: [AdminClient@" + url + "].process()");
            if (!reply.equals(ADMIN_CLIENT_SUCCESS_)) {
                throw new AxisException("; Axis replied: " + reply);
            }
        } catch (Exception e) {
            log_.caught(LogLevel.ERROR, failmsg + e.getMessage(), e);
            throw new AxisException(failmsg, e);
        }
    }

    /**
     * Get the current base <code>URL</code>, which the first of
     * {@link #availableBaseURLs() all available base URLs}.
     * <p><b>Notice</b>: This method will be called once for each
     * <code>WebserviceService</code> interface call. From there until the
     * call returns, the base <code>URL</code> is assumed constant, i.e.
     * none of the underlying services should be removed manually (e.g. via
     * <i>Envision</i> or {@link Environment#retract(String)}).
     *
     * @return The current <i>Axis</i> service <code>URL</code>
     *
     * @throws AxisException
     *   if the <i>Axis</i> framework is not accessible
     */
    protected String currentBaseURL() throws AxisException {
        String[] urls;
        String msg;
        if ((urls = availableBaseURLs()).length > 0) {
            return urls[0];
        }
        msg = "Locating webserver and/or AxisMasterServlet failed";
        log_.error(msg);
        throw new AxisException(msg);
    }

    /**
     * Find available <i>Axis</i> base <code>URL</code>s founding on
     * <tt>HTTP(S)</tt> <code>Server</code> instances available in the
     * {@link Environment}. If a matching <code>AxisMasterServlet</code>
     * could be found, build the corresponding <i>Axis</i> <code>URL</code>.
     *
     * @return All available <i>Axis</i> base <code>URL</code>s
     */
    protected String[] availableBaseURLs() {
        String[] protocols;
        List<String> urls;
        Server server;
        int i;
        protocols = new String[] { HTTPS_WHATIS_, HTTP_WHATIS_ };
        urls = new ArrayList<String>();
        for (i = 0; i < protocols.length; i++) {
            try {
                if (((server = lookupServer(protocols[i])) != null) && (lookupAxisMasterServlet(protocols[i]) != null)) {
                    urls.add(server.localURL() + "/" + axisurl_ + "/" + SERVICES_PATH_ + "/");
                }
            } catch (IllegalArgumentException e) {
            }
        }
        return urls.toArray(new String[0]);
    }

    @Override
    public String toString() {
        List<HandlerDescriptor> handlers;
        StringBuffer strbuf;
        StringBuffer urls;
        String[] avail;
        int i;
        avail = availableBaseURLs();
        urls = new StringBuffer();
        strbuf = new StringBuffer(super.toString());
        strbuf.append("\n\nAxis configuration:");
        strbuf.append("\n> Axis base:     " + axisbase_);
        strbuf.append("\n> Client config: " + axisclientconf_);
        for (i = 0; i < avail.length; i++) {
            if (i > 0) {
                urls.append(",");
            }
            urls.append(avail[i]);
        }
        strbuf.append("\n> Base URL(s):   " + urls);
        strbuf.append("\n\nAxis handler:");
        strbuf.append("\n> Client handlers: ");
        try {
            handlers = getClientHandlers();
            for (i = 0; i < handlers.size(); i++) {
                strbuf.append("\n  > " + handlers.get(i));
            }
        } catch (Exception e) {
            strbuf.append("yet unknown");
        }
        strbuf.append("\n> Server handlers: ");
        try {
            handlers = getServerHandlers();
            for (i = 0; i < handlers.size(); i++) {
                strbuf.append("\n  > " + handlers.get(i));
            }
        } catch (Exception e) {
            strbuf.append("yet unknown");
        }
        strbuf.append("\n\nAxis services:");
        strbuf.append("\n> Services:        ");
        try {
            strbuf.append(Arrays.asList(getWebserviceNames()));
        } catch (Exception e) {
            strbuf.append("yet unknown");
        }
        strbuf.append("\n> Imports:         " + imports_);
        return strbuf.toString();
    }

    /**
     * The command line interface implementation. For more information run
     * <p><nobreak><tt>java WebserviceServiceImpl -help</tt></nobreak>
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        Object lookuphttps;
        Object lookuphttp;
        Environment env;
        ArgsParser ap;
        boolean https;
        boolean http;
        Object envobj;
        String whatis;
        File[] configFiles;
        File file;
        File dir;
        int protocols;
        int i;
        ap = new ArgsParser(DESCR_);
        try {
            ap.parse(args);
            if (ap.isDefined(CMDLINE_HELP_)) {
                System.out.println(usage());
                return;
            }
            https = ap.isDefined(CMDLINE_HTTPS_);
            http = ap.isDefined(CMDLINE_HTTP_);
            if (!https && !http) {
                log_.warning("Neither HTTPS nor HTTP protocol specified");
                return;
            }
            env = Environment.getEnvironment();
            whatis = Lookup.whatIs(WebserviceService.WHATIS);
            envobj = env.lookup(whatis);
            if (envobj == null) {
                ref_ = null;
                proxy_ = null;
            } else if (envobj != proxy_) {
                log_.error("Unknown object in Environment:" + whatis + " (" + envobj + ")");
                throw new RuntimeException("Environment:" + whatis + " contains an unknown object, cannot proceed");
            }
            if (ap.isDefined(CMDLINE_PUBLICATION_)) {
                if (ref_ != null) {
                    if (ap.isDefined(CMDLINE_AXISBASEDIR_) && !ref_.axisbase_.equals(ap.fileValue(CMDLINE_AXISBASEDIR_))) {
                        throw new BadArgsException("Published instance has different axis base " + "directory: " + ref_.axisbase_);
                    }
                    if (ap.isDefined(CMDLINE_AXISURL_) && !ref_.axisurl_.equals(ap.stringValue(CMDLINE_AXISURL_))) {
                        throw new BadArgsException("Published instance has different axis URL: " + ref_.axisurl_);
                    }
                } else {
                    if (!ap.isDefined(CMDLINE_AXISBASEDIR_)) {
                        throw new BadArgsException("Missing axis base directory, which is " + "required for inital registration");
                    }
                    if (ap.isDefined(CMDLINE_CLEAN_)) {
                        try {
                            dir = ap.fileValue(CMDLINE_AXISBASEDIR_);
                            if (!dir.isDirectory()) {
                                throw new IllegalArgumentException("Invalid Axis base directory: " + dir);
                            }
                            configFiles = new File[2];
                            configFiles[0] = new File(dir, AXIS_SERVER_CONFIG_);
                            configFiles[1] = ap.fileValue(CMDLINE_AXISCLIENTCONF_, null);
                            if (configFiles[1] == null) {
                                configFiles[1] = new File(dir, AXIS_CLIENT_CONFIG_);
                            }
                            for (i = 0; i < configFiles.length; i++) {
                                file = configFiles[i];
                                try {
                                    if (file.delete()) {
                                        log_.info("AXIS configuration file '" + file + "' removed.");
                                    } else {
                                        log_.debug("Could not remove AXIS configuration file '" + file + "'.");
                                    }
                                } catch (Exception e) {
                                    log_.caught(LogLevel.WARNING, "Could not remove AXIS configuration file '" + file + "'.", e);
                                }
                            }
                        } catch (Exception e) {
                            log_.caught(LogLevel.WARNING, "Could not clean AXIS configuration files", e);
                        }
                    }
                    ref_ = new WebserviceServiceImpl(ap.fileValue(CMDLINE_AXISBASEDIR_), ap.fileValue(CMDLINE_AXISCLIENTCONF_, null), ap.stringValue(CMDLINE_AXISURL_, null));
                }
            } else if (ap.isDefined(CMDLINE_RETRACTION_)) {
                if (ref_ == null) {
                    throw new NoSuchObjectException("Environment:" + whatis + " does not contain an appropriate " + WebserviceService.class.getName());
                }
                if (ap.isDefined(CMDLINE_AXISBASEDIR_) || ap.isDefined(CMDLINE_AXISCLIENTCONF_) || ap.isDefined(CMDLINE_AXISURL_)) {
                    log_.warning("Ignoring invalid option -" + CMDLINE_AXISBASEDIR_ + ", -" + CMDLINE_AXISCLIENTCONF_ + " or -" + CMDLINE_AXISURL_ + " for -" + CMDLINE_RETRACTION_ + "...");
                }
            }
            protocols = 0;
            lookuphttps = ref_.lookupAxisMasterServlet(HTTPS_WHATIS_);
            lookuphttp = ref_.lookupAxisMasterServlet(HTTP_WHATIS_);
            if (ap.isDefined(CMDLINE_PUBLICATION_)) {
                if (envobj == null) {
                    env.publish(whatis, ref_, Environment.PLAIN_PROXY | Environment.DETACH);
                    proxy_ = env.lookup(whatis);
                    log_.info("Instance successfully published into " + "Environment:" + whatis);
                }
                if (https && lookuphttps == null) {
                    protocols |= ServletLauncher.HTTPS;
                }
                if (http && lookuphttp == null) {
                    protocols |= ServletLauncher.HTTP;
                }
                if (protocols != 0) {
                    ServletLauncher.publish(new AxisMasterServlet(), ref_.axisurl_, null, new Properties(), ref_.axisbase_.getAbsolutePath(), protocols);
                }
                if (envobj == null) {
                    log_.debug("Undeploying outdated webservices...");
                    ref_.undeployAllWebservices();
                }
            } else if (ap.isDefined(CMDLINE_RETRACTION_)) {
                if (https && lookuphttps != null) {
                    protocols |= ServletLauncher.HTTPS;
                }
                if (http && lookuphttp != null) {
                    protocols |= ServletLauncher.HTTP;
                }
                if (protocols == 0) {
                    log_.warning("Nothing to retract");
                    return;
                }
                if ((https && http) || (https && lookuphttp == null) || (http && lookuphttps == null)) {
                    log_.debug("Undeploying all webservices before retracting " + "remaining AxisMasterServlets...");
                    ref_.undeployAllWebservices();
                }
                ServletLauncher.retract(ref_.axisurl_, protocols);
                if (ref_.lookupAxisMasterServlet(HTTPS_WHATIS_) == null && ref_.lookupAxisMasterServlet(HTTP_WHATIS_) == null) {
                    env.retract(whatis);
                    ref_ = null;
                    proxy_ = null;
                    log_.info("Instance successfully retracted from " + "Environment:" + whatis);
                }
            }
        } catch (Exception e) {
            log_.caught(e);
            System.err.println("[WebserviceServiceImpl] Error: " + e.getMessage());
            if (e instanceof ArgsParserException) {
                System.err.println("\n" + usage());
            }
        }
    }

    /**
     * @return Some usage info.
     */
    protected static String usage() {
        return ("Usage: java WebserviceServiceImpl\n" + "\t-" + CMDLINE_HELP_ + "\n" + "\t-" + CMDLINE_PUBLICATION_ + " [-" + CMDLINE_AXISBASEDIR_ + " <dir>]" + " [-" + CMDLINE_AXISCLIENTCONF_ + " <file>]" + " [-" + CMDLINE_AXISURL_ + " <url>]" + " [-" + CMDLINE_HTTPS_ + "]" + " [-" + CMDLINE_CLEAN_ + "]" + " [-" + CMDLINE_HTTP_ + "]\n" + "\t-" + CMDLINE_RETRACTION_ + " [-" + CMDLINE_HTTPS_ + "]" + " [-" + CMDLINE_HTTP_ + "]\n" + "\nwhere:\n" + "\n-" + CMDLINE_HELP_ + "\n" + "\tShows this text.\n" + "\n-" + CMDLINE_PUBLICATION_ + "\n" + "\tPublishes the WebserviceService into the Environment\n" + "\tand an AxisMasterServlet with the HTTP(S) server(s).\n" + "\n\t-" + CMDLINE_AXISBASEDIR_ + " <dir>\n" + "\t\tThe Axis base directory on the local server.\n" + "\t\tRequired only for initial registration. Subsequent usage\n" + "\t\tis only valid with matching parameter.\n" + "\n\t-" + CMDLINE_AXISCLIENTCONF_ + " <file>\n" + "\t\tThe path to the Axis client configuration file.\n" + "\t\tIf omitted, the default configuration file provided by\n" + "\t\tAxis will be used.\n" + "\n\t-" + CMDLINE_AXISURL_ + " <url>\n" + "\t\t(optional) The Axis URL part (e.g. 'myaxis', as in\n" + "\t\t'http://myserver:8080/myaxis/" + SERVICES_PATH_ + "/...')\n" + "\t\tIf omitted,'" + DEFAULT_AXIS_URL_ + "' will be used.\n" + "\t\tSubsequent usage is only valid with matching parameter.\n" + "\n\t-" + CMDLINE_HTTPS_ + "\n" + "\t\tPublishes an AxisMasterServlet with the HTTPS server.\n" + "\n\t-" + CMDLINE_HTTP_ + "\n" + "\t\tPublishes an AxisMasterServlet with the HTTP server.\n" + "\n\t-" + CMDLINE_CLEAN_ + "\n" + "\t\tCleans axisbase directory by removing previous AXIS.\n" + "\t\tconfiguration files.\n" + "\n-" + CMDLINE_RETRACTION_ + "\n" + "\tRetracts the AxisMasterServlet from the HTTP(S) server(s).\n" + "\tOnce the last servlet is retracted, the WebserviceService\n" + "\twill retract itself from the Environment.\n" + "\n\t-" + CMDLINE_HTTPS_ + "\n" + "\t\tRetracts the AxisMasterServlet from the HTTPS server.\n" + "\n\t-" + CMDLINE_HTTP_ + "\n" + "\t\tRetracts the AxisMasterServlet from the HTTP server.\n" + "\nNOTICE: For more information about servlet handling " + "please concern\n\t" + ServletLauncher.class.getName());
    }

    /**
     * This is a container class for service import objects, assuring
     * one single object per <code>Class</code> only. This is a necessity since
     * the <code>SicariProvider</code> (which extends the <i>Apache Axis</i>
     * framework) requires to identify and create service objects by class.
     * <p>This class is not thread-safe.
     *
     * @see SicariProvider#makeNewServiceObject(org.apache.axis.MessageContext,String)
     */
    protected static class Imports {

        /**
         * The <code>Logger</code> instance for this class
         */
        private static Logger log2_ = LoggerFactory.getLogger("webservice");

        /**
         * The mapping of imported service objects to their service names
         */
        protected Map<Object, Set<String>> map_;

        /**
         * Create an <code>Imports</code> container.
         */
        public Imports() {
            map_ = new HashMap<Object, Set<String>>();
        }

        /**
         * Imports a service object or adds a new name for an existing
         * service object.
         * <p><b>Notice</b>: In case a different service object of the same
         * type already exists, it will be silently replaced and the new
         * service object will automatically inherit all names.
         *
         * @param service The service object
         * @param name The name of the corresponding webservice
         *
         * @return <code>true</code> if the entry was added successfully,
         *   <code>false</code> if the entry already exists
         */
        public boolean add(Object service, String name) {
            Set<String> names;
            Class<?> clazz;
            Object obj;
            if ((service == null) || (name == null)) {
                log2_.warning("Service object and/or name is null");
                return false;
            }
            clazz = service.getClass();
            obj = get(clazz);
            if ((obj != null) && !obj.equals(service)) {
                map_.put(service, map_.remove(obj));
                log2_.debug("Replaced " + obj + " by " + service);
            }
            names = map_.get(service);
            if (names == null) {
                names = new HashSet<String>();
                map_.put(service, names);
                log2_.debug("Successfully created new service import entry for " + clazz);
            }
            if (names.add(name)) {
                log2_.debug("Successfully added name '" + name + "' for imported service " + clazz);
                return true;
            }
            log2_.warning("Service import (" + clazz + "," + name + ") already exists");
            return false;
        }

        /**
         * Remove the given name for the imported service object
         * of the specified type.
         *
         * @param type The service type
         * @param name The service name to be removed
         *
         * @return <code>true</code> if the entry was removed successfully,
         *   <code>false</code> if the entry does not exist
         */
        public boolean remove(Class<?> type, String name) {
            Set<String> names;
            Object obj;
            if ((type == null) || (name == null)) {
                log2_.warning("Service type and/or name is null");
                return false;
            }
            if ((obj = get(type)) != null) {
                names = map_.get(obj);
                if ((names != null) && names.remove(name)) {
                    log2_.debug("Successfully removed name '" + name + "' for imported service " + type);
                    if (names.isEmpty()) {
                        map_.remove(obj);
                        log2_.debug("Successfully deleted service import entry for " + type);
                    }
                    return true;
                }
            }
            log2_.warning("Service import (" + type + "," + name + ") does not exist");
            return false;
        }

        /**
         * Get the service import of the given type.
         *
         * @param type The type of the requested service import
         *
         * @return The service import object or <code>null</code> if no
         *   matching object could be found
         */
        public Object get(Class<?> type) {
            if (type == null) {
                log2_.warning("Requested service object type is null");
                return null;
            }
            for (Object obj : map_.keySet()) {
                if (type.equals(obj.getClass())) {
                    return obj;
                }
            }
            return null;
        }

        /**
         * Resolve the given <code>name</code> into a service import class.
         *
         * @param name The name to resolve
         *
         * @return The corresponding service import class or <code>null</code>
         *   if there is no matching import
         *
         * @throws ClassNotFoundException in case <code>name</code> could not
         *   be resolved
         */
        public Class<?> findClass(String name) throws ClassNotFoundException {
            if (name == null) {
                log2_.warning("Name is null");
                return null;
            }
            Class<?> clazz;
            for (Object obj : map_.keySet()) {
                clazz = obj.getClass();
                if (name.equals(clazz.getName())) {
                    return clazz;
                }
            }
            throw new ClassNotFoundException(name);
        }

        @Override
        public String toString() {
            return (map_.isEmpty() ? "None" : map_.toString());
        }
    }

    /**
     * This <code>InvocationHandler</code> performs on the given target
     * object on behalf of the current remote user, which is available
     * via the {@link SecurityTokenHandler}.
     */
    protected static class RemoteInvocationHandler implements InvocationHandler {

        /**
         * The <code>Logger</code> instance for this class
         */
        private static Logger log2_ = LoggerFactory.getLogger("webservice");

        /**
         * The target object
         */
        private Object target_;

        /**
         * Create a <code>RemoteInvocationHandler</code>.
         */
        public RemoteInvocationHandler(Object target) {
            target_ = target;
            log2_.debug("Instance (" + target + ") successfully created");
        }

        public Object getTarget() {
            return target_;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Subject user;
            logUid("Method '" + method + "' called by ");
            user = SecurityTokenHandler.currentUser();
            method = target_.getClass().getMethod(method.getName(), method.getParameterTypes());
            if (user == null) {
                log2_.warning("No remote user found. Performing standard invocation.");
                try {
                    logUid("Method '" + method + "' invoked as ");
                    return method.invoke(target_, args);
                } catch (Throwable t) {
                    while (t instanceof InvocationTargetException) {
                        t = t.getCause();
                    }
                    throw t;
                }
            }
            final Method _method = method;
            final Object[] _args = args;
            final Object _target = target_;
            UserHistory.addCurrentUser();
            try {
                return Subject.doAs(user, new PrivilegedExceptionAction<Object>() {

                    public Object run() throws InvocationTargetException {
                        try {
                            logUid("Method '" + _method + "' invoked as ");
                            return _method.invoke(_target, _args);
                        } catch (Throwable t) {
                            throw new InvocationTargetException(t);
                        }
                    }
                });
            } catch (PrivilegedActionException e) {
                Throwable t = e.getCause();
                while (t instanceof InvocationTargetException) {
                    t = t.getCause();
                }
                throw t;
            } finally {
                UserHistory.removeLatestUser();
            }
        }

        void logUid(String msg) {
            if (log2_.isEnabled(LogLevel.TRACE)) {
                UID uid;
                try {
                    uid = UID.find(SicariSecurityManager.currentUser());
                    log2_.trace(msg + uid);
                } catch (Exception e) {
                    try {
                        log2_.caught(LogLevel.TRACE, msg + "<unresolvable>", e);
                    } catch (Exception e2) {
                    }
                }
            }
        }
    }
}
