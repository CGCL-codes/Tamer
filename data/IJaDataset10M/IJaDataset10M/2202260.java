package com.volantis.mcs.servlet;

import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.mcs.application.ApplicationRegistry;
import com.volantis.mcs.application.ApplicationRegistryContainer;
import com.volantis.mcs.application.MarinerApplication;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.http.servlet.HttpServletFactory;
import com.volantis.mcs.internal.DefaultInternalApplicationContextFactory;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.ExternalPathToInternalURLMapper;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.synergetics.log.LogDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class provides a servlet specific interface to the MarinerApplication.
 * <p>
 * Only one instance of a MarinerServletApplication must be created within
 * a single servlet context.
 * <p>
 * To use this class from within a jsp page use the following code:
 * <pre> &lt;jsp:useBean id="marinerApplication"
 *              class="com.volantis.mcs.servlet.MarinerServletApplication"
 *              scope="application"&gt;
 * &lt;/jsp:useBean&gt;
 * &lt;%marinerApplication.initialize (application);%&gt;</pre>
 * <p>
 * To use this class from within a servlet use the following code:
 * <pre>MarinerServletApplication marinerApplication = MarinerServletApplication.getInstance (context);</pre>
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate
 */
public class MarinerServletApplication extends MarinerApplication {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(MarinerServletApplication.class);

    /**
   * The constant which is the name of the attribute in the ServletContext
   * which is used to hold the reference to the current instance of this
   * class.
   * <h2>
   * This MUST match the name specified in the jsp:useBean directive in the
   * JSP pages.
   * </h2>
   */
    private static final String MARINER_APPLICATION_NAME = "marinerApplication";

    /**
   * The constant which is the name of the attribute in the ServletContext
   * which is used to hold the reference to the current instance of the
   * Volantis bean.
   * <h2>
   * This MUST match the name specified in the old jsp:useBean directives
   * in the JSP pages.
   * </h2>
   */
    private static final String MARINER_VOLANTIS_NAME = "volantis";

    /**
   * A flag which indicates whether this class has completed successfully.
   * @see #checkInitializationState
   */
    private boolean initialized;

    /**
   * The name used to store the device in the session.  Normally MCS uses the
   * device stored in the MarinerSessionContext, but the public API method in
   * this class doesn't have access to it so has to store the resolved device
   * in the HttpSession.
   * It is not wasted however, as DefaultApplicationContextFactory#resolveDevice
   * can check the session for the device resolved here and reuse it.
   *
   * @see #getDevice
   */
    static final String SESSION_DEVICE_NAME = Device.class.getName();

    /**
     * Sometimes the initialization of this class may fail. If it does so then
     * this Throwable object contains a reference to the exception which was
     * thrown. Subsequent attempts to retrieve this instance will result in
     * a ServletException being thrown which has this as its root cause.
     * @see #checkInitializationState
     */
    private Throwable initializationFailure;

    /**
   * The ServletContext within which this application is running.
   */
    private ServletContext servletContext;

    /**
     * This is the current instance of the MarinerServletApplication
     */
    private static MarinerServletApplication instance;

    /**
     * The factory responsible for creating servlet specific instances of
     * objects.
     */
    private final HttpServletFactory servletFactory;

    /**
     * Get the MarinerServletApplication instance for the specified
     * ServletContext.
     * <p>
     * This method will create and initialise it if it is not already done so.
     * @param context The ServletContext within which the mariner application is
     * running.
     * @return The current instance of the MarinerServletApplication.
     * @throws ServletException If there was a problem retrieving the object.
     */
    public static MarinerServletApplication getInstance(ServletContext context) throws ServletException {
        MarinerServletApplication application;
        synchronized (context) {
            application = (MarinerServletApplication) context.getAttribute(MARINER_APPLICATION_NAME);
            if (application == null) {
                application = new MarinerServletApplication();
                context.setAttribute(MARINER_APPLICATION_NAME, application);
                application.initializeInternal(context);
            }
            instance = application;
        }
        return application;
    }

    /**
     * Get the MarinerServletApplication instance for the specified
     * ServletContext.
     * <p>
     * If requested this method will create and initialise it if it is not
     * already done so.
     * @param context The ServletContext within which the mariner application is
     * running.
     * @param create If true then this method should create the object if it is
     * not already present.
     * @return The current instance of the MarinerServletApplication, or null if
     * it was not present and was not created.
     * @throws ServletException If there was a problem retrieving the object.
     * @deprecated Use findInstance to determine the existance of or retrieve
     * an existing MarinerServletApplication for the ServletContext and
     * getInstance(context) to get and if necessary create a the
     * MarinerServletApplicationContext for the ServletContext.
     */
    public static MarinerServletApplication getInstance(ServletContext context, boolean create) throws ServletException {
        MarinerServletApplication application;
        synchronized (context) {
            application = findInstance(context);
            if (application == null) {
                if (!create) {
                    final Volantis volantisBean = findVolantisBean(context);
                    if (volantisBean == null) {
                        return null;
                    }
                }
                application = new MarinerServletApplication();
                context.setAttribute(MARINER_APPLICATION_NAME, application);
            }
            application.initializeInternal(context);
        }
        return application;
    }

    /**
     * This method will find the existing MarinerServletApplication for a
     * specified ServletContext.
     *
     * @param context The ServletContext within which the mariner application is
     * running.
     * @return the MarinerServletApplication in the specified ServletContext or
     * null if none was present.
     */
    private static MarinerServletApplication findInstance(ServletContext context) {
        return (MarinerServletApplication) context.getAttribute(MARINER_APPLICATION_NAME);
    }

    /**
     * Identify the Device from the HTTP Headers specified in the
     * HttpServletRequest.
     *
     * @param request The HttpServletRequest made by the Device to
     * be identified.
     * @return The identified Device.
     * @throws RepositoryException If there is a problem retrieving the Device.
     */
    public Device getDevice(HttpServletRequest request) throws RepositoryException {
        Volantis volantisBean = ApplicationInternals.getVolantisBean(this);
        if (volantisBean == null) {
            throw new IllegalStateException("MCS Application not initialised");
        }
        HttpSession session = request.getSession(true);
        if (logger.isDebugEnabled()) {
            logger.debug("Session information: " + session.toString());
            logger.debug("Session max interval time: " + session.getMaxInactiveInterval());
        }
        DefaultDevice device = (DefaultDevice) session.getAttribute(SESSION_DEVICE_NAME);
        if (device != null && !device.isValid()) {
            device = null;
        }
        if (device == null) {
            DeviceRepository deviceRepository;
            try {
                deviceRepository = getRuntimeDeviceRepository();
                HttpHeaders headers = servletFactory.getHTTPHeaders(request);
                String defaultDeviceName = volantisBean.getDevicesConfiguration().getDefaultDeviceName();
                device = (DefaultDevice) deviceRepository.getDevice(headers, defaultDeviceName);
            } catch (DeviceRepositoryException e) {
                throw new RepositoryException(e);
            }
            try {
                session.setAttribute(SESSION_DEVICE_NAME, device);
            } catch (IllegalStateException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Unable to store the device in the session", e);
                    e.printStackTrace();
                }
            }
        }
        return device;
    }

    /**
   * Create a new <code>MarinerServletApplication</code>.
   * <p>
   * This is only public because it has to be invoked by code generated from
   * jsp:useBean. Other users must use {@link #getInstance}.
   * </p>
   *
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
    public MarinerServletApplication() {
        servletFactory = HttpServletFactory.getDefaultInstance();
    }

    /**
     * Initialize the object.
     * <p>
     * This must only be called by the jsp:useBean code, all user code must call
     * the {@link #getInstance} method.
     * </p>
     * @param context The ServletContext which is used to contexture the
     * application.
     */
    public void initialize(ServletContext context) throws ServletException {
        synchronized (context) {
            initializeInternal(context);
        }
    }

    /**
     * Initialize the object.
     * <p>
     * <strong>NOTE: This method must be called while synchronized on the
     * ServletContext.</strong>
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    private void initializeInternal(ServletContext context) throws ServletException {
        if (checkInitializationState()) {
            return;
        }
        servletContext = context;
        try {
            ApplicationRegistry ar = ApplicationRegistry.getSingleton();
            ApplicationRegistryContainer arc = new ApplicationRegistryContainer(new DefaultInternalApplicationContextFactory(), new DefaultServletApplicationContextFactory());
            ar.registerApplication(ApplicationRegistry.DEFAULT_APPLICATION_NAME, arc);
            if (logger.isDebugEnabled()) {
                logger.debug("Registered default application");
            }
            Volantis volantisBean = getVolantisBean(servletContext);
            ApplicationInternals.setVolantisBean(this, volantisBean);
            initialized = true;
        } catch (Throwable t) {
            initializationFailure = t;
            logger.error("unexpected-exception", t);
            throw new ServletException("Mariner application could not be initialised", initializationFailure);
        }
    }

    /**
   * Check to see what the initialization state of this object is.
   * @return True if it has been successfully initialized, false if it has
   * not yet been initialized.
   * @throws ServletException If a previous attempt to initialize it failed.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
    private boolean checkInitializationState() throws ServletException {
        if (initialized) {
            return true;
        }
        if (initializationFailure != null) {
            throw new ServletException("Mariner application was not correctly initialized", initializationFailure);
        }
        return false;
    }

    /**
     * Get the Volantis bean instance for the specified ServletContext.
     * <p>
     * If requested this method will create and initialise it if it is not
     * already done so.
     * <p>
     * <strong>NOTE: This method must be called while synchronized on the
     * ServletContext.</strong>
     *
     * @param context The ServletContext within which the Volantis bean is
     * running.
     * @return The current instance of the Volantis bean, or null if
     * it was not present and was not created.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    private Volantis getVolantisBean(ServletContext context) throws ConfigurationException {
        Volantis volantis = findVolantisBean(context);
        if (volantis == null) {
            volantis = new Volantis();
            context.setAttribute(MARINER_VOLANTIS_NAME, volantis);
        }
        ExternalPathToInternalURLMapper pathURLMapper;
        pathURLMapper = new ServletExternalPathToInternalURLMapper(servletContext);
        ConfigContext cc = new ServletConfigContext(context);
        volantis.initializeInternal(pathURLMapper, cc, this);
        return volantis;
    }

    /**
     * Look for an existing instance of the Volantis bean in the context
     * and return it if found.
     * @param context the ServletContext
     * @return the Volantis bean within the context if it exists; otherwise
     * null.
     */
    static Volantis findVolantisBean(ServletContext context) {
        return (Volantis) context.getAttribute(MARINER_VOLANTIS_NAME);
    }

    public ServletContext getServletContext() {
        return (this.servletContext);
    }

    /**
     * This method turns MarinerApplicationContext into a global variable. It is a complete hack and is only being
     * used to enable the Volantis Bean to be moved to runtime.
     * @deprecated Do not use this method
     * @return  The current instance of thw MarinerServletApplication.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    public static MarinerServletApplication getInstance() {
        return instance;
    }
}
