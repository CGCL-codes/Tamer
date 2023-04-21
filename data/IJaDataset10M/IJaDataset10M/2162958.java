package org.wahlzeit.main;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * 
 * @author driehle
 *
 */
public abstract class AbstractServlet extends HttpServlet {

    /**
	 * 
	 */
    protected static int lastSessionId = 0;

    /**
	 * 
	 */
    public static synchronized int getLastSessionId() {
        return lastSessionId;
    }

    /**
	 * 
	 */
    public static void setLastSessionId(int newSessionId) {
        lastSessionId = newSessionId;
    }

    /**
	 * 
	 */
    public static synchronized int getNextSessionId() {
        return ++lastSessionId;
    }

    /**
	 * 
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserSession ctx = ensureWebContext(request);
        ContextManager.setThreadLocalContext(ctx);
        if (Wahlzeit.isShuttingDown() || (ctx == null)) {
            displayNullPage(request, response);
        } else {
            myGet(request, response);
            ctx.dropDatabaseConnection();
        }
        ContextManager.dropThreadLocalContext();
    }

    /**
	 * 
	 */
    protected void myGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
	 * 
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserSession ctx = ensureWebContext(request);
        ContextManager.setThreadLocalContext(ctx);
        if (Wahlzeit.isShuttingDown() || (ctx == null)) {
            displayNullPage(request, response);
        } else {
            myPost(request, response);
            ctx.dropDatabaseConnection();
        }
        ContextManager.dropThreadLocalContext();
    }

    /**
	 * 
	 */
    protected void myPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
	 * 
	 */
    protected UserSession ensureWebContext(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        UserSession result = (UserSession) httpSession.getAttribute("context");
        if (result == null) {
            try {
                String ctxName = "ctx" + getNextSessionId();
                result = new UserSession(ctxName);
                SysLog.logCreatedObject("WebContext", ctxName);
                String referrer = request.getHeader("Referer");
                SysLog.logInfo("request referrer: " + referrer);
                if (request.getLocale().getLanguage().equals("de")) {
                    result.setConfiguration(LanguageConfigs.get(Language.GERMAN));
                }
            } catch (Exception ex) {
                SysLog.logThrowable(ex);
            }
            httpSession.setAttribute("context", result);
            httpSession.setMaxInactiveInterval(24 * 60 * 60);
        }
        return result;
    }

    /**
	 * 
	 */
    protected void displayNullPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print("The system is undergoing maintenance and will be back in a minute. Thank you for your patience!");
        out.close();
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
	 * 
	 */
    protected void redirectRequest(HttpServletResponse response, String link) throws IOException {
        response.setContentType("text/html");
        response.sendRedirect(link + ".html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
	 * 
	 */
    protected void configureResponse(Session ctx, HttpServletResponse response, WebPart result) throws IOException {
        long processingTime = ctx.getProcessingTime();
        result.addString("processingTime", StringUtil.asStringInSeconds((processingTime == 0) ? 1 : processingTime));
        SysLog.logValue("proctime", String.valueOf(processingTime));
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        result.writeOn(out);
        out.close();
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
	 * 
	 */
    protected boolean isLocalHost(HttpServletRequest request) {
        String remoteHost = request.getRemoteHost();
        String localHost = null;
        try {
            localHost = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ex) {
        }
        return remoteHost.equals(localHost) || remoteHost.equals("localhost");
    }

    /**
	 * 
	 */
    protected String getRequestArgsAsString(UserSession ctx, Map args) {
        StringBuffer result = new StringBuffer(96);
        for (Iterator i = args.keySet().iterator(); i.hasNext(); ) {
            String key = i.next().toString();
            String value = ctx.getAsString(args, key);
            result.append(key + "=" + value);
            if (i.hasNext()) {
                result.append("; ");
            }
        }
        return "[" + result.toString() + "]";
    }
}
