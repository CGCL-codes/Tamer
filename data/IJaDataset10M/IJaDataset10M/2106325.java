package com.ramsayconz.wocore;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import com.ramsayconz.wocore.woc.ErrorPage;
import com.ramsayconz.wocore.woc.TimeoutPage;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.appserver.WOSession;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOEventCenter;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.jdbcadaptor.JDBCAdaptor;

/**
 *  CoreApplication
 *  
 *  CoreApplication extends the capabilities of the WebObjects WOApplication by
 *  initializing logging through Log4J, reading all the properties into a key-value
 *  coding container ...
 */
public class CoreApplication extends er.extensions.appserver.ERXApplication {

    private static final Logger logger = Logger.getLogger(CoreApplication.class);

    private boolean _traceRequests, _traceResponses;

    private NSTimestamp _startTime, _endTime;

    protected static CoreHelpRepo _helpRepo;

    /**
	 * CoreApplication.properties is a static variable containing all the collected Properties
	 */
    public static CoreProperties properties = null;

    public static CoreConfiguration configuration = null;

    /**
     *  This basic override of the WebObjects application class adds several things:
     *
     *      ... establishes Log4J as the logging mechanism
     *
     *      ... transfers ALL properties to CoreProperties (implementing Key Value Coding)
     *
     *      ... (optionally) uses the Jakarta Configuration library to read files
     */
    public CoreApplication() {
        super();
        logger.trace("+++ constructor // establish notification catchers ...");
        NSNotificationCenter.defaultCenter().addObserver(this, new NSSelector<Object>("applicationWillFinishLaunching", new Class[] { NSNotification.class }), WOApplication.ApplicationWillFinishLaunchingNotification, null);
        NSNotificationCenter.defaultCenter().addObserver(this, new NSSelector<Object>("applicationDidFinishLaunching", new Class[] { NSNotification.class }), WOApplication.ApplicationDidFinishLaunchingNotification, null);
        NSNotificationCenter.defaultCenter().addObserver(this, new NSSelector<Object>("modelAdded", new Class[] { NSNotification.class }), EOModelGroup.ModelAddedNotification, null);
        NSNotificationCenter.defaultCenter().addObserver(this, new NSSelector<Object>("sessionDidCreate", new Class[] { NSNotification.class }), WOSession.SessionDidCreateNotification, null);
        CoreApplication.properties = new CoreProperties(System.getProperties());
        SimpleDateFormat sdf = new SimpleDateFormat("MMMdd/yy HH:mm");
        CoreApplication.properties.setProperty("startTimeString", sdf.format(new Date()));
        if (CoreApplication.properties.getBoolean("application.begin.dumpProperties", "FALSE")) {
            CoreApplication.properties.alphaDump(true);
        }
        if (CoreApplication.properties.getBoolean("application.begin.jakartaConfigUsed", "FALSE")) {
            CoreApplication.configuration = new CoreConfiguration(CoreApplication.properties.getString("application.begin.jakartaConfigFile", "Configuration"));
            if ((null != CoreApplication.configuration) && (CoreApplication.properties.getBoolean("application.begin.dumpConfiguration", "FALSE"))) CoreApplication.configuration.alphaDump(true);
        }
        this._traceRequests = CoreApplication.properties.getBoolean("application.trace.requests", "FALSE");
        this._traceResponses = CoreApplication.properties.getBoolean("application.trace.responses", "FALSE");
        setSessionTimeOut(new Integer(CoreApplication.properties.getInt("application.begin.sessionTimeoutMinutes", "5") * 60));
    }

    @Override
    public void terminate() {
        logger.info("--- .terminate()");
        super.terminate();
    }

    @Override
    public WOResponse dispatchRequest(WORequest request) {
        if (this._traceRequests) {
            logger.info("+---- request/response start ---------------------------------");
            logger.info("Starting to dispatch URL: " + request.uri());
            logger.info("                 request: " + request);
        }
        this._startTime = new NSTimestamp();
        WOResponse response = super.dispatchRequest(request);
        this._endTime = new NSTimestamp();
        if (this._traceResponses) {
            logger.info("                response: " + response);
            logger.info("Finished dispatching URL: " + request.uri());
            logger.info("        time to complete: " + (this._endTime.getTime() - this._startTime.getTime()));
        }
        return response;
    }

    @Override
    public WOResponse handleException(Exception anException, WOContext aContext) {
        logger.trace("!-- .handleException", anException);
        if (anException.getClass().getName().equals("java.lang.OutOfMemoryError")) {
            logger.fatal("Trapped a completely fatal OutOfMemory error -- goodbye!");
            System.exit(0);
        }
        super.handleException(anException, aContext);
        if (aContext.hasSession()) aContext.session().terminate();
        ErrorPage errorPage = (ErrorPage) pageWithName(ErrorPage.class.getName(), aContext);
        errorPage.setException(anException);
        return errorPage.generateResponse();
    }

    @Override
    public WOResponse handleSessionRestorationErrorInContext(WOContext aContext) {
        logger.info("!-- .handleSessionRestorationErrorInContext(...)");
        logger.info("--- context: " + aContext + " and context.session(): " + aContext.session());
        TimeoutPage timeoutPage = (TimeoutPage) pageWithName(TimeoutPage.class.getName(), aContext);
        return timeoutPage.generateResponse();
    }

    /**
     * Notification method called when the application posts
     * the notification {@link WOApplication#ApplicationWillFinishLaunchingNotification}. 
     * This method calls subclasses' {@link #willFinishLaunchingApplication} method. 
     * 
     * @param n notification that is posted after the WOApplication
     *      has been constructed, but before the application is
     *      ready for accepting requests.
     */
    public final void applicationWillFinishLaunching(NSNotification n) {
        logger.trace("!-- " + WOApplication.ApplicationWillFinishLaunchingNotification + " [" + n + "]");
        EOEventCenter.setPassword(System.getProperty("EOEventLoggingPassword"));
        CoreApplication.properties.setProperty("applicationInstance", getInstance());
        CoreApplication.properties.setProperty("applicationPort", port().toString());
        applicationWillFinishLaunching();
    }

    /**
     * Override this to perform application initialization. (optional)
     */
    public void applicationWillFinishLaunching() {
    }

    public final void applicationDidFinishLaunching(NSNotification n) {
        logger.trace("!-- .didFinishLaunchingApplication [" + n + "]");
        applicationDidFinishLaunching();
    }

    /**
     * Override this to perform tasks after the application has been initialized. (optional)
     */
    public void applicationDidFinishLaunching() {
    }

    /**
     * Notification method called when an EOModel is accumulated
     */
    public final void modelAdded(NSNotification n) {
        EOModel eoModel = (EOModel) n.object();
        logger.info("!-- didAddModelToEOModelGroup [" + eoModel.name() + "]");
        adjustEOModel(eoModel);
    }

    /**
     * Called when the session posts the notification "SessionDidCreateNotification".
     * This method calls subclasses' {@link #didCreateSession} method.
     *
     * @param n - the Session instance.
     */
    public final void sessionDidCreate(NSNotification n) {
        logger.trace("!-- " + WOSession.SessionDidCreateNotification + " [" + ((WOSession) n.object()).sessionID() + "]");
        sessionDidCreate(((WOSession) n.object()).sessionID());
    }

    /**
     * Override this to perform session initialization. (optional)
     */
    public void sessionDidCreate(String sessionID) {
    }

    /**
     * Adjusts the connectionDictionaries for an EOModel from Property values
     * mentioned in the properties / configuration files (NOTE: UPPER CASE).
     *
     *			    user <-- "EOM.USER.DATABASE";
     *			password <-- "EOM.PASS.DATABASE";
     *	             URL <-- "EOM.JDBC.DATABASE.";
     */
    @SuppressWarnings("unchecked")
    private void adjustEOModel(EOModel model) {
        String eoConnStringUser, eoConnStringPass, eoConnStringJDBC;
        String modelName = model.name().toUpperCase();
        if (null == model.connectionDictionary()) {
            logger.warn("--- adjustEOModel - connectionDictionary is null in: " + modelName + ".");
            return;
        }
        logger.trace("--- adjustEOModel - adjusting connectionDictionary for: " + modelName + ".");
        eoConnStringUser = getConfProp("EOM.USER." + modelName);
        eoConnStringPass = getConfProp("EOM.PASS." + modelName);
        eoConnStringJDBC = getConfProp("EOM.JDBC." + modelName, "-");
        @SuppressWarnings("rawtypes") NSMutableDictionary dict = new NSMutableDictionary(model.connectionDictionary());
        boolean gotEOModelAdustment = false;
        logger.trace("--- adjustEOModel - eoConnStringUser is: " + eoConnStringUser);
        if (eoConnStringUser != null && eoConnStringUser.length() > 1) {
            dict.setObjectForKey(("-NONE-".equalsIgnoreCase(eoConnStringUser)) ? "" : eoConnStringUser, JDBCAdaptor.UsernameKey);
            gotEOModelAdustment = true;
        }
        logger.trace("--- adjustEOModel - eoConnStringPass is: ****");
        if (eoConnStringPass != null && eoConnStringPass.length() > 1) {
            dict.setObjectForKey(("-NONE-".equalsIgnoreCase(eoConnStringPass)) ? "" : eoConnStringPass, JDBCAdaptor.PasswordKey);
            gotEOModelAdustment = true;
        }
        logger.trace("--- adjustEOModel - eoConnStringJDBC is: " + eoConnStringJDBC);
        if (eoConnStringJDBC != null && eoConnStringJDBC.length() > 1) {
            dict.setObjectForKey(eoConnStringJDBC, JDBCAdaptor.URLKey);
            gotEOModelAdustment = true;
        }
        logger.info("--- adjustEOModel - database: " + model.name().toUpperCase() + " using connection dictionary from " + ((gotEOModelAdustment) ? "props" : "model"));
        logger.trace("--- adjustEOModel - [user=" + dict.objectForKey(JDBCAdaptor.UsernameKey) + "/pass=" + (null == dict.objectForKey(JDBCAdaptor.PasswordKey) ? "NONE" : "****") + "/URL=" + dict.objectForKey(JDBCAdaptor.URLKey) + "]");
        model.setConnectionDictionary(dict);
    }

    public String getConfProp(String key) {
        return getConfProp(key, "");
    }

    public String getConfProp(String key, String defaultValue) {
        String resultConfProp = null;
        if (null != CoreApplication.configuration) {
            resultConfProp = CoreApplication.configuration.getString(key);
            logger.trace("getConfProp : configuration.getString(" + key + ")=" + resultConfProp);
        }
        if (null == resultConfProp) {
            resultConfProp = CoreApplication.properties.getString(key);
            logger.trace("getConfProp : properties.getString(" + key + ")=" + resultConfProp);
        }
        return (null == resultConfProp) ? defaultValue : resultConfProp;
    }

    private NSMutableDictionary<String, CoreSession> sessions = new NSMutableDictionary<String, CoreSession>();

    public NSDictionary<String, CoreSession> getSessionDictionary() {
        return this.sessions;
    }

    public void addSession(CoreSession session, String sessionID) {
        this.sessions.setObjectForKey(session, sessionID);
    }

    public void delSession(String sessionID) {
        this.sessions.removeObjectForKey(sessionID);
    }

    public CoreSession getSession(String sessionID) {
        return this.sessions.objectForKey(sessionID);
    }

    public String getInstance() {
        String logFile = WOApplication.application().outputPath();
        if (null == logFile || logFile.indexOf("-") < 0) {
            return "X";
        }
        return logFile.substring(logFile.indexOf("-") + 1);
    }

    public CoreHelpRepo getHelpRepo() {
        return _helpRepo;
    }
}
