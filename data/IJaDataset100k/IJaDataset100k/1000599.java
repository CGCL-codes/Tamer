package org.eaasyst.eaa.syst;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;
import org.eaasyst.eaa.Constants;
import org.eaasyst.eaa.events.EventDrivenException;
import org.eaasyst.eaa.events.EventHandlingException;
import org.eaasyst.eaa.resources.PropertiesManager;
import org.eaasyst.eaa.security.User;
import org.eaasyst.eaa.service.ServiceLocator;
import org.eaasyst.eaa.syst.data.persistent.NoticeTemplate;
import org.eaasyst.eaa.syst.data.persistent.Person;
import org.eaasyst.eaa.syst.data.transients.DeliveredNotice;
import org.eaasyst.eaa.syst.data.transients.Event;
import org.eaasyst.eaa.syst.data.transients.EventDetails;
import org.eaasyst.eaa.syst.data.transients.ServiceRegistration;
import org.eaasyst.eaa.syst.data.transients.UserMessage;

/**
 * <p>This class is a collection of utility service methods implemented as static
 * method calls in the format <strong><code>EaasyStreet.<em>&lt;method name&gt;</em>
 * ([arg0], [arg1], ..., [argN])</code></strong>. Currently, the following generalized
 * services have been implemented:</p>
 * 
 * <table border=1 cellspacing=0 cellpadding=2>
 *  <tr>
 *   <th>Service</th>
 *   <th>Method Name</th>
 *   <th>Parameters</th>
 *   <th>Description</th>
 *  </tr>
 *  <tr>
 *   <td>Logging</td>
 *   <td><code>log<em>&lt;level&gt;</em>()</code></td>
 *   <td>message, [exception]</td>
 *   <td>Logs the message and optional exception according to the log level requested.</td>
 *  </tr>
 *  <tr>
 *   <td>Authentication</td>
 *   <td><code>isAuthenticated()</code></td>
 *   <td>request</td>
 *   <td>Returns true if the user has been authenticated.</td>
 *  </tr>
 *  <tr>
 *   <td>Authorization</td>
 *   <td><code>isAuthorized()</code></td>
 *   <td>type, resource, request</td>
 *   <td>Returns true if the user is authorized for the specified resource.</td>
 *  </tr>
 *  <tr>
 *   <td>Navigation</td>
 *   <td><code>getNavigation()</code></td>
 *   <td>request</td>
 *   <td>Returns the HTML code for the application menu options authorized for the current user.</td>
 *  </tr>
 *  <tr>
 *   <td>User Messages</td>
 *   <td><code>setUserMessage()</code></td>
 *   <td>request</td>
 *   <td>These methods are used to pass messages to the user.</td>
 *  </tr>
 *  <tr>
 *   <td>Event Handling</td>
 *   <td><code>handleEvent()</code></td>
 *   <td>request, event</td>
 *   <td>Handles application events by invoking predefined event responders as required.</td>
 *  </tr>
 *  <tr>
 *   <td>Resources</td>
 *   <td><code>getResources()</code></td>
 *   <td>source file name</td>
 *   <td>Returns an instance of <code>Resources</code> loaded from the named source.</td>
 *  </tr>
 *  <tr>
 *   <td>Class Loading</td>
 *   <td><code>getInstance()</code></td>
 *   <td>class name</td>
 *   <td>Returns an instance of the class named, or <code>null</code> if the class cannot be loaded.</td>
 *  </tr>
 *  <tr>
 *   <td>Property Values</td>
 *   <td><code>getProperty()</code></td>
 *   <td>property name</td>
 *   <td>Returns the value of a configuration parameter based on the name provided.</td>
 *  </tr>
 *  <tr>
 *   <td>Context Attributes</td>
 *   <td><code>getContextAttribute()</code></td>
 *   <td>attribute name</td>
 *   <td>Returns the value of the <code>ServletContext</code> attribute based on the name provided.</td>
 *  </tr>
 *  <tr>
 *   <td>Real Path</td>
 *   <td><code>getRealPath()</code></td>
 *   <td>virtual path</td>
 *   <td>Returns the actual path based on the virtual path provided.</td>
 *  </tr>
 * </table>
 * 
 * <p>
 *  This class is designed to be used in a servlet environment. To use the
 *  service methods provided, you must perform the follwing steps:
 *  <ol>
 *   <li>Place a text file named <code>EaasyStreet.properties</code> in the
 *    <code>/WEB-INF/</code> directory of your .war file.</li>
 *   <li>Add a line to the <code>EaasyStreet.properties</code> file that identifies
 *    the name of your application system:
 *    <p><code>eaa.system.name=<em>&lt;your-system-name&gt;</em></code></p></li>
 *   <li>In the <code>init(config)</code> method of your servlet, add the following
 *    lines of code:
 *    <ul>
 *     <li>In the beginning: <code>EaasyStreet.init(config);</code></li>
 *     <li>At the end: <code>EaasyStreet.unregister();</code></li>
 *    </ul></li>
 *   <li>In the service methods (<code>doGet(), doPost(),</code> etc.) of your
 *    servlet, add the following lines of code:
 *    <ul>
 *     <li>In the beginning:
 *      <code>EaasyStreet.register(<em>&lt;your-system-name&gt;</em>);</code></li>
 *     <li>At the end: <code>EaasyStreet.unregister();</code></li>
 *    </ul>
 *   </li>
 *  </ol>
 * </p>
 * 
 * <p>Each service has specific requirements for proper initialization. The
 * <code>init(config)</code> method should only be called once per system name,
 * at which time all services will be initialized for that application system.
 * Subsequent calls to the <code>init(config)</code> method for the same system
 * will result in an exception.</p>
 *
 * @version 2.9.1
 * @author Jeff Chilton
 */
public final class EaasyStreet {

    private static final String EVI0001T = "EVI0001T";

    private static final String EVI0003T = "EVI0003T";

    private static final String EVI0004T = "EVI0004T";

    private static final String EVI0010T = "EVI0010T";

    private static final String EVI0011T = "EVI0011T";

    private static final String EVI0014W = "EVI0014W";

    private static final String SERVICE_LOCATOR_TEXT_KEY = "string.service.locator";

    private static final String INITIALIZATION_NOTICE_KEY = "string.initialization.notice";

    private static final String TERMINATION_NOTICE_KEY = "string.termination.notice";

    private static final MessageResources localStrings = MessageResources.getMessageResources("org.eaasyst.eaa.syst.LocalStrings");

    private static final Log log = LogFactory.getLog("EaasyStreet");

    private static final Properties defaultValues = PropertiesManager.getProperties("org.eaasyst.eaa.syst.DefaultValues");

    private static final int periodicLimit = 10000;

    private static HashMap serviceLocators = new HashMap();

    private static HashMap threads = new HashMap();

    private static HashMap sessions = new HashMap();

    private static int runningCount = 0;

    /**
	 * <p>This method is used to initialize the services. Each implemented
	 * service has specific requirements for proper initialization. The
	 * <code>init()</code> method may only be called once per application
	 * system, at which time all services will be initialized. Subsequent
	 * calls to the <code>init()</code> method will result in an
	 * <code>EaasyStreetSystemError</code> exception.</p>
	 * 
	 * @param config the <code>ServletConfig</code> object associated with the
	 * application
	 * @throws EaasyStreetSystemError
	 * @since Eaasy Street 2.0
	 */
    public static void init(ServletConfig config) throws EaasyStreetSystemError {
        PropertiesManager propertiesManager = new PropertiesManager(config.getServletContext());
        Properties props = propertiesManager.loadProperties(defaultValues.getProperty(Constants.CFG_CONFIG_SRC), defaultValues);
        config.getServletContext().setAttribute(Constants.CTX_SYSTEM_PROPERTIES, props);
        String systemName = null;
        if (props.getProperty(Constants.CFG_SYSTEM_NAME).equals(systemName)) {
            log.warn(localStrings.getMessage(EVI0014W, new String[] { Constants.CFG_SYSTEM_NAME, systemName }));
        } else {
            systemName = props.getProperty(Constants.CFG_SYSTEM_NAME);
        }
        config.getServletContext().setAttribute(Constants.CTX_SYSTEM_NAME, systemName);
        String serviceLocatorClassName = props.getProperty(Constants.CFG_SVC_LOCATOR_CLASS);
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = (ServiceLocator) Class.forName(serviceLocatorClassName).newInstance();
        } catch (Exception e) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                serviceLocator = (ServiceLocator) classLoader.loadClass(serviceLocatorClassName).newInstance();
            } catch (Throwable t) {
                systemError(EVI0003T, new String[] { localStrings.getMessage(SERVICE_LOCATOR_TEXT_KEY), serviceLocatorClassName, t.toString() }, t);
            }
        }
        if (serviceLocator != null) {
            synchronized (serviceLocators) {
                if (serviceLocators.containsKey(systemName)) {
                    systemError(EVI0001T, null, null);
                } else {
                    log.info(localStrings.getMessage(INITIALIZATION_NOTICE_KEY, systemName));
                    serviceLocators.put(systemName, serviceLocator);
                    register(systemName, null);
                    serviceLocator.initialize(config);
                }
            }
        }
    }

    /**
	 * <p>This method is used to terminate services for a specific
	 * application system identified by the system name parameter.</p>
	 * 
	 * @param systemName a String containing the name of the system associated
	 * with this run unit. This should be the same name that is specified in the
	 * EaasyStreet configuration file for this application system.
	 * @since Eaasy Street 2.0
	 */
    public static void destroy(String systemName) {
        synchronized (serviceLocators) {
            serviceLocators.remove(systemName);
        }
        synchronized (threads) {
            List toBeRemoved = new ArrayList();
            Iterator i = threads.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                ServiceRegistration registration = (ServiceRegistration) threads.get(key);
                if (systemName.equals(registration.getSystemName())) {
                    toBeRemoved.add(key);
                }
            }
            i = toBeRemoved.iterator();
            while (i.hasNext()) {
                threads.remove(i.next());
            }
        }
        synchronized (sessions) {
            List toBeRemoved = new ArrayList();
            Iterator i = sessions.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                ServiceRegistration registration = (ServiceRegistration) sessions.get(key);
                if (systemName.equals(registration.getSystemName())) {
                    toBeRemoved.add(key);
                }
            }
            i = toBeRemoved.iterator();
            while (i.hasNext()) {
                sessions.remove(i.next());
            }
        }
        log.info(localStrings.getMessage(TERMINATION_NOTICE_KEY, systemName));
    }

    /**
	 * <p>This method is used to register for EaasyStreet services. This method
	 * should be called during each execution of a servlet's service methods.</p>
	 * 
	 * @param systemName a String containing the name of the system associated
	 * with this run unit. This should be the same name that is specified in the
	 * EaasyStreet configuration file for this application system.
	 * @param req the <code>HttpServeltRequest</code> object
	 * @since Eaasy Street 2.0
	 */
    public static void register(String systemName, HttpServletRequest req) {
        ServiceRegistration registration = null;
        String threadId = Thread.currentThread().getName();
        Date dateTime = new Date();
        String sessionId = null;
        String userId = null;
        String useName = null;
        if (req != null) {
            HttpSession session = req.getSession();
            if (session != null) {
                sessionId = session.getId();
                User profile = (User) session.getAttribute(Constants.SAK_USER_PROFILE);
                if (profile != null) {
                    Person person = profile.getPerson();
                    userId = profile.getUserId();
                    useName = person.getUseName();
                }
                synchronized (sessions) {
                    if (sessions.containsKey(sessionId)) {
                        registration = (ServiceRegistration) sessions.get(sessionId);
                    }
                }
            }
        }
        if (registration == null) {
            registration = new ServiceRegistration();
            registration.setThreadId(threadId);
            registration.setDateTime(dateTime);
            registration.setSystemName(systemName);
            registration.setSessionId(sessionId);
            registration.setUserId(userId);
            registration.setUseName(useName);
            registration.setServletRequest(req);
            synchronized (threads) {
                threads.put(threadId, registration);
            }
            if (sessionId != null) {
                synchronized (sessions) {
                    sessions.put(sessionId, registration);
                }
            }
        } else {
            registration.setThreadId(threadId);
            registration.setDateTime(dateTime);
            registration.setSystemName(systemName);
            registration.setUserId(userId);
            registration.setUseName(useName);
            registration.setServletRequest(req);
            synchronized (threads) {
                threads.put(threadId, registration);
            }
        }
        runningCount = runningCount + 1;
        if (runningCount > periodicLimit) {
            runningCount = 0;
            periodicHouseCleaning();
        }
    }

    /**
	 * <p>This method determines if an application user has been authenticated,
	 * and if not, performs the necessary steps to authenticate the user.</p>
	 *
	 * @return true if the user has been authenticated
	 * @since Eaasy Street 2.0.2
	 */
    public static boolean isAuthenticated() {
        return isAuthenticated(getServletRequest());
    }

    /**
	 * <p>This method determines if an application user has been authenticated,
	 * and if not, performs the necessary steps to authenticate the user.</p>
	 *
	 * @param req the <code>HttpServeltRequest</code> object
	 * @return true if the user has been authenticated
	 * @since Eaasy Street 2.0
	 */
    public static boolean isAuthenticated(HttpServletRequest req) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            return serviceLocator.isAuthenticated(req);
        } else {
            return false;
        }
    }

    /**
	 * <p>This method determines if an application user is authorized to access
	 * the specified resource.</p>
	 *
	 * @param type the type of resource identified by the <code>resource</code>
	 * parameter
	 * @param resource the name or identifier of the requested resource
	 * @return true if the user is authorized for the resource
	 * @since Eaasy Street 2.0
	 */
    public static boolean isAuthorized(String type, String resource) {
        return isAuthorized(type, resource, null, getServletRequest());
    }

    /**
	 * <p>This method determines if an application user is authorized to access
	 * the specified resource.</p>
	 *
	 * @param type the type of resource identified by the <code>resource</code>
	 * parameter
	 * @param resource the name or identifier of the requested resource
	 * @param function the function to be performed on the requested resource
	 * @return true if the user is authorized for the resource
	 * @since Eaasy Street 2.0
	 */
    public static boolean isAuthorized(String type, String resource, String function) {
        return isAuthorized(type, resource, function, getServletRequest());
    }

    /**
	 * <p>This method determines if an application user is authorized to access
	 * the specified resource.</p>
	 *
	 * @param type the type of resource identified by the <code>resource</code>
	 * parameter
	 * @param resource the name or identifier of the requested resource
	 * @param req the <code>HttpServeltRequest</code> object
	 * @return true if the user is authorized for the resource
	 * @since Eaasy Street 2.0
	 */
    public static boolean isAuthorized(String type, String resource, HttpServletRequest req) {
        return isAuthorized(type, resource, null, req);
    }

    /**
	 * <p>This method determines if an application user is authorized to access
	 * the specified resource.</p>
	 *
	 * @param type the type of resource identified by the <code>resource</code>
	 * parameter
	 * @param resource the name or identifier of the requested resource
	 * @param req the <code>HttpServeltRequest</code> object
	 * @param function the function to be performed on the requested resource
	 * @return true if the user is authorized for the resource
	 * @since Eaasy Street 2.0
	 */
    public static boolean isAuthorized(String type, String resource, String function, HttpServletRequest req) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            return serviceLocator.isAuthorized(req, type, resource, function);
        } else {
            return false;
        }
    }

    /**
	 * <p>Returns a String containing the real path for a given virtual path.
	 * For example, the path "/index.html" returns the absolute file path on
	 * the server's filesystem would be served by a request for
	 * "http://host/contextPath/index.html", where contextPath is the context
	 * path of this ServletContext.</p>
	 * 
	 * <p>The real path returned will be in a form appropriate to the computer
	 * and operating system on which the servlet container is running, including
	 * the proper path separators. This method returns null if the servlet
	 * container cannot translate the virtual path to a real path for any reason
	 * (such as when the content is being made available from a .war archive).</p>
	 * 
	 * @param path a <code>String</code> specifying a virtual path
	 * @return a <code>String</code> specifying the real path, or null if the
	 * translation cannot be performed
	 * @since Eaasy Street 2.9
	 */
    public static String getRealPath(String path) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        String returnValue = null;
        if (serviceLocator != null) {
            returnValue = serviceLocator.getRealPath(path);
        }
        return returnValue;
    }

    /**
	 * <p>This method returns a <code>Properties</code> object loaded
	 * with the EaasyStreet system properties.</p>
	 *
	 * @return the Eaasy Street system properties
	 * @since Eaasy Street 2.0.1
	 */
    public static Properties getProperties() {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        Properties returnValue = null;
        if (serviceLocator != null) {
            returnValue = serviceLocator.getProperties();
        }
        return returnValue;
    }

    /**
	 * <p>This method returns a <code>Properties</code> object loaded
	 * with the name/value pairs from the specified file.</p>
	 *
	 * @param source the name of the file containing the source data
	 * @return the requested properties
	 * @since Eaasy Street 2.0
	 */
    public static Properties getProperties(String source) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        Properties returnValue = null;
        if (serviceLocator != null) {
            returnValue = serviceLocator.getProperties(source);
        }
        return returnValue;
    }

    /**
	 * <p>This method returns the Struts "ApplicationResources".</p>
	 *
	 * @return the Struts "ApplicationResources"
	 * @since Eaasy Street 2.0
	 */
    public static MessageResources getApplicationResources() {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        MessageResources returnValue = null;
        if (serviceLocator != null) {
            returnValue = serviceLocator.getApplicationResources();
        }
        return returnValue;
    }

    /**
	 * <p>This method returns a <code>String</code> containing the fully
	 * formatted HTML code for the application menu options that are
	 * authorized for the current user.</p>
	 *
	 * @return the HTML code for the application menu options
	 * @since Eaasy Street 2.0
	 */
    public static String getNavigation() {
        return getNavigation(getServletRequest());
    }

    /**
	 * <p>This method returns a <code>String</code> containing the fully
	 * formatted HTML code for the application menu options that are
	 * authorized for the current user.</p>
	 *
	 * @param req the <code>HttpServeltRequest</code> object
	 * @return the HTML code for the application menu options
	 * @since Eaasy Street 2.0
	 */
    public static String getNavigation(HttpServletRequest req) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        String formattedNavigation = Constants.EMPTY_STRING;
        if (serviceLocator != null) {
            formattedNavigation = serviceLocator.getNavigation(req);
        }
        return formattedNavigation;
    }

    /**
	 * <p>This method sets the value of the user message queue. This is a
	 * nondestructive set, which simply adds this new message to whatever
	 * other messages may have accumulated previously during this processing
	 * cycle.</p>
	 * 
	 * @param message a <code>UserMessage</code> object containing the
	 * details of the user message
	 * @since Eaasy Street 2.0.2
	 */
    public static void setUserMessage(UserMessage message) {
        setUserMessage(getServletRequest(), message);
    }

    /**
	 * <p>This method sets the value of the user message queue. This is a
	 * nondestructive set, which simply adds this new message to whatever
	 * other messages may have accumulated previously during this processing
	 * cycle.</p>
	 * 
	 * @param req the <code>HttpServeltRequest</code> object
	 * @param message a <code>UserMessage</code> object containing the
	 * details of the user message
	 * @since Eaasy Street 2.0
	 */
    public static void setUserMessage(HttpServletRequest req, UserMessage message) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.setUserMessage(req, message);
        }
    }

    /**
	 * <p>Delivers a notification based on the parameters provided.</p>
	 *
	 * @param destination the destination to whom the notification
	 * should be sent
	 * @param templateId the identifying key of the template upon which
	 * the notification is to be based.
	 * @param sourceData the information or keys necessary to access the
	 * data required to resolve the symbolic variables in the template
	 * @return the delivered notification
	 * @since Eaasy Street 2.0.2
	 */
    public static DeliveredNotice notify(String destination, String templateId, Object sourceData) {
        DeliveredNotice deliveredNotice = null;
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            deliveredNotice = serviceLocator.notify(destination, templateId, sourceData);
        }
        return deliveredNotice;
    }

    /**
	 * <p>Delivers a notification based on the parameters provided.</p>
	 *
	 * @param destination the destination to whom the notification
	 * should be sent
	 * @param templateId the identifying key of the template upon which
	 * the notification is to be based.
	 * @param sourceData the information or keys necessary to access the
	 * data required to resolve the symbolic variables in the template
	 * @return the delivered notification
	 * @since Eaasy Street 2.0.2
	 */
    public static DeliveredNotice notify(String destination, NoticeTemplate template, Object sourceData) {
        DeliveredNotice deliveredNotice = null;
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            deliveredNotice = serviceLocator.notify(destination, template, sourceData);
        }
        return deliveredNotice;
    }

    /**
	 * <p>Delivers a notification based on the parameters provided.</p>
	 *
	 * @param destination the destination to whom the notification
	 * should be sent
	 * @param templateId the identifying key of the template upon which
	 * the notification is to be based.
	 * @param sourceData the information or keys necessary to access the
	 * data required to resolve the symbolic variables in the template
	 * @param deliveryMethod the method by which the notice will be
	 * delivered
	 * @return the delivered notification
	 * @since Eaasy Street 2.0.2
	 */
    public static DeliveredNotice notify(String destination, String templateId, Object sourceData, String deliveryMethod) {
        DeliveredNotice deliveredNotice = null;
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            deliveredNotice = serviceLocator.notify(destination, templateId, sourceData, deliveryMethod);
        }
        return deliveredNotice;
    }

    /**
	 * <p>Delivers a notification based on the parameters provided.</p>
	 *
	 * @param destination the destination to whom the notification
	 * should be sent
	 * @param templateId the identifying key of the template upon which
	 * the notification is to be based.
	 * @param sourceData the information or keys necessary to access the
	 * data required to resolve the symbolic variables in the template
	 * @param deliveryMethod the method by which the notice will be
	 * delivered
	 * @return the delivered notification
	 * @since Eaasy Street 2.0.2
	 */
    public static DeliveredNotice notify(String destination, NoticeTemplate template, Object sourceData, String deliveryMethod) {
        DeliveredNotice deliveredNotice = null;
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            deliveredNotice = serviceLocator.notify(destination, template, sourceData, deliveryMethod);
        }
        return deliveredNotice;
    }

    /**
	 * <p>This method is used to handle a single event. The <code>Event</code>
	 * object contains the key to the Event and any associated details further
	 * describing the specifics of the incident.</p>
	 * <p>This method will <strong>NOT</strong> throw an exception, so it
	 * should not be used for events that may result in an intentional
	 * exception. Any exceptions encountered will be caught and logged, but
	 * not passed on.</p>
	 * 
	 * @param <code>event</code> the <code>Event</code> object containing the specifics about the event
	 * @since Eaasy Street 2.0
	 */
    public static void handleSafeEvent(Event event) {
        handleSafeEvent(getServletRequest(), event);
    }

    /**
	 * <p>This method is used to handle a single event. The <code>Event</code>
	 * object contains the key to the Event and any associated details further
	 * describing the specifics of the incident. The <code>HttpServletRequest</code>
	 * object is also required, as some event responders may need access to
	 * information found in the request object.</p>
	 * <p>This method will <strong>NOT</strong> throw an exception, so it
	 * should not be used for events that may result in an intentional
	 * exception. Any exceptions encountered will be caught and logged, but
	 * not passed on.</p>
	 * 
	 * @param <code>req</code> the <code>HttpServletRequest</code> object associated with the application
	 * @param <code>event</code> the <code>Event</code> object containing the specifics about the event
	 * @since Eaasy Street 2.0
	 */
    public static void handleSafeEvent(HttpServletRequest req, Event event) {
        try {
            handleEvent(req, event);
        } catch (Exception e) {
            Exception x = new EventHandlingException(new EventDetails(event, null, req), e.toString(), e);
            logError(x.toString(), x);
            logError(e.toString(), e);
        }
        return;
    }

    /**
	 * <p>This method is used to handle a single event. The <code>Event</code>
	 * object contains the key to the Event and any associated details further
	 * describing the specifics of the incident.</p>
	 * 
	 * @param <code>event</code> the <code>Event</code> object containing the specifics about the event
	 * @throws EventHandlingException if there is a problem handling this event
	 * @throws EventDrivenException if an event responder responds by throwing an
	 * exception
	 * @since Eaasy Street 2.0
	 */
    public static void handleEvent(Event event) throws EventHandlingException, EventDrivenException {
        handleEvent(getServletRequest(), event);
    }

    /**
	 * <p>This method is used to handle a single event. The <code>Event</code>
	 * object contains the key to the Event and any associated details further
	 * describing the specifics of the incident. The <code>HttpServletRequest</code>
	 * object is also required, as some event responders may need access to
	 * information found in the request object.</p>
	 * 
	 * @param <code>req</code> the <code>HttpServletRequest</code> object associated with the application
	 * @param <code>event</code> the <code>Event</code> object containing the specifics about the event
	 * @throws EventHandlingException if there is a problem handling this event
	 * @throws EventDrivenException if an event responder responds by throwing an
	 * exception
	 * @since Eaasy Street 2.0
	 */
    public static void handleEvent(HttpServletRequest req, Event event) throws EventHandlingException, EventDrivenException {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator == null) {
            throw new EventHandlingException(new EventDetails(event, null, req), null, null);
        } else {
            serviceLocator.handleEvent(req, event);
        }
    }

    /**
	 * <p>This method is used to obtain a Java class identified by the
	 * <code>className</code> parameter. The class name should be a fully
	 * qualified Java class name of a class that is available to the
	 * application server. If the class cannot be loaded, this method
	 * will return <code>null</code>.
	 * 
	 * @param className
	 * a <code>String</code> containing the name of the requested class
	 * @return the requested class, or <code>null</code> if the class
	 * cannot be loaded
	 * @since Eaasy Street 2.3.3
	 */
    public static Class getClass(String className) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            log.warn(e.toString(), e);
        }
        if (serviceLocator != null) {
            return serviceLocator.getClass(className);
        } else {
            return null;
        }
    }

    /**
	 * <p>This method is used to obtain a shared instance of a Java class
	 * identified by the <code>className</code> parameter. The class name
	 * should be a fully qualified Java class name of a class that is 
	 * available to the application server. If the class cannot be loaded,
	 * this method will return <code>null</code>.
	 * 
	 * @param className a <code>String</code> containing the name of the
	 * requested class
	 * @return <code>instance</code> - a shared instance of the requested
	 * class, or <code>null</code> if the class cannot be loaded
	 * @since Eaasy Street 2.0
	 */
    public static Object getInstance(String className) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            log.warn(e.toString(), e);
        }
        if (serviceLocator != null) {
            return serviceLocator.getInstance(className);
        } else {
            return null;
        }
    }

    /**
	 * <p>This method is used to obtain a new instance of a Java class
	 * identified by the <code>className</code> parameter. The class name
	 * should be a fully qualified Java class name of a class that is 
	 * available to the application server. If the class cannot be loaded,
	 * this method will return <code>null</code>.
	 * 
	 * @param className a <code>String</code> containing the name of the
	 * requested class
	 * @return <code>instance</code> - a new instance of the requested
	 * class, or <code>null</code> if the class cannot be loaded
	 * @since Eaasy Street 2.0.2
	 */
    public static Object getNewInstance(String className) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            log.warn(e.toString(), e);
        }
        if (serviceLocator != null) {
            return serviceLocator.getNewInstance(className);
        } else {
            return null;
        }
    }

    /**
	 * Log a message with trace log level.
	 * 
	 * @param message log this message
	 * @since Eaasy Street 2.0
	 */
    public static void logTrace(Object message) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.logTrace(getServletRequest(), message, null);
        } else {
            log.trace(message);
        }
    }

    /**
	 * Log an error with trace log level.
	 * 
	 * @param message log this message
	 * @param t log this cause
	 * @since Eaasy Street 2.0
	 */
    public static void logTrace(Object message, Throwable t) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.logTrace(getServletRequest(), message, t);
        } else {
            log.trace(message, t);
        }
    }

    /**
	 * Log a message with debug log level.
	 * 
	 * @param message log this message
	 * @since Eaasy Street 2.0
	 */
    public static void logDebug(Object message) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.logDebug(getServletRequest(), message, null);
        } else {
            log.debug(message);
        }
    }

    /**
	 * Log an error with debug log level.
	 * 
	 * @param message log this message
	 * @param t log this cause
	 * @since Eaasy Street 2.0
	 */
    public static void logDebug(Object message, Throwable t) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.logDebug(getServletRequest(), message, t);
        } else {
            log.debug(message, t);
        }
    }

    /**
	 * Log a message with info log level.
	 * 
	 * @param message log this message
	 * @since Eaasy Street 2.0
	 */
    public static void logInfo(Object message) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.logInfo(getServletRequest(), message, null);
        } else {
            log.info(message);
        }
    }

    /**
	 * Log an error with info log level.
	 * 
	 * @param message log this message
	 * @param t log this cause
	 * @since Eaasy Street 2.0
	 */
    public static void logInfo(Object message, Throwable t) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.logInfo(getServletRequest(), message, t);
        } else {
            log.info(message, t);
        }
    }

    /**
	 * Log a message with warn log level.
	 * 
	 * @param message log this message
	 * @since Eaasy Street 2.0
	 */
    public static void logWarn(Object message) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.logWarn(getServletRequest(), message, null);
        } else {
            log.warn(message);
        }
    }

    /**
	 * Log an error with warn log level.
	 * 
	 * @param message log this message
	 * @param t log this cause
	 * @since Eaasy Street 2.0
	 */
    public static void logWarn(Object message, Throwable t) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            if (e != null) {
                log.fatal(e.toString(), e);
                if (e.getClass().isAssignableFrom(EaasyStreetSystemError.class)) {
                    EaasyStreetSystemError ese = (EaasyStreetSystemError) e;
                    if (ese.getRootCause() != null) {
                        log.fatal(ese.getRootCause().toString(), ese.getRootCause());
                    }
                }
            }
        }
        if (serviceLocator != null) {
            serviceLocator.logWarn(getServletRequest(), message, t);
        } else {
            log.warn(message, t);
        }
    }

    /**
	 * Log a message with error log level.
	 * 
	 * @param message log this message
	 * @since Eaasy Street 2.0
	 */
    public static void logError(Object message) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.logError(getServletRequest(), message, null);
        } else {
            log.error(message);
        }
    }

    /**
	 * Log an error with error log level.
	 * 
	 * @param message log this message
	 * @param t log this cause
	 * @since Eaasy Street 2.0
	 */
    public static void logError(Object message, Throwable t) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.logError(getServletRequest(), message, t);
        } else {
            log.error(message, t);
        }
    }

    /**
	 * Log a message with fatal log level.
	 * 
	 * @param message log this message
	 * @since Eaasy Street 2.0
	 */
    public static void logFatal(Object message) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.logFatal(getServletRequest(), message, null);
        } else {
            log.fatal(message);
        }
    }

    /**
	 * Log an error with fatal log level.
	 * 
	 * @param message log this message
	 * @param t log this cause
	 * @since Eaasy Street 2.0
	 */
    public static void logFatal(Object message, Throwable t) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.logFatal(getServletRequest(), message, t);
        } else {
            log.fatal(message, t);
        }
    }

    /**
	 * <p>This method is used to obtain the name of the currently active
	 * presentation theme.</p>
	 * 
	 * @return the name of the currently active presentation theme
	 * @since Eaasy Street 2.3
	 */
    public static String getPresentationTheme() {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            return serviceLocator.getPresentationTheme(getServletRequest());
        } else {
            return null;
        }
    }

    /**
	 * <p>This method is used to obtain the value of a configuration property
	 * related to the current presentation "theme" based on the <code>key</code>
	 * passed as an argument to the method.</p>
	 * 
	 * @param key
	 * the <code>String</code> containing the value of the key
	 * @return the value associated with the given key. If there is no
	 * configuration property associated with this key, or if there is an error
	 * obtaining the value, this method will return <code>null</code>.
	 * @since Eaasy Street 2.3
	 */
    public static String getThemeProperty(String key) {
        return getThemeProperty(getPresentationTheme(), key);
    }

    /**
	 * <p>This method is used to obtain the value of a configuration property
	 * related to the current presentation "theme" based on the <code>key</code>
	 * passed as an argument to the method.</p>
	 * 
	 * @param theme
	 * the <code>String</code> containing the name of the theme
	 * @param key
	 * the <code>String</code> containing the value of the key
	 * @return the value associated with the given key. If there is no
	 * configuration property associated with this key, or if there is an error
	 * obtaining the value, this method will return <code>null</code>.
	 * @since Eaasy Street 2.3
	 */
    public static String getThemeProperty(String theme, String key) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            return serviceLocator.getThemeProperty(theme, key);
        } else {
            return null;
        }
    }

    /**
	 * <p>This method is used to obtain the value of a configuration property
	 * based on the <code>key</code> passed as an argument to the method.
	 * 
	 * @param key the <code>String</code> containing the value of the key
	 * @return <code>value</code> - the value associated with the given key.
	 * If there is no configuration property associated with this key, or if
	 * there is an error obtaining the value, this method will return
	 * <code>null</code>.
	 * @since Eaasy Street 2.0
	 */
    public static String getProperty(String key) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            return serviceLocator.getProperty(key);
        } else {
            return null;
        }
    }

    /**
	 * <p>This method is used to obtain the value of a <code>ServletContext</code>
	 * attribute based on the <code>key</code> passed as an argument to
	 * the method.
	 * 
	 * @param key the <code>String</code> containing the value of the key
	 * @return <code>value</code> - the value associated with the given key.
	 * If there is no context attribute associated with this key, or if
	 * there is an error obtaining the value, this method will return
	 * <code>null</code>.
	 * @since Eaasy Street 2.0
	 */
    public static Object getContextAttribute(String key) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            return serviceLocator.getContextAttribute(key);
        } else {
            return null;
        }
    }

    /**
	 * <p>This method is used to set the value of a <code>ServletContext</code>
	 * attribute based on the <code>key</code> passed as an argument to
	 * the method.
	 * 
	 * @param key the <code>String</code> containing the value of the key
	 * @param value the <code>Object</code> to be associated with the given key.
	 * @since Eaasy Street 2.0
	 */
    public static void setContextAttribute(String key, Object value) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.setContextAttribute(key, value);
        }
    }

    /**
	 * <p>This method is used to remove a <code>ServletContext</code>
	 * attribute based on the <code>key</code> passed as an argument to
	 * the method.
	 * 
	 * @param key the <code>String</code> containing the value of the key
	 * @since Eaasy Street 2.0.2
	 */
    public static void removeContextAttribute(String key) {
        ServiceLocator serviceLocator = null;
        try {
            serviceLocator = getServiceLocator();
        } catch (Exception e) {
            logWarn(e.toString(), e);
        }
        if (serviceLocator != null) {
            serviceLocator.removeContextAttribute(key);
        }
    }

    /**
	 * <p>Returns true if Eaasy Street has been initialized and the current
	 * thread is registered; otherwise, returns false.</p>
	 * 
	 * @return true if Eaasy Street has been initialized and the current
	 * thread is registered; otherwise, returns false
	 * @since Eaasy Street 2.6.4
	 */
    public static boolean isEaasyStreetActive() {
        boolean isActive = false;
        String thisThread = Thread.currentThread().getName();
        if (threads.containsKey(thisThread)) {
            ServiceRegistration registration = (ServiceRegistration) threads.get(thisThread);
            String systemName = registration.getSystemName();
            if (serviceLocators.containsKey(systemName)) {
                ServiceLocator serviceLocator = (ServiceLocator) serviceLocators.get(systemName);
                if (serviceLocator.getInitializationError() == null) {
                    isActive = true;
                }
            }
        }
        return isActive;
    }

    /**
	 * Returns the HttpServletRequest object associated with the current
	 * thread. If the thread is not registered, or there is no request
	 * object associated with the registered threadd, this method will
	 * return null.
	 * 
	 * @return the HttpServletRequest object associated with the current
	 * thread
	 * @since Eaasy Street 2.0
	 */
    public static HttpServletRequest getServletRequest() {
        HttpServletRequest request = null;
        String thisThread = Thread.currentThread().getName();
        if (threads.containsKey(thisThread)) {
            ServiceRegistration registration = (ServiceRegistration) threads.get(thisThread);
            request = registration.getServletRequest();
        }
        return request;
    }

    /**
	 * Returns the correct instance of <code>EaasyStreet</code> for the
	 * current thread. If the correct instance cannot be found, or if the
	 * correct instance was not propertly initialized, this method will
	 * throw an <code>EaasyStreetSystemError</code>.
	 * 
	 * @return the proper instance for the current thread
	 * @since Eaasy Street 2.0
	 */
    private static ServiceLocator getServiceLocator() throws EaasyStreetSystemError {
        ServiceLocator serviceLocator = null;
        String thisThread = Thread.currentThread().getName();
        if (threads.containsKey(thisThread)) {
            ServiceRegistration registration = (ServiceRegistration) threads.get(thisThread);
            String systemName = registration.getSystemName();
            if (serviceLocators.containsKey(systemName)) {
                serviceLocator = (ServiceLocator) serviceLocators.get(systemName);
                if (serviceLocator.getInitializationError() != null) {
                    Throwable ie = serviceLocator.getInitializationError();
                    systemError(EVI0010T, new String[] { systemName, ie.toString() }, ie);
                }
            } else {
                systemError(EVI0004T, new String[] { systemName }, null);
            }
        } else {
            systemError(EVI0011T, null, null);
        }
        return serviceLocator;
    }

    /**
	 * <p>This method is used to handle internal system errors.</p>
	 * 
	 * @param internalEventKey the key to the error
	 * @param args an Object array containing additional details related to the error
	 * @param rootCause an optional throwable related to the root cause of the error
	 * @throws EaasyStreetSystemError
	 * @since Eaasy Street 2.0
	 */
    private static void systemError(String internalEventKey, Object[] args, Throwable rootCause) throws EaasyStreetSystemError {
        log.error(localStrings.getMessage(internalEventKey, args), rootCause);
        throw new EaasyStreetSystemError(localStrings.getMessage(internalEventKey, args), rootCause);
    }

    /**
	 * <p>Periodically cleans out unused threads. New in 2.0, this method
	 * eliminates the need to "unregister" for services -- registrations
	 * are just dropped after a certain time. This approach ensures that
	 * there are no hanging threads from systems that failed to perform
	 * the "unregister" process.</p>
	 * 
	 * @since Eaasy Street 2.0
	 */
    private static void periodicHouseCleaning() {
        log.info("[In] EaasyStreet.periodicHouseCleaning()");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);
        Date cutOff = cal.getTime();
        log.info("Cut off date/time = " + cutOff);
        log.info("Thread size (before) = " + threads.size());
        log.info("Session size (before) = " + sessions.size());
        ArrayList keys = new ArrayList();
        synchronized (threads) {
            Iterator i = threads.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                ServiceRegistration registration = (ServiceRegistration) threads.get(key);
                Date regDate = registration.getDateTime();
                if (regDate.before(cutOff)) {
                    keys.add(key);
                }
            }
            i = keys.iterator();
            while (i.hasNext()) {
                threads.remove(i.next());
            }
        }
        keys = new ArrayList();
        synchronized (sessions) {
            Iterator i = sessions.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                ServiceRegistration registration = (ServiceRegistration) sessions.get(key);
                Date regDate = registration.getDateTime();
                if (regDate.before(cutOff)) {
                    keys.add(key);
                }
            }
            i = keys.iterator();
            while (i.hasNext()) {
                sessions.remove(i.next());
            }
        }
        log.info("Thread size (after) = " + threads.size());
        log.info("Session size (after) = " + sessions.size());
        log.info("[Out] EaasyStreet.periodicHouseCleaning()");
    }
}
