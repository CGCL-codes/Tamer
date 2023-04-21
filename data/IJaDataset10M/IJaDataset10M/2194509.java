package hambo;

import java.util.*;
import java.text.*;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.http.Cookie;
import com.lutris.Enhydra;
import com.lutris.appserver.server.ApplicationException;
import com.lutris.appserver.server.StandardApplication;
import com.lutris.appserver.server.httpPresentation.*;
import com.lutris.appserver.server.session.*;
import com.lutris.appserver.server.user.User;
import com.lutris.util.*;
import hambo.svc.*;
import hambo.svc.log.*;
import hambo.internationalization.TranslationAdminService;
import hambo.internationalization.TranslationServiceManager;
import hambo.util.SubProperties;
import hambo.messaging.Messaging;
import hambo.app.util.Link;
import hambo.app.core.HamboApplicationManager;
import hambo.app.navigation.Application;
import portal.presentation.messenger.AttachmentViewer;

public class Portal extends StandardApplication {

    /** If true, get some debuggin output. Error messages that shouldn't
     * happen get written even if DEBUG is false. */
    private final boolean DEBUG = false;

    private static Config conf = null;

    /**
     * @deprecated Use {@link hambo.config.ConfigManager} instead.
     */
    public static Config getAppConfig() {
        return conf;
    }

    /**
     * Get a lot of parameters from the config and start up the server.
     * @param appConfig interface to the configuration file.
     */
    public void startup(Config appConfig) throws ApplicationException {
        super.startup(appConfig);
        conf = appConfig;
        try {
            System.err.println("Application server: " + Enhydra.getEnhydraLongName() + " " + Enhydra.getEnhydraVersion());
            String hamboPropFile = appConfig.getString("hambo.services.properties");
            Properties hamboProp = new Properties();
            FileInputStream propStream = null;
            try {
                if (hamboPropFile != null) {
                    propStream = new FileInputStream(hamboPropFile);
                    hamboProp.load(propStream);
                    (new ServiceManagerLoader(hamboProp)).loadServices();
                }
            } catch (ServiceManagerException sme) {
                System.err.println("Failed to load Translation services.");
            } finally {
                if (propStream != null) propStream.close();
            }
            Messaging.init(new SubProperties(hamboProp, "Messaging"));
        } catch (Exception e) {
            System.err.println("Failed to load hambo services.");
            e.printStackTrace();
        }
        HamboApplicationManager.getApplicationManager().loadApplications();
        try {
            Application.loadDefaultPages();
            System.err.println("\n------------------------------------------------------------------------");
            System.err.println("--------------- HAMBO PORTAL READY TO RECEIVE REQUESTS :-) ---------------");
            System.err.println("------------------------------------------------------------------------\n");
        } catch (Exception ex) {
            System.err.println("Exception during startup.");
            ex.printStackTrace();
        }
    }

    /**
     * This method is executed every time a page (or an object in the page) in
     * the application is loaded.
     * @param comms Object containing request, response and redirect objects.
     * @return Always returns false, as request handling is not done.
     */
    public boolean requestPreprocessor(HttpPresentationComms comms) throws Exception {
        String page = null;
        String query = null;
        try {
            page = comms.request.getRequestURI();
            query = comms.request.getQueryString();
            boolean isAttachment = (page.indexOf("attachment/") != -1);
            if (isAttachment) {
                doAttachment(comms, page);
                return true;
            } else {
                super.requestPreprocessor(comms);
            }
            int index = page.indexOf(";");
            String subpage = page;
            if (index != -1) subpage = page.substring(0, index);
            if (!subpage.endsWith(".po")) return false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            System.err.println("\nPre-processor... " + page + (query != null ? "?" + query : "") + " *** " + comms.request.getProtocol() + " *** " + comms.sessionData.getString("language"));
            if (comms.request.getParameter("portal_device") != null) {
                String userId = comms.sessionData.getString("userid");
                if (userId != null && comms.sessionData.get("device") != null) {
                    Session newSession = sessionManager.createSession();
                    try {
                        String[] keys = comms.sessionData.leafKeys();
                        for (int i = 0; i < keys.length; i++) {
                            if (!keys[i].equals("device")) {
                                Object dataValue = comms.sessionData.get(keys[i]);
                                newSession.getSessionData().set(keys[i], dataValue);
                            }
                        }
                        comms.session = newSession;
                        comms.sessionData = newSession.getSessionData();
                        comms.sessionData.set("userid", userId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (DEBUG) {
                for (Enumeration e = comms.request.getHeaderNames(); e.hasMoreElements(); ) {
                    String header = (String) e.nextElement();
                    System.err.println("Header-name=" + header + "  value=" + comms.request.getHeader(header));
                }
                Cookie[] cookies = comms.request.getCookies();
                if (cookies != null) {
                    System.err.print("Cookies [ ");
                    for (int i = 0; i < cookies.length; i++) System.err.print(cookies[i].getName() + "=" + cookies[i].getValue() + " ");
                    System.err.println(" ]");
                }
                List history = (List) comms.sessionData.get("history");
                if (history != null) {
                    System.err.println("--------- HISTORY -------");
                    for (Iterator i = history.iterator(); i.hasNext(); ) System.err.println(i.next().toString());
                    System.err.println("-------- /HISTORY -------");
                }
            }
            return false;
        } catch (ClientPageRedirectException redir) {
            throw redir;
        } catch (Exception e) {
            System.err.println("Portal.requestPreprocessor: Exception " + e.getMessage() + "\n");
            e.printStackTrace();
            throw e;
        } catch (Error e) {
            System.err.println(" Portal: requestPreprocessor: Error " + e.getMessage() + "\n");
            e.printStackTrace();
            throw e;
        }
    }

    private void doAttachment(HttpPresentationComms comms, String url) throws Exception {
        System.err.println("Attachment URL: " + url);
        String sessionKey = null;
        int indexQuest = url.indexOf("?");
        int indexAmp = url.indexOf("&");
        if (indexQuest != -1) sessionKey = url.substring(indexQuest + Link.SESSION_URL_PARAMETER_NAME.length() + 2, (indexAmp != -1) ? indexAmp : url.length()); else sessionKey = comms.request.getParameter("sk");
        if (sessionKey == null) return;
        System.err.println("Portal.doAttachment: Session key=" + sessionKey);
        comms.request.setRequestedSessionIdFromUrl(true);
        comms.request.setRequestedSessionIdFromCookie(false);
        comms.response.setSessionIdEncodeUrlRequired(true);
        comms.response.setSessionIdCookieRequired(false);
        comms.response.setSessionKey(sessionKey);
        comms.response.setSessionManager(comms.application.getSessionManager());
        comms.session = comms.application.getSessionManager().getSession(Thread.currentThread(), sessionKey);
        if (comms.session == null) System.err.println("Portal.doAttachment: Session is null!");
        if (comms.session != null && sessionManager.sessionExists(comms.session.getSessionKey())) {
            comms.sessionData = comms.session.getSessionData();
        }
        AttachmentViewer attach = new AttachmentViewer();
        attach.run(comms);
        comms.response.flush();
        requestPostProcessor(comms);
    }

    public boolean restartDatabaseManager() {
        boolean success = true;
        try {
            Config c = getConfig().getConfig("DatabaseManager");
            databaseManager.shutdown();
            databaseManager = createDatabaseManager(c);
        } catch (ApplicationException e) {
            e.printStackTrace();
            System.err.println("Portal: restartDataBaseManager " + e.getMessage());
            success = false;
        } catch (ConfigException e) {
            e.printStackTrace();
            System.err.println("Portal: restartDataBaseManager " + e.getMessage());
            success = false;
        } catch (KeywordValueException e) {
            e.printStackTrace();
            System.err.println("Portal: restartDataBaseManager " + e.getMessage());
            success = false;
        }
        return success;
    }
}
